package com.factly.dega.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.factly.dega.config.Constants;
import com.factly.dega.service.ClaimService;
import com.factly.dega.service.FactcheckService;
import com.factly.dega.service.dto.ClaimDTO;
import com.factly.dega.service.dto.StatusDTO;
import com.factly.dega.web.rest.errors.BadRequestAlertException;
import com.factly.dega.web.rest.util.CommonUtil;
import com.factly.dega.web.rest.util.HeaderUtil;
import com.factly.dega.web.rest.util.PaginationUtil;
import com.factly.dega.service.dto.FactcheckDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * REST controller for managing Factcheck.
 */
@RestController
@RequestMapping("/api")
public class FactcheckResource {

    private final Logger log = LoggerFactory.getLogger(FactcheckResource.class);

    private static final String ENTITY_NAME = "factcheckFactcheck";

    private final FactcheckService factcheckService;

    private final ClaimService claimService;

    private final RestTemplate restTemplate;

    private final String coreServiceUrl;


    public FactcheckResource(FactcheckService factcheckService, ClaimService claimService, RestTemplate restTemplate, @Value("${dega.core.url}") String coreServiceUrl) {
        this.factcheckService = factcheckService;
        this.claimService = claimService;
        this.restTemplate = restTemplate;
        this.coreServiceUrl = coreServiceUrl;
    }

