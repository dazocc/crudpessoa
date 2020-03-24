package br.com.exemplo.crud.mongo.repositories;

import br.com.exemplo.crud.mongo.models.LivroEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LivroRepository extends MongoRepository<LivroEntity, String> {
}
