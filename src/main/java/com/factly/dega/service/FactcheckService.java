package com.factly.dega.service;

import com.factly.dega.service.dto.FactcheckDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Factcheck.
 */
public interface FactcheckService {

    /**
     * Save a factcheck.
     *
     * @param factcheckDTO the entity to save
     * @return the persisted entity
     */
    FactcheckDTO save(FactcheckDTO factcheckDTO);

    /**
     * Get all the factchecks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FactcheckDTO> findAll(Pageable pageable);

    /**
     * Get all the Factcheck with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<FactcheckDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" factcheck.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FactcheckDTO> findOne(String id);

    /**
     * Delete the "id" factcheck.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the factcheck corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FactcheckDTO> search(String query, Pageable pageable);

    /**
     * Get the factcheck by clientId and slug.
     *
     * @param clientId the clientId of the FactcheckDTO
     * @param slug the slug of the FactcheckDTO
     * @return Optional<FactcheckDTO> factcheck by clientId and slug
     */
    Optional<FactcheckDTO> findByClientIdAndSlug(String clientId, String slug);
}
