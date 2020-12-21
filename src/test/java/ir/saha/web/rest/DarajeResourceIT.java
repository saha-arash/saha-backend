package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Daraje;
import ir.saha.repository.DarajeRepository;
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
 * Integration tests for the {@link DarajeResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class DarajeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DarajeRepository darajeRepository;

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

    private MockMvc restDarajeMockMvc;

    private Daraje daraje;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DarajeResource darajeResource = new DarajeResource(darajeRepository);
        this.restDarajeMockMvc = MockMvcBuilders.standaloneSetup(darajeResource)
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
    public static Daraje createEntity(EntityManager em) {
        Daraje daraje = new Daraje()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return daraje;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Daraje createUpdatedEntity(EntityManager em) {
        Daraje daraje = new Daraje()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return daraje;
    }

    @BeforeEach
    public void initTest() {
        daraje = createEntity(em);
    }

    @Test
    @Transactional
    public void createDaraje() throws Exception {
        int databaseSizeBeforeCreate = darajeRepository.findAll().size();

        // Create the Daraje
        restDarajeMockMvc.perform(post("/api/darajes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(daraje)))
            .andExpect(status().isCreated());

        // Validate the Daraje in the database
        List<Daraje> darajeList = darajeRepository.findAll();
        assertThat(darajeList).hasSize(databaseSizeBeforeCreate + 1);
        Daraje testDaraje = darajeList.get(darajeList.size() - 1);
        assertThat(testDaraje.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDaraje.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDarajeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = darajeRepository.findAll().size();

        // Create the Daraje with an existing ID
        daraje.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDarajeMockMvc.perform(post("/api/darajes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(daraje)))
            .andExpect(status().isBadRequest());

        // Validate the Daraje in the database
        List<Daraje> darajeList = darajeRepository.findAll();
        assertThat(darajeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDarajes() throws Exception {
        // Initialize the database
        darajeRepository.saveAndFlush(daraje);

        // Get all the darajeList
        restDarajeMockMvc.perform(get("/api/darajes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(daraje.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getDaraje() throws Exception {
        // Initialize the database
        darajeRepository.saveAndFlush(daraje);

        // Get the daraje
        restDarajeMockMvc.perform(get("/api/darajes/{id}", daraje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(daraje.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingDaraje() throws Exception {
        // Get the daraje
        restDarajeMockMvc.perform(get("/api/darajes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDaraje() throws Exception {
        // Initialize the database
        darajeRepository.saveAndFlush(daraje);

        int databaseSizeBeforeUpdate = darajeRepository.findAll().size();

        // Update the daraje
        Daraje updatedDaraje = darajeRepository.findById(daraje.getId()).get();
        // Disconnect from session so that the updates on updatedDaraje are not directly saved in db
        em.detach(updatedDaraje);
        updatedDaraje
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restDarajeMockMvc.perform(put("/api/darajes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDaraje)))
            .andExpect(status().isOk());

        // Validate the Daraje in the database
        List<Daraje> darajeList = darajeRepository.findAll();
        assertThat(darajeList).hasSize(databaseSizeBeforeUpdate);
        Daraje testDaraje = darajeList.get(darajeList.size() - 1);
        assertThat(testDaraje.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDaraje.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDaraje() throws Exception {
        int databaseSizeBeforeUpdate = darajeRepository.findAll().size();

        // Create the Daraje

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDarajeMockMvc.perform(put("/api/darajes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(daraje)))
            .andExpect(status().isBadRequest());

        // Validate the Daraje in the database
        List<Daraje> darajeList = darajeRepository.findAll();
        assertThat(darajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDaraje() throws Exception {
        // Initialize the database
        darajeRepository.saveAndFlush(daraje);

        int databaseSizeBeforeDelete = darajeRepository.findAll().size();

        // Delete the daraje
        restDarajeMockMvc.perform(delete("/api/darajes/{id}", daraje.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Daraje> darajeList = darajeRepository.findAll();
        assertThat(darajeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
