package com.example.jokenpo.entity;

import java.util.ArrayList;
import java.util.List;

public class Jogo {

    private List<Jogador> jogadores;
    
    private List<Entrada> entradas;   
    
    public Jogo() {
        setJogadores(new ArrayList<Jogador>());
        setEntradas(new ArrayList<Entrada>());
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }
    
    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }
    
    public List<Entrada> getEntradas() {
        return entradas;
    }
    
    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }
    
    public void limparEntradas() {
        getEntradas().stream().forEach(entrada -> {
            entrada.getJogador().limparPontuacao();
        });
        getEntradas().clear();
    }

    @Override
    public String toString() {
        return "Jogo [jogadores=" + jogadores + ", entradas=" + entradas + "]";
    } 
    
}
