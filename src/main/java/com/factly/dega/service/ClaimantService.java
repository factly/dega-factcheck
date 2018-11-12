package com.factly.dega.service;

import com.factly.dega.service.dto.ClaimantDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Claimant.
 */
public interface ClaimantService {

    /**
     * Save a claimant.
     *
     * @param claimantDTO the entity to save
     * @return the persisted entity
     */
    ClaimantDTO save(ClaimantDTO claimantDTO);

    /**
     * Get all the claimants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClaimantDTO> findAll(Pageable pageable);


    /**
     * Get the "id" claimant.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ClaimantDTO> findOne(String id);

    /**
     * Delete the "id" claimant.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the claimant corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClaimantDTO> search(String query, Pageable pageable);
}
