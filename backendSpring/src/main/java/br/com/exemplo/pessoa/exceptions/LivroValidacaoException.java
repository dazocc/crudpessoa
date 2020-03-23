package br.com.exemplo.pessoa.exceptions;

public class LivroValidacaoException extends RuntimeException {

    public LivroValidacaoException(String mensagem){
        super(mensagem);
    }

    public LivroValidacaoException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

}
