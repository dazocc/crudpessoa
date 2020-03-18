package br.com.exemplo.pessoa.handler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DetalheException {

    private Integer status;

    private Long horario;

    private List<String> titulos;

    public DetalheException(Integer status, Long horario, String titulo) {
        this.status = status;
        this.horario = horario;
        this.titulos = new ArrayList<>();
        this.titulos.add(titulo);
    }


    public DetalheException(Integer status, Long horario, List<String> titulos) {
        this.status = status;
        this.horario = horario;
        this.titulos = titulos;
    }

}
