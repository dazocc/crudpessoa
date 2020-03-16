package br.com.exemplo.pessoa.resources;

import br.com.exemplo.pessoa.models.PessoaEntity;
import br.com.exemplo.pessoa.repositories.PessoaFilter;
import br.com.exemplo.pessoa.services.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<Page<PessoaEntity>> listar(PessoaFilter pessoaFilter, @PageableDefault(value = Integer.MAX_VALUE)  Pageable pageable) {

        Page<PessoaEntity> pagePessoa = pessoaService.listar(pessoaFilter, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(pagePessoa);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PessoaEntity> buscar(@PathVariable Long id) {

        PessoaEntity pessoa = pessoaService.buscarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(pessoa);
    }

    @PostMapping
    public ResponseEntity<PessoaEntity> salvar(@Valid @RequestBody PessoaEntity pessoa) {

        pessoa = pessoaService.salvar(pessoa);

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> atualizar(@Valid @RequestBody PessoaEntity pessoa, @PathVariable Long id) {

        pessoaService.atualizar(pessoa, id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}/arquivo")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam MultipartFile file) {
         try {

             pessoaService.atualizarArquivo(id, file.getBytes(), file.getContentType(), file.getOriginalFilename(), file.getSize());
         } catch (IOException e) {
             return ResponseEntity.badRequest().build();
         }

         return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        pessoaService.deletar(id);

        return ResponseEntity.noContent().build();
    }


}
