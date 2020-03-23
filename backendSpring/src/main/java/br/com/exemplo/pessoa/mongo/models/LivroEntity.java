package br.com.exemplo.pessoa.mongo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Document(collection = "Livro")
@Getter
@Setter
@NoArgsConstructor
public class LivroEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotEmpty(message = "{notempty.nome}")
    @Size(max = 200, message = "{size.max.nome}")
    private String nome;

    @NotEmpty(message = "{notempty.editora}")
    @Size(max = 100, message = "{size.max.editora}")
    private String editora;

}
