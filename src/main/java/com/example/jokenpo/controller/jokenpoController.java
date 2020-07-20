package com.example.jokenpo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jokenpo.exception.JogadaInvalidaException;
import com.example.jokenpo.exception.JogadorComEntradaException;
import com.example.jokenpo.exception.JogadorJaExistenteException;
import com.example.jokenpo.exception.JogadorNaoEncontradoException;
import com.example.jokenpo.exception.JogoComEntradaUnicaException;
import com.example.jokenpo.exception.JogoSemEntradasException;
import com.example.jokenpo.service.jokenpoService;

@RestController
public class jokenpoController {

    @Autowired
    private jokenpoService jokenpoService;
    
    @RequestMapping(value = "/jokenpo/jogador/{nome}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> criarJogador(
             @PathVariable(value = "nome", required = true) String nome) {
        try {
            return new ResponseEntity<String>(jokenpoService.criarJogador(nome), HttpStatus.CREATED);
        } catch (JogadorJaExistenteException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/jokenpo/entrada", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> criarEntrada(@RequestParam(value = "jogador", required = true) String jogador, @RequestParam(value = "jogada", required = true) String jogada) {
        try {
            return new ResponseEntity<String>(jokenpoService.criarEntrada(jogador, jogada), HttpStatus.OK);
        } catch (JogadorNaoEncontradoException | JogadaInvalidaException | JogadorComEntradaException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/jokenpo/jogar", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> jogar()  {
        try {
            return new ResponseEntity<String>(jokenpoService.jogar(), HttpStatus.OK);
        } catch (JogoSemEntradasException | JogoComEntradaUnicaException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
