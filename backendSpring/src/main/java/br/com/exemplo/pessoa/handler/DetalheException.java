package br.com.exemplo.pessoa.handler;

import java.util.ArrayList;
import java.util.List;

public class DetalheException {

    private Integer status;

    private Long horario;

    private List<String> titulos;

    public DetalheException(){
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getHorario() {
        return horario;
    }

    public void setHorario(Long horario) {
        this.horario = horario;
    }

    public List<String> getTitulos() {
        return titulos;
    }

    public void setTitulos(List<String> titulos) {
        this.titulos = titulos;
    }
}
