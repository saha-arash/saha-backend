package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Mantaghe;
import ir.saha.repository.MantagheRepository;
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
 * Integration tests for the {@link MantagheResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class MantagheResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MantagheRepository mantagheRepository;

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

    private MockMvc restMantagheMockMvc;

    private Mantaghe mantaghe;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MantagheResource mantagheResource = new MantagheResource(mantagheRepository);
        this.restMantagheMockMvc = MockMvcBuilders.standaloneSetup(mantagheResource)
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
    public static Mantaghe createEntity(EntityManager em) {
        Mantaghe mantaghe = new Mantaghe()
            .name(DEFAULT_NAME);
        return mantaghe;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mantaghe createUpdatedEntity(EntityManager em) {
        Mantaghe mantaghe = new Mantaghe()
            .name(UPDATED_NAME);
        return mantaghe;
    }

    @BeforeEach
    public void initTest() {
        mantaghe = createEntity(em);
    }

    @Test
    @Transactional
    public void createMantaghe() throws Exception {
        int databaseSizeBeforeCreate = mantagheRepository.findAll().size();

        // Create the Mantaghe
        restMantagheMockMvc.perform(post("/api/mantaghes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mantaghe)))
            .andExpect(status().isCreated());

        // Validate the Mantaghe in the database
        List<Mantaghe> mantagheList = mantagheRepository.findAll();
        assertThat(mantagheList).hasSize(databaseSizeBeforeCreate + 1);
        Mantaghe testMantaghe = mantagheList.get(mantagheList.size() - 1);
        assertThat(testMantaghe.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMantagheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mantagheRepository.findAll().size();

        // Create the Mantaghe with an existing ID
        mantaghe.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMantagheMockMvc.perform(post("/api/mantaghes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mantaghe)))
            .andExpect(status().isBadRequest());

        // Validate the Mantaghe in the database
        List<Mantaghe> mantagheList = mantagheRepository.findAll();
        assertThat(mantagheList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMantaghes() throws Exception {
        // Initialize the database
        mantagheRepository.saveAndFlush(mantaghe);

        // Get all the mantagheList
        restMantagheMockMvc.perform(get("/api/mantaghes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mantaghe.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getMantaghe() throws Exception {
        // Initialize the database
        mantagheRepository.saveAndFlush(mantaghe);

        // Get the mantaghe
        restMantagheMockMvc.perform(get("/api/mantaghes/{id}", mantaghe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mantaghe.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingMantaghe() throws Exception {
        // Get the mantaghe
        restMantagheMockMvc.perform(get("/api/mantaghes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMantaghe() throws Exception {
        // Initialize the database
        mantagheRepository.saveAndFlush(mantaghe);

        int databaseSizeBeforeUpdate = mantagheRepository.findAll().size();

        // Update the mantaghe
        Mantaghe updatedMantaghe = mantagheRepository.findById(mantaghe.getId()).get();
        // Disconnect from session so that the updates on updatedMantaghe are not directly saved in db
        em.detach(updatedMantaghe);
        updatedMantaghe
            .name(UPDATED_NAME);

        restMantagheMockMvc.perform(put("/api/mantaghes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMantaghe)))
            .andExpect(status().isOk());

        // Validate the Mantaghe in the database
        List<Mantaghe> mantagheList = mantagheRepository.findAll();
        assertThat(mantagheList).hasSize(databaseSizeBeforeUpdate);
        Mantaghe testMantaghe = mantagheList.get(mantagheList.size() - 1);
        assertThat(testMantaghe.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMantaghe() throws Exception {
        int databaseSizeBeforeUpdate = mantagheRepository.findAll().size();

        // Create the Mantaghe

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMantagheMockMvc.perform(put("/api/mantaghes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mantaghe)))
            .andExpect(status().isBadRequest());

        // Validate the Mantaghe in the database
        List<Mantaghe> mantagheList = mantagheRepository.findAll();
        assertThat(mantagheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMantaghe() throws Exception {
        // Initialize the database
        mantagheRepository.saveAndFlush(mantaghe);

        int databaseSizeBeforeDelete = mantagheRepository.findAll().size();

        // Delete the mantaghe
        restMantagheMockMvc.perform(delete("/api/mantaghes/{id}", mantaghe.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mantaghe> mantagheList = mantagheRepository.findAll();
        assertThat(mantagheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
