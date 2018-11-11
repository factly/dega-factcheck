package com.factly.dega.service;

import com.factly.dega.service.dto.RatingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Rating.
 */
public interface RatingService {

    /**
     * Save a rating.
     *
     * @param ratingDTO the entity to save
     * @return the persisted entity
     */
    RatingDTO save(RatingDTO ratingDTO);

    /**
     * Get all the ratings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RatingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" rating.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RatingDTO> findOne(String id);

    /**
     * Delete the "id" rating.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the rating corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RatingDTO> search(String query, Pageable pageable);
}
