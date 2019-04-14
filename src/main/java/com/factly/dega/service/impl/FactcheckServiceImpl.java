package com.factly.dega.service.impl;

import com.factly.dega.service.FactcheckService;
import com.factly.dega.domain.Factcheck;
import com.factly.dega.repository.FactcheckRepository;
import com.factly.dega.repository.search.FactcheckSearchRepository;
import com.factly.dega.service.dto.FactcheckDTO;
import com.factly.dega.service.mapper.FactcheckMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Factcheck.
 */
@Service
public class FactcheckServiceImpl implements FactcheckService {

    private final Logger log = LoggerFactory.getLogger(FactcheckServiceImpl.class);

    private final FactcheckRepository factcheckRepository;

    private final FactcheckMapper factcheckMapper;

    private final FactcheckSearchRepository factcheckSearchRepository;

    public FactcheckServiceImpl(FactcheckRepository factcheckRepository, FactcheckMapper factcheckMapper, FactcheckSearchRepository factcheckSearchRepository) {
        this.factcheckRepository = factcheckRepository;
        this.factcheckMapper = factcheckMapper;
        this.factcheckSearchRepository = factcheckSearchRepository;
    }

    /**
     * Save a factcheck.
     *
     * @param factcheckDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FactcheckDTO save(FactcheckDTO factcheckDTO) {
        log.debug("Request to save Factcheck : {}", factcheckDTO);

        Factcheck factcheck = factcheckMapper.toEntity(factcheckDTO);
        factcheck = factcheckRepository.save(factcheck);
        FactcheckDTO result = factcheckMapper.toDto(factcheck);
        factcheckSearchRepository.save(factcheck);
        return result;
    }

    /**
     * Get all the factchecks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<FactcheckDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factchecks");
        return factcheckRepository.findAll(pageable)
            .map(factcheckMapper::toDto);
    }

    /**
     * Get all the Factcheck with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<FactcheckDTO> findAllWithEagerRelationships(Pageable pageable) {
        return factcheckRepository.findAllWithEagerRelationships(pageable).map(factcheckMapper::toDto);
    }
    

    /**
     * Get one factcheck by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<FactcheckDTO> findOne(String id) {
        log.debug("Request to get Factcheck : {}", id);
        return factcheckRepository.findOneWithEagerRelationships(id)
            .map(factcheckMapper::toDto);
    }

    /**
     * Delete the factcheck by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Factcheck : {}", id);
        factcheckRepository.deleteById(id);
        factcheckSearchRepository.deleteById(id);
    }

    /**
     * Search for the factcheck corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<FactcheckDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Factchecks for query {}", query);
        return factcheckSearchRepository.search(queryStringQuery(query), pageable)
            .map(factcheckMapper::toDto);
    }

    /**
     * Get the factcheck by clientId and slug.
     *
     * @param clientId the clientId of the FactcheckDTO
     * @param slug the slug of the FactcheckDTO
     * @return Optional<FactcheckDTO> factcheck by clientId and slug
     */
    @Override
    public Optional<FactcheckDTO> findByClientIdAndSlug(String clientId, String slug) {
        log.debug("Request to get post  by clienId : {} and slug : {}", clientId, slug);
        return factcheckRepository.findByClientIdAndSlug(clientId, slug)
            .map(factcheckMapper::toDto);
    }

    /**
     * Get all factchecks by clientId.
     *
     * @param clientId the client id of the entity
     * @return the entity
     */
    @Override
    public Page<FactcheckDTO> findByClientId(String clientId, Pageable pageable) {
        log.debug("Request to get Factchecks : {}", clientId);
        return factcheckRepository.findByClientId(clientId, pageable).map(factcheckMapper::toDto);
    }
}
