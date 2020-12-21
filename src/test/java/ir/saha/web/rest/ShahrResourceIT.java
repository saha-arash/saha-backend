package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Shahr;
import ir.saha.repository.ShahrRepository;
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
 * Integration tests for the {@link ShahrResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class ShahrResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ZARIB_ABO_HAVA = 1;
    private static final Integer UPDATED_ZARIB_ABO_HAVA = 2;

    private static final Integer DEFAULT_ZARIB_TASHILAT = 1;
    private static final Integer UPDATED_ZARIB_TASHILAT = 2;

    private static final Integer DEFAULT_MASAFAT_TA_MARKAZ = 1;
    private static final Integer UPDATED_MASAFAT_TA_MARKAZ = 2;

    @Autowired
    private ShahrRepository shahrRepository;

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

    private MockMvc restShahrMockMvc;

    private Shahr shahr;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShahrResource shahrResource = new ShahrResource(shahrRepository);
        this.restShahrMockMvc = MockMvcBuilders.standaloneSetup(shahrResource)
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
    public static Shahr createEntity(EntityManager em) {
        Shahr shahr = new Shahr()
            .name(DEFAULT_NAME)
            .zaribAboHava(DEFAULT_ZARIB_ABO_HAVA)
            .zaribTashilat(DEFAULT_ZARIB_TASHILAT)
            .masafatTaMarkaz(DEFAULT_MASAFAT_TA_MARKAZ);
        return shahr;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shahr createUpdatedEntity(EntityManager em) {
        Shahr shahr = new Shahr()
            .name(UPDATED_NAME)
            .zaribAboHava(UPDATED_ZARIB_ABO_HAVA)
            .zaribTashilat(UPDATED_ZARIB_TASHILAT)
            .masafatTaMarkaz(UPDATED_MASAFAT_TA_MARKAZ);
        return shahr;
    }

    @BeforeEach
    public void initTest() {
        shahr = createEntity(em);
    }

    @Test
    @Transactional
    public void createShahr() throws Exception {
        int databaseSizeBeforeCreate = shahrRepository.findAll().size();

        // Create the Shahr
        restShahrMockMvc.perform(post("/api/shahrs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shahr)))
            .andExpect(status().isCreated());

        // Validate the Shahr in the database
        List<Shahr> shahrList = shahrRepository.findAll();
        assertThat(shahrList).hasSize(databaseSizeBeforeCreate + 1);
        Shahr testShahr = shahrList.get(shahrList.size() - 1);
        assertThat(testShahr.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShahr.getZaribAboHava()).isEqualTo(DEFAULT_ZARIB_ABO_HAVA);
        assertThat(testShahr.getZaribTashilat()).isEqualTo(DEFAULT_ZARIB_TASHILAT);
        assertThat(testShahr.getMasafatTaMarkaz()).isEqualTo(DEFAULT_MASAFAT_TA_MARKAZ);
    }

    @Test
    @Transactional
    public void createShahrWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shahrRepository.findAll().size();

        // Create the Shahr with an existing ID
        shahr.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShahrMockMvc.perform(post("/api/shahrs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shahr)))
            .andExpect(status().isBadRequest());

        // Validate the Shahr in the database
        List<Shahr> shahrList = shahrRepository.findAll();
        assertThat(shahrList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShahrs() throws Exception {
        // Initialize the database
        shahrRepository.saveAndFlush(shahr);

        // Get all the shahrList
        restShahrMockMvc.perform(get("/api/shahrs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shahr.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].zaribAboHava").value(hasItem(DEFAULT_ZARIB_ABO_HAVA)))
            .andExpect(jsonPath("$.[*].zaribTashilat").value(hasItem(DEFAULT_ZARIB_TASHILAT)))
            .andExpect(jsonPath("$.[*].masafatTaMarkaz").value(hasItem(DEFAULT_MASAFAT_TA_MARKAZ)));
    }
    
    @Test
    @Transactional
    public void getShahr() throws Exception {
        // Initialize the database
        shahrRepository.saveAndFlush(shahr);

        // Get the shahr
        restShahrMockMvc.perform(get("/api/shahrs/{id}", shahr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shahr.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.zaribAboHava").value(DEFAULT_ZARIB_ABO_HAVA))
            .andExpect(jsonPath("$.zaribTashilat").value(DEFAULT_ZARIB_TASHILAT))
            .andExpect(jsonPath("$.masafatTaMarkaz").value(DEFAULT_MASAFAT_TA_MARKAZ));
    }

    @Test
    @Transactional
    public void getNonExistingShahr() throws Exception {
        // Get the shahr
        restShahrMockMvc.perform(get("/api/shahrs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShahr() throws Exception {
        // Initialize the database
        shahrRepository.saveAndFlush(shahr);

        int databaseSizeBeforeUpdate = shahrRepository.findAll().size();

        // Update the shahr
        Shahr updatedShahr = shahrRepository.findById(shahr.getId()).get();
        // Disconnect from session so that the updates on updatedShahr are not directly saved in db
        em.detach(updatedShahr);
        updatedShahr
            .name(UPDATED_NAME)
            .zaribAboHava(UPDATED_ZARIB_ABO_HAVA)
            .zaribTashilat(UPDATED_ZARIB_TASHILAT)
            .masafatTaMarkaz(UPDATED_MASAFAT_TA_MARKAZ);

        restShahrMockMvc.perform(put("/api/shahrs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedShahr)))
            .andExpect(status().isOk());

        // Validate the Shahr in the database
        List<Shahr> shahrList = shahrRepository.findAll();
        assertThat(shahrList).hasSize(databaseSizeBeforeUpdate);
        Shahr testShahr = shahrList.get(shahrList.size() - 1);
        assertThat(testShahr.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShahr.getZaribAboHava()).isEqualTo(UPDATED_ZARIB_ABO_HAVA);
        assertThat(testShahr.getZaribTashilat()).isEqualTo(UPDATED_ZARIB_TASHILAT);
        assertThat(testShahr.getMasafatTaMarkaz()).isEqualTo(UPDATED_MASAFAT_TA_MARKAZ);
    }

    @Test
    @Transactional
    public void updateNonExistingShahr() throws Exception {
        int databaseSizeBeforeUpdate = shahrRepository.findAll().size();

        // Create the Shahr

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShahrMockMvc.perform(put("/api/shahrs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shahr)))
            .andExpect(status().isBadRequest());

        // Validate the Shahr in the database
        List<Shahr> shahrList = shahrRepository.findAll();
        assertThat(shahrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShahr() throws Exception {
        // Initialize the database
        shahrRepository.saveAndFlush(shahr);

        int databaseSizeBeforeDelete = shahrRepository.findAll().size();

        // Delete the shahr
        restShahrMockMvc.perform(delete("/api/shahrs/{id}", shahr.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shahr> shahrList = shahrRepository.findAll();
        assertThat(shahrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
