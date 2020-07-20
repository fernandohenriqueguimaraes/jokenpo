package com.example.jokenpo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.jokenpo.entity.Entrada;
import com.example.jokenpo.entity.Jogador;
import com.example.jokenpo.entity.Jogo;
import com.example.jokenpo.entity.enumeration.JogadaEnum;
import com.example.jokenpo.exception.JogadaInvalidaException;
import com.example.jokenpo.exception.JogadorComEntradaException;
import com.example.jokenpo.exception.JogadorJaExistenteException;
import com.example.jokenpo.exception.JogadorNaoEncontradoException;
import com.example.jokenpo.exception.JogoComEntradaUnicaException;
import com.example.jokenpo.exception.JogoSemEntradasException;
import com.example.jokenpo.util.MessageConstants;

@RunWith(SpringRunner.class)
public class jokenpoServiceTest {
    
    @InjectMocks
    private jokenpoService jokenpoService;
    
    @Mock
    private Jogo jogo;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void criarJogadorComSuccessoTest() throws JogadorJaExistenteException {
        
        String nome = "Fernando";        
        assertEquals(MessageConstants.JOGADOR + nome + MessageConstants.CRIADO, jokenpoService.criarJogador(nome));
    }
    
    @Test(expected = JogadorJaExistenteException.class)
    public void criarJogadorComNomeJaExistenteTest() throws JogadorJaExistenteException {
        
        String nome = "Fernando"; 
        List<Jogador> jogadores = new ArrayList<Jogador>();
        jogadores.add(new Jogador(nome));
        
        when(jogo.getJogadores()).thenReturn(jogadores);
              
        jokenpoService.criarJogador(nome);
    }
    
    @Test
    public void criarEntradaComSuccessoTest() throws JogadorNaoEncontradoException, JogadaInvalidaException, JogadorComEntradaException {
        
        String nomeJogador = "Fernando";
        List<Jogador> jogadores = new ArrayList<Jogador>();
        jogadores.add(new Jogador(nomeJogador));
        
        when(jogo.getJogadores()).thenReturn(jogadores);
        
        assertEquals(MessageConstants.ENTRADA_COM_JOGADOR + nomeJogador + MessageConstants.E_JOGADA + JogadaEnum.PAPEL.getDescricao() + MessageConstants.CRIADO, jokenpoService.criarEntrada(nomeJogador, JogadaEnum.PAPEL.getDescricao()));
    }
    
    @Test(expected = JogadaInvalidaException.class)
    public void criarEntradaComJogadaInexistenteTest() throws JogadorNaoEncontradoException, JogadaInvalidaException, JogadorComEntradaException {
        
        String nomeJogador = "Fernando";
        List<Jogador> jogadores = new ArrayList<Jogador>();
        jogadores.add(new Jogador(nomeJogador));
        
        when(jogo.getJogadores()).thenReturn(jogadores);
        
        jokenpoService.criarEntrada(nomeJogador, "Jogada Inexistente");
    }
    
    @Test(expected = JogadorNaoEncontradoException.class)
    public void criarEntradaComJogadorInexistenteTest() throws JogadorNaoEncontradoException, JogadaInvalidaException, JogadorComEntradaException {
        
        String nomeJogador = "Fernando";
        
        jokenpoService.criarEntrada(nomeJogador, JogadaEnum.PAPEL.getDescricao());
    }
    
    @Test(expected = JogadorComEntradaException.class)
    public void criarEntradaComJogadorComJogadaExistenteTest() throws JogadorNaoEncontradoException, JogadaInvalidaException, JogadorComEntradaException {
        
        String nomeJogador = "Fernando";
        List<Jogador> jogadores = new ArrayList<Jogador>();
        Jogador jogador = new Jogador(nomeJogador);
        jogadores.add(jogador);
        
        when(jogo.getJogadores()).thenReturn(jogadores);
        
        List<Entrada> entradas = new ArrayList<Entrada>();
        entradas.add(new Entrada(jogador, JogadaEnum.PAPEL));
        
        when(jogo.getEntradas()).thenReturn(entradas);
        
        jokenpoService.criarEntrada(nomeJogador, JogadaEnum.PAPEL.getDescricao());
    }
    
    @Test(expected = JogoSemEntradasException.class)
    public void jogoSemEntradaTest() throws JogoSemEntradasException, JogoComEntradaUnicaException {
        
        jokenpoService.jogar();
    }
    
