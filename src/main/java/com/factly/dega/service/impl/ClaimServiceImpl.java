package com.factly.dega.service.impl;

import com.factly.dega.service.ClaimService;
import com.factly.dega.domain.Claim;
import com.factly.dega.repository.ClaimRepository;
import com.factly.dega.repository.search.ClaimSearchRepository;
import com.factly.dega.service.dto.ClaimDTO;
import com.factly.dega.service.mapper.ClaimMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Claim.
 */
@Service
public class ClaimServiceImpl implements ClaimService {

    private final Logger log = LoggerFactory.getLogger(ClaimServiceImpl.class);

    private final ClaimRepository claimRepository;

    private final ClaimMapper claimMapper;

    private final ClaimSearchRepository claimSearchRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository, ClaimMapper claimMapper, ClaimSearchRepository claimSearchRepository) {
        this.claimRepository = claimRepository;
        this.claimMapper = claimMapper;
        this.claimSearchRepository = claimSearchRepository;
    }

    /**
     * Save a claim.
     *
     * @param claimDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ClaimDTO save(ClaimDTO claimDTO) {
        log.debug("Request to save Claim : {}", claimDTO);

        Claim claim = claimMapper.toEntity(claimDTO);
        claim = claimRepository.save(claim);
        ClaimDTO result = claimMapper.toDto(claim);
        claimSearchRepository.save(claim);
        return result;
    }

    /**
     * Get all the claims.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ClaimDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Claims");
        return claimRepository.findAll(pageable)
            .map(claimMapper::toDto);
    }


    /**
     * Get one claim by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ClaimDTO> findOne(String id) {
        log.debug("Request to get Claim : {}", id);
        return claimRepository.findById(id)
            .map(claimMapper::toDto);
    }

    /**
     * Delete the claim by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Claim : {}", id);
        claimRepository.deleteById(id);
        claimSearchRepository.deleteById(id);
    }

    /**
     * Search for the claim corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ClaimDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Claims for query {}", query);
        return claimSearchRepository.search(queryStringQuery(query), pageable)
            .map(claimMapper::toDto);
    }
}
