package br.com.exemplo.crud.mongo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Document(collection = "Editora")
@Getter
@Setter
@NoArgsConstructor
public class EditoraEntity {

    @NotEmpty(message = "{notempty.nome}")
    @Size(max = 200, message = "{size.max.nome}")
    private String nome;

    public EditoraEntity(String nome) {
        this.nome = nome;
    }
}
