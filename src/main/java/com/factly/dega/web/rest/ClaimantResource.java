package com.factly.dega.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.factly.dega.config.Constants;
import com.factly.dega.service.ClaimantService;
import com.factly.dega.web.rest.errors.BadRequestAlertException;
import com.factly.dega.web.rest.util.CommonUtil;
import com.factly.dega.web.rest.util.HeaderUtil;
import com.factly.dega.web.rest.util.PaginationUtil;
import com.factly.dega.service.dto.ClaimantDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Claimant.
 */
@RestController
@RequestMapping("/api")
public class ClaimantResource {

    private final Logger log = LoggerFactory.getLogger(ClaimantResource.class);

    private static final String ENTITY_NAME = "factcheckClaimant";

    private final ClaimantService claimantService;

    public ClaimantResource(ClaimantService claimantService) {
        this.claimantService = claimantService;
    }

    /**
     * POST  /claimants : Create a new claimant.
     *
     * @param claimantDTO the claimantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new claimantDTO, or with status 400 (Bad Request) if the claimant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/claimants")
    @Timed
    public ResponseEntity<ClaimantDTO> createClaimant(@Valid @RequestBody ClaimantDTO claimantDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Claimant : {}", claimantDTO);
        if (claimantDTO.getId() != null) {
            throw new BadRequestAlertException("A new claimant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        claimantDTO.setClientId(null);
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            claimantDTO.setClientId((String) obj);
        }
        claimantDTO.setSlug(getSlug((String) obj, CommonUtil.removeSpecialCharsFromString(claimantDTO.getName())));
        claimantDTO.setCreatedDate(ZonedDateTime.now());
        claimantDTO.setLastUpdatedDate(ZonedDateTime.now());
        ClaimantDTO result = claimantService.save(claimantDTO);
        return ResponseEntity.created(new URI("/api/claimants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /claimants : Updates an existing claimant.
     *
     * @param claimantDTO the claimantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated claimantDTO,
     * or with status 400 (Bad Request) if the claimantDTO is not valid,
     * or with status 500 (Internal Server Error) if the claimantDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/claimants")
    @Timed
    public ResponseEntity<ClaimantDTO> updateClaimant(@Valid @RequestBody ClaimantDTO claimantDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update Claimant : {}", claimantDTO);
        if (claimantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        claimantDTO.setClientId(null);
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            claimantDTO.setClientId((String) obj);
        }
        claimantDTO.setLastUpdatedDate(ZonedDateTime.now());
        ClaimantDTO result = claimantService.save(claimantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, claimantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /claimants : get all the claimants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of claimants in body
     */
    @GetMapping("/claimants")
    @Timed
    public ResponseEntity<List<ClaimantDTO>> getAllClaimants(Pageable pageable, HttpServletRequest request) {
        log.debug("REST request to get a page of Claimants");
        Page<ClaimantDTO> page = new PageImpl<>(new ArrayList<>());
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            String clientId = (String) obj;
            page = claimantService.findByClientId(clientId, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/claimants");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /claimants/:id : get the "id" claimant.
     *
     * @param id the id of the claimantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the claimantDTO, or with status 404 (Not Found)
     */
    @GetMapping("/claimants/{id}")
    @Timed
    public ResponseEntity<ClaimantDTO> getClaimant(@PathVariable String id) {
        log.debug("REST request to get Claimant : {}", id);
        Optional<ClaimantDTO> claimantDTO = claimantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(claimantDTO);
    }

    /**
     * DELETE  /claimants/:id : delete the "id" claimant.
     *
     * @param id the id of the claimantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/claimants/{id}")
    @Timed
    public ResponseEntity<Void> deleteClaimant(@PathVariable String id) {
        log.debug("REST request to delete Claimant : {}", id);
        claimantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/claimants?query=:query : search for the claimant corresponding
     * to the query.
     *
     * @param query the query of the claimant search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/claimants")
    @Timed
    public ResponseEntity<List<ClaimantDTO>> searchClaimants(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Claimants for query {}", query);
        Page<ClaimantDTO> page = claimantService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/claimants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /claimantbyslug/:slug : get the claimant.
     *
     * @param slug the slug of the ClaimantDTO
     * @return Optional<ClaimantDTO> claimant by clientId and slug
     */
    @GetMapping("/claimantbyslug/{slug}")
    @Timed
    public Optional<ClaimantDTO> getClaimantBySlug(@PathVariable String slug, HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        String clientId = null;
        if (obj != null) {
            clientId = (String) obj;
        }
        log.debug("REST request to get Claimant by clienId : {} and slug : {}", clientId, slug);
        Optional<ClaimantDTO> claimantDTO = claimantService.findByClientIdAndSlug(clientId, slug);
        return claimantDTO;
    }

    public String getSlug(String clientId, String name){
        if(clientId != null && name != null){
            int slugExtention = 0;
            return createSlug(clientId, name, name, slugExtention);
        }
        return null;
    }

    public String createSlug(String clientId, String slug, String tempSlug, int slugExtention){
        Optional<ClaimantDTO> claimantDTO = claimantService.findByClientIdAndSlug(clientId, slug);
        if(claimantDTO.isPresent()){
            slugExtention += 1;
            slug = tempSlug + slugExtention;
            return createSlug(clientId, slug, tempSlug, slugExtention);
        }
        return slug;
    }
}
