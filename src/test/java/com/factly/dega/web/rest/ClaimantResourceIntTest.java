package com.factly.dega.web.rest;

import com.factly.dega.FactcheckApp;

import com.factly.dega.domain.Claimant;
import com.factly.dega.repository.ClaimantRepository;
import com.factly.dega.repository.search.ClaimantSearchRepository;
import com.factly.dega.service.ClaimantService;
import com.factly.dega.service.dto.ClaimantDTO;
import com.factly.dega.service.mapper.ClaimantMapper;
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

import java.util.Collections;
import java.util.List;


import static com.factly.dega.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClaimantResource REST controller.
 *
 * @see ClaimantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactcheckApp.class)
public class ClaimantResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAG_LINE = "AAAAAAAAAA";
    private static final String UPDATED_TAG_LINE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    @Autowired
    private ClaimantRepository claimantRepository;

    @Autowired
    private ClaimantMapper claimantMapper;
    
    @Autowired
    private ClaimantService claimantService;

    /**
     * This repository is mocked in the com.factly.dega.repository.search test package.
     *
     * @see com.factly.dega.repository.search.ClaimantSearchRepositoryMockConfiguration
     */
    @Autowired
    private ClaimantSearchRepository mockClaimantSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restClaimantMockMvc;

    private Claimant claimant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClaimantResource claimantResource = new ClaimantResource(claimantService);
        this.restClaimantMockMvc = MockMvcBuilders.standaloneSetup(claimantResource)
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
    public static Claimant createEntity() {
        Claimant claimant = new Claimant()
            .name(DEFAULT_NAME)
            .tagLine(DEFAULT_TAG_LINE)
            .description(DEFAULT_DESCRIPTION)
            .imageURL(DEFAULT_IMAGE_URL)
            .clientId(DEFAULT_CLIENT_ID);
        return claimant;
    }

    @Before
    public void initTest() {
        claimantRepository.deleteAll();
        claimant = createEntity();
    }

    @Test
    public void createClaimant() throws Exception {
        int databaseSizeBeforeCreate = claimantRepository.findAll().size();

        // Create the Claimant
        ClaimantDTO claimantDTO = claimantMapper.toDto(claimant);
        restClaimantMockMvc.perform(post("/api/claimants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimantDTO)))
            .andExpect(status().isCreated());

        // Validate the Claimant in the database
        List<Claimant> claimantList = claimantRepository.findAll();
        assertThat(claimantList).hasSize(databaseSizeBeforeCreate + 1);
        Claimant testClaimant = claimantList.get(claimantList.size() - 1);
        assertThat(testClaimant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClaimant.getTagLine()).isEqualTo(DEFAULT_TAG_LINE);
        assertThat(testClaimant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClaimant.getImageURL()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testClaimant.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);

        // Validate the Claimant in Elasticsearch
        verify(mockClaimantSearchRepository, times(1)).save(testClaimant);
    }

    @Test
    public void createClaimantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = claimantRepository.findAll().size();

        // Create the Claimant with an existing ID
        claimant.setId("existing_id");
        ClaimantDTO claimantDTO = claimantMapper.toDto(claimant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClaimantMockMvc.perform(post("/api/claimants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Claimant in the database
        List<Claimant> claimantList = claimantRepository.findAll();
        assertThat(claimantList).hasSize(databaseSizeBeforeCreate);

        // Validate the Claimant in Elasticsearch
        verify(mockClaimantSearchRepository, times(0)).save(claimant);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimantRepository.findAll().size();
        // set the field null
        claimant.setName(null);

        // Create the Claimant, which fails.
        ClaimantDTO claimantDTO = claimantMapper.toDto(claimant);

        restClaimantMockMvc.perform(post("/api/claimants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimantDTO)))
            .andExpect(status().isBadRequest());

        List<Claimant> claimantList = claimantRepository.findAll();
        assertThat(claimantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimantRepository.findAll().size();
        // set the field null
        claimant.setClientId(null);

        // Create the Claimant, which fails.
        ClaimantDTO claimantDTO = claimantMapper.toDto(claimant);

        restClaimantMockMvc.perform(post("/api/claimants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimantDTO)))
            .andExpect(status().isBadRequest());

        List<Claimant> claimantList = claimantRepository.findAll();
        assertThat(claimantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllClaimants() throws Exception {
        // Initialize the database
        claimantRepository.save(claimant);

        // Get all the claimantList
        restClaimantMockMvc.perform(get("/api/claimants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claimant.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].tagLine").value(hasItem(DEFAULT_TAG_LINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageURL").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())));
    }
    
    @Test
    public void getClaimant() throws Exception {
        // Initialize the database
        claimantRepository.save(claimant);

        // Get the claimant
        restClaimantMockMvc.perform(get("/api/claimants/{id}", claimant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(claimant.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.tagLine").value(DEFAULT_TAG_LINE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageURL").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()));
    }

    @Test
    public void getNonExistingClaimant() throws Exception {
        // Get the claimant
        restClaimantMockMvc.perform(get("/api/claimants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateClaimant() throws Exception {
        // Initialize the database
        claimantRepository.save(claimant);

        int databaseSizeBeforeUpdate = claimantRepository.findAll().size();

        // Update the claimant
        Claimant updatedClaimant = claimantRepository.findById(claimant.getId()).get();
        updatedClaimant
            .name(UPDATED_NAME)
            .tagLine(UPDATED_TAG_LINE)
            .description(UPDATED_DESCRIPTION)
            .imageURL(UPDATED_IMAGE_URL)
            .clientId(UPDATED_CLIENT_ID);
        ClaimantDTO claimantDTO = claimantMapper.toDto(updatedClaimant);

        restClaimantMockMvc.perform(put("/api/claimants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimantDTO)))
            .andExpect(status().isOk());

        // Validate the Claimant in the database
        List<Claimant> claimantList = claimantRepository.findAll();
        assertThat(claimantList).hasSize(databaseSizeBeforeUpdate);
        Claimant testClaimant = claimantList.get(claimantList.size() - 1);
        assertThat(testClaimant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClaimant.getTagLine()).isEqualTo(UPDATED_TAG_LINE);
        assertThat(testClaimant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClaimant.getImageURL()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testClaimant.getClientId()).isEqualTo(UPDATED_CLIENT_ID);

        // Validate the Claimant in Elasticsearch
        verify(mockClaimantSearchRepository, times(1)).save(testClaimant);
    }

    @Test
    public void updateNonExistingClaimant() throws Exception {
        int databaseSizeBeforeUpdate = claimantRepository.findAll().size();

        // Create the Claimant
        ClaimantDTO claimantDTO = claimantMapper.toDto(claimant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimantMockMvc.perform(put("/api/claimants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claimantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Claimant in the database
        List<Claimant> claimantList = claimantRepository.findAll();
        assertThat(claimantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Claimant in Elasticsearch
        verify(mockClaimantSearchRepository, times(0)).save(claimant);
    }

    @Test
    public void deleteClaimant() throws Exception {
        // Initialize the database
        claimantRepository.save(claimant);

        int databaseSizeBeforeDelete = claimantRepository.findAll().size();

        // Get the claimant
        restClaimantMockMvc.perform(delete("/api/claimants/{id}", claimant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Claimant> claimantList = claimantRepository.findAll();
        assertThat(claimantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Claimant in Elasticsearch
        verify(mockClaimantSearchRepository, times(1)).deleteById(claimant.getId());
    }

    @Test
    public void searchClaimant() throws Exception {
        // Initialize the database
        claimantRepository.save(claimant);
        when(mockClaimantSearchRepository.search(queryStringQuery("id:" + claimant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(claimant), PageRequest.of(0, 1), 1));
        // Search the claimant
        restClaimantMockMvc.perform(get("/api/_search/claimants?query=id:" + claimant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claimant.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].tagLine").value(hasItem(DEFAULT_TAG_LINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageURL").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Claimant.class);
        Claimant claimant1 = new Claimant();
        claimant1.setId("id1");
        Claimant claimant2 = new Claimant();
        claimant2.setId(claimant1.getId());
        assertThat(claimant1).isEqualTo(claimant2);
        claimant2.setId("id2");
        assertThat(claimant1).isNotEqualTo(claimant2);
        claimant1.setId(null);
        assertThat(claimant1).isNotEqualTo(claimant2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClaimantDTO.class);
        ClaimantDTO claimantDTO1 = new ClaimantDTO();
        claimantDTO1.setId("id1");
        ClaimantDTO claimantDTO2 = new ClaimantDTO();
        assertThat(claimantDTO1).isNotEqualTo(claimantDTO2);
        claimantDTO2.setId(claimantDTO1.getId());
        assertThat(claimantDTO1).isEqualTo(claimantDTO2);
        claimantDTO2.setId("id2");
        assertThat(claimantDTO1).isNotEqualTo(claimantDTO2);
        claimantDTO1.setId(null);
        assertThat(claimantDTO1).isNotEqualTo(claimantDTO2);
    }
}
