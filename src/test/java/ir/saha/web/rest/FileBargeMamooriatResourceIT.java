package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.FileBargeMamooriat;
import ir.saha.repository.FileBargeMamooriatRepository;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static ir.saha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FileBargeMamooriatResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class FileBargeMamooriatResourceIT {

    private static final byte[] DEFAULT_MADAREK = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MADAREK = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MADAREK_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MADAREK_CONTENT_TYPE = "image/png";

    @Autowired
    private FileBargeMamooriatRepository fileBargeMamooriatRepository;

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

    private MockMvc restFileBargeMamooriatMockMvc;

    private FileBargeMamooriat fileBargeMamooriat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileBargeMamooriatResource fileBargeMamooriatResource = new FileBargeMamooriatResource(fileBargeMamooriatRepository);
        this.restFileBargeMamooriatMockMvc = MockMvcBuilders.standaloneSetup(fileBargeMamooriatResource)
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
    public static FileBargeMamooriat createEntity(EntityManager em) {
        FileBargeMamooriat fileBargeMamooriat = new FileBargeMamooriat()
            .madarek(DEFAULT_MADAREK)
            .madarekContentType(DEFAULT_MADAREK_CONTENT_TYPE);
        return fileBargeMamooriat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileBargeMamooriat createUpdatedEntity(EntityManager em) {
        FileBargeMamooriat fileBargeMamooriat = new FileBargeMamooriat()
            .madarek(UPDATED_MADAREK)
            .madarekContentType(UPDATED_MADAREK_CONTENT_TYPE);
        return fileBargeMamooriat;
    }

    @BeforeEach
    public void initTest() {
        fileBargeMamooriat = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileBargeMamooriat() throws Exception {
        int databaseSizeBeforeCreate = fileBargeMamooriatRepository.findAll().size();

        // Create the FileBargeMamooriat
        restFileBargeMamooriatMockMvc.perform(post("/api/file-barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileBargeMamooriat)))
            .andExpect(status().isCreated());

        // Validate the FileBargeMamooriat in the database
        List<FileBargeMamooriat> fileBargeMamooriatList = fileBargeMamooriatRepository.findAll();
        assertThat(fileBargeMamooriatList).hasSize(databaseSizeBeforeCreate + 1);
        FileBargeMamooriat testFileBargeMamooriat = fileBargeMamooriatList.get(fileBargeMamooriatList.size() - 1);
        assertThat(testFileBargeMamooriat.getMadarek()).isEqualTo(DEFAULT_MADAREK);
        assertThat(testFileBargeMamooriat.getMadarekContentType()).isEqualTo(DEFAULT_MADAREK_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFileBargeMamooriatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileBargeMamooriatRepository.findAll().size();

        // Create the FileBargeMamooriat with an existing ID
        fileBargeMamooriat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileBargeMamooriatMockMvc.perform(post("/api/file-barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileBargeMamooriat)))
            .andExpect(status().isBadRequest());

        // Validate the FileBargeMamooriat in the database
        List<FileBargeMamooriat> fileBargeMamooriatList = fileBargeMamooriatRepository.findAll();
        assertThat(fileBargeMamooriatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFileBargeMamooriats() throws Exception {
        // Initialize the database
        fileBargeMamooriatRepository.saveAndFlush(fileBargeMamooriat);

        // Get all the fileBargeMamooriatList
        restFileBargeMamooriatMockMvc.perform(get("/api/file-barge-mamooriats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileBargeMamooriat.getId().intValue())))
            .andExpect(jsonPath("$.[*].madarekContentType").value(hasItem(DEFAULT_MADAREK_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].madarek").value(hasItem(Base64Utils.encodeToString(DEFAULT_MADAREK))));
    }
    
    @Test
    @Transactional
    public void getFileBargeMamooriat() throws Exception {
        // Initialize the database
        fileBargeMamooriatRepository.saveAndFlush(fileBargeMamooriat);

        // Get the fileBargeMamooriat
        restFileBargeMamooriatMockMvc.perform(get("/api/file-barge-mamooriats/{id}", fileBargeMamooriat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileBargeMamooriat.getId().intValue()))
            .andExpect(jsonPath("$.madarekContentType").value(DEFAULT_MADAREK_CONTENT_TYPE))
            .andExpect(jsonPath("$.madarek").value(Base64Utils.encodeToString(DEFAULT_MADAREK)));
    }

    @Test
    @Transactional
    public void getNonExistingFileBargeMamooriat() throws Exception {
        // Get the fileBargeMamooriat
        restFileBargeMamooriatMockMvc.perform(get("/api/file-barge-mamooriats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileBargeMamooriat() throws Exception {
        // Initialize the database
        fileBargeMamooriatRepository.saveAndFlush(fileBargeMamooriat);

        int databaseSizeBeforeUpdate = fileBargeMamooriatRepository.findAll().size();

        // Update the fileBargeMamooriat
        FileBargeMamooriat updatedFileBargeMamooriat = fileBargeMamooriatRepository.findById(fileBargeMamooriat.getId()).get();
        // Disconnect from session so that the updates on updatedFileBargeMamooriat are not directly saved in db
        em.detach(updatedFileBargeMamooriat);
        updatedFileBargeMamooriat
            .madarek(UPDATED_MADAREK)
            .madarekContentType(UPDATED_MADAREK_CONTENT_TYPE);

        restFileBargeMamooriatMockMvc.perform(put("/api/file-barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileBargeMamooriat)))
            .andExpect(status().isOk());

        // Validate the FileBargeMamooriat in the database
        List<FileBargeMamooriat> fileBargeMamooriatList = fileBargeMamooriatRepository.findAll();
        assertThat(fileBargeMamooriatList).hasSize(databaseSizeBeforeUpdate);
        FileBargeMamooriat testFileBargeMamooriat = fileBargeMamooriatList.get(fileBargeMamooriatList.size() - 1);
        assertThat(testFileBargeMamooriat.getMadarek()).isEqualTo(UPDATED_MADAREK);
        assertThat(testFileBargeMamooriat.getMadarekContentType()).isEqualTo(UPDATED_MADAREK_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFileBargeMamooriat() throws Exception {
        int databaseSizeBeforeUpdate = fileBargeMamooriatRepository.findAll().size();

        // Create the FileBargeMamooriat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileBargeMamooriatMockMvc.perform(put("/api/file-barge-mamooriats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileBargeMamooriat)))
            .andExpect(status().isBadRequest());

        // Validate the FileBargeMamooriat in the database
        List<FileBargeMamooriat> fileBargeMamooriatList = fileBargeMamooriatRepository.findAll();
        assertThat(fileBargeMamooriatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileBargeMamooriat() throws Exception {
        // Initialize the database
        fileBargeMamooriatRepository.saveAndFlush(fileBargeMamooriat);

        int databaseSizeBeforeDelete = fileBargeMamooriatRepository.findAll().size();

        // Delete the fileBargeMamooriat
        restFileBargeMamooriatMockMvc.perform(delete("/api/file-barge-mamooriats/{id}", fileBargeMamooriat.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileBargeMamooriat> fileBargeMamooriatList = fileBargeMamooriatRepository.findAll();
        assertThat(fileBargeMamooriatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
