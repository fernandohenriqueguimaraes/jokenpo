package com.example.jokenpo.entity.enumeration;

import com.example.jokenpo.exception.JogadaInvalidaException;
import com.example.jokenpo.util.MessageConstants;

public enum JogadaEnum {

    SPOCK("SPOCK"),
    TESOURA("TESOURA"),
    LARGATO("LARGATO"),
    PAPEL("PAPEL"),
    PEDRA("SEM CONTRATO");

    private String descricao;

    private JogadaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static JogadaEnum jogadaPorNome(String descricao) throws JogadaInvalidaException {
        if (descricao != null) {
            for (JogadaEnum jogada : JogadaEnum.values()) {
                if (descricao.equals(jogada.getDescricao())) {
                    return jogada;
                }
            }

            throw new JogadaInvalidaException(MessageConstants.JOGADA_INVALIDA);
        }
        return null;
    }

    public Boolean venceJogada(JogadaEnum jogadaOponente) {
        switch (this) {
        case SPOCK:

            if (jogadaOponente.equals(TESOURA) || jogadaOponente.equals(PEDRA)) {
                return true;
            }
            break;
        case TESOURA:

            if (jogadaOponente.equals(LARGATO) || jogadaOponente.equals(PAPEL)) {
                return true;
            }
            break;
        case PEDRA:

            if (jogadaOponente.equals(LARGATO) || jogadaOponente.equals(TESOURA)) {
                return true;
            }
            break;
        case LARGATO:

            if (jogadaOponente.equals(SPOCK) || jogadaOponente.equals(PAPEL)) {
                return true;
            }
            break;
        case PAPEL:

            if (jogadaOponente.equals(PEDRA) || jogadaOponente.equals(SPOCK)) {
                return true;
            }
            break;
        }
        return false;

    }

}
