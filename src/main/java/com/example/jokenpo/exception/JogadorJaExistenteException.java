package com.example.jokenpo.exception;

public class JogadorJaExistenteException extends Exception {

    private static final long serialVersionUID = 1L;

    public JogadorJaExistenteException(String message) {
        super(message);
    }
    
}
