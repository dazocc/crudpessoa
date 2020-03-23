package br.com.exemplo.pessoa.jpa.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pessoa"/*, uniqueConstraints = @UniqueConstraint(columnNames = "cpf", name = "pesssoa_cnpj_uk") */)
@Where(clause = "ativo = true")
@Getter
@Setter
@NoArgsConstructor
public class PessoaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{notempty.nome}")
    @Size(max = 150, message = "{size.max.nome}")
    @Column(nullable = false)
    private String nome;

    @NotEmpty(message = "{notempty.cpf}")
    @CPF(message = "{cpf.not.valid}")
    @Column(nullable = false)
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @NotEmpty(message = "{notempty.email}")
    @Size(max = 400, message = "{size.max.email}")
    @Email(message = "{valid.email}")
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_avatar")
    private ArquivoEntity avatar;

    public PessoaEntity(String nome, String cpf, String email,  LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

}
