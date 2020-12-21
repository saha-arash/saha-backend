package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Yegan;
import ir.saha.repository.YeganRepository;
import ir.saha.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static ir.saha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link YeganResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class YeganResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private YeganRepository yeganRepository;

    @Mock
    private YeganRepository yeganRepositoryMock;

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

    private MockMvc restYeganMockMvc;

    private Yegan yegan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final YeganResource yeganResource = new YeganResource(yeganRepository);
        this.restYeganMockMvc = MockMvcBuilders.standaloneSetup(yeganResource)
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
    public static Yegan createEntity(EntityManager em) {
        Yegan yegan = new Yegan()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE);
        return yegan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Yegan createUpdatedEntity(EntityManager em) {
        Yegan yegan = new Yegan()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);
        return yegan;
    }

    @BeforeEach
    public void initTest() {
        yegan = createEntity(em);
    }

    @Test
    @Transactional
    public void createYegan() throws Exception {
        int databaseSizeBeforeCreate = yeganRepository.findAll().size();

        // Create the Yegan
        restYeganMockMvc.perform(post("/api/yegans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yegan)))
            .andExpect(status().isCreated());

        // Validate the Yegan in the database
        List<Yegan> yeganList = yeganRepository.findAll();
        assertThat(yeganList).hasSize(databaseSizeBeforeCreate + 1);
        Yegan testYegan = yeganList.get(yeganList.size() - 1);
        assertThat(testYegan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testYegan.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createYeganWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = yeganRepository.findAll().size();

        // Create the Yegan with an existing ID
        yegan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restYeganMockMvc.perform(post("/api/yegans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yegan)))
            .andExpect(status().isBadRequest());

        // Validate the Yegan in the database
        List<Yegan> yeganList = yeganRepository.findAll();
        assertThat(yeganList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllYegans() throws Exception {
        // Initialize the database
        yeganRepository.saveAndFlush(yegan);

        // Get all the yeganList
        restYeganMockMvc.perform(get("/api/yegans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yegan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllYegansWithEagerRelationshipsIsEnabled() throws Exception {
        YeganResource yeganResource = new YeganResource(yeganRepositoryMock);
        when(yeganRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restYeganMockMvc = MockMvcBuilders.standaloneSetup(yeganResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restYeganMockMvc.perform(get("/api/yegans?eagerload=true"))
        .andExpect(status().isOk());

        verify(yeganRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllYegansWithEagerRelationshipsIsNotEnabled() throws Exception {
        YeganResource yeganResource = new YeganResource(yeganRepositoryMock);
            when(yeganRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restYeganMockMvc = MockMvcBuilders.standaloneSetup(yeganResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restYeganMockMvc.perform(get("/api/yegans?eagerload=true"))
        .andExpect(status().isOk());

            verify(yeganRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getYegan() throws Exception {
        // Initialize the database
        yeganRepository.saveAndFlush(yegan);

        // Get the yegan
        restYeganMockMvc.perform(get("/api/yegans/{id}", yegan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(yegan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingYegan() throws Exception {
        // Get the yegan
        restYeganMockMvc.perform(get("/api/yegans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYegan() throws Exception {
        // Initialize the database
        yeganRepository.saveAndFlush(yegan);

        int databaseSizeBeforeUpdate = yeganRepository.findAll().size();

        // Update the yegan
        Yegan updatedYegan = yeganRepository.findById(yegan.getId()).get();
        // Disconnect from session so that the updates on updatedYegan are not directly saved in db
        em.detach(updatedYegan);
        updatedYegan
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);

        restYeganMockMvc.perform(put("/api/yegans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedYegan)))
            .andExpect(status().isOk());

        // Validate the Yegan in the database
        List<Yegan> yeganList = yeganRepository.findAll();
        assertThat(yeganList).hasSize(databaseSizeBeforeUpdate);
        Yegan testYegan = yeganList.get(yeganList.size() - 1);
        assertThat(testYegan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testYegan.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingYegan() throws Exception {
        int databaseSizeBeforeUpdate = yeganRepository.findAll().size();

        // Create the Yegan

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYeganMockMvc.perform(put("/api/yegans")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(yegan)))
            .andExpect(status().isBadRequest());

        // Validate the Yegan in the database
        List<Yegan> yeganList = yeganRepository.findAll();
        assertThat(yeganList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteYegan() throws Exception {
        // Initialize the database
        yeganRepository.saveAndFlush(yegan);

        int databaseSizeBeforeDelete = yeganRepository.findAll().size();

        // Delete the yegan
        restYeganMockMvc.perform(delete("/api/yegans/{id}", yegan.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Yegan> yeganList = yeganRepository.findAll();
        assertThat(yeganList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
