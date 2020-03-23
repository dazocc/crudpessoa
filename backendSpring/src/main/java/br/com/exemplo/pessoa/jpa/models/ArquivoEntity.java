package br.com.exemplo.pessoa.jpa.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_arquivo")
@Getter
@Setter
@NoArgsConstructor
public class ArquivoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String tipo;

    @Lob
    private byte[] arquivo;

}
