package br.com.exemplo.pessoa.exceptions;

public class PessoaNaoEncontradaException extends RuntimeException {

    public PessoaNaoEncontradaException(String mensagem){
        super(mensagem);
    }

    public PessoaNaoEncontradaException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

}
