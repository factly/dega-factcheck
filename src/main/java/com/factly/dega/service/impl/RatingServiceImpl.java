package com.factly.dega.service.impl;

import com.factly.dega.service.RatingService;
import com.factly.dega.domain.Rating;
import com.factly.dega.repository.RatingRepository;
import com.factly.dega.repository.search.RatingSearchRepository;
import com.factly.dega.service.dto.RatingDTO;
import com.factly.dega.service.mapper.RatingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Rating.
 */
@Service
public class RatingServiceImpl implements RatingService {

    private final Logger log = LoggerFactory.getLogger(RatingServiceImpl.class);

    private final RatingRepository ratingRepository;

    private final RatingMapper ratingMapper;

    private final RatingSearchRepository ratingSearchRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, RatingMapper ratingMapper, RatingSearchRepository ratingSearchRepository) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.ratingSearchRepository = ratingSearchRepository;
    }

    /**
     * Save a rating.
     *
     * @param ratingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RatingDTO save(RatingDTO ratingDTO) {
        log.debug("Request to save Rating : {}", ratingDTO);

        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating = ratingRepository.save(rating);
        RatingDTO result = ratingMapper.toDto(rating);
        ratingSearchRepository.save(rating);
        return result;
    }

    /**
     * Get all the ratings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<RatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ratings");
        return ratingRepository.findAll(pageable)
            .map(ratingMapper::toDto);
    }


    /**
     * Get one rating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<RatingDTO> findOne(String id) {
        log.debug("Request to get Rating : {}", id);
        return ratingRepository.findById(id)
            .map(ratingMapper::toDto);
    }

    /**
     * Delete the rating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
        ratingSearchRepository.deleteById(id);
    }

    /**
     * Search for the rating corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<RatingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ratings for query {}", query);
        return ratingSearchRepository.search(queryStringQuery(query), pageable)
            .map(ratingMapper::toDto);
    }

    /**
     * Get the rating by clientId and slug.
     *
     * @param clientId the clientId of the RatingDTO
     * @param slug the slug of the RatingDTO
     * @return Optional<RatingDTO> rating by clientId and slug
     */
    @Override
    public Optional<RatingDTO> findByClientIdAndSlug(String clientId, String slug) {
        log.debug("Request to get post  by clienId : {} and slug : {}", clientId, slug);
        return ratingRepository.findByClientIdAndSlug(clientId, slug)
            .map(ratingMapper::toDto);
    }
}
