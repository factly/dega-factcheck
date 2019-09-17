package com.factly.dega.web.rest;

import com.factly.dega.FactcheckApp;

import com.factly.dega.domain.Claim;
import com.factly.dega.domain.Rating;
import com.factly.dega.domain.Claimant;
import com.factly.dega.repository.ClaimRepository;
import com.factly.dega.repository.search.ClaimSearchRepository;
import com.factly.dega.service.ClaimService;
import com.factly.dega.service.dto.ClaimDTO;
import com.factly.dega.service.mapper.ClaimMapper;
import com.factly.dega.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.factly.dega.web.rest.TestUtil.clientIDSessionAttributes;
import static com.factly.dega.web.rest.TestUtil.sameInstant;
import static com.factly.dega.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClaimResource REST controller.
 *
 * @see ClaimResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactcheckApp.class)
public class ClaimResourceIntTest {

    private static final String DEFAULT_CLAIM = "AAAAAAAAAA";
    private static final String UPDATED_CLAIM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CLAIM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLAIM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CLAIM_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_CLAIM_SOURCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CHECKED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECKED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REVIEW_SOURCES = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_SOURCES = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEW = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEW_TAG_LINE = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_TAG_LINE = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ClaimMapper claimMapper;

    @Autowired
    private ClaimService claimService;

    /**
     * This repository is mocked in the com.factly.dega.repository.search test package.
     *
     * @see com.factly.dega.repository.search.ClaimSearchRepositoryMockConfiguration
     */
    @Autowired
    private ClaimSearchRepository mockClaimSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restClaimMockMvc;

    private Claim claim;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClaimResource claimResource = new ClaimResource(claimService);
        this.restClaimMockMvc = MockMvcBuilders.standaloneSetup(claimResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createEntity() {
        Claim claim = new Claim()
            .claim(DEFAULT_CLAIM)
            .description(DEFAULT_DESCRIPTION)
            .claimDate(DEFAULT_CLAIM_DATE)
            .claimSource(DEFAULT_CLAIM_SOURCE)
            .checkedDate(DEFAULT_CHECKED_DATE)
            .reviewSources(DEFAULT_REVIEW_SOURCES)
            .review(DEFAULT_REVIEW)
            .reviewTagLine(DEFAULT_REVIEW_TAG_LINE)
            .clientId(DEFAULT_CLIENT_ID)
            .slug(DEFAULT_SLUG)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE);
        // Add required entity
        Rating rating = RatingResourceIntTest.createEntity();
        rating.setId("fixed-id-for-tests");
        claim.setRating(rating);
        // Add required entity
        Claimant claimant = ClaimantResourceIntTest.createEntity();
        claimant.setId("fixed-id-for-tests");
        claim.setClaimant(claimant);
        return claim;
    }

    @Before
    public void initTest() {
        claimRepository.deleteAll();
        claim = createEntity();
    }

