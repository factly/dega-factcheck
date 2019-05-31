package com.factly.dega.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.factly.dega.config.Constants;
import com.factly.dega.service.RatingService;
import com.factly.dega.web.rest.errors.BadRequestAlertException;
import com.factly.dega.web.rest.util.CommonUtil;
import com.factly.dega.web.rest.util.HeaderUtil;
import com.factly.dega.web.rest.util.PaginationUtil;
import com.factly.dega.service.dto.RatingDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Rating.
 */
@RestController
@RequestMapping("/api")
public class RatingResource {

    private final Logger log = LoggerFactory.getLogger(RatingResource.class);

    private static final String ENTITY_NAME = "factcheckRating";

    private final RatingService ratingService;

    public RatingResource(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * POST  /ratings : Create a new rating.
     *
     * @param ratingDTO the ratingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ratingDTO, or with status 400 (Bad Request) if the rating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ratings")
    @Timed
    public ResponseEntity<RatingDTO> createRating(@Valid @RequestBody RatingDTO ratingDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Rating : {}", ratingDTO);
        if (ratingDTO.getId() != null) {
            throw new BadRequestAlertException("A new rating cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (ratingDTO.isIsDefault()) {
            log.info("IsDefault is enabled on this rating, setting client id to default");
            ratingDTO.setClientId(Constants.DEFAULT_CLIENTID);
        } else {
            ratingDTO.setClientId(null);
            Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
            if (obj != null) {
                log.info("Setting client id from session attribute");
                ratingDTO.setClientId((String) obj);
            }
        }

        ratingDTO.setSlug(getSlug(ratingDTO.getClientId(), CommonUtil.removeSpecialCharsFromString(ratingDTO.getName())));
        ratingDTO.setCreatedDate(ZonedDateTime.now());
        ratingDTO.setLastUpdatedDate(ZonedDateTime.now());
        RatingDTO result = ratingService.save(ratingDTO);
        return ResponseEntity.created(new URI("/api/ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ratings : Updates an existing rating.
     *
     * @param ratingDTO the ratingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ratingDTO,
     * or with status 400 (Bad Request) if the ratingDTO is not valid,
     * or with status 500 (Internal Server Error) if the ratingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ratings")
    @Timed
    public ResponseEntity<RatingDTO> updateRating(@Valid @RequestBody RatingDTO ratingDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update Rating : {}", ratingDTO);
        if (ratingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (ratingDTO.isIsDefault()) {
            log.info("IsDefault is enabled on this rating, setting client id to default");
            ratingDTO.setClientId(Constants.DEFAULT_CLIENTID);
        } else {
            ratingDTO.setClientId(null);
            Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
            if (obj != null) {
                log.info("Setting client id from session attribute");
                ratingDTO.setClientId((String) obj);
            }
        }
        ratingDTO.setLastUpdatedDate(ZonedDateTime.now());
        RatingDTO result = ratingService.save(ratingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ratingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ratings : get all the ratings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ratings in body
     */
    @GetMapping("/ratings")
    @Timed
    public ResponseEntity<List<RatingDTO>> getAllRatings(Pageable pageable) {
        log.debug("REST request to get a page of Ratings");
        Page<RatingDTO> page = ratingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ratings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /ratings/:id : get the "id" rating.
     *
     * @param id the id of the ratingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ratingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<RatingDTO> getRating(@PathVariable String id) {
        log.debug("REST request to get Rating : {}", id);
        Optional<RatingDTO> ratingDTO = ratingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ratingDTO);
    }

    /**
     * DELETE  /ratings/:id : delete the "id" rating.
     *
     * @param id the id of the ratingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteRating(@PathVariable String id) {
        log.debug("REST request to delete Rating : {}", id);
        ratingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/ratings?query=:query : search for the rating corresponding
     * to the query.
     *
     * @param query the query of the rating search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ratings")
    @Timed
    public ResponseEntity<List<RatingDTO>> searchRatings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ratings for query {}", query);
        Page<RatingDTO> page = ratingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ratings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ratingbyslug/:slug : get the rating.
     *
     * @param slug the slug of the RatingDTO
     * @return Optional<RatingDTO> rating by clientId and slug
     */
    @GetMapping("/ratingbyslug/{slug}")
    @Timed
    public Optional<RatingDTO> getRatingBySlug(@PathVariable String slug, HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        String clientId = null;
        if (obj != null) {
            clientId = (String) obj;
        }
        log.debug("REST request to get Rating by clienId : {} and slug : {}", clientId, slug);
        Optional<RatingDTO> ratingDTO = ratingService.findByClientIdAndSlug(clientId, slug);
        return ratingDTO;
    }

    public String getSlug(String clientId, String name){
        if(clientId != null && name != null){
            int slugExtention = 0;
            return createSlug(clientId, name, name, slugExtention);
        }
        return null;
    }

    public String createSlug(String clientId, String slug, String tempSlug, int slugExtention){
        Optional<RatingDTO> ratingDTO = ratingService.findByClientIdAndSlug(clientId, slug);
        if(ratingDTO.isPresent()){
            slugExtention += 1;
            slug = tempSlug + slugExtention;
            return createSlug(clientId, slug, tempSlug, slugExtention);
        }
        return slug;
    }

}
