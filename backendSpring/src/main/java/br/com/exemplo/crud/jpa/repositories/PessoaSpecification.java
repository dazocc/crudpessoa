package br.com.exemplo.crud.jpa.repositories;

import br.com.exemplo.crud.jpa.models.PessoaEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class PessoaSpecification implements Specification<PessoaEntity>  {

    private PessoaFilter pessoaFilter;

    public PessoaSpecification(PessoaFilter pessoaFilter) {
        this.pessoaFilter = pessoaFilter;
    }

    @Override
    public Predicate toPredicate(Root<PessoaEntity> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(pessoaFilter.getCpf())){
            predicates.add(cb.equal(root.get("cpf"), pessoaFilter.getCpf()));
        }

        if(!StringUtils.isEmpty(pessoaFilter.getNome())){
            predicates.add(cb.like(root.get("nome"), "%" + pessoaFilter.getNome() + "%"));
        }

        if(!StringUtils.isEmpty(pessoaFilter.getEmail())){
            predicates.add(cb.equal(root.get("email"), pessoaFilter.getEmail()));
        }

        if(!StringUtils.isEmpty(pessoaFilter.getDataNascimento())){
            predicates.add(cb.equal(root.get("dataNascimento"), pessoaFilter.getDataNascimento()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

}