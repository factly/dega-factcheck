package com.factly.dega.service.impl;

import com.factly.dega.service.FactCheckService;
import com.factly.dega.domain.FactCheck;
import com.factly.dega.repository.FactCheckRepository;
import com.factly.dega.repository.search.FactCheckSearchRepository;
import com.factly.dega.service.dto.FactCheckDTO;
import com.factly.dega.service.mapper.FactCheckMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FactCheck.
 */
@Service
public class FactCheckServiceImpl implements FactCheckService {

    private final Logger log = LoggerFactory.getLogger(FactCheckServiceImpl.class);

    private final FactCheckRepository factCheckRepository;

    private final FactCheckMapper factCheckMapper;

    private final FactCheckSearchRepository factCheckSearchRepository;

    public FactCheckServiceImpl(FactCheckRepository factCheckRepository, FactCheckMapper factCheckMapper, FactCheckSearchRepository factCheckSearchRepository) {
        this.factCheckRepository = factCheckRepository;
        this.factCheckMapper = factCheckMapper;
        this.factCheckSearchRepository = factCheckSearchRepository;
    }

    /**
     * Save a factCheck.
     *
     * @param factCheckDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FactCheckDTO save(FactCheckDTO factCheckDTO) {
        log.debug("Request to save FactCheck : {}", factCheckDTO);

        FactCheck factCheck = factCheckMapper.toEntity(factCheckDTO);
        factCheck = factCheckRepository.save(factCheck);
        FactCheckDTO result = factCheckMapper.toDto(factCheck);
        factCheckSearchRepository.save(factCheck);
        return result;
    }

    /**
     * Get all the factChecks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<FactCheckDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FactChecks");
        return factCheckRepository.findAll(pageable)
            .map(factCheckMapper::toDto);
    }

    /**
     * Get all the FactCheck with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<FactCheckDTO> findAllWithEagerRelationships(Pageable pageable) {
        return factCheckRepository.findAllWithEagerRelationships(pageable).map(factCheckMapper::toDto);
    }
    

    /**
     * Get one factCheck by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<FactCheckDTO> findOne(String id) {
        log.debug("Request to get FactCheck : {}", id);
        return factCheckRepository.findOneWithEagerRelationships(id)
            .map(factCheckMapper::toDto);
    }

    /**
     * Delete the factCheck by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete FactCheck : {}", id);
        factCheckRepository.deleteById(id);
        factCheckSearchRepository.deleteById(id);
    }

    /**
     * Search for the factCheck corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<FactCheckDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FactChecks for query {}", query);
        return factCheckSearchRepository.search(queryStringQuery(query), pageable)
            .map(factCheckMapper::toDto);
    }
}
