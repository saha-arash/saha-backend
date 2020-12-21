package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.NirooCode;
import ir.saha.repository.NirooCodeRepository;
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
 * Integration tests for the {@link NirooCodeResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class NirooCodeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private NirooCodeRepository nirooCodeRepository;

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

    private MockMvc restNirooCodeMockMvc;

    private NirooCode nirooCode;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NirooCodeResource nirooCodeResource = new NirooCodeResource(nirooCodeRepository);
        this.restNirooCodeMockMvc = MockMvcBuilders.standaloneSetup(nirooCodeResource)
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
    public static NirooCode createEntity(EntityManager em) {
        NirooCode nirooCode = new NirooCode()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE);
        return nirooCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NirooCode createUpdatedEntity(EntityManager em) {
        NirooCode nirooCode = new NirooCode()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);
        return nirooCode;
    }

    @BeforeEach
    public void initTest() {
        nirooCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createNirooCode() throws Exception {
        int databaseSizeBeforeCreate = nirooCodeRepository.findAll().size();

        // Create the NirooCode
        restNirooCodeMockMvc.perform(post("/api/niroo-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nirooCode)))
            .andExpect(status().isCreated());

        // Validate the NirooCode in the database
        List<NirooCode> nirooCodeList = nirooCodeRepository.findAll();
        assertThat(nirooCodeList).hasSize(databaseSizeBeforeCreate + 1);
        NirooCode testNirooCode = nirooCodeList.get(nirooCodeList.size() - 1);
        assertThat(testNirooCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNirooCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createNirooCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nirooCodeRepository.findAll().size();

        // Create the NirooCode with an existing ID
        nirooCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNirooCodeMockMvc.perform(post("/api/niroo-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nirooCode)))
            .andExpect(status().isBadRequest());

        // Validate the NirooCode in the database
        List<NirooCode> nirooCodeList = nirooCodeRepository.findAll();
        assertThat(nirooCodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNirooCodes() throws Exception {
        // Initialize the database
        nirooCodeRepository.saveAndFlush(nirooCode);

        // Get all the nirooCodeList
        restNirooCodeMockMvc.perform(get("/api/niroo-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nirooCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
    
    @Test
    @Transactional
    public void getNirooCode() throws Exception {
        // Initialize the database
        nirooCodeRepository.saveAndFlush(nirooCode);

        // Get the nirooCode
        restNirooCodeMockMvc.perform(get("/api/niroo-codes/{id}", nirooCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nirooCode.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingNirooCode() throws Exception {
        // Get the nirooCode
        restNirooCodeMockMvc.perform(get("/api/niroo-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNirooCode() throws Exception {
        // Initialize the database
        nirooCodeRepository.saveAndFlush(nirooCode);

        int databaseSizeBeforeUpdate = nirooCodeRepository.findAll().size();

        // Update the nirooCode
        NirooCode updatedNirooCode = nirooCodeRepository.findById(nirooCode.getId()).get();
        // Disconnect from session so that the updates on updatedNirooCode are not directly saved in db
        em.detach(updatedNirooCode);
        updatedNirooCode
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);

        restNirooCodeMockMvc.perform(put("/api/niroo-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNirooCode)))
            .andExpect(status().isOk());

        // Validate the NirooCode in the database
        List<NirooCode> nirooCodeList = nirooCodeRepository.findAll();
        assertThat(nirooCodeList).hasSize(databaseSizeBeforeUpdate);
        NirooCode testNirooCode = nirooCodeList.get(nirooCodeList.size() - 1);
        assertThat(testNirooCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNirooCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingNirooCode() throws Exception {
        int databaseSizeBeforeUpdate = nirooCodeRepository.findAll().size();

        // Create the NirooCode

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNirooCodeMockMvc.perform(put("/api/niroo-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(nirooCode)))
            .andExpect(status().isBadRequest());

        // Validate the NirooCode in the database
        List<NirooCode> nirooCodeList = nirooCodeRepository.findAll();
        assertThat(nirooCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNirooCode() throws Exception {
        // Initialize the database
        nirooCodeRepository.saveAndFlush(nirooCode);

        int databaseSizeBeforeDelete = nirooCodeRepository.findAll().size();

        // Delete the nirooCode
        restNirooCodeMockMvc.perform(delete("/api/niroo-codes/{id}", nirooCode.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NirooCode> nirooCodeList = nirooCodeRepository.findAll();
        assertThat(nirooCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
