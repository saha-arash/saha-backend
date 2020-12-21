package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.YeganCode;
import ir.saha.repository.YeganCodeRepository;
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
 * Integration tests for the {@link YeganCodeResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class YeganCodeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private YeganCodeRepository yeganCodeRepository;

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

    private MockMvc restYeganCodeMockMvc;

    private YeganCode yeganCode;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final YeganCodeResource yeganCodeResource = new YeganCodeResource(yeganCodeRepository);
        this.restYeganCodeMockMvc = MockMvcBuilders.standaloneSetup(yeganCodeResource)
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
    public static YeganCode createEntity(EntityManager em) {
        YeganCode yeganCode = new YeganCode()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE);
        return yeganCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static YeganCode createUpdatedEntity(EntityManager em) {
        YeganCode yeganCode = new YeganCode()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);
        return yeganCode;
    }

    @BeforeEach
    public void initTest() {
        yeganCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createYeganCode() throws Exception {
        int databaseSizeBeforeCreate = yeganCodeRepository.findAll().size();

        // Create the YeganCode
        restYeganCodeMockMvc.perform(post("/api/yegan-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yeganCode)))
            .andExpect(status().isCreated());

        // Validate the YeganCode in the database
        List<YeganCode> yeganCodeList = yeganCodeRepository.findAll();
        assertThat(yeganCodeList).hasSize(databaseSizeBeforeCreate + 1);
        YeganCode testYeganCode = yeganCodeList.get(yeganCodeList.size() - 1);
        assertThat(testYeganCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testYeganCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createYeganCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = yeganCodeRepository.findAll().size();

        // Create the YeganCode with an existing ID
        yeganCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restYeganCodeMockMvc.perform(post("/api/yegan-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yeganCode)))
            .andExpect(status().isBadRequest());

        // Validate the YeganCode in the database
        List<YeganCode> yeganCodeList = yeganCodeRepository.findAll();
        assertThat(yeganCodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllYeganCodes() throws Exception {
        // Initialize the database
        yeganCodeRepository.saveAndFlush(yeganCode);

        // Get all the yeganCodeList
        restYeganCodeMockMvc.perform(get("/api/yegan-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yeganCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
    
    @Test
    @Transactional
    public void getYeganCode() throws Exception {
        // Initialize the database
        yeganCodeRepository.saveAndFlush(yeganCode);

        // Get the yeganCode
        restYeganCodeMockMvc.perform(get("/api/yegan-codes/{id}", yeganCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(yeganCode.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingYeganCode() throws Exception {
        // Get the yeganCode
        restYeganCodeMockMvc.perform(get("/api/yegan-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYeganCode() throws Exception {
        // Initialize the database
        yeganCodeRepository.saveAndFlush(yeganCode);

        int databaseSizeBeforeUpdate = yeganCodeRepository.findAll().size();

        // Update the yeganCode
        YeganCode updatedYeganCode = yeganCodeRepository.findById(yeganCode.getId()).get();
        // Disconnect from session so that the updates on updatedYeganCode are not directly saved in db
        em.detach(updatedYeganCode);
        updatedYeganCode
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);

        restYeganCodeMockMvc.perform(put("/api/yegan-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedYeganCode)))
            .andExpect(status().isOk());

        // Validate the YeganCode in the database
        List<YeganCode> yeganCodeList = yeganCodeRepository.findAll();
        assertThat(yeganCodeList).hasSize(databaseSizeBeforeUpdate);
        YeganCode testYeganCode = yeganCodeList.get(yeganCodeList.size() - 1);
        assertThat(testYeganCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testYeganCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingYeganCode() throws Exception {
        int databaseSizeBeforeUpdate = yeganCodeRepository.findAll().size();

        // Create the YeganCode

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYeganCodeMockMvc.perform(put("/api/yegan-codes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yeganCode)))
            .andExpect(status().isBadRequest());

        // Validate the YeganCode in the database
        List<YeganCode> yeganCodeList = yeganCodeRepository.findAll();
        assertThat(yeganCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteYeganCode() throws Exception {
        // Initialize the database
        yeganCodeRepository.saveAndFlush(yeganCode);

        int databaseSizeBeforeDelete = yeganCodeRepository.findAll().size();

        // Delete the yeganCode
        restYeganCodeMockMvc.perform(delete("/api/yegan-codes/{id}", yeganCode.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<YeganCode> yeganCodeList = yeganCodeRepository.findAll();
        assertThat(yeganCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
