package com.example.jokenpo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

@Service("jokenpoService")
public class jokenpoService {

    private static final Logger log = LoggerFactory.getLogger(jokenpoService.class);

    private Jogo jogo;

    public jokenpoService() {
        jogo = new Jogo();
    }

    public String criarJogador(String nome) throws JogadorJaExistenteException {
        validarJogadorExistente(nome);
        jogo.getJogadores().add(new Jogador(nome));
        String message = MessageConstants.JOGADOR + nome + MessageConstants.CRIADO;
        log.info(message);
        return message;
    }

    public String criarEntrada(String jogador, String jogada)
            throws JogadorNaoEncontradoException, JogadaInvalidaException, JogadorComEntradaException {
        validarJogadorComEntrada(jogador);
        jogo.getEntradas().add(new Entrada(jogadorPorNome(jogador), JogadaEnum.jogadaPorNome(jogada)));
        String message = MessageConstants.ENTRADA_COM_JOGADOR + jogador + MessageConstants.E_JOGADA + jogada + MessageConstants.CRIADO;
        log.info(message);
        return message;
    }

    public String jogar() throws JogoSemEntradasException, JogoComEntradaUnicaException {

        if (jogo.getEntradas().isEmpty()) {
            log.error(MessageConstants.JOGO_SEM_ENTRADAS);
            throw new JogoSemEntradasException(MessageConstants.JOGO_SEM_ENTRADAS);
        }

        if (jogo.getEntradas().size() == 1) {
            log.error(MessageConstants.JOGO_COM_UNICA_ENTRADA);
            throw new JogoComEntradaUnicaException(MessageConstants.JOGO_COM_UNICA_ENTRADA);
        }

        Map<JogadaEnum, List<Jogador>> agrupamentoEntradas = jogo.getEntradas().stream().collect(
                Collectors.groupingBy(Entrada::getJogada,
                        Collectors.mapping(Entrada::getJogador, Collectors.toList())));

        jogo.getEntradas().stream().forEach(entrada -> {
            for (JogadaEnum jogadaOponente : JogadaEnum.values()) {
                if (!entrada.getJogada().equals(jogadaOponente)) {
                    if (agrupamentoEntradas.containsKey(jogadaOponente)) {
                        if (entrada.getJogada().venceJogada(jogadaOponente)) {
                            entrada.getJogador().ganhaUmPonto();
                        }
                    }
                }
            }
        });

        int maiorPontuacao = jogo.getJogadores().stream().mapToInt(Jogador::getPontuacao).max().orElse(-1);

        List<Jogador> vencedores = jogo.getJogadores().stream().filter(j -> j.getPontuacao() == maiorPontuacao)
                .collect(Collectors.toList());

        String resultado;

        if (vencedores.size() == jogo.getEntradas().size()) {
            log.info(MessageConstants.RESULTADO_TODOS_EMPATARAM);
            resultado = MessageConstants.RESULTADO_TODOS_EMPATARAM;
        } else if (vencedores.size() == 1) {
            String mensagem = MessageConstants.RESULTADO + vencedores.stream().findFirst().get().getNome() + MessageConstants.VENCEU;
            log.info(mensagem);
            resultado = mensagem;
        } else {
            String mensagem = MessageConstants.RESULTADO + vencedores.stream().map(Jogador::getNome).collect(Collectors.joining(", "))
                    + MessageConstants.EMPATARAM;
            log.info(mensagem);
            resultado = mensagem;
        }

        jogo.limparEntradas();
        return resultado;
    }

    private Jogador jogadorPorNome(String nomeJogador) throws JogadorNaoEncontradoException {

        Optional<Jogador> jogador = jogo.getJogadores().stream().filter(j -> j.getNome().equalsIgnoreCase(nomeJogador)).findFirst();

        if (!jogador.isPresent()) {
            log.error(MessageConstants.JOGADOR_NAO_ENCONTRADO);
            throw new JogadorNaoEncontradoException(MessageConstants.JOGADOR_NAO_ENCONTRADO);
        }

        return jogador.get();
    }

    private void validarJogadorExistente(String nomeJogador) throws JogadorJaExistenteException {

        Optional<Jogador> jogador = jogo.getJogadores().stream().filter(j -> j.getNome().equalsIgnoreCase(nomeJogador)).findFirst();

        if (jogador.isPresent()) {
            log.error(MessageConstants.JOGADOR_JA_EXISTENTE);
            throw new JogadorJaExistenteException(MessageConstants.JOGADOR_JA_EXISTENTE);
        }

    }

    private void validarJogadorComEntrada(String nomeJogador) throws JogadorComEntradaException {

        Optional<Entrada> entrada = jogo.getEntradas().stream().filter(e -> e.getJogador().getNome().equalsIgnoreCase(nomeJogador))
                .findFirst();

        if (entrada.isPresent()) {
            log.error(MessageConstants.JOGADOR_COM_ENTRADA);
            throw new JogadorComEntradaException(MessageConstants.JOGADOR_COM_ENTRADA);
        }

    }

}
