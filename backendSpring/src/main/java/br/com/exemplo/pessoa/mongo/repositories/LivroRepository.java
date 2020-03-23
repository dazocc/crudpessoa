package br.com.exemplo.pessoa.mongo.repositories;

import br.com.exemplo.pessoa.mongo.models.LivroEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LivroRepository extends MongoRepository<LivroEntity, String> {
}
