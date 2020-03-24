package br.com.exemplo.crud.services;

import br.com.exemplo.crud.exceptions.LivroValidacaoException;
import br.com.exemplo.crud.mongo.models.LivroEntity;
import br.com.exemplo.crud.mongo.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public Page<LivroEntity> listar(/*PessoaFilter pessoaFilter, */Pageable pageable) {

//        PessoaSpecification ps = new PessoaSpecification(pessoaFilter);

        return livroRepository.findAll(/*ps, */pageable);
    }

    public LivroEntity buscarPorId(String id) {

        Optional<LivroEntity> livroOptional = livroRepository.findById(id);

        if(!livroOptional.isPresent()){
            throw new LivroValidacaoException("cadastroLivro.M1");
        }

        return livroOptional.get();
    }

    public LivroEntity salvar(LivroEntity pessoa){
        return livroRepository.save(pessoa);
    }

    public void deletar(String id){
        LivroEntity livro = buscarPorId(id);

        if (livro != null) {
            livroRepository.delete(livro);
        }
    }

    public void atualizar(LivroEntity livro, String id) {

        LivroEntity livroBanco = buscarPorId(id);

        if (livroBanco != null) {

            livro.setId(id);

            livroRepository.save(livro);
        }else{
            throw new LivroValidacaoException("cadastroLivro.M1");
        }
    }

}
