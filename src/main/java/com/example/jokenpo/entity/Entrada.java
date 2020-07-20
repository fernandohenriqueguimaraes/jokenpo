package com.example.jokenpo.entity;

import java.io.Serializable;
import java.util.UUID;

import com.example.jokenpo.entity.enumeration.JogadaEnum;

public class Entrada  implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private Jogador jogador;
    private JogadaEnum jogada;
    
    public Entrada(Jogador jogador, JogadaEnum jogada) {
        this.id = UUID.randomUUID();
        setJogador(jogador);
        setJogada(jogada);
    }

    public UUID getId() {
        return id;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public JogadaEnum getJogada() {
        return jogada;
    }
    
    public void setJogada(JogadaEnum jogada) {
        this.jogada = jogada;
    }

    @Override
    public String toString() {
        return "Entrada [id=" + id + ", jogador=" + jogador + ", jogada=" + jogada + "]";
    }    

}