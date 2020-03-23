package br.com.exemplo.pessoa.services;

import br.com.exemplo.pessoa.exceptions.PessoaValidacaoException;
import br.com.exemplo.pessoa.jpa.models.ArquivoEntity;
import br.com.exemplo.pessoa.jpa.models.PessoaEntity;
import br.com.exemplo.pessoa.jpa.repositories.PessoaFilter;
import br.com.exemplo.pessoa.jpa.repositories.PessoaRepository;
import br.com.exemplo.pessoa.jpa.repositories.PessoaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PessoaService {

    private final String [] EXTENSOES = new String []{"jpeg", "bmp", "png"};
    private final Long TAMANHO_ARQUIVO = 1024L*1024L;

    @Autowired
    private PessoaRepository pessoaRepository;

    public Page<PessoaEntity> listar(PessoaFilter pessoaFilter, Pageable pageable) {

        PessoaSpecification ps = new PessoaSpecification(pessoaFilter);

        return pessoaRepository.findAll(ps, pageable);
    }

    public PessoaEntity buscarPorId(Long id) {

        Optional<PessoaEntity> pessoaOptional = pessoaRepository.findById(id);

        if(!pessoaOptional.isPresent()){
            throw new PessoaValidacaoException("cadastroPessoa.M1");
        }

        return pessoaOptional.get();
    }

    public PessoaEntity salvar(PessoaEntity pessoa){

        validaAntesSalvarAtualizar(pessoa);

        return pessoaRepository.save(pessoa);
    }

    public void validaAntesSalvarAtualizar(PessoaEntity pessoa) {

        if(pessoa != null &&
                !StringUtils.isEmpty(pessoa.getCpf()) &&
                validaCpfDuplicado(pessoa.getId(), pessoa.getCpf())){
            throw new PessoaValidacaoException("cadastroPessoa.M2");
        }

    }

    public void deletar(Long id){
        PessoaEntity pessoa = buscarPorId(id);

        if (pessoa != null) {
            pessoa.setAtivo(Boolean.FALSE);

            salvar(pessoa);
        }
    }

    public void atualizar(PessoaEntity pessoa, Long id) {

        PessoaEntity pessoaBanco = buscarPorId(id);

        if (pessoaBanco != null) {

            pessoa.setId(id);
            pessoa.setAvatar(pessoaBanco.getAvatar());

            validaAntesSalvarAtualizar(pessoa);

            pessoaRepository.save(pessoa);
        }else{
            throw new PessoaValidacaoException("cadastroPessoa.M1");
        }
    }

    private boolean validaCpfDuplicado(Long id, String cpf) {

        boolean existeCpfBase;

        if(id == null){
            existeCpfBase = pessoaRepository.existsByCpf(cpf);
        }else{
            existeCpfBase = pessoaRepository.existsByCpfAndIdNot(cpf, id);
        }

        return existeCpfBase;
    }

    public void atualizarArquivo(Long id, byte[] avatar, String tipo, String nome, Long tamanho) {

        PessoaEntity pessoa = buscarPorId(id);

        validaAntesAtualizarArquivo(tamanho, tipo);

        if (pessoa != null) {

            if(pessoa.getAvatar() == null){
                ArquivoEntity arquivo = new ArquivoEntity();

                arquivo.setArquivo(avatar);
                arquivo.setNome(nome);
                arquivo.setTipo(tipo);

                pessoa.setAvatar(arquivo);
            }else{
                pessoa.getAvatar().setArquivo(avatar);
                pessoa.getAvatar().setNome(nome);
                pessoa.getAvatar().setTipo(tipo);
            }

            pessoaRepository.save(pessoa);
        }else{
            throw new PessoaValidacaoException("cadastroPessoa.M1");
        }
    }

    public void validaAntesAtualizarArquivo(Long tamanho, String tipo) {

        if(tamanho != null &&
                tamanho >  TAMANHO_ARQUIVO){
            throw new PessoaValidacaoException("cadastroPessoaArquivo.M1");
        }

        List<String> tipos = Stream.of(EXTENSOES).collect(Collectors.toList());

        if (!StringUtils.isEmpty(tipo) &&
                tipo.contains(".")) {
            String extensaoDoArquivo = tipo.substring(tipo.lastIndexOf(46) + 1);

            if(!tipos.contains(extensaoDoArquivo)){
                throw new PessoaValidacaoException("cadastroPessoaArquivo.M2");
            }
        }

    }

}
