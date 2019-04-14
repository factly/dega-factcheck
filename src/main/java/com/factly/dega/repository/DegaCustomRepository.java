package com.factly.dega.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Created by Sravan on 12/6/2018.
 */
@NoRepositoryBean
public interface DegaCustomRepository<T, ID> extends MongoRepository<T, ID> {

    Page<T> findByClientId(String clientId, Pageable pageable);
}
