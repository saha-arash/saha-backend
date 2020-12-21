package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.FileGozaresh;
import ir.saha.repository.FileGozareshRepository;
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
 * Integration tests for the {@link FileGozareshResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class FileGozareshResourceIT {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private FileGozareshRepository fileGozareshRepository;

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

    private MockMvc restFileGozareshMockMvc;

    private FileGozaresh fileGozaresh;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileGozareshResource fileGozareshResource = new FileGozareshResource(fileGozareshRepository);
        this.restFileGozareshMockMvc = MockMvcBuilders.standaloneSetup(fileGozareshResource)
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
    public static FileGozaresh createEntity(EntityManager em) {
        FileGozaresh fileGozaresh = new FileGozaresh()
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        return fileGozaresh;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileGozaresh createUpdatedEntity(EntityManager em) {
        FileGozaresh fileGozaresh = new FileGozaresh()
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);
        return fileGozaresh;
    }

    @BeforeEach
    public void initTest() {
        fileGozaresh = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileGozaresh() throws Exception {
        int databaseSizeBeforeCreate = fileGozareshRepository.findAll().size();

        // Create the FileGozaresh
        restFileGozareshMockMvc.perform(post("/api/file-gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileGozaresh)))
            .andExpect(status().isCreated());

        // Validate the FileGozaresh in the database
        List<FileGozaresh> fileGozareshList = fileGozareshRepository.findAll();
        assertThat(fileGozareshList).hasSize(databaseSizeBeforeCreate + 1);
        FileGozaresh testFileGozaresh = fileGozareshList.get(fileGozareshList.size() - 1);
        assertThat(testFileGozaresh.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testFileGozaresh.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFileGozareshWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileGozareshRepository.findAll().size();

        // Create the FileGozaresh with an existing ID
        fileGozaresh.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileGozareshMockMvc.perform(post("/api/file-gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileGozaresh)))
            .andExpect(status().isBadRequest());

        // Validate the FileGozaresh in the database
        List<FileGozaresh> fileGozareshList = fileGozareshRepository.findAll();
        assertThat(fileGozareshList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFileGozareshes() throws Exception {
        // Initialize the database
        fileGozareshRepository.saveAndFlush(fileGozaresh);

        // Get all the fileGozareshList
        restFileGozareshMockMvc.perform(get("/api/file-gozareshes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileGozaresh.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }
    
    @Test
    @Transactional
    public void getFileGozaresh() throws Exception {
        // Initialize the database
        fileGozareshRepository.saveAndFlush(fileGozaresh);

        // Get the fileGozaresh
        restFileGozareshMockMvc.perform(get("/api/file-gozareshes/{id}", fileGozaresh.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileGozaresh.getId().intValue()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingFileGozaresh() throws Exception {
        // Get the fileGozaresh
        restFileGozareshMockMvc.perform(get("/api/file-gozareshes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileGozaresh() throws Exception {
        // Initialize the database
        fileGozareshRepository.saveAndFlush(fileGozaresh);

        int databaseSizeBeforeUpdate = fileGozareshRepository.findAll().size();

        // Update the fileGozaresh
        FileGozaresh updatedFileGozaresh = fileGozareshRepository.findById(fileGozaresh.getId()).get();
        // Disconnect from session so that the updates on updatedFileGozaresh are not directly saved in db
        em.detach(updatedFileGozaresh);
        updatedFileGozaresh
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restFileGozareshMockMvc.perform(put("/api/file-gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileGozaresh)))
            .andExpect(status().isOk());

        // Validate the FileGozaresh in the database
        List<FileGozaresh> fileGozareshList = fileGozareshRepository.findAll();
        assertThat(fileGozareshList).hasSize(databaseSizeBeforeUpdate);
        FileGozaresh testFileGozaresh = fileGozareshList.get(fileGozareshList.size() - 1);
        assertThat(testFileGozaresh.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testFileGozaresh.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFileGozaresh() throws Exception {
        int databaseSizeBeforeUpdate = fileGozareshRepository.findAll().size();

        // Create the FileGozaresh

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileGozareshMockMvc.perform(put("/api/file-gozareshes")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileGozaresh)))
            .andExpect(status().isBadRequest());

        // Validate the FileGozaresh in the database
        List<FileGozaresh> fileGozareshList = fileGozareshRepository.findAll();
        assertThat(fileGozareshList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileGozaresh() throws Exception {
        // Initialize the database
        fileGozareshRepository.saveAndFlush(fileGozaresh);

        int databaseSizeBeforeDelete = fileGozareshRepository.findAll().size();

        // Delete the fileGozaresh
        restFileGozareshMockMvc.perform(delete("/api/file-gozareshes/{id}", fileGozaresh.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileGozaresh> fileGozareshList = fileGozareshRepository.findAll();
        assertThat(fileGozareshList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
