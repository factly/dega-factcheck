package com.factly.dega.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.factly.dega.service.FactCheckService;
import com.factly.dega.web.rest.errors.BadRequestAlertException;
import com.factly.dega.web.rest.util.HeaderUtil;
import com.factly.dega.web.rest.util.PaginationUtil;
import com.factly.dega.service.dto.FactCheckDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FactCheck.
 */
@RestController
@RequestMapping("/api")
public class FactCheckResource {

    private final Logger log = LoggerFactory.getLogger(FactCheckResource.class);

    private static final String ENTITY_NAME = "factcheckFactCheck";

    private final FactCheckService factCheckService;

    public FactCheckResource(FactCheckService factCheckService) {
        this.factCheckService = factCheckService;
    }

    /**
     * POST  /fact-checks : Create a new factCheck.
     *
     * @param factCheckDTO the factCheckDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new factCheckDTO, or with status 400 (Bad Request) if the factCheck has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fact-checks")
    @Timed
    public ResponseEntity<FactCheckDTO> createFactCheck(@Valid @RequestBody FactCheckDTO factCheckDTO) throws URISyntaxException {
        log.debug("REST request to save FactCheck : {}", factCheckDTO);
        if (factCheckDTO.getId() != null) {
            throw new BadRequestAlertException("A new factCheck cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactCheckDTO result = factCheckService.save(factCheckDTO);
        return ResponseEntity.created(new URI("/api/fact-checks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fact-checks : Updates an existing factCheck.
     *
     * @param factCheckDTO the factCheckDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated factCheckDTO,
     * or with status 400 (Bad Request) if the factCheckDTO is not valid,
     * or with status 500 (Internal Server Error) if the factCheckDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fact-checks")
    @Timed
    public ResponseEntity<FactCheckDTO> updateFactCheck(@Valid @RequestBody FactCheckDTO factCheckDTO) throws URISyntaxException {
        log.debug("REST request to update FactCheck : {}", factCheckDTO);
        if (factCheckDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FactCheckDTO result = factCheckService.save(factCheckDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, factCheckDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fact-checks : get all the factChecks.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of factChecks in body
     */
    @GetMapping("/fact-checks")
    @Timed
    public ResponseEntity<List<FactCheckDTO>> getAllFactChecks(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of FactChecks");
        Page<FactCheckDTO> page;
        if (eagerload) {
            page = factCheckService.findAllWithEagerRelationships(pageable);
        } else {
            page = factCheckService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/fact-checks?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /fact-checks/:id : get the "id" factCheck.
     *
     * @param id the id of the factCheckDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the factCheckDTO, or with status 404 (Not Found)
     */
    @GetMapping("/fact-checks/{id}")
    @Timed
    public ResponseEntity<FactCheckDTO> getFactCheck(@PathVariable String id) {
        log.debug("REST request to get FactCheck : {}", id);
        Optional<FactCheckDTO> factCheckDTO = factCheckService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factCheckDTO);
    }

    /**
     * DELETE  /fact-checks/:id : delete the "id" factCheck.
     *
     * @param id the id of the factCheckDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fact-checks/{id}")
    @Timed
    public ResponseEntity<Void> deleteFactCheck(@PathVariable String id) {
        log.debug("REST request to delete FactCheck : {}", id);
        factCheckService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/fact-checks?query=:query : search for the factCheck corresponding
     * to the query.
     *
     * @param query the query of the factCheck search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/fact-checks")
    @Timed
    public ResponseEntity<List<FactCheckDTO>> searchFactChecks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FactChecks for query {}", query);
        Page<FactCheckDTO> page = factCheckService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fact-checks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
