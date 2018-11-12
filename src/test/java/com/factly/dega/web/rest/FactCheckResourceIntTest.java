package com.factly.dega.web.rest;

import com.factly.dega.FactcheckApp;

import com.factly.dega.domain.FactCheck;
import com.factly.dega.domain.Claim;
import com.factly.dega.repository.FactCheckRepository;
import com.factly.dega.repository.search.FactCheckSearchRepository;
import com.factly.dega.service.FactCheckService;
import com.factly.dega.service.dto.FactCheckDTO;
import com.factly.dega.service.mapper.FactCheckMapper;
import com.factly.dega.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
 * Test class for the FactCheckResource REST controller.
 *
 * @see FactCheckResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactcheckApp.class)
public class FactCheckResourceIntTest {

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

    private static final ZonedDateTime DEFAULT_PUBLISHED_DATE_GMT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISHED_DATE_GMT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE_GMT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE_GMT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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

    @Autowired
    private FactCheckRepository factCheckRepository;

    @Mock
    private FactCheckRepository factCheckRepositoryMock;

    @Autowired
    private FactCheckMapper factCheckMapper;
    

    @Mock
    private FactCheckService factCheckServiceMock;

    @Autowired
    private FactCheckService factCheckService;

    /**
     * This repository is mocked in the com.factly.dega.repository.search test package.
     *
     * @see com.factly.dega.repository.search.FactCheckSearchRepositoryMockConfiguration
     */
    @Autowired
    private FactCheckSearchRepository mockFactCheckSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restFactCheckMockMvc;

    private FactCheck factCheck;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FactCheckResource factCheckResource = new FactCheckResource(factCheckService);
        this.restFactCheckMockMvc = MockMvcBuilders.standaloneSetup(factCheckResource)
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
    public static FactCheck createEntity() {
        FactCheck factCheck = new FactCheck()
            .title(DEFAULT_TITLE)
            .clientId(DEFAULT_CLIENT_ID)
            .introduction(DEFAULT_INTRODUCTION)
            .summary(DEFAULT_SUMMARY)
            .excerpt(DEFAULT_EXCERPT)
            .publishedDate(DEFAULT_PUBLISHED_DATE)
            .publishedDateGMT(DEFAULT_PUBLISHED_DATE_GMT)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .lastUpdatedDateGMT(DEFAULT_LAST_UPDATED_DATE_GMT)
            .featured(DEFAULT_FEATURED)
            .sticky(DEFAULT_STICKY)
            .updates(DEFAULT_UPDATES)
            .slug(DEFAULT_SLUG)
            .password(DEFAULT_PASSWORD)
            .featuredMedia(DEFAULT_FEATURED_MEDIA)
            .subTitle(DEFAULT_SUB_TITLE);
        // Add required entity
        Claim claim = ClaimResourceIntTest.createEntity();
        claim.setId("fixed-id-for-tests");
        factCheck.getClaims().add(claim);
        return factCheck;
    }

    @Before
    public void initTest() {
        factCheckRepository.deleteAll();
        factCheck = createEntity();
    }

