package com.factly.dega.web.rest;

import com.factly.dega.FactcheckApp;

import com.factly.dega.domain.Factcheck;
import com.factly.dega.domain.Claim;
import com.factly.dega.repository.FactcheckRepository;
import com.factly.dega.repository.search.FactcheckSearchRepository;
import com.factly.dega.service.ClaimService;
import com.factly.dega.service.FactcheckService;
import com.factly.dega.service.dto.FactcheckDTO;
import com.factly.dega.service.mapper.FactcheckMapper;
import com.factly.dega.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.factly.dega.web.rest.TestUtil.sameInstant;
import static com.factly.dega.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FactcheckResource REST controller.
 *
 * @see FactcheckResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactcheckApp.class)
public class FactcheckResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INTRODUCTION = "AAAAAAAAAA";
    private static final String UPDATED_INTRODUCTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_EXCERPT = "AAAAAAAAAA";
    private static final String UPDATED_EXCERPT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISHED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISHED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_FEATURED = false;
    private static final Boolean UPDATED_FEATURED = true;

    private static final Boolean DEFAULT_STICKY = false;
    private static final Boolean UPDATED_STICKY = true;

    private static final String DEFAULT_UPDATES = "AAAAAAAAAA";
    private static final String UPDATED_UPDATES = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURED_MEDIA = "AAAAAAAAAA";
    private static final String UPDATED_FEATURED_MEDIA = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FactcheckRepository factcheckRepository;

    @Mock
    private FactcheckRepository factcheckRepositoryMock;

    @Autowired
    private FactcheckMapper factcheckMapper;


    @Mock
    private FactcheckService factcheckServiceMock;

    @Autowired
    private FactcheckService factcheckService;

    /**
     * This repository is mocked in the com.factly.dega.repository.search test package.
     *
     * @see com.factly.dega.repository.search.FactcheckSearchRepositoryMockConfiguration
     */
    @Autowired
    private FactcheckSearchRepository mockFactcheckSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restFactcheckMockMvc;

    private Factcheck factcheck;
    private ClaimService claimService;

    @Autowired
    private RestTemplate restTemplate;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FactcheckResource factcheckResource = new FactcheckResource(factcheckService, claimService, restTemplate, "");
        this.restFactcheckMockMvc = MockMvcBuilders.standaloneSetup(factcheckResource)
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
    public static Factcheck createEntity() {
        Factcheck factcheck = new Factcheck()
            .title(DEFAULT_TITLE)
            .clientId(DEFAULT_CLIENT_ID)
            .introduction(DEFAULT_INTRODUCTION)
            .summary(DEFAULT_SUMMARY)
            .excerpt(DEFAULT_EXCERPT)
            .publishedDate(DEFAULT_PUBLISHED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .featured(DEFAULT_FEATURED)
            .sticky(DEFAULT_STICKY)
            .updates(DEFAULT_UPDATES)
            .slug(DEFAULT_SLUG)
            .password(DEFAULT_PASSWORD)
            .featuredMedia(DEFAULT_FEATURED_MEDIA)
            .subTitle(DEFAULT_SUB_TITLE)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        Claim claim = ClaimResourceIntTest.createEntity();
        claim.setId("fixed-id-for-tests");
        factcheck.getClaims().add(claim);
        return factcheck;
    }

    @Before
    public void initTest() {
        factcheckRepository.deleteAll();
        factcheck = createEntity();
    }

    @Test
    public void createFactcheck() throws Exception {
        int databaseSizeBeforeCreate = factcheckRepository.findAll().size();

        // Create the Factcheck
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);
        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isCreated());

        // Validate the Factcheck in the database
        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeCreate + 1);
        Factcheck testFactcheck = factcheckList.get(factcheckList.size() - 1);
        assertThat(testFactcheck.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactcheck.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testFactcheck.getIntroduction()).isEqualTo(DEFAULT_INTRODUCTION);
        assertThat(testFactcheck.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testFactcheck.getExcerpt()).isEqualTo(DEFAULT_EXCERPT);
        assertThat(testFactcheck.getPublishedDate()).isEqualTo(DEFAULT_PUBLISHED_DATE);
        assertThat(testFactcheck.getLastUpdatedDate().toLocalDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE.toLocalDate());
        assertThat(testFactcheck.isFeatured()).isEqualTo(DEFAULT_FEATURED);
        assertThat(testFactcheck.isSticky()).isEqualTo(DEFAULT_STICKY);
        assertThat(testFactcheck.getUpdates()).isEqualTo(DEFAULT_UPDATES);
        assertThat(testFactcheck.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testFactcheck.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testFactcheck.getFeaturedMedia()).isEqualTo(DEFAULT_FEATURED_MEDIA);
        assertThat(testFactcheck.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testFactcheck.getCreatedDate().toLocalDate()).isEqualTo(UPDATED_CREATED_DATE.toLocalDate());

        // Validate the Factcheck in Elasticsearch
        verify(mockFactcheckSearchRepository, times(1)).save(testFactcheck);
    }

    @Test
    public void createFactcheckWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factcheckRepository.findAll().size();

        // Create the Factcheck with an existing ID
        factcheck.setId("existing_id");
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factcheck in the database
        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeCreate);

        // Validate the Factcheck in Elasticsearch
        verify(mockFactcheckSearchRepository, times(0)).save(factcheck);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = factcheckRepository.findAll().size();
        // set the field null
        factcheck.setTitle(null);

        // Create the Factcheck, which fails.
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isBadRequest());

        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = factcheckRepository.findAll().size();
        // set the field null
        factcheck.setClientId(null);

        // Create the Factcheck, which fails.
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isBadRequest());

        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSummaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = factcheckRepository.findAll().size();
        // set the field null
        factcheck.setSummary(null);

        // Create the Factcheck, which fails.
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isBadRequest());

        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPublishedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = factcheckRepository.findAll().size();
        // set the field null
        factcheck.setPublishedDate(null);

        // Create the Factcheck, which fails.
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isBadRequest());

        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastUpdatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = factcheckRepository.findAll().size();
        // set the field null
        factcheck.setLastUpdatedDate(null);

        // Create the Factcheck, which fails.
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isCreated());

        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = factcheckRepository.findAll().size();
        // set the field null
        factcheck.setSlug(null);

        // Create the Factcheck, which fails.
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isBadRequest());

        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = factcheckRepository.findAll().size();
        // set the field null
        factcheck.setCreatedDate(null);

        // Create the Factcheck, which fails.
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        restFactcheckMockMvc.perform(post("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isCreated());

        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    public void getAllFactchecks() throws Exception {
        // Initialize the database
        factcheckRepository.save(factcheck);

        // Get all the factcheckList
        restFactcheckMockMvc.perform(get("/api/factchecks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factcheck.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION.toString())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].excerpt").value(hasItem(DEFAULT_EXCERPT.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].featured").value(hasItem(DEFAULT_FEATURED.booleanValue())))
            .andExpect(jsonPath("$.[*].sticky").value(hasItem(DEFAULT_STICKY.booleanValue())))
            .andExpect(jsonPath("$.[*].updates").value(hasItem(DEFAULT_UPDATES.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].featuredMedia").value(hasItem(DEFAULT_FEATURED_MEDIA.toString())))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    public void getAllFactchecksWithEagerRelationshipsIsEnabled() throws Exception {
        FactcheckResource factcheckResource = new FactcheckResource(factcheckServiceMock, claimService, restTemplate, "");
        when(factcheckServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restFactcheckMockMvc = MockMvcBuilders.standaloneSetup(factcheckResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFactcheckMockMvc.perform(get("/api/factchecks?eagerload=true"))
        .andExpect(status().isOk());

        verify(factcheckServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllFactchecksWithEagerRelationshipsIsNotEnabled() throws Exception {
        FactcheckResource factcheckResource = new FactcheckResource(factcheckServiceMock, claimService, restTemplate, "");
            when(factcheckServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restFactcheckMockMvc = MockMvcBuilders.standaloneSetup(factcheckResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFactcheckMockMvc.perform(get("/api/factchecks?eagerload=true"))
        .andExpect(status().isOk());

            verify(factcheckServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getFactcheck() throws Exception {
        // Initialize the database
        factcheckRepository.save(factcheck);

        // Get the factcheck
        restFactcheckMockMvc.perform(get("/api/factchecks/{id}", factcheck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(factcheck.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.introduction").value(DEFAULT_INTRODUCTION.toString()))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.excerpt").value(DEFAULT_EXCERPT.toString()))
            .andExpect(jsonPath("$.publishedDate").value(sameInstant(DEFAULT_PUBLISHED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.featured").value(DEFAULT_FEATURED.booleanValue()))
            .andExpect(jsonPath("$.sticky").value(DEFAULT_STICKY.booleanValue()))
            .andExpect(jsonPath("$.updates").value(DEFAULT_UPDATES.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.featuredMedia").value(DEFAULT_FEATURED_MEDIA.toString()))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    public void getNonExistingFactcheck() throws Exception {
        // Get the factcheck
        restFactcheckMockMvc.perform(get("/api/factchecks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFactcheck() throws Exception {
        // Initialize the database
        factcheckRepository.save(factcheck);

        int databaseSizeBeforeUpdate = factcheckRepository.findAll().size();

        // Update the factcheck
        Factcheck updatedFactcheck = factcheckRepository.findById(factcheck.getId()).get();
        updatedFactcheck
            .title(UPDATED_TITLE)
            .clientId(UPDATED_CLIENT_ID)
            .introduction(UPDATED_INTRODUCTION)
            .summary(UPDATED_SUMMARY)
            .excerpt(UPDATED_EXCERPT)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .featured(UPDATED_FEATURED)
            .sticky(UPDATED_STICKY)
            .updates(UPDATED_UPDATES)
            .slug(UPDATED_SLUG)
            .password(UPDATED_PASSWORD)
            .featuredMedia(UPDATED_FEATURED_MEDIA)
            .subTitle(UPDATED_SUB_TITLE)
            .createdDate(UPDATED_CREATED_DATE);
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(updatedFactcheck);

        restFactcheckMockMvc.perform(put("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isOk());

        // Validate the Factcheck in the database
        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeUpdate);
        Factcheck testFactcheck = factcheckList.get(factcheckList.size() - 1);
        assertThat(testFactcheck.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactcheck.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testFactcheck.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
        assertThat(testFactcheck.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testFactcheck.getExcerpt()).isEqualTo(UPDATED_EXCERPT);
        assertThat(testFactcheck.getPublishedDate()).isEqualTo(UPDATED_PUBLISHED_DATE);
        assertThat(testFactcheck.getLastUpdatedDate().toLocalDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE.toLocalDate());
        assertThat(testFactcheck.isFeatured()).isEqualTo(UPDATED_FEATURED);
        assertThat(testFactcheck.isSticky()).isEqualTo(UPDATED_STICKY);
        assertThat(testFactcheck.getUpdates()).isEqualTo(UPDATED_UPDATES);
        assertThat(testFactcheck.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testFactcheck.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testFactcheck.getFeaturedMedia()).isEqualTo(UPDATED_FEATURED_MEDIA);
        assertThat(testFactcheck.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testFactcheck.getCreatedDate().toLocalDate()).isEqualTo(UPDATED_CREATED_DATE.toLocalDate());

        // Validate the Factcheck in Elasticsearch
        verify(mockFactcheckSearchRepository, times(1)).save(testFactcheck);
    }

    @Test
    public void updateNonExistingFactcheck() throws Exception {
        int databaseSizeBeforeUpdate = factcheckRepository.findAll().size();

        // Create the Factcheck
        FactcheckDTO factcheckDTO = factcheckMapper.toDto(factcheck);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactcheckMockMvc.perform(put("/api/factchecks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factcheckDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factcheck in the database
        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Factcheck in Elasticsearch
        verify(mockFactcheckSearchRepository, times(0)).save(factcheck);
    }

    @Test
    public void deleteFactcheck() throws Exception {
        // Initialize the database
        factcheckRepository.save(factcheck);

        int databaseSizeBeforeDelete = factcheckRepository.findAll().size();

        // Get the factcheck
        restFactcheckMockMvc.perform(delete("/api/factchecks/{id}", factcheck.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Factcheck> factcheckList = factcheckRepository.findAll();
        assertThat(factcheckList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Factcheck in Elasticsearch
        verify(mockFactcheckSearchRepository, times(1)).deleteById(factcheck.getId());
    }

    @Test
    public void searchFactcheck() throws Exception {
        // Initialize the database
        factcheckRepository.save(factcheck);
        when(mockFactcheckSearchRepository.search(queryStringQuery("id:" + factcheck.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(factcheck), PageRequest.of(0, 1), 1));
        // Search the factcheck
        restFactcheckMockMvc.perform(get("/api/_search/factchecks?query=id:" + factcheck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factcheck.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION.toString())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].excerpt").value(hasItem(DEFAULT_EXCERPT.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].featured").value(hasItem(DEFAULT_FEATURED.booleanValue())))
            .andExpect(jsonPath("$.[*].sticky").value(hasItem(DEFAULT_STICKY.booleanValue())))
            .andExpect(jsonPath("$.[*].updates").value(hasItem(DEFAULT_UPDATES.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].featuredMedia").value(hasItem(DEFAULT_FEATURED_MEDIA.toString())))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factcheck.class);
        Factcheck factcheck1 = new Factcheck();
        factcheck1.setId("id1");
        Factcheck factcheck2 = new Factcheck();
        factcheck2.setId(factcheck1.getId());
        assertThat(factcheck1).isEqualTo(factcheck2);
        factcheck2.setId("id2");
        assertThat(factcheck1).isNotEqualTo(factcheck2);
        factcheck1.setId(null);
        assertThat(factcheck1).isNotEqualTo(factcheck2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactcheckDTO.class);
        FactcheckDTO factcheckDTO1 = new FactcheckDTO();
        factcheckDTO1.setId("id1");
        FactcheckDTO factcheckDTO2 = new FactcheckDTO();
        assertThat(factcheckDTO1).isNotEqualTo(factcheckDTO2);
        factcheckDTO2.setId(factcheckDTO1.getId());
        assertThat(factcheckDTO1).isEqualTo(factcheckDTO2);
        factcheckDTO2.setId("id2");
        assertThat(factcheckDTO1).isNotEqualTo(factcheckDTO2);
        factcheckDTO1.setId(null);
        assertThat(factcheckDTO1).isNotEqualTo(factcheckDTO2);
    }
}
