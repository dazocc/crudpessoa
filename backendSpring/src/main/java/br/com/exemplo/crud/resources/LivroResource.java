package br.com.exemplo.crud.resources;

import br.com.exemplo.crud.mongo.models.LivroEntity;
import br.com.exemplo.crud.mongo.repositories.LivroFilter;
import br.com.exemplo.crud.services.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livro", description = "Serviço de Livros")
public class LivroResource {

    private final String STATUS_OK = "200";
    private final String STATUS_OK_DESCRIPTION = "OK";
    private final String STATUS_CREATED = "201";
    private final String STATUS_NO_CONTENT = "204";
    private final String STATUS_CONFLIT = "409";
    private final String STATUS_CONFLIT_DESCRIPTION = "Conflict";

    @Autowired
    private LivroService livroService;


    @Operation(description = "Lista os livros", responses = {
            @ApiResponse(responseCode = STATUS_OK, description = STATUS_OK_DESCRIPTION, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = STATUS_CONFLIT, description = STATUS_CONFLIT_DESCRIPTION, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public ResponseEntity<Page<LivroEntity>> listar(@PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        Page<LivroEntity> pageLivro = livroService.listar(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(pageLivro);
    }

    @Operation(description = "Busca um livro", responses = {
            @ApiResponse(responseCode = STATUS_OK, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<LivroEntity> buscar(@PathVariable String id) {

        LivroEntity livro = livroService.buscarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(livro);
    }

    @Operation(description = "Grava um livro", responses = {
            @ApiResponse(responseCode = STATUS_CREATED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<LivroEntity> salvar(@Valid @RequestBody LivroEntity livro) {

        livro = livroService.salvar(livro);

        return ResponseEntity.status(HttpStatus.CREATED).body(livro);
    }

    @Operation(description = "Atualiza um livro", responses = {
            @ApiResponse(responseCode = STATUS_NO_CONTENT, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> atualizar(@Valid @RequestBody LivroEntity livro, @PathVariable String id) {

        livroService.atualizar(livro, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Deleta um livro", responses = {
            @ApiResponse(responseCode = STATUS_NO_CONTENT, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {

        livroService.deletar(id);

        return ResponseEntity.noContent().build();
    }


}
