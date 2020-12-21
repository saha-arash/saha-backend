package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.FileHesabResi;
import ir.saha.repository.FileHesabResiRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ir.saha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ir.saha.domain.enumeration.FileType;
/**
 * Integration tests for the {@link FileHesabResiResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class FileHesabResiResourceIT {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_SHOMARE = 1;
    private static final Integer UPDATED_SHOMARE = 2;

    private static final Instant DEFAULT_TARIKH_NAME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TARIKH_NAME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MOZOO = "AAAAAAAAAA";
    private static final String UPDATED_MOZOO = "BBBBBBBBBB";

    private static final FileType DEFAULT_FILE_TYPE = FileType.MadarekBarnameHesabResi;
    private static final FileType UPDATED_FILE_TYPE = FileType.MohasebeHazineMamooriat;

    @Autowired
    private FileHesabResiRepository fileHesabResiRepository;

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

    private MockMvc restFileHesabResiMockMvc;

    private FileHesabResi fileHesabResi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileHesabResiResource fileHesabResiResource = new FileHesabResiResource(fileHesabResiRepository);
        this.restFileHesabResiMockMvc = MockMvcBuilders.standaloneSetup(fileHesabResiResource)
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
    public static FileHesabResi createEntity(EntityManager em) {
        FileHesabResi fileHesabResi = new FileHesabResi()
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .shomare(DEFAULT_SHOMARE)
            .tarikhName(DEFAULT_TARIKH_NAME)
            .mozoo(DEFAULT_MOZOO)
            .fileType(DEFAULT_FILE_TYPE);
        return fileHesabResi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileHesabResi createUpdatedEntity(EntityManager em) {
        FileHesabResi fileHesabResi = new FileHesabResi()
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .shomare(UPDATED_SHOMARE)
            .tarikhName(UPDATED_TARIKH_NAME)
            .mozoo(UPDATED_MOZOO)
            .fileType(UPDATED_FILE_TYPE);
        return fileHesabResi;
    }

    @BeforeEach
    public void initTest() {
        fileHesabResi = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileHesabResi() throws Exception {
        int databaseSizeBeforeCreate = fileHesabResiRepository.findAll().size();

        // Create the FileHesabResi
        restFileHesabResiMockMvc.perform(post("/api/file-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileHesabResi)))
            .andExpect(status().isCreated());

        // Validate the FileHesabResi in the database
        List<FileHesabResi> fileHesabResiList = fileHesabResiRepository.findAll();
        assertThat(fileHesabResiList).hasSize(databaseSizeBeforeCreate + 1);
        FileHesabResi testFileHesabResi = fileHesabResiList.get(fileHesabResiList.size() - 1);
        assertThat(testFileHesabResi.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testFileHesabResi.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testFileHesabResi.getShomare()).isEqualTo(DEFAULT_SHOMARE);
        assertThat(testFileHesabResi.getTarikhName()).isEqualTo(DEFAULT_TARIKH_NAME);
        assertThat(testFileHesabResi.getMozoo()).isEqualTo(DEFAULT_MOZOO);
        assertThat(testFileHesabResi.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
    }

    @Test
    @Transactional
    public void createFileHesabResiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileHesabResiRepository.findAll().size();

        // Create the FileHesabResi with an existing ID
        fileHesabResi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileHesabResiMockMvc.perform(post("/api/file-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileHesabResi)))
            .andExpect(status().isBadRequest());

        // Validate the FileHesabResi in the database
        List<FileHesabResi> fileHesabResiList = fileHesabResiRepository.findAll();
        assertThat(fileHesabResiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFileHesabResis() throws Exception {
        // Initialize the database
        fileHesabResiRepository.saveAndFlush(fileHesabResi);

        // Get all the fileHesabResiList
        restFileHesabResiMockMvc.perform(get("/api/file-hesab-resis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileHesabResi.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].shomare").value(hasItem(DEFAULT_SHOMARE)))
            .andExpect(jsonPath("$.[*].tarikhName").value(hasItem(DEFAULT_TARIKH_NAME.toString())))
            .andExpect(jsonPath("$.[*].mozoo").value(hasItem(DEFAULT_MOZOO)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getFileHesabResi() throws Exception {
        // Initialize the database
        fileHesabResiRepository.saveAndFlush(fileHesabResi);

        // Get the fileHesabResi
        restFileHesabResiMockMvc.perform(get("/api/file-hesab-resis/{id}", fileHesabResi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileHesabResi.getId().intValue()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.shomare").value(DEFAULT_SHOMARE))
            .andExpect(jsonPath("$.tarikhName").value(DEFAULT_TARIKH_NAME.toString()))
            .andExpect(jsonPath("$.mozoo").value(DEFAULT_MOZOO))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFileHesabResi() throws Exception {
        // Get the fileHesabResi
        restFileHesabResiMockMvc.perform(get("/api/file-hesab-resis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileHesabResi() throws Exception {
        // Initialize the database
        fileHesabResiRepository.saveAndFlush(fileHesabResi);

        int databaseSizeBeforeUpdate = fileHesabResiRepository.findAll().size();

        // Update the fileHesabResi
        FileHesabResi updatedFileHesabResi = fileHesabResiRepository.findById(fileHesabResi.getId()).get();
        // Disconnect from session so that the updates on updatedFileHesabResi are not directly saved in db
        em.detach(updatedFileHesabResi);
        updatedFileHesabResi
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .shomare(UPDATED_SHOMARE)
            .tarikhName(UPDATED_TARIKH_NAME)
            .mozoo(UPDATED_MOZOO)
            .fileType(UPDATED_FILE_TYPE);

        restFileHesabResiMockMvc.perform(put("/api/file-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileHesabResi)))
            .andExpect(status().isOk());

        // Validate the FileHesabResi in the database
        List<FileHesabResi> fileHesabResiList = fileHesabResiRepository.findAll();
        assertThat(fileHesabResiList).hasSize(databaseSizeBeforeUpdate);
        FileHesabResi testFileHesabResi = fileHesabResiList.get(fileHesabResiList.size() - 1);
        assertThat(testFileHesabResi.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testFileHesabResi.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testFileHesabResi.getShomare()).isEqualTo(UPDATED_SHOMARE);
        assertThat(testFileHesabResi.getTarikhName()).isEqualTo(UPDATED_TARIKH_NAME);
        assertThat(testFileHesabResi.getMozoo()).isEqualTo(UPDATED_MOZOO);
        assertThat(testFileHesabResi.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFileHesabResi() throws Exception {
        int databaseSizeBeforeUpdate = fileHesabResiRepository.findAll().size();

        // Create the FileHesabResi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileHesabResiMockMvc.perform(put("/api/file-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileHesabResi)))
            .andExpect(status().isBadRequest());

        // Validate the FileHesabResi in the database
        List<FileHesabResi> fileHesabResiList = fileHesabResiRepository.findAll();
        assertThat(fileHesabResiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileHesabResi() throws Exception {
        // Initialize the database
        fileHesabResiRepository.saveAndFlush(fileHesabResi);

        int databaseSizeBeforeDelete = fileHesabResiRepository.findAll().size();

        // Delete the fileHesabResi
        restFileHesabResiMockMvc.perform(delete("/api/file-hesab-resis/{id}", fileHesabResi.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileHesabResi> fileHesabResiList = fileHesabResiRepository.findAll();
        assertThat(fileHesabResiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