    @Test
    public void createClaim() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);
        restClaimMockMvc.perform(post("/api/claims").sessionAttrs(clientIDSessionAttributes())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isCreated());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate + 1);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getClaim()).isEqualTo(DEFAULT_CLAIM);
        assertThat(testClaim.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClaim.getClaimDate()).isEqualTo(DEFAULT_CLAIM_DATE);
        assertThat(testClaim.getClaimSource()).isEqualTo(DEFAULT_CLAIM_SOURCE);
        assertThat(testClaim.getCheckedDate()).isEqualTo(DEFAULT_CHECKED_DATE);
        assertThat(testClaim.getReviewSources()).isEqualTo(DEFAULT_REVIEW_SOURCES);
        assertThat(testClaim.getReview()).isEqualTo(DEFAULT_REVIEW);
        assertThat(testClaim.getReviewTagLine()).isEqualTo(DEFAULT_REVIEW_TAG_LINE);
        assertThat(testClaim.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testClaim.getSlug()).isEqualToIgnoringCase(DEFAULT_SLUG);
        assertThat(testClaim.getCreatedDate().toLocalDate()).isEqualTo(UPDATED_CREATED_DATE.toLocalDate());
        assertThat(testClaim.getLastUpdatedDate().toLocalDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE.toLocalDate());

        // Validate the Claim in Elasticsearch
        verify(mockClaimSearchRepository, times(1)).save(testClaim);
    }

    @Test
    public void createClaimWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();

        // Create the Claim with an existing ID
        claim.setId("existing_id");
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate);

        // Validate the Claim in Elasticsearch
        verify(mockClaimSearchRepository, times(0)).save(claim);
    }

    @Test
    public void checkClaimIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setClaim(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setDescription(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkClaimSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setClaimSource(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCheckedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setCheckedDate(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkReviewSourcesIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setReviewSources(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkReviewIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setReview(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setClientId(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setSlug(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setCreatedDate(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims").sessionAttrs(clientIDSessionAttributes())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isCreated());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    public void checkLastUpdatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setLastUpdatedDate(null);

        // Create the Claim, which fails.
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        restClaimMockMvc.perform(post("/api/claims").sessionAttrs(clientIDSessionAttributes())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isCreated());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    public void getAllClaims() throws Exception {
        // Initialize the database
        claimRepository.save(claim);

        // Get all the claimList
        restClaimMockMvc.perform(get("/api/claims?sort=id,desc").sessionAttrs(clientIDSessionAttributes()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId())))
            .andExpect(jsonPath("$.[*].claim").value(hasItem(DEFAULT_CLAIM.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].claimDate").value(hasItem(DEFAULT_CLAIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].claimSource").value(hasItem(DEFAULT_CLAIM_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].checkedDate").value(hasItem(DEFAULT_CHECKED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reviewSources").value(hasItem(DEFAULT_REVIEW_SOURCES.toString())))
            .andExpect(jsonPath("$.[*].review").value(hasItem(DEFAULT_REVIEW.toString())))
            .andExpect(jsonPath("$.[*].reviewTagLine").value(hasItem(DEFAULT_REVIEW_TAG_LINE.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    public void getClaim() throws Exception {
        // Initialize the database
        claimRepository.save(claim);

        // Get the claim
        restClaimMockMvc.perform(get("/api/claims/{id}", claim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(claim.getId()))
            .andExpect(jsonPath("$.claim").value(DEFAULT_CLAIM.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.claimDate").value(DEFAULT_CLAIM_DATE.toString()))
            .andExpect(jsonPath("$.claimSource").value(DEFAULT_CLAIM_SOURCE.toString()))
            .andExpect(jsonPath("$.checkedDate").value(DEFAULT_CHECKED_DATE.toString()))
            .andExpect(jsonPath("$.reviewSources").value(DEFAULT_REVIEW_SOURCES.toString()))
            .andExpect(jsonPath("$.review").value(DEFAULT_REVIEW.toString()))
            .andExpect(jsonPath("$.reviewTagLine").value(DEFAULT_REVIEW_TAG_LINE.toString()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)));
    }

    @Test
    public void getNonExistingClaim() throws Exception {
        // Get the claim
        restClaimMockMvc.perform(get("/api/claims/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateClaim() throws Exception {
        // Initialize the database
        claimRepository.save(claim);

        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Update the claim
        Claim updatedClaim = claimRepository.findById(claim.getId()).get();
        updatedClaim
            .claim(UPDATED_CLAIM)
            .description(UPDATED_DESCRIPTION)
            .claimDate(UPDATED_CLAIM_DATE)
            .claimSource(UPDATED_CLAIM_SOURCE)
            .checkedDate(UPDATED_CHECKED_DATE)
            .reviewSources(UPDATED_REVIEW_SOURCES)
            .review(UPDATED_REVIEW)
            .reviewTagLine(UPDATED_REVIEW_TAG_LINE)
            .clientId(UPDATED_CLIENT_ID)
            .slug(UPDATED_SLUG)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE);
        ClaimDTO claimDTO = claimMapper.toDto(updatedClaim);

        restClaimMockMvc.perform(put("/api/claims").sessionAttrs(clientIDSessionAttributes())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isOk());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getClaim()).isEqualTo(UPDATED_CLAIM);
        assertThat(testClaim.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClaim.getClaimDate()).isEqualTo(UPDATED_CLAIM_DATE);
        assertThat(testClaim.getClaimSource()).isEqualTo(UPDATED_CLAIM_SOURCE);
        assertThat(testClaim.getCheckedDate()).isEqualTo(UPDATED_CHECKED_DATE);
        assertThat(testClaim.getReviewSources()).isEqualTo(UPDATED_REVIEW_SOURCES);
        assertThat(testClaim.getReview()).isEqualTo(UPDATED_REVIEW);
        assertThat(testClaim.getReviewTagLine()).isEqualTo(UPDATED_REVIEW_TAG_LINE);
        assertThat(testClaim.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testClaim.getSlug()).isEqualToIgnoringCase(UPDATED_SLUG);
        assertThat(testClaim.getCreatedDate().toLocalDate()).isEqualTo(UPDATED_CREATED_DATE.toLocalDate());
        assertThat(testClaim.getLastUpdatedDate().toLocalDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE.toLocalDate());

        // Validate the Claim in Elasticsearch
        verify(mockClaimSearchRepository, times(1)).save(testClaim);
    }

    @Test
    public void updateNonExistingClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimMockMvc.perform(put("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Claim in Elasticsearch
        verify(mockClaimSearchRepository, times(0)).save(claim);
    }

    @Test
    public void deleteClaim() throws Exception {
        // Initialize the database
        claimRepository.save(claim);

        int databaseSizeBeforeDelete = claimRepository.findAll().size();

        // Get the claim
        restClaimMockMvc.perform(delete("/api/claims/{id}", claim.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Claim in Elasticsearch
        verify(mockClaimSearchRepository, times(1)).deleteById(claim.getId());
    }

    @Test
    public void searchClaim() throws Exception {
        // Initialize the database
        claimRepository.save(claim);
        when(mockClaimSearchRepository.search(queryStringQuery("id:" + claim.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(claim), PageRequest.of(0, 1), 1));
        // Search the claim
        restClaimMockMvc.perform(get("/api/_search/claims?query=id:" + claim.getId()).sessionAttrs(clientIDSessionAttributes()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId())))
            .andExpect(jsonPath("$.[*].claim").value(hasItem(DEFAULT_CLAIM.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].claimDate").value(hasItem(DEFAULT_CLAIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].claimSource").value(hasItem(DEFAULT_CLAIM_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].checkedDate").value(hasItem(DEFAULT_CHECKED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reviewSources").value(hasItem(DEFAULT_REVIEW_SOURCES.toString())))
            .andExpect(jsonPath("$.[*].review").value(hasItem(DEFAULT_REVIEW.toString())))
            .andExpect(jsonPath("$.[*].reviewTagLine").value(hasItem(DEFAULT_REVIEW_TAG_LINE.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Claim.class);
        Claim claim1 = new Claim();
        claim1.setId("id1");
        Claim claim2 = new Claim();
        claim2.setId(claim1.getId());
        assertThat(claim1).isEqualTo(claim2);
        claim2.setId("id2");
        assertThat(claim1).isNotEqualTo(claim2);
        claim1.setId(null);
        assertThat(claim1).isNotEqualTo(claim2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClaimDTO.class);
        ClaimDTO claimDTO1 = new ClaimDTO();
        claimDTO1.setId("id1");
        ClaimDTO claimDTO2 = new ClaimDTO();
        assertThat(claimDTO1).isNotEqualTo(claimDTO2);
        claimDTO2.setId(claimDTO1.getId());
        assertThat(claimDTO1).isEqualTo(claimDTO2);
        claimDTO2.setId("id2");
        assertThat(claimDTO1).isNotEqualTo(claimDTO2);
        claimDTO1.setId(null);
        assertThat(claimDTO1).isNotEqualTo(claimDTO2);
    }
}
