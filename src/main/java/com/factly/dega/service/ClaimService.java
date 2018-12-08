package com.factly.dega.service;

import com.factly.dega.service.dto.ClaimDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Claim.
 */
public interface ClaimService {

    /**
     * Save a claim.
     *
     * @param claimDTO the entity to save
     * @return the persisted entity
     */
    ClaimDTO save(ClaimDTO claimDTO);

    /**
     * Get all the claims.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClaimDTO> findAll(Pageable pageable);


    /**
     * Get the "id" claim.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ClaimDTO> findOne(String id);

    /**
     * Delete the "id" claim.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the claim corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClaimDTO> search(String query, Pageable pageable);

    /**
     * Get the claim by clientId and slug.
     *
     * @param clientId the clientId of the ClaimDTO
     * @param slug the slug of the ClaimDTO
     * @return Optional<ClaimDTO> claim by clientId and slug
     */
    Optional<ClaimDTO> findByClientIdAndSlug(String clientId, String slug);
}
