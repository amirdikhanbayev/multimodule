package com.develop.Repository;

import com.develop.Model.DictionaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DictionaryRepository extends MongoRepository<DictionaryDocument, String> {

}
