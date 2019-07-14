package com.factly.dega.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.factly.dega.config.Constants;
import com.factly.dega.service.ClaimService;
import com.factly.dega.web.rest.errors.BadRequestAlertException;
import com.factly.dega.web.rest.util.CommonUtil;
import com.factly.dega.web.rest.util.HeaderUtil;
import com.factly.dega.web.rest.util.PaginationUtil;
import com.factly.dega.service.dto.ClaimDTO;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Claim.
 */
@RestController
@RequestMapping("/api")
public class ClaimResource {

    private final Logger log = LoggerFactory.getLogger(ClaimResource.class);

    private static final String ENTITY_NAME = "factcheckClaim";

    private final ClaimService claimService;

    public ClaimResource(ClaimService claimService) {
        this.claimService = claimService;
    }

    /**
     * POST  /claims : Create a new claim.
     *
     * @param claimDTO the claimDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new claimDTO, or with status 400 (Bad Request) if the claim has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/claims")
    @Timed
    public ResponseEntity<ClaimDTO> createClaim(@Valid @RequestBody ClaimDTO claimDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Claim : {}", claimDTO);
        if (claimDTO.getId() != null) {
            throw new BadRequestAlertException("A new claim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            claimDTO.setClientId((String) obj);
        } else {
            throw new BadRequestAlertException("You are not allowed to update this client entries", ENTITY_NAME, "invalidclient");
        }
        claimDTO.setSlug(getSlug(claimDTO.getClientId(), CommonUtil.removeSpecialCharsFromString(claimDTO.getClaim())));
        claimDTO.setCreatedDate(ZonedDateTime.now());
        claimDTO.setLastUpdatedDate(ZonedDateTime.now());
        ClaimDTO result = claimService.save(claimDTO);
        return ResponseEntity.created(new URI("/api/claims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /claims : Updates an existing claim.
     *
     * @param claimDTO the claimDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated claimDTO,
     * or with status 400 (Bad Request) if the claimDTO is not valid,
     * or with status 500 (Internal Server Error) if the claimDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/claims")
    @Timed
    public ResponseEntity<ClaimDTO> updateClaim(@Valid @RequestBody ClaimDTO claimDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update Claim : {}", claimDTO);
        if (claimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<ClaimDTO> savedClaimData = claimService.findOne(claimDTO.getId());
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (savedClaimData.isPresent()) {
            if (savedClaimData.get().getClientId() != obj){
                throw new BadRequestAlertException("You are not allowed to update this client entries", ENTITY_NAME, "invalidclient");
            }
            claimDTO.setClientId(savedClaimData.get().getClientId());
        } else {
            throw new BadRequestAlertException("Claim does not exist", ENTITY_NAME, "invalidclaim");
        }

        // If a slug is updated from client.
        if (!claimDTO.getSlug().equals(savedClaimData.get().getSlug())) {
            // Slug needs to be verified in db, if a slug exists with the same text then add auto extension of digit.
            claimDTO.setSlug(getSlug((String) obj, CommonUtil.removeSpecialCharsFromString(claimDTO.getSlug())));
        }

        claimDTO.setLastUpdatedDate(ZonedDateTime.now());
        ClaimDTO result = claimService.save(claimDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, claimDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /claims : get all the claims.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of claims in body
     */
    @GetMapping("/claims")
    @Timed
    public ResponseEntity<List<ClaimDTO>> getAllClaims(Pageable pageable, HttpServletRequest request) {
        log.debug("REST request to get a page of Claims");
        Page<ClaimDTO> page = new PageImpl<>(new ArrayList<>());
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            String clientId = (String) obj;
            page = claimService.findByClientId(clientId, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/claims");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /claims/:id : get the "id" claim.
     *
     * @param id the id of the claimDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the claimDTO, or with status 404 (Not Found)
     */
    @GetMapping("/claims/{id}")
    @Timed
    public ResponseEntity<ClaimDTO> getClaim(@PathVariable String id) {
        log.debug("REST request to get Claim : {}", id);
        Optional<ClaimDTO> claimDTO = claimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(claimDTO);
    }

    /**
     * DELETE  /claims/:id : delete the "id" claim.
     *
     * @param id the id of the claimDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/claims/{id}")
    @Timed
    public ResponseEntity<Void> deleteClaim(@PathVariable String id) {
        log.debug("REST request to delete Claim : {}", id);
        claimService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/claims?query=:query : search for the claim corresponding
     * to the query.
     *
     * @param query the query of the claim search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/claims")
    @Timed
    public ResponseEntity<List<ClaimDTO>> searchClaims(@RequestParam String query, Pageable pageable, HttpServletRequest request) {
        log.debug("REST request to search for a page of Claims for query {}", query);
        String clientId = (String) request.getSession().getAttribute(Constants.CLIENT_ID);
        Page<ClaimDTO> page = claimService.search(query, pageable);
        List<ClaimDTO> claimDTOList = page.getContent().stream().filter(claimDTO -> claimDTO.getClientId().equals(clientId)).collect(Collectors.toList());
        Page<ClaimDTO> claimDTOPage = new PageImpl<>(claimDTOList, pageable, claimDTOList.size());
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, claimDTOPage, "/api/_search/claims");
        return new ResponseEntity<>(claimDTOPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /claimbyslug/:slug : get the claim.
     *
     * @param slug the slug of the ClaimDTO
     * @return Optional<ClaimDTO> claim by clientId and slug
     */
    @GetMapping("/claimbyslug/{slug}")
    @Timed
    public Optional<ClaimDTO> getClaimBySlug(@PathVariable String slug, HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        String clientId = null;
        if (obj != null) {
            clientId = (String) obj;
        }
        log.debug("REST request to get Claim by clienId : {} and slug : {}", clientId, slug);
        Optional<ClaimDTO> claimDTO = claimService.findByClientIdAndSlug(clientId, slug);
        return claimDTO;
    }

    public String getSlug(String clientId, String claim){
        if(clientId != null && claim != null){
            int slugExtention = 0;
            return createSlug(clientId, claim, claim, slugExtention);
        }
        return null;
    }

    public String createSlug(String clientId, String slug, String tempSlug, int slugExtention){
        Optional<ClaimDTO> claimDTO = claimService.findByClientIdAndSlug(clientId, slug);
        if(claimDTO.isPresent()){
            slugExtention += 1;
            slug = tempSlug + slugExtention;
            return createSlug(clientId, slug, tempSlug, slugExtention);
        }
        return slug;
    }

}
