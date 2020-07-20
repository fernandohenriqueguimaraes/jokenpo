package com.example.jokenpo.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import com.example.jokenpo.exception.JogadorComEntradaException;
import com.example.jokenpo.exception.JogadorJaExistenteException;
import com.example.jokenpo.exception.JogoSemEntradasException;
import com.example.jokenpo.service.jokenpoService;
import com.example.jokenpo.util.MessageConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class jokenpoControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private jokenpoController jokenpoController;

    @MockBean
    private jokenpoService jokenpoService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jokenpoController = mock(jokenpoController.class);
    }

    @Test
    @DisplayName("POST /jokenpo/jogador/{nome}")
    public void criarJogadorComSuccessoTest() throws Exception {

        String nome = "Fernando";

        when(jokenpoService.criarJogador(nome)).thenReturn(MessageConstants.JOGADOR + nome + MessageConstants.CRIADO);

        mvc.perform(post("/jokenpo/jogador/{nome}", nome))
                .andExpect(status().isCreated());

        verify(jokenpoService, times(1)).criarJogador(nome);

    }

    @Test
    @DisplayName("POST /jokenpo/jogador/{nome}")
    public void criarJogadorComNomeVazioTest() throws Exception {

        String nome = "";

        mvc.perform(post("/jokenpo/jogador/{nome}", nome))
                .andExpect(status().isNotFound());

        verify(jokenpoService, never()).criarJogador(nome);

    }

    @Test
    @DisplayName("POST /jokenpo/jogador/{nome}")
    public void criarJogadorComNomeJaExistenteNaListaTest() throws Exception {
    
        String nome = "Fernando";
        
        when(jokenpoService.criarJogador(nome)).thenThrow(JogadorJaExistenteException.class);
    
        mvc.perform(post("/jokenpo/jogador/{nome}", nome))
                .andExpect(status().isBadRequest());
        
        verify(jokenpoService, times(1)).criarJogador(nome);
    
    }

    @Test
    @DisplayName("POST /jokenpo/entrada?jogador=Fernando&jogada=SPOCK")
    public void criarEntradaComSuccessoTest() throws Exception {

        String jogador = "Fernando";
        String jogada = "SPOCK";

        when(jokenpoService.criarEntrada(jogador, jogada))
                .thenReturn(MessageConstants.ENTRADA_COM_JOGADOR + jogador + MessageConstants.E_JOGADA + jogada + MessageConstants.CRIADO);

        mvc.perform(post("/jokenpo/entrada")
                .param("jogador", jogador)
                .param("jogada", jogada))
                .andExpect(status().isOk());

        verify(jokenpoService, times(1)).criarEntrada(jogador, jogada);

    }
    
    @Test
    @DisplayName("POST /jokenpo/entrada?jogador=Fernando&jogada=SPOCK")
    public void criarEntradaComRepeticaoTest() throws Exception {

        String jogador = "Fernando";
        String jogada = "SPOCK";

        doThrow(JogadorComEntradaException.class).when(jokenpoService).criarEntrada(jogador, jogada);

        mvc.perform(post("/jokenpo/entrada")
                .param("jogador", jogador)
                .param("jogada", jogada))
                .andExpect(status().isBadRequest());

        verify(jokenpoService, times(1)).criarEntrada(jogador, jogada);

    }
    
    @Test
    @DisplayName("GET /jokenpo/jogar")
    public void criarJogarComSuccessoTest() throws Exception {

       String jogador = "Fernando";
        
       when(jokenpoService.jogar()).thenReturn(MessageConstants.RESULTADO + jogador + MessageConstants.VENCEU);

        mvc.perform(get("/jokenpo/jogar"))
                .andExpect(status().isOk());

        verify(jokenpoService, times(1)).jogar();

    }
    
    @Test
    @DisplayName("GET /jokenpo/jogar")
    public void criarJogarSemEntradaTest() throws Exception {
        
       doThrow(JogoSemEntradasException.class).when(jokenpoService).jogar();

        mvc.perform(get("/jokenpo/jogar"))
                .andExpect(status().isBadRequest());

        verify(jokenpoService, times(1)).jogar();

    }

}
