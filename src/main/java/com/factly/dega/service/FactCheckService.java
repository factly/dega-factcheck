package com.factly.dega.service;

import com.factly.dega.service.dto.FactCheckDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FactCheck.
 */
public interface FactCheckService {

    /**
     * Save a factCheck.
     *
     * @param factCheckDTO the entity to save
     * @return the persisted entity
     */
    FactCheckDTO save(FactCheckDTO factCheckDTO);

    /**
     * Get all the factChecks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FactCheckDTO> findAll(Pageable pageable);

    /**
     * Get all the FactCheck with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<FactCheckDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" factCheck.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FactCheckDTO> findOne(String id);

    /**
     * Delete the "id" factCheck.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the factCheck corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FactCheckDTO> search(String query, Pageable pageable);
}
