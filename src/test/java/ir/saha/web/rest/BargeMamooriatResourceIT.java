package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.BargeMamooriat;
import ir.saha.repository.BargeMamooriatRepository;
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

import ir.saha.domain.enumeration.VaziatBargeMamooriat;
/**
 * Integration tests for the {@link BargeMamooriatResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class BargeMamooriatResourceIT {

    private static final VaziatBargeMamooriat DEFAULT_VAZIAT = VaziatBargeMamooriat.SARPARAST_TIME_HESABRESI;
    private static final VaziatBargeMamooriat UPDATED_VAZIAT = VaziatBargeMamooriat.DAR_ENTEZAR_TAEED_MODIR_HESABRESI;

    private static final Integer DEFAULT_SALE_MAMOORIAT = 1;
    private static final Integer UPDATED_SALE_MAMOORIAT = 2;

    private static final Instant DEFAULT_SHOROO_MAMOORIAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHOROO_MAMOORIAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PAYAN_MAMOORIAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYAN_MAMOORIAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BargeMamooriatRepository bargeMamooriatRepository;

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

    private MockMvc restBargeMamooriatMockMvc;

    private BargeMamooriat bargeMamooriat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BargeMamooriatResource bargeMamooriatResource = new BargeMamooriatResource(bargeMamooriatRepository);
        this.restBargeMamooriatMockMvc = MockMvcBuilders.standaloneSetup(bargeMamooriatResource)
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
    public static BargeMamooriat createEntity(EntityManager em) {
        BargeMamooriat bargeMamooriat = new BargeMamooriat()
            .vaziat(DEFAULT_VAZIAT)
            .saleMamooriat(DEFAULT_SALE_MAMOORIAT)
            .shorooMamooriat(DEFAULT_SHOROO_MAMOORIAT)
            .payanMamooriat(DEFAULT_PAYAN_MAMOORIAT);
        return bargeMamooriat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BargeMamooriat createUpdatedEntity(EntityManager em) {
        BargeMamooriat bargeMamooriat = new BargeMamooriat()
            .vaziat(UPDATED_VAZIAT)
            .saleMamooriat(UPDATED_SALE_MAMOORIAT)
            .shorooMamooriat(UPDATED_SHOROO_MAMOORIAT)
            .payanMamooriat(UPDATED_PAYAN_MAMOORIAT);
        return bargeMamooriat;
    }

    @BeforeEach
    public void initTest() {
        bargeMamooriat = createEntity(em);
    }

    @Test
    @Transactional
    public void createBargeMamooriat() throws Exception {
        int databaseSizeBeforeCreate = bargeMamooriatRepository.findAll().size();

        // Create the BargeMamooriat
        restBargeMamooriatMockMvc.perform(post("/api/barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bargeMamooriat)))
            .andExpect(status().isCreated());

        // Validate the BargeMamooriat in the database
        List<BargeMamooriat> bargeMamooriatList = bargeMamooriatRepository.findAll();
        assertThat(bargeMamooriatList).hasSize(databaseSizeBeforeCreate + 1);
        BargeMamooriat testBargeMamooriat = bargeMamooriatList.get(bargeMamooriatList.size() - 1);
        assertThat(testBargeMamooriat.getVaziat()).isEqualTo(DEFAULT_VAZIAT);
        assertThat(testBargeMamooriat.getSaleMamooriat()).isEqualTo(DEFAULT_SALE_MAMOORIAT);
        assertThat(testBargeMamooriat.getShorooMamooriat()).isEqualTo(DEFAULT_SHOROO_MAMOORIAT);
        assertThat(testBargeMamooriat.getPayanMamooriat()).isEqualTo(DEFAULT_PAYAN_MAMOORIAT);
    }

    @Test
    @Transactional
    public void createBargeMamooriatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bargeMamooriatRepository.findAll().size();

        // Create the BargeMamooriat with an existing ID
        bargeMamooriat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBargeMamooriatMockMvc.perform(post("/api/barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bargeMamooriat)))
            .andExpect(status().isBadRequest());

        // Validate the BargeMamooriat in the database
        List<BargeMamooriat> bargeMamooriatList = bargeMamooriatRepository.findAll();
        assertThat(bargeMamooriatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBargeMamooriats() throws Exception {
        // Initialize the database
        bargeMamooriatRepository.saveAndFlush(bargeMamooriat);

        // Get all the bargeMamooriatList
        restBargeMamooriatMockMvc.perform(get("/api/barge-mamooriats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bargeMamooriat.getId().intValue())))
            .andExpect(jsonPath("$.[*].vaziat").value(hasItem(DEFAULT_VAZIAT.toString())))
            .andExpect(jsonPath("$.[*].saleMamooriat").value(hasItem(DEFAULT_SALE_MAMOORIAT)))
            .andExpect(jsonPath("$.[*].shorooMamooriat").value(hasItem(DEFAULT_SHOROO_MAMOORIAT.toString())))
            .andExpect(jsonPath("$.[*].payanMamooriat").value(hasItem(DEFAULT_PAYAN_MAMOORIAT.toString())));
    }
    
    @Test
    @Transactional
    public void getBargeMamooriat() throws Exception {
        // Initialize the database
        bargeMamooriatRepository.saveAndFlush(bargeMamooriat);

        // Get the bargeMamooriat
        restBargeMamooriatMockMvc.perform(get("/api/barge-mamooriats/{id}", bargeMamooriat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bargeMamooriat.getId().intValue()))
            .andExpect(jsonPath("$.vaziat").value(DEFAULT_VAZIAT.toString()))
            .andExpect(jsonPath("$.saleMamooriat").value(DEFAULT_SALE_MAMOORIAT))
            .andExpect(jsonPath("$.shorooMamooriat").value(DEFAULT_SHOROO_MAMOORIAT.toString()))
            .andExpect(jsonPath("$.payanMamooriat").value(DEFAULT_PAYAN_MAMOORIAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBargeMamooriat() throws Exception {
        // Get the bargeMamooriat
        restBargeMamooriatMockMvc.perform(get("/api/barge-mamooriats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBargeMamooriat() throws Exception {
        // Initialize the database
        bargeMamooriatRepository.saveAndFlush(bargeMamooriat);

        int databaseSizeBeforeUpdate = bargeMamooriatRepository.findAll().size();

        // Update the bargeMamooriat
        BargeMamooriat updatedBargeMamooriat = bargeMamooriatRepository.findById(bargeMamooriat.getId()).get();
        // Disconnect from session so that the updates on updatedBargeMamooriat are not directly saved in db
        em.detach(updatedBargeMamooriat);
        updatedBargeMamooriat
            .vaziat(UPDATED_VAZIAT)
            .saleMamooriat(UPDATED_SALE_MAMOORIAT)
            .shorooMamooriat(UPDATED_SHOROO_MAMOORIAT)
            .payanMamooriat(UPDATED_PAYAN_MAMOORIAT);

        restBargeMamooriatMockMvc.perform(put("/api/barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBargeMamooriat)))
            .andExpect(status().isOk());

        // Validate the BargeMamooriat in the database
        List<BargeMamooriat> bargeMamooriatList = bargeMamooriatRepository.findAll();
        assertThat(bargeMamooriatList).hasSize(databaseSizeBeforeUpdate);
        BargeMamooriat testBargeMamooriat = bargeMamooriatList.get(bargeMamooriatList.size() - 1);
        assertThat(testBargeMamooriat.getVaziat()).isEqualTo(UPDATED_VAZIAT);
        assertThat(testBargeMamooriat.getSaleMamooriat()).isEqualTo(UPDATED_SALE_MAMOORIAT);
        assertThat(testBargeMamooriat.getShorooMamooriat()).isEqualTo(UPDATED_SHOROO_MAMOORIAT);
        assertThat(testBargeMamooriat.getPayanMamooriat()).isEqualTo(UPDATED_PAYAN_MAMOORIAT);
    }

    @Test
    @Transactional
    public void updateNonExistingBargeMamooriat() throws Exception {
        int databaseSizeBeforeUpdate = bargeMamooriatRepository.findAll().size();

        // Create the BargeMamooriat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBargeMamooriatMockMvc.perform(put("/api/barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bargeMamooriat)))
            .andExpect(status().isBadRequest());

        // Validate the BargeMamooriat in the database
        List<BargeMamooriat> bargeMamooriatList = bargeMamooriatRepository.findAll();
        assertThat(bargeMamooriatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBargeMamooriat() throws Exception {
        // Initialize the database
        bargeMamooriatRepository.saveAndFlush(bargeMamooriat);

        int databaseSizeBeforeDelete = bargeMamooriatRepository.findAll().size();

        // Delete the bargeMamooriat
        restBargeMamooriatMockMvc.perform(delete("/api/barge-mamooriats/{id}", bargeMamooriat.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BargeMamooriat> bargeMamooriatList = bargeMamooriatRepository.findAll();
        assertThat(bargeMamooriatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
