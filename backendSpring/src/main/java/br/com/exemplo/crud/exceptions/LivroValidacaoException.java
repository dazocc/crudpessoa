package br.com.exemplo.crud.exceptions;

public class LivroValidacaoException extends RuntimeException {

    public LivroValidacaoException(String mensagem){
        super(mensagem);
    }

    public LivroValidacaoException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

}
