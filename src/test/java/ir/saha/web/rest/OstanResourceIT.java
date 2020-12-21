package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Ostan;
import ir.saha.repository.OstanRepository;
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

/**
 * Integration tests for the {@link OstanResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class OstanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private OstanRepository ostanRepository;

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

    private MockMvc restOstanMockMvc;

    private Ostan ostan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OstanResource ostanResource = new OstanResource(ostanRepository);
        this.restOstanMockMvc = MockMvcBuilders.standaloneSetup(ostanResource)
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
    public static Ostan createEntity(EntityManager em) {
        Ostan ostan = new Ostan()
            .name(DEFAULT_NAME);
        return ostan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ostan createUpdatedEntity(EntityManager em) {
        Ostan ostan = new Ostan()
            .name(UPDATED_NAME);
        return ostan;
    }

    @BeforeEach
    public void initTest() {
        ostan = createEntity(em);
    }

    @Test
    @Transactional
    public void createOstan() throws Exception {
        int databaseSizeBeforeCreate = ostanRepository.findAll().size();

        // Create the Ostan
        restOstanMockMvc.perform(post("/api/ostans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ostan)))
            .andExpect(status().isCreated());

        // Validate the Ostan in the database
        List<Ostan> ostanList = ostanRepository.findAll();
        assertThat(ostanList).hasSize(databaseSizeBeforeCreate + 1);
        Ostan testOstan = ostanList.get(ostanList.size() - 1);
        assertThat(testOstan.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createOstanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ostanRepository.findAll().size();

        // Create the Ostan with an existing ID
        ostan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOstanMockMvc.perform(post("/api/ostans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ostan)))
            .andExpect(status().isBadRequest());

        // Validate the Ostan in the database
        List<Ostan> ostanList = ostanRepository.findAll();
        assertThat(ostanList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOstans() throws Exception {
        // Initialize the database
        ostanRepository.saveAndFlush(ostan);

        // Get all the ostanList
        restOstanMockMvc.perform(get("/api/ostans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ostan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getOstan() throws Exception {
        // Initialize the database
        ostanRepository.saveAndFlush(ostan);

        // Get the ostan
        restOstanMockMvc.perform(get("/api/ostans/{id}", ostan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ostan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingOstan() throws Exception {
        // Get the ostan
        restOstanMockMvc.perform(get("/api/ostans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOstan() throws Exception {
        // Initialize the database
        ostanRepository.saveAndFlush(ostan);

        int databaseSizeBeforeUpdate = ostanRepository.findAll().size();

        // Update the ostan
        Ostan updatedOstan = ostanRepository.findById(ostan.getId()).get();
        // Disconnect from session so that the updates on updatedOstan are not directly saved in db
        em.detach(updatedOstan);
        updatedOstan
            .name(UPDATED_NAME);

        restOstanMockMvc.perform(put("/api/ostans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOstan)))
            .andExpect(status().isOk());

        // Validate the Ostan in the database
        List<Ostan> ostanList = ostanRepository.findAll();
        assertThat(ostanList).hasSize(databaseSizeBeforeUpdate);
        Ostan testOstan = ostanList.get(ostanList.size() - 1);
        assertThat(testOstan.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingOstan() throws Exception {
        int databaseSizeBeforeUpdate = ostanRepository.findAll().size();

        // Create the Ostan

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOstanMockMvc.perform(put("/api/ostans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ostan)))
            .andExpect(status().isBadRequest());

        // Validate the Ostan in the database
        List<Ostan> ostanList = ostanRepository.findAll();
        assertThat(ostanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOstan() throws Exception {
        // Initialize the database
        ostanRepository.saveAndFlush(ostan);

        int databaseSizeBeforeDelete = ostanRepository.findAll().size();

        // Delete the ostan
        restOstanMockMvc.perform(delete("/api/ostans/{id}", ostan.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ostan> ostanList = ostanRepository.findAll();
        assertThat(ostanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
