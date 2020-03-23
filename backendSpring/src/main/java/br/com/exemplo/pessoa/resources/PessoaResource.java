package br.com.exemplo.pessoa.resources;

import br.com.exemplo.pessoa.jpa.models.PessoaEntity;
import br.com.exemplo.pessoa.jpa.repositories.PessoaFilter;
import br.com.exemplo.pessoa.services.PessoaService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/pessoas")
@Tag(name = "Pessoas", description = "Serviço de Pessoas")
public class PessoaResource {

    private final String STATUS_OK = "200";
    private final String STATUS_OK_DESCRIPTION = "OK";
    private final String STATUS_CREATED = "201";
    private final String STATUS_NO_CONTENT = "204";
    private final String STATUS_CONFLIT = "409";
    private final String STATUS_CONFLIT_DESCRIPTION = "Conflict";

    @Autowired
    private PessoaService pessoaService;


    @Operation(description = "Lista as pessoas", responses = {
            @ApiResponse(responseCode = STATUS_OK, description = STATUS_OK_DESCRIPTION, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = STATUS_CONFLIT, description = STATUS_CONFLIT_DESCRIPTION, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public ResponseEntity<Page<PessoaEntity>> listar(PessoaFilter pessoaFilter, @PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        Page<PessoaEntity> pagePessoa = pessoaService.listar(pessoaFilter, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(pagePessoa);
    }

    @Operation(description = "Busca uma pessoa", responses = {
            @ApiResponse(responseCode = STATUS_OK, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<PessoaEntity> buscar(@PathVariable Long id) {

        PessoaEntity pessoa = pessoaService.buscarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(pessoa);
    }

    @Operation(description = "Grava uma pessoa", responses = {
            @ApiResponse(responseCode = STATUS_CREATED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<PessoaEntity> salvar(@Valid @RequestBody PessoaEntity pessoa) {

        pessoa = pessoaService.salvar(pessoa);

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
    }

    @Operation(description = "Atualiza uma pessoa", responses = {
            @ApiResponse(responseCode = STATUS_NO_CONTENT, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> atualizar(@Valid @RequestBody PessoaEntity pessoa, @PathVariable Long id) {

        pessoaService.atualizar(pessoa, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Faz Upload de um arquivo para uma pessoa", responses = {
            @ApiResponse(responseCode = STATUS_NO_CONTENT, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(value = "/{id}/arquivo")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam MultipartFile file) {
        try {

            pessoaService.atualizarArquivo(id, file.getBytes(), file.getContentType(), file.getOriginalFilename(), file.getSize());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Deleta uma pessoa", responses = {
            @ApiResponse(responseCode = STATUS_NO_CONTENT, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        pessoaService.deletar(id);

        return ResponseEntity.noContent().build();
    }


}