    @Test
    public void createFactCheck() throws Exception {
        int databaseSizeBeforeCreate = factCheckRepository.findAll().size();

        // Create the FactCheck
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);
        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isCreated());

        // Validate the FactCheck in the database
        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeCreate + 1);
        FactCheck testFactCheck = factCheckList.get(factCheckList.size() - 1);
        assertThat(testFactCheck.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactCheck.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testFactCheck.getIntroduction()).isEqualTo(DEFAULT_INTRODUCTION);
        assertThat(testFactCheck.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testFactCheck.getExcerpt()).isEqualTo(DEFAULT_EXCERPT);
        assertThat(testFactCheck.getPublishedDate()).isEqualTo(DEFAULT_PUBLISHED_DATE);
        assertThat(testFactCheck.getPublishedDateGMT()).isEqualTo(DEFAULT_PUBLISHED_DATE_GMT);
        assertThat(testFactCheck.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testFactCheck.getLastUpdatedDateGMT()).isEqualTo(DEFAULT_LAST_UPDATED_DATE_GMT);
        assertThat(testFactCheck.isFeatured()).isEqualTo(DEFAULT_FEATURED);
        assertThat(testFactCheck.isSticky()).isEqualTo(DEFAULT_STICKY);
        assertThat(testFactCheck.getUpdates()).isEqualTo(DEFAULT_UPDATES);
        assertThat(testFactCheck.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testFactCheck.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testFactCheck.getFeaturedMedia()).isEqualTo(DEFAULT_FEATURED_MEDIA);
        assertThat(testFactCheck.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);

        // Validate the FactCheck in Elasticsearch
        verify(mockFactCheckSearchRepository, times(1)).save(testFactCheck);
    }

    @Test
    public void createFactCheckWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factCheckRepository.findAll().size();

        // Create the FactCheck with an existing ID
        factCheck.setId("existing_id");
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FactCheck in the database
        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeCreate);

        // Validate the FactCheck in Elasticsearch
        verify(mockFactCheckSearchRepository, times(0)).save(factCheck);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setTitle(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setClientId(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSummaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setSummary(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPublishedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setPublishedDate(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPublishedDateGMTIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setPublishedDateGMT(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastUpdatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setLastUpdatedDate(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastUpdatedDateGMTIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setLastUpdatedDateGMT(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = factCheckRepository.findAll().size();
        // set the field null
        factCheck.setSlug(null);

        // Create the FactCheck, which fails.
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        restFactCheckMockMvc.perform(post("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllFactChecks() throws Exception {
        // Initialize the database
        factCheckRepository.save(factCheck);

        // Get all the factCheckList
        restFactCheckMockMvc.perform(get("/api/fact-checks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factCheck.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION.toString())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].excerpt").value(hasItem(DEFAULT_EXCERPT.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE))))
            .andExpect(jsonPath("$.[*].publishedDateGMT").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE_GMT))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDateGMT").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE_GMT))))
            .andExpect(jsonPath("$.[*].featured").value(hasItem(DEFAULT_FEATURED.booleanValue())))
            .andExpect(jsonPath("$.[*].sticky").value(hasItem(DEFAULT_STICKY.booleanValue())))
            .andExpect(jsonPath("$.[*].updates").value(hasItem(DEFAULT_UPDATES.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].featuredMedia").value(hasItem(DEFAULT_FEATURED_MEDIA.toString())))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE.toString())));
    }
    
    public void getAllFactChecksWithEagerRelationshipsIsEnabled() throws Exception {
        FactCheckResource factCheckResource = new FactCheckResource(factCheckServiceMock);
        when(factCheckServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restFactCheckMockMvc = MockMvcBuilders.standaloneSetup(factCheckResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFactCheckMockMvc.perform(get("/api/fact-checks?eagerload=true"))
        .andExpect(status().isOk());

        verify(factCheckServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllFactChecksWithEagerRelationshipsIsNotEnabled() throws Exception {
        FactCheckResource factCheckResource = new FactCheckResource(factCheckServiceMock);
            when(factCheckServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restFactCheckMockMvc = MockMvcBuilders.standaloneSetup(factCheckResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFactCheckMockMvc.perform(get("/api/fact-checks?eagerload=true"))
        .andExpect(status().isOk());

            verify(factCheckServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getFactCheck() throws Exception {
        // Initialize the database
        factCheckRepository.save(factCheck);

        // Get the factCheck
        restFactCheckMockMvc.perform(get("/api/fact-checks/{id}", factCheck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(factCheck.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.introduction").value(DEFAULT_INTRODUCTION.toString()))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.excerpt").value(DEFAULT_EXCERPT.toString()))
            .andExpect(jsonPath("$.publishedDate").value(sameInstant(DEFAULT_PUBLISHED_DATE)))
            .andExpect(jsonPath("$.publishedDateGMT").value(sameInstant(DEFAULT_PUBLISHED_DATE_GMT)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDateGMT").value(sameInstant(DEFAULT_LAST_UPDATED_DATE_GMT)))
            .andExpect(jsonPath("$.featured").value(DEFAULT_FEATURED.booleanValue()))
            .andExpect(jsonPath("$.sticky").value(DEFAULT_STICKY.booleanValue()))
            .andExpect(jsonPath("$.updates").value(DEFAULT_UPDATES.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.featuredMedia").value(DEFAULT_FEATURED_MEDIA.toString()))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE.toString()));
    }

    @Test
    public void getNonExistingFactCheck() throws Exception {
        // Get the factCheck
        restFactCheckMockMvc.perform(get("/api/fact-checks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFactCheck() throws Exception {
        // Initialize the database
        factCheckRepository.save(factCheck);

        int databaseSizeBeforeUpdate = factCheckRepository.findAll().size();

        // Update the factCheck
        FactCheck updatedFactCheck = factCheckRepository.findById(factCheck.getId()).get();
        updatedFactCheck
            .title(UPDATED_TITLE)
            .clientId(UPDATED_CLIENT_ID)
            .introduction(UPDATED_INTRODUCTION)
            .summary(UPDATED_SUMMARY)
            .excerpt(UPDATED_EXCERPT)
            .publishedDate(UPDATED_PUBLISHED_DATE)
            .publishedDateGMT(UPDATED_PUBLISHED_DATE_GMT)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .lastUpdatedDateGMT(UPDATED_LAST_UPDATED_DATE_GMT)
            .featured(UPDATED_FEATURED)
            .sticky(UPDATED_STICKY)
            .updates(UPDATED_UPDATES)
            .slug(UPDATED_SLUG)
            .password(UPDATED_PASSWORD)
            .featuredMedia(UPDATED_FEATURED_MEDIA)
            .subTitle(UPDATED_SUB_TITLE);
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(updatedFactCheck);

        restFactCheckMockMvc.perform(put("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isOk());

        // Validate the FactCheck in the database
        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeUpdate);
        FactCheck testFactCheck = factCheckList.get(factCheckList.size() - 1);
        assertThat(testFactCheck.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactCheck.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testFactCheck.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
        assertThat(testFactCheck.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testFactCheck.getExcerpt()).isEqualTo(UPDATED_EXCERPT);
        assertThat(testFactCheck.getPublishedDate()).isEqualTo(UPDATED_PUBLISHED_DATE);
        assertThat(testFactCheck.getPublishedDateGMT()).isEqualTo(UPDATED_PUBLISHED_DATE_GMT);
        assertThat(testFactCheck.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testFactCheck.getLastUpdatedDateGMT()).isEqualTo(UPDATED_LAST_UPDATED_DATE_GMT);
        assertThat(testFactCheck.isFeatured()).isEqualTo(UPDATED_FEATURED);
        assertThat(testFactCheck.isSticky()).isEqualTo(UPDATED_STICKY);
        assertThat(testFactCheck.getUpdates()).isEqualTo(UPDATED_UPDATES);
        assertThat(testFactCheck.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testFactCheck.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testFactCheck.getFeaturedMedia()).isEqualTo(UPDATED_FEATURED_MEDIA);
        assertThat(testFactCheck.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);

        // Validate the FactCheck in Elasticsearch
        verify(mockFactCheckSearchRepository, times(1)).save(testFactCheck);
    }

    @Test
    public void updateNonExistingFactCheck() throws Exception {
        int databaseSizeBeforeUpdate = factCheckRepository.findAll().size();

        // Create the FactCheck
        FactCheckDTO factCheckDTO = factCheckMapper.toDto(factCheck);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactCheckMockMvc.perform(put("/api/fact-checks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factCheckDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FactCheck in the database
        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FactCheck in Elasticsearch
        verify(mockFactCheckSearchRepository, times(0)).save(factCheck);
    }

    @Test
    public void deleteFactCheck() throws Exception {
        // Initialize the database
        factCheckRepository.save(factCheck);

        int databaseSizeBeforeDelete = factCheckRepository.findAll().size();

        // Get the factCheck
        restFactCheckMockMvc.perform(delete("/api/fact-checks/{id}", factCheck.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FactCheck> factCheckList = factCheckRepository.findAll();
        assertThat(factCheckList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FactCheck in Elasticsearch
        verify(mockFactCheckSearchRepository, times(1)).deleteById(factCheck.getId());
    }

    @Test
    public void searchFactCheck() throws Exception {
        // Initialize the database
        factCheckRepository.save(factCheck);
        when(mockFactCheckSearchRepository.search(queryStringQuery("id:" + factCheck.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(factCheck), PageRequest.of(0, 1), 1));
        // Search the factCheck
        restFactCheckMockMvc.perform(get("/api/_search/fact-checks?query=id:" + factCheck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factCheck.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION.toString())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].excerpt").value(hasItem(DEFAULT_EXCERPT.toString())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE))))
            .andExpect(jsonPath("$.[*].publishedDateGMT").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE_GMT))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDateGMT").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE_GMT))))
            .andExpect(jsonPath("$.[*].featured").value(hasItem(DEFAULT_FEATURED.booleanValue())))
            .andExpect(jsonPath("$.[*].sticky").value(hasItem(DEFAULT_STICKY.booleanValue())))
            .andExpect(jsonPath("$.[*].updates").value(hasItem(DEFAULT_UPDATES.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].featuredMedia").value(hasItem(DEFAULT_FEATURED_MEDIA.toString())))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactCheck.class);
        FactCheck factCheck1 = new FactCheck();
        factCheck1.setId("id1");
        FactCheck factCheck2 = new FactCheck();
        factCheck2.setId(factCheck1.getId());
        assertThat(factCheck1).isEqualTo(factCheck2);
        factCheck2.setId("id2");
        assertThat(factCheck1).isNotEqualTo(factCheck2);
        factCheck1.setId(null);
        assertThat(factCheck1).isNotEqualTo(factCheck2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactCheckDTO.class);
        FactCheckDTO factCheckDTO1 = new FactCheckDTO();
        factCheckDTO1.setId("id1");
        FactCheckDTO factCheckDTO2 = new FactCheckDTO();
        assertThat(factCheckDTO1).isNotEqualTo(factCheckDTO2);
        factCheckDTO2.setId(factCheckDTO1.getId());
        assertThat(factCheckDTO1).isEqualTo(factCheckDTO2);
        factCheckDTO2.setId("id2");
        assertThat(factCheckDTO1).isNotEqualTo(factCheckDTO2);
        factCheckDTO1.setId(null);
        assertThat(factCheckDTO1).isNotEqualTo(factCheckDTO2);
    }
}
