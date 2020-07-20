package com.example.jokenpo.entity;

import java.io.Serializable;
import java.util.UUID;

public class Jogador implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String nome;
    
    private int pontuacao;

    public Jogador(String name) {
        this.id = UUID.randomUUID();
        setNome(name);
        limparPontuacao();
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public int getPontuacao() {
        return pontuacao;
    }
 
    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
    
    public void ganhaUmPonto() {
        setPontuacao(getPontuacao() + 1);
    }
    
    public void limparPontuacao() {
        setPontuacao(0);
    }

    @Override
    public String toString() {
        return "Jogador [id=" + id + ", nome=" + nome + ", pontuacao=" + pontuacao + "]";
    }

}