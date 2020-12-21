package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.FileName;
import ir.saha.repository.FileNameRepository;
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
 * Integration tests for the {@link FileNameResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class FileNameResourceIT {

    private static final byte[] DEFAULT_MADRAK = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MADRAK = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MADRAK_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MADRAK_CONTENT_TYPE = "image/png";

    @Autowired
    private FileNameRepository fileNameRepository;

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

    private MockMvc restFileNameMockMvc;

    private FileName fileName;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileNameResource fileNameResource = new FileNameResource(fileNameRepository);
        this.restFileNameMockMvc = MockMvcBuilders.standaloneSetup(fileNameResource)
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
    public static FileName createEntity(EntityManager em) {
        FileName fileName = new FileName()
            .madrak(DEFAULT_MADRAK)
            .madrakContentType(DEFAULT_MADRAK_CONTENT_TYPE);
        return fileName;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileName createUpdatedEntity(EntityManager em) {
        FileName fileName = new FileName()
            .madrak(UPDATED_MADRAK)
            .madrakContentType(UPDATED_MADRAK_CONTENT_TYPE);
        return fileName;
    }

    @BeforeEach
    public void initTest() {
        fileName = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileName() throws Exception {
        int databaseSizeBeforeCreate = fileNameRepository.findAll().size();

        // Create the FileName
        restFileNameMockMvc.perform(post("/api/file-names")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileName)))
            .andExpect(status().isCreated());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeCreate + 1);
        FileName testFileName = fileNameList.get(fileNameList.size() - 1);
        assertThat(testFileName.getMadrak()).isEqualTo(DEFAULT_MADRAK);
        assertThat(testFileName.getMadrakContentType()).isEqualTo(DEFAULT_MADRAK_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFileNameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileNameRepository.findAll().size();

        // Create the FileName with an existing ID
        fileName.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileNameMockMvc.perform(post("/api/file-names")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileName)))
            .andExpect(status().isBadRequest());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFileNames() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        // Get all the fileNameList
        restFileNameMockMvc.perform(get("/api/file-names?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileName.getId().intValue())))
            .andExpect(jsonPath("$.[*].madrakContentType").value(hasItem(DEFAULT_MADRAK_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].madrak").value(hasItem(Base64Utils.encodeToString(DEFAULT_MADRAK))));
    }
    
    @Test
    @Transactional
    public void getFileName() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        // Get the fileName
        restFileNameMockMvc.perform(get("/api/file-names/{id}", fileName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileName.getId().intValue()))
            .andExpect(jsonPath("$.madrakContentType").value(DEFAULT_MADRAK_CONTENT_TYPE))
            .andExpect(jsonPath("$.madrak").value(Base64Utils.encodeToString(DEFAULT_MADRAK)));
    }

    @Test
    @Transactional
    public void getNonExistingFileName() throws Exception {
        // Get the fileName
        restFileNameMockMvc.perform(get("/api/file-names/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileName() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();

        // Update the fileName
        FileName updatedFileName = fileNameRepository.findById(fileName.getId()).get();
        // Disconnect from session so that the updates on updatedFileName are not directly saved in db
        em.detach(updatedFileName);
        updatedFileName
            .madrak(UPDATED_MADRAK)
            .madrakContentType(UPDATED_MADRAK_CONTENT_TYPE);

        restFileNameMockMvc.perform(put("/api/file-names")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileName)))
            .andExpect(status().isOk());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
        FileName testFileName = fileNameList.get(fileNameList.size() - 1);
        assertThat(testFileName.getMadrak()).isEqualTo(UPDATED_MADRAK);
        assertThat(testFileName.getMadrakContentType()).isEqualTo(UPDATED_MADRAK_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFileName() throws Exception {
        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();

        // Create the FileName

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileNameMockMvc.perform(put("/api/file-names")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileName)))
            .andExpect(status().isBadRequest());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileName() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        int databaseSizeBeforeDelete = fileNameRepository.findAll().size();

        // Delete the fileName
        restFileNameMockMvc.perform(delete("/api/file-names/{id}", fileName.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