    /**
     * POST  /factchecks : Create a new factcheck.
     *
     * @param factcheckDTO the factcheckDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new factcheckDTO, or with status 400 (Bad Request) if the factcheck has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/factchecks")
    @Timed
    public ResponseEntity<FactcheckDTO> createFactcheck(@Valid @RequestBody FactcheckDTO factcheckDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Factcheck : {}", factcheckDTO);
        if (factcheckDTO.getId() != null) {
            throw new BadRequestAlertException("A new factcheck cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<StatusDTO> statusDTO = getStatusDTO("Draft");
        if(statusDTO != null && statusDTO.get() != null) {
            factcheckDTO.setStatusID(statusDTO.get().getId());
        }

        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            factcheckDTO.setClientId((String) obj);
        }
        factcheckDTO.setSlug(getSlug(factcheckDTO.getClientId(), CommonUtil.removeSpecialCharsFromString(factcheckDTO.getTitle())));
        factcheckDTO.setCreatedDate(ZonedDateTime.now());
        factcheckDTO.setLastUpdatedDate(ZonedDateTime.now());
        Set<ClaimDTO> claimsSet = factcheckDTO.getClaims();
        Set<ClaimDTO> newClaimsSet = new HashSet<ClaimDTO>();
        getCopy(claimsSet, newClaimsSet, obj);

        factcheckDTO.setClaims(newClaimsSet);
        FactcheckDTO result = factcheckService.save(factcheckDTO);
        return ResponseEntity.created(new URI("/api/factchecks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private void getCopy(Set<ClaimDTO> claimsSet, Set<ClaimDTO> newClaimsSet, Object clientId) {
        for(ClaimDTO claimDTO: claimsSet){
            if(claimDTO.getId() != null){
                newClaimsSet.add(claimDTO);
            } else {
                String cId = (clientId != null) ? (String) clientId : null;
                claimDTO.setSlug(getSlug(cId, claimDTO.getClaim()));
                claimDTO.setCreatedDate(ZonedDateTime.now());
                claimDTO.setLastUpdatedDate(ZonedDateTime.now());
                claimDTO.setClientId(cId);
                newClaimsSet.add(claimService.save(claimDTO));
            }
        }
    }

    /**
     * POST  /factchecks : Create a new factcheck.
     *
     * @param factcheckDTO the factcheckDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new factcheckDTO, or with status 400 (Bad Request) if the factcheck has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/publish")
    @Timed
    public ResponseEntity<FactcheckDTO> publishFactcheck(@Valid @RequestBody FactcheckDTO factcheckDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Factcheck : {}", factcheckDTO);
        if (factcheckDTO.getId() != null) {
            throw new BadRequestAlertException("A new factcheck cannot already have an ID", ENTITY_NAME, "idexists");
        }

       Optional<StatusDTO> statusDTO = getStatusDTO("Publish");
        if(statusDTO != null && statusDTO.get() != null) {
            factcheckDTO.setStatusID(statusDTO.get().getId());
        }

        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            factcheckDTO.setClientId((String) obj);
        }
        factcheckDTO.setSlug(getSlug(factcheckDTO.getClientId(), CommonUtil.removeSpecialCharsFromString(factcheckDTO.getTitle())));
        factcheckDTO.setCreatedDate(ZonedDateTime.now());
        factcheckDTO.setLastUpdatedDate(ZonedDateTime.now());
        factcheckDTO.setPublishedDate(ZonedDateTime.now());
        Set<ClaimDTO> claimsSet = factcheckDTO.getClaims();
        Set<ClaimDTO> newClaimsSet = new HashSet<ClaimDTO>();
        getCopy(claimsSet, newClaimsSet, obj);

        factcheckDTO.setClaims(newClaimsSet);
        FactcheckDTO result = factcheckService.save(factcheckDTO);
        return ResponseEntity.created(new URI("/api/factchecks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * PUT  /factchecks : Updates an existing factcheck.
     *
     * @param factcheckDTO the factcheckDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated factcheckDTO,
     * or with status 400 (Bad Request) if the factcheckDTO is not valid,
     * or with status 500 (Internal Server Error) if the factcheckDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/factchecks")
    @Timed
    public ResponseEntity<FactcheckDTO> updateFactcheck(@Valid @RequestBody FactcheckDTO factcheckDTO) throws URISyntaxException {
        log.debug("REST request to update Factcheck : {}", factcheckDTO);
        if (factcheckDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<StatusDTO> statusDTO = getStatusDTO(factcheckDTO.getStatusName());
        if(statusDTO != null && statusDTO.get() != null) {
            factcheckDTO.setStatusID(statusDTO.get().getId());
        }
        factcheckDTO.setLastUpdatedDate(ZonedDateTime.now());
        FactcheckDTO result = factcheckService.save(factcheckDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, factcheckDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /factchecks : get all the factchecks.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of factchecks in body
     */
    @GetMapping("/factchecks")
    @Timed
    public ResponseEntity<List<FactcheckDTO>> getAllFactchecks(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload, HttpServletRequest request) {
        log.debug("REST request to get a page of Factchecks");
        Page<FactcheckDTO> page = new PageImpl<>(new ArrayList<>());
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        if (obj != null) {
            String clientId = (String) obj;
            page = factcheckService.findByClientId(clientId, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/factchecks?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /factchecks/:id : get the "id" factcheck.
     *
     * @param id the id of the factcheckDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the factcheckDTO, or with status 404 (Not Found)
     */
    @GetMapping("/factchecks/{id}")
    @Timed
    public ResponseEntity<FactcheckDTO> getFactcheck(@PathVariable String id) {
        log.debug("REST request to get Factcheck : {}", id);
        Optional<FactcheckDTO> factcheckDTO = factcheckService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factcheckDTO);
    }

    /**
     * DELETE  /factchecks/:id : delete the "id" factcheck.
     *
     * @param id the id of the factcheckDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/factchecks/{id}")
    @Timed
    public ResponseEntity<Void> deleteFactcheck(@PathVariable String id) {
        log.debug("REST request to delete Factcheck : {}", id);
        factcheckService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/factchecks?query=:query : search for the factcheck corresponding
     * to the query.
     *
     * @param query the query of the factcheck search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/factchecks")
    @Timed
    public ResponseEntity<List<FactcheckDTO>> searchFactchecks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Factchecks for query {}", query);
        Page<FactcheckDTO> page = factcheckService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/factchecks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /factcheckbyslug/:slug : get the factcheck.
     *
     * @param slug the slug of the FactcheckDTO
     * @return Optional<FactcheckDTO> factcheck by clientId and slug
     */
    @GetMapping("/factcheckbyslug/{slug}")
    @Timed
    public Optional<FactcheckDTO> getFactcheckBySlug(@PathVariable String slug, HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(Constants.CLIENT_ID);
        String clientId = null;
        if (obj != null) {
            clientId = (String) obj;
        }
        log.debug("REST request to get Factcheck by clienId : {} and slug : {}", clientId, slug);
        Optional<FactcheckDTO> factcheckDTO = factcheckService.findByClientIdAndSlug(clientId, slug);
        return factcheckDTO;
    }

    public String getSlug(String clientId, String claim){
        if(clientId != null && claim != null){
            int slugExtention = 0;
            String tempSlug = CommonUtil.removeSpecialCharsFromString(claim);
            return createSlug(clientId, tempSlug, tempSlug, slugExtention);
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

    private Optional<StatusDTO> getStatusDTO(String status) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<List<StatusDTO>> response = restTemplate.exchange(
            coreServiceUrl+"/statuses", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<StatusDTO>>(){});
        return response.getBody().stream().filter(statusdto -> statusdto.getName().equalsIgnoreCase(status)).findFirst();
    }

}
