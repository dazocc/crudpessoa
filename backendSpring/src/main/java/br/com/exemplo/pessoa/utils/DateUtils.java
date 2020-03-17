package br.com.exemplo.pessoa.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String getDataDDMMYYYY(LocalDate data){

        String dataFormatada = "";

        if(data != null){
            dataFormatada = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        return dataFormatada;
    }

}
