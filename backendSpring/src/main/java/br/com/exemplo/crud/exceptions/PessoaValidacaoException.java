package br.com.exemplo.crud.exceptions;

public class PessoaValidacaoException extends RuntimeException {

    public PessoaValidacaoException(String mensagem){
        super(mensagem);
    }

    public PessoaValidacaoException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

}
