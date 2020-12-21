package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Dore;
import ir.saha.repository.DoreRepository;
import ir.saha.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ir.saha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DoreResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class DoreResourceIT {

    private static final Instant DEFAULT_BEGIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BEGIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DoreRepository doreRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDoreMockMvc;

    private Dore dore;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoreResource doreResource = new DoreResource(doreRepository);
        this.restDoreMockMvc = MockMvcBuilders.standaloneSetup(doreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dore createEntity(EntityManager em) {
        Dore dore = new Dore()
            .begin(DEFAULT_BEGIN)
            .end(DEFAULT_END);
        return dore;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dore createUpdatedEntity(EntityManager em) {
        Dore dore = new Dore()
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END);
        return dore;
    }

    @BeforeEach
    public void initTest() {
        dore = createEntity(em);
    }

    @Test
    @Transactional
    public void createDore() throws Exception {
        int databaseSizeBeforeCreate = doreRepository.findAll().size();

        // Create the Dore
        restDoreMockMvc.perform(post("/api/dores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dore)))
            .andExpect(status().isCreated());

        // Validate the Dore in the database
        List<Dore> doreList = doreRepository.findAll();
        assertThat(doreList).hasSize(databaseSizeBeforeCreate + 1);
        Dore testDore = doreList.get(doreList.size() - 1);
        assertThat(testDore.getBegin()).isEqualTo(DEFAULT_BEGIN);
        assertThat(testDore.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    public void createDoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doreRepository.findAll().size();

        // Create the Dore with an existing ID
        dore.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoreMockMvc.perform(post("/api/dores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dore)))
            .andExpect(status().isBadRequest());

        // Validate the Dore in the database
        List<Dore> doreList = doreRepository.findAll();
        assertThat(doreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDores() throws Exception {
        // Initialize the database
        doreRepository.saveAndFlush(dore);

        // Get all the doreList
        restDoreMockMvc.perform(get("/api/dores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dore.getId().intValue())))
            .andExpect(jsonPath("$.[*].begin").value(hasItem(DEFAULT_BEGIN.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }
    
    @Test
    @Transactional
    public void getDore() throws Exception {
        // Initialize the database
        doreRepository.saveAndFlush(dore);

        // Get the dore
        restDoreMockMvc.perform(get("/api/dores/{id}", dore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dore.getId().intValue()))
            .andExpect(jsonPath("$.begin").value(DEFAULT_BEGIN.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDore() throws Exception {
        // Get the dore
        restDoreMockMvc.perform(get("/api/dores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDore() throws Exception {
        // Initialize the database
        doreRepository.saveAndFlush(dore);

        int databaseSizeBeforeUpdate = doreRepository.findAll().size();

        // Update the dore
        Dore updatedDore = doreRepository.findById(dore.getId()).get();
        // Disconnect from session so that the updates on updatedDore are not directly saved in db
        em.detach(updatedDore);
        updatedDore
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END);

        restDoreMockMvc.perform(put("/api/dores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDore)))
            .andExpect(status().isOk());

        // Validate the Dore in the database
        List<Dore> doreList = doreRepository.findAll();
        assertThat(doreList).hasSize(databaseSizeBeforeUpdate);
        Dore testDore = doreList.get(doreList.size() - 1);
        assertThat(testDore.getBegin()).isEqualTo(UPDATED_BEGIN);
        assertThat(testDore.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    public void updateNonExistingDore() throws Exception {
        int databaseSizeBeforeUpdate = doreRepository.findAll().size();

        // Create the Dore

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoreMockMvc.perform(put("/api/dores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dore)))
            .andExpect(status().isBadRequest());

        // Validate the Dore in the database
        List<Dore> doreList = doreRepository.findAll();
        assertThat(doreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDore() throws Exception {
        // Initialize the database
        doreRepository.saveAndFlush(dore);

        int databaseSizeBeforeDelete = doreRepository.findAll().size();

        // Delete the dore
        restDoreMockMvc.perform(delete("/api/dores/{id}", dore.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dore> doreList = doreRepository.findAll();
        assertThat(doreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
