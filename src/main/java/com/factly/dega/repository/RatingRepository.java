package com.factly.dega.repository;

import com.factly.dega.domain.Rating;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

}
