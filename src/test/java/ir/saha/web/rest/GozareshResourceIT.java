package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Gozaresh;
import ir.saha.repository.GozareshRepository;
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
import java.util.List;

import static ir.saha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ir.saha.domain.enumeration.VaziatGozaresh;
/**
 * Integration tests for the {@link GozareshResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class GozareshResourceIT {

    private static final VaziatGozaresh DEFAULT_VAZIAT = VaziatGozaresh.AVALIE;
    private static final VaziatGozaresh UPDATED_VAZIAT = VaziatGozaresh.MODIR;

    @Autowired
    private GozareshRepository gozareshRepository;

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

    private MockMvc restGozareshMockMvc;

    private Gozaresh gozaresh;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GozareshResource gozareshResource = new GozareshResource(gozareshRepository);
        this.restGozareshMockMvc = MockMvcBuilders.standaloneSetup(gozareshResource)
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
    public static Gozaresh createEntity(EntityManager em) {
        Gozaresh gozaresh = new Gozaresh()
            .vaziat(DEFAULT_VAZIAT);
        return gozaresh;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gozaresh createUpdatedEntity(EntityManager em) {
        Gozaresh gozaresh = new Gozaresh()
            .vaziat(UPDATED_VAZIAT);
        return gozaresh;
    }

    @BeforeEach
    public void initTest() {
        gozaresh = createEntity(em);
    }

    @Test
    @Transactional
    public void createGozaresh() throws Exception {
        int databaseSizeBeforeCreate = gozareshRepository.findAll().size();

        // Create the Gozaresh
        restGozareshMockMvc.perform(post("/api/gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gozaresh)))
            .andExpect(status().isCreated());

        // Validate the Gozaresh in the database
        List<Gozaresh> gozareshList = gozareshRepository.findAll();
        assertThat(gozareshList).hasSize(databaseSizeBeforeCreate + 1);
        Gozaresh testGozaresh = gozareshList.get(gozareshList.size() - 1);
        assertThat(testGozaresh.getVaziat()).isEqualTo(DEFAULT_VAZIAT);
    }

    @Test
    @Transactional
    public void createGozareshWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gozareshRepository.findAll().size();

        // Create the Gozaresh with an existing ID
        gozaresh.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGozareshMockMvc.perform(post("/api/gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gozaresh)))
            .andExpect(status().isBadRequest());

        // Validate the Gozaresh in the database
        List<Gozaresh> gozareshList = gozareshRepository.findAll();
        assertThat(gozareshList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGozareshes() throws Exception {
        // Initialize the database
        gozareshRepository.saveAndFlush(gozaresh);

        // Get all the gozareshList
        restGozareshMockMvc.perform(get("/api/gozareshes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gozaresh.getId().intValue())))
            .andExpect(jsonPath("$.[*].vaziat").value(hasItem(DEFAULT_VAZIAT.toString())));
    }
    
    @Test
    @Transactional
    public void getGozaresh() throws Exception {
        // Initialize the database
        gozareshRepository.saveAndFlush(gozaresh);

        // Get the gozaresh
        restGozareshMockMvc.perform(get("/api/gozareshes/{id}", gozaresh.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gozaresh.getId().intValue()))
            .andExpect(jsonPath("$.vaziat").value(DEFAULT_VAZIAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGozaresh() throws Exception {
        // Get the gozaresh
        restGozareshMockMvc.perform(get("/api/gozareshes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGozaresh() throws Exception {
        // Initialize the database
        gozareshRepository.saveAndFlush(gozaresh);

        int databaseSizeBeforeUpdate = gozareshRepository.findAll().size();

        // Update the gozaresh
        Gozaresh updatedGozaresh = gozareshRepository.findById(gozaresh.getId()).get();
        // Disconnect from session so that the updates on updatedGozaresh are not directly saved in db
        em.detach(updatedGozaresh);
        updatedGozaresh
            .vaziat(UPDATED_VAZIAT);

        restGozareshMockMvc.perform(put("/api/gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedGozaresh)))
            .andExpect(status().isOk());

        // Validate the Gozaresh in the database
        List<Gozaresh> gozareshList = gozareshRepository.findAll();
        assertThat(gozareshList).hasSize(databaseSizeBeforeUpdate);
        Gozaresh testGozaresh = gozareshList.get(gozareshList.size() - 1);
        assertThat(testGozaresh.getVaziat()).isEqualTo(UPDATED_VAZIAT);
    }

    @Test
    @Transactional
    public void updateNonExistingGozaresh() throws Exception {
        int databaseSizeBeforeUpdate = gozareshRepository.findAll().size();

        // Create the Gozaresh

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGozareshMockMvc.perform(put("/api/gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(gozaresh)))
            .andExpect(status().isBadRequest());

        // Validate the Gozaresh in the database
        List<Gozaresh> gozareshList = gozareshRepository.findAll();
        assertThat(gozareshList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGozaresh() throws Exception {
        // Initialize the database
        gozareshRepository.saveAndFlush(gozaresh);

        int databaseSizeBeforeDelete = gozareshRepository.findAll().size();

        // Delete the gozaresh
        restGozareshMockMvc.perform(delete("/api/gozareshes/{id}", gozaresh.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gozaresh> gozareshList = gozareshRepository.findAll();
        assertThat(gozareshList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
