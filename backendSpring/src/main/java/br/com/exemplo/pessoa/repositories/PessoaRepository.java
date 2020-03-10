package br.com.exemplo.pessoa.repositories;

import br.com.exemplo.pessoa.models.PessoaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PessoaRepository extends JpaRepository<PessoaEntity, Long>, JpaSpecificationExecutor<PessoaEntity> {

    Boolean existsByCpf(String cpf);

    Boolean existsByCpfAndIdNot(String cpf, Long id);

}
