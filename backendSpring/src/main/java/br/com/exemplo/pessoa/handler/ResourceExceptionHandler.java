package br.com.exemplo.pessoa.handler;

import br.com.exemplo.pessoa.exceptions.PessoaNaoEncontradaException;
import br.com.exemplo.pessoa.exceptions.PessoaValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<DetalheException> handlePessoaNaoEncontrada(PessoaNaoEncontradaException e, HttpServletRequest request) {

        DetalheException detalheException = new DetalheException(HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis(), "A pessoa não foi encontrado");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheException);
    }

    @ExceptionHandler(PessoaValidacaoException.class)
    public ResponseEntity<DetalheException> handlePessoaValidacao(PessoaValidacaoException e, HttpServletRequest request) {

        String message;

        try {
            message = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());
        }catch (Exception exception){
            message = e.getMessage();
        }

        DetalheException detalheException = new DetalheException(HttpStatus.CONFLICT.value(),
                System.currentTimeMillis(), message);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(detalheException);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        DetalheException detalheException = new DetalheException(HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(), errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detalheException);

    }

}