    @Test(expected = JogoComEntradaUnicaException.class)
    public void jogoComUmaEntradaTest() throws JogoSemEntradasException, JogoComEntradaUnicaException {
        
        String nomeJogador = "Fernando";
        List<Jogador> jogadores = new ArrayList<Jogador>();
        Jogador jogador = new Jogador(nomeJogador);
        jogadores.add(jogador);
        
        when(jogo.getJogadores()).thenReturn(jogadores);
        
        List<Entrada> entradas = new ArrayList<Entrada>();
        entradas.add(new Entrada(jogador, JogadaEnum.PAPEL));
        
        when(jogo.getEntradas()).thenReturn(entradas);
        jokenpoService.jogar();
    }
    
    @Test()
    public void jogoComSucessoTest() throws JogoSemEntradasException, JogoComEntradaUnicaException {
        
        String nomeJogador = "Fernando";
        String nomeJogador2 = "Henrique";
        List<Jogador> jogadores = new ArrayList<Jogador>();
        Jogador jogador = new Jogador(nomeJogador);
        Jogador jogador2 = new Jogador(nomeJogador2);
        jogadores.add(jogador);
        jogadores.add(jogador2);
        
        when(jogo.getJogadores()).thenReturn(jogadores);
        
        List<Entrada> entradas = new ArrayList<Entrada>();
        entradas.add(new Entrada(jogador, JogadaEnum.PAPEL));
        entradas.add(new Entrada(jogador2, JogadaEnum.TESOURA));
        
        when(jogo.getEntradas()).thenReturn(entradas);
        
        assertEquals(MessageConstants.RESULTADO + jogador2.getNome() + MessageConstants.VENCEU, jokenpoService.jogar());
    }
    
    @Test()
    public void jogoComTodosEmpatadosTest() throws JogoSemEntradasException, JogoComEntradaUnicaException {
        
        String nomeJogador = "Fernando";
        String nomeJogador2 = "Henrique";
        String nomeJogador3 = "Alexandre";
        List<Jogador> jogadores = new ArrayList<Jogador>();
        Jogador jogador = new Jogador(nomeJogador);
        Jogador jogador2 = new Jogador(nomeJogador2);
        Jogador jogador3 = new Jogador(nomeJogador3);
        jogadores.add(jogador);
        jogadores.add(jogador2);
        jogadores.add(jogador3);
        
        when(jogo.getJogadores()).thenReturn(jogadores);
        
        List<Entrada> entradas = new ArrayList<Entrada>();
        entradas.add(new Entrada(jogador, JogadaEnum.PAPEL));
        entradas.add(new Entrada(jogador2, JogadaEnum.TESOURA));
        entradas.add(new Entrada(jogador3, JogadaEnum.PEDRA));
        
        when(jogo.getEntradas()).thenReturn(entradas);
        
        assertEquals(MessageConstants.RESULTADO_TODOS_EMPATARAM, jokenpoService.jogar());
    }
    
    @Test()
    public void jogoComEmpateTest() throws JogoSemEntradasException, JogoComEntradaUnicaException {
        
        String nomeJogador = "Fernando";
        String nomeJogador2 = "Henrique";
        String nomeJogador3 = "Alexandre";
        List<Jogador> jogadores = new ArrayList<Jogador>();
        Jogador jogador = new Jogador(nomeJogador);
        Jogador jogador2 = new Jogador(nomeJogador2);
        Jogador jogador3 = new Jogador(nomeJogador3);
        jogadores.add(jogador);
        jogadores.add(jogador2);
        jogadores.add(jogador3);
        
        when(jogo.getJogadores()).thenReturn(jogadores);
        
        List<Entrada> entradas = new ArrayList<Entrada>();
        entradas.add(new Entrada(jogador, JogadaEnum.PAPEL));
        entradas.add(new Entrada(jogador2, JogadaEnum.TESOURA));
        entradas.add(new Entrada(jogador3, JogadaEnum.TESOURA));
        
        when(jogo.getEntradas()).thenReturn(entradas);
        
        assertEquals(MessageConstants.RESULTADO + nomeJogador2 + ", " + nomeJogador3 +
                 MessageConstants.EMPATARAM, jokenpoService.jogar());
    }

}
