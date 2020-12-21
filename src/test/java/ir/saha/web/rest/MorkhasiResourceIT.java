package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Morkhasi;
import ir.saha.repository.MorkhasiRepository;
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
 * Integration tests for the {@link MorkhasiResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class MorkhasiResourceIT {

    private static final Instant DEFAULT_BEGIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BEGIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MorkhasiRepository morkhasiRepository;

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

    private MockMvc restMorkhasiMockMvc;

    private Morkhasi morkhasi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MorkhasiResource morkhasiResource = new MorkhasiResource(morkhasiRepository);
        this.restMorkhasiMockMvc = MockMvcBuilders.standaloneSetup(morkhasiResource)
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
    public static Morkhasi createEntity(EntityManager em) {
        Morkhasi morkhasi = new Morkhasi()
            .begin(DEFAULT_BEGIN)
            .end(DEFAULT_END);
        return morkhasi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Morkhasi createUpdatedEntity(EntityManager em) {
        Morkhasi morkhasi = new Morkhasi()
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END);
        return morkhasi;
    }

    @BeforeEach
    public void initTest() {
        morkhasi = createEntity(em);
    }

    @Test
    @Transactional
    public void createMorkhasi() throws Exception {
        int databaseSizeBeforeCreate = morkhasiRepository.findAll().size();

        // Create the Morkhasi
        restMorkhasiMockMvc.perform(post("/api/morkhasis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(morkhasi)))
            .andExpect(status().isCreated());

        // Validate the Morkhasi in the database
        List<Morkhasi> morkhasiList = morkhasiRepository.findAll();
        assertThat(morkhasiList).hasSize(databaseSizeBeforeCreate + 1);
        Morkhasi testMorkhasi = morkhasiList.get(morkhasiList.size() - 1);
        assertThat(testMorkhasi.getBegin()).isEqualTo(DEFAULT_BEGIN);
        assertThat(testMorkhasi.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    public void createMorkhasiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = morkhasiRepository.findAll().size();

        // Create the Morkhasi with an existing ID
        morkhasi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMorkhasiMockMvc.perform(post("/api/morkhasis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(morkhasi)))
            .andExpect(status().isBadRequest());

        // Validate the Morkhasi in the database
        List<Morkhasi> morkhasiList = morkhasiRepository.findAll();
        assertThat(morkhasiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMorkhasis() throws Exception {
        // Initialize the database
        morkhasiRepository.saveAndFlush(morkhasi);

        // Get all the morkhasiList
        restMorkhasiMockMvc.perform(get("/api/morkhasis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(morkhasi.getId().intValue())))
            .andExpect(jsonPath("$.[*].begin").value(hasItem(DEFAULT_BEGIN.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }
    
    @Test
    @Transactional
    public void getMorkhasi() throws Exception {
        // Initialize the database
        morkhasiRepository.saveAndFlush(morkhasi);

        // Get the morkhasi
        restMorkhasiMockMvc.perform(get("/api/morkhasis/{id}", morkhasi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(morkhasi.getId().intValue()))
            .andExpect(jsonPath("$.begin").value(DEFAULT_BEGIN.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMorkhasi() throws Exception {
        // Get the morkhasi
        restMorkhasiMockMvc.perform(get("/api/morkhasis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMorkhasi() throws Exception {
        // Initialize the database
        morkhasiRepository.saveAndFlush(morkhasi);

        int databaseSizeBeforeUpdate = morkhasiRepository.findAll().size();

        // Update the morkhasi
        Morkhasi updatedMorkhasi = morkhasiRepository.findById(morkhasi.getId()).get();
        // Disconnect from session so that the updates on updatedMorkhasi are not directly saved in db
        em.detach(updatedMorkhasi);
        updatedMorkhasi
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END);

        restMorkhasiMockMvc.perform(put("/api/morkhasis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMorkhasi)))
            .andExpect(status().isOk());

        // Validate the Morkhasi in the database
        List<Morkhasi> morkhasiList = morkhasiRepository.findAll();
        assertThat(morkhasiList).hasSize(databaseSizeBeforeUpdate);
        Morkhasi testMorkhasi = morkhasiList.get(morkhasiList.size() - 1);
        assertThat(testMorkhasi.getBegin()).isEqualTo(UPDATED_BEGIN);
        assertThat(testMorkhasi.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    public void updateNonExistingMorkhasi() throws Exception {
        int databaseSizeBeforeUpdate = morkhasiRepository.findAll().size();

        // Create the Morkhasi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMorkhasiMockMvc.perform(put("/api/morkhasis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(morkhasi)))
            .andExpect(status().isBadRequest());

        // Validate the Morkhasi in the database
        List<Morkhasi> morkhasiList = morkhasiRepository.findAll();
        assertThat(morkhasiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMorkhasi() throws Exception {
        // Initialize the database
        morkhasiRepository.saveAndFlush(morkhasi);

        int databaseSizeBeforeDelete = morkhasiRepository.findAll().size();

        // Delete the morkhasi
        restMorkhasiMockMvc.perform(delete("/api/morkhasis/{id}", morkhasi.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Morkhasi> morkhasiList = morkhasiRepository.findAll();
        assertThat(morkhasiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
