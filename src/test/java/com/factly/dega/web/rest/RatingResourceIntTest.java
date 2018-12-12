package com.factly.dega.web.rest;

import com.factly.dega.FactcheckApp;

import com.factly.dega.domain.Rating;
import com.factly.dega.repository.RatingRepository;
import com.factly.dega.repository.search.RatingSearchRepository;
import com.factly.dega.service.RatingService;
import com.factly.dega.service.dto.RatingDTO;
import com.factly.dega.service.mapper.RatingMapper;
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

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
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
 * Test class for the RatingResource REST controller.
 *
 * @see RatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactcheckApp.class)
public class RatingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERIC_VALUE = 1;
    private static final Integer UPDATED_NUMERIC_VALUE = 2;

    private static final String DEFAULT_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_ICON_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;
    
    @Autowired
    private RatingService ratingService;

    /**
     * This repository is mocked in the com.factly.dega.repository.search test package.
     *
     * @see com.factly.dega.repository.search.RatingSearchRepositoryMockConfiguration
     */
    @Autowired
    private RatingSearchRepository mockRatingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restRatingMockMvc;

    private Rating rating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RatingResource ratingResource = new RatingResource(ratingService);
        this.restRatingMockMvc = MockMvcBuilders.standaloneSetup(ratingResource)
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
    public static Rating createEntity() {
        Rating rating = new Rating()
            .name(DEFAULT_NAME)
            .numericValue(DEFAULT_NUMERIC_VALUE)
            .iconURL(DEFAULT_ICON_URL)
            .isDefault(DEFAULT_IS_DEFAULT)
            .clientId(DEFAULT_CLIENT_ID)
            .slug(DEFAULT_SLUG)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .description(DEFAULT_DESCRIPTION);
        return rating;
    }

    @Before
    public void initTest() {
        ratingRepository.deleteAll();
        rating = createEntity();
    }

    @Test
    public void createRating() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);
        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isCreated());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate + 1);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRating.getNumericValue()).isEqualTo(DEFAULT_NUMERIC_VALUE);
        assertThat(testRating.getIconURL()).isEqualTo(DEFAULT_ICON_URL);
        assertThat(testRating.isIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testRating.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testRating.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testRating.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testRating.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testRating.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    public void createRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating with an existing ID
        rating.setId("existing_id");
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(0)).save(rating);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setName(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNumericValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setNumericValue(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setClientId(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setSlug(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setCreatedDate(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastUpdatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setLastUpdatedDate(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setDescription(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllRatings() throws Exception {
        // Initialize the database
        ratingRepository.save(rating);

        // Get all the ratingList
        restRatingMockMvc.perform(get("/api/ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].numericValue").value(hasItem(DEFAULT_NUMERIC_VALUE)))
            .andExpect(jsonPath("$.[*].iconURL").value(hasItem(DEFAULT_ICON_URL.toString())))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    public void getRating() throws Exception {
        // Initialize the database
        ratingRepository.save(rating);

        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rating.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.numericValue").value(DEFAULT_NUMERIC_VALUE))
            .andExpect(jsonPath("$.iconURL").value(DEFAULT_ICON_URL.toString()))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdatedDate").value(sameInstant(DEFAULT_LAST_UPDATED_DATE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    public void getNonExistingRating() throws Exception {
        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRating() throws Exception {
        // Initialize the database
        ratingRepository.save(rating);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        updatedRating
            .name(UPDATED_NAME)
            .numericValue(UPDATED_NUMERIC_VALUE)
            .iconURL(UPDATED_ICON_URL)
            .isDefault(UPDATED_IS_DEFAULT)
            .clientId(UPDATED_CLIENT_ID)
            .slug(UPDATED_SLUG)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .description(UPDATED_DESCRIPTION);
        RatingDTO ratingDTO = ratingMapper.toDto(updatedRating);

        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRating.getNumericValue()).isEqualTo(UPDATED_NUMERIC_VALUE);
        assertThat(testRating.getIconURL()).isEqualTo(UPDATED_ICON_URL);
        assertThat(testRating.isIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testRating.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testRating.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testRating.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testRating.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testRating.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    public void updateNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(0)).save(rating);
    }

    @Test
    public void deleteRating() throws Exception {
        // Initialize the database
        ratingRepository.save(rating);

        int databaseSizeBeforeDelete = ratingRepository.findAll().size();

        // Get the rating
        restRatingMockMvc.perform(delete("/api/ratings/{id}", rating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).deleteById(rating.getId());
    }

    @Test
    public void searchRating() throws Exception {
        // Initialize the database
        ratingRepository.save(rating);
        when(mockRatingSearchRepository.search(queryStringQuery("id:" + rating.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rating), PageRequest.of(0, 1), 1));
        // Search the rating
        restRatingMockMvc.perform(get("/api/_search/ratings?query=id:" + rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].numericValue").value(hasItem(DEFAULT_NUMERIC_VALUE)))
            .andExpect(jsonPath("$.[*].iconURL").value(hasItem(DEFAULT_ICON_URL.toString())))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = new Rating();
        rating1.setId("id1");
        Rating rating2 = new Rating();
        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);
        rating2.setId("id2");
        assertThat(rating1).isNotEqualTo(rating2);
        rating1.setId(null);
        assertThat(rating1).isNotEqualTo(rating2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingDTO.class);
        RatingDTO ratingDTO1 = new RatingDTO();
        ratingDTO1.setId("id1");
        RatingDTO ratingDTO2 = new RatingDTO();
        assertThat(ratingDTO1).isNotEqualTo(ratingDTO2);
        ratingDTO2.setId(ratingDTO1.getId());
        assertThat(ratingDTO1).isEqualTo(ratingDTO2);
        ratingDTO2.setId("id2");
        assertThat(ratingDTO1).isNotEqualTo(ratingDTO2);
        ratingDTO1.setId(null);
        assertThat(ratingDTO1).isNotEqualTo(ratingDTO2);
    }
}
