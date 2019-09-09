package com.factly.dega.service.impl;

import com.factly.dega.service.ClaimantService;
import com.factly.dega.domain.Claimant;
import com.factly.dega.repository.ClaimantRepository;
import com.factly.dega.repository.search.ClaimantSearchRepository;
import com.factly.dega.service.dto.ClaimantDTO;
import com.factly.dega.service.dto.MediaDTO;
import com.factly.dega.service.mapper.ClaimantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Claimant.
 */
@Service
public class ClaimantServiceImpl implements ClaimantService {

    private final Logger log = LoggerFactory.getLogger(ClaimantServiceImpl.class);

    private final ClaimantRepository claimantRepository;

    private final ClaimantMapper claimantMapper;

    private final ClaimantSearchRepository claimantSearchRepository;

    private final RestTemplate restTemplate;

    private final String coreServiceUrl;

    public ClaimantServiceImpl(ClaimantRepository claimantRepository, ClaimantMapper claimantMapper, ClaimantSearchRepository claimantSearchRepository,
                               RestTemplate restTemplate, @Value("${dega.core.url}") String coreServiceUrl) {
        this.claimantRepository = claimantRepository;
        this.claimantMapper = claimantMapper;
        this.claimantSearchRepository = claimantSearchRepository;
        this.restTemplate = restTemplate;
        this.coreServiceUrl = coreServiceUrl;
    }

    /**
     * Save a claimant.
     *
     * @param claimantDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ClaimantDTO save(ClaimantDTO claimantDTO) {
        log.debug("Request to save Claimant : {}", claimantDTO);

        Claimant claimant = claimantMapper.toEntity(claimantDTO);
        claimant = claimantRepository.save(claimant);
        ClaimantDTO result = claimantMapper.toDto(claimant);
        if(result.getMedia() != null && !result.getMedia().getId().isEmpty()){
            result.setMedia(getMediaDTO(result.getMedia().getId()));
        }
        claimantSearchRepository.save(claimant);
        return result;
    }

    /**
     * Get all the claimants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ClaimantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Claimants");
        return claimantRepository.findAll(pageable)
            .map(claimantMapper::toDto);
    }


    /**
     * Get one claimant by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ClaimantDTO> findOne(String id) {
        log.debug("Request to get Claimant : {}", id);
        return claimantRepository.findById(id)
            .map(claimantMapper::toDto);
    }

    /**
     * Delete the claimant by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Claimant : {}", id);
        claimantRepository.deleteById(id);
        claimantSearchRepository.deleteById(id);
    }

    /**
     * Search for the claimant corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ClaimantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Claimants for query {}", query);
        return claimantSearchRepository.search(queryStringQuery(query), pageable)
            .map(claimantMapper::toDto);
    }

    /**
     * Get the claimant by clientId and slug.
     *
     * @param clientId the clientId of the ClaimantDTO
     * @param slug the slug of the ClaimantDTO
     * @return Optional<ClaimantDTO> claimant by clientId and slug
     */
    @Override
    public Optional<ClaimantDTO> findByClientIdAndSlug(String clientId, String slug) {
        log.debug("Request to get post  by clienId : {} and slug : {}", clientId, slug);
        return claimantRepository.findByClientIdAndSlug(clientId, slug)
            .map(claimantMapper::toDto);
    }

    /**
     * Get all claimants by clientId.
     *
     * @param clientId the client id of the entity
     * @return the entity
     */
    @Override
    public Page<ClaimantDTO> findByClientId(String clientId, Pageable pageable) {
        log.debug("Request to get Claimants : {}", clientId);
        return claimantRepository.findByClientId(clientId, pageable).map(claimantMapper::toDto);
    }

    private MediaDTO getMediaDTO(String mediaId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<MediaDTO> response = restTemplate.exchange(
            coreServiceUrl+"/media/"+ mediaId, HttpMethod.GET, httpEntity, MediaDTO.class);
        return response.getBody();
    }
}
