package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.HesabResi;
import ir.saha.repository.HesabResiRepository;
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

import ir.saha.domain.enumeration.VaziateHesabResi;
/**
 * Integration tests for the {@link HesabResiResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class HesabResiResourceIT {

    private static final Integer DEFAULT_SAL = 1;
    private static final Integer UPDATED_SAL = 2;

    private static final VaziateHesabResi DEFAULT_VAZIATE_HESAB_RESI = VaziateHesabResi.SODOOR_BARGE_MAMOORIAT;
    private static final VaziateHesabResi UPDATED_VAZIATE_HESAB_RESI = VaziateHesabResi.DAR_SHOROF_MAMOORIAT;

    @Autowired
    private HesabResiRepository hesabResiRepository;

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

    private MockMvc restHesabResiMockMvc;

    private HesabResi hesabResi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HesabResiResource hesabResiResource = new HesabResiResource(hesabResiRepository);
        this.restHesabResiMockMvc = MockMvcBuilders.standaloneSetup(hesabResiResource)
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
    public static HesabResi createEntity(EntityManager em) {
        HesabResi hesabResi = new HesabResi()
            .sal(DEFAULT_SAL)
            .vaziateHesabResi(DEFAULT_VAZIATE_HESAB_RESI);
        return hesabResi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HesabResi createUpdatedEntity(EntityManager em) {
        HesabResi hesabResi = new HesabResi()
            .sal(UPDATED_SAL)
            .vaziateHesabResi(UPDATED_VAZIATE_HESAB_RESI);
        return hesabResi;
    }

    @BeforeEach
    public void initTest() {
        hesabResi = createEntity(em);
    }

    @Test
    @Transactional
    public void createHesabResi() throws Exception {
        int databaseSizeBeforeCreate = hesabResiRepository.findAll().size();

        // Create the HesabResi
        restHesabResiMockMvc.perform(post("/api/hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hesabResi)))
            .andExpect(status().isCreated());

        // Validate the HesabResi in the database
        List<HesabResi> hesabResiList = hesabResiRepository.findAll();
        assertThat(hesabResiList).hasSize(databaseSizeBeforeCreate + 1);
        HesabResi testHesabResi = hesabResiList.get(hesabResiList.size() - 1);
        assertThat(testHesabResi.getSal()).isEqualTo(DEFAULT_SAL);
        assertThat(testHesabResi.getVaziateHesabResi()).isEqualTo(DEFAULT_VAZIATE_HESAB_RESI);
    }

    @Test
    @Transactional
    public void createHesabResiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hesabResiRepository.findAll().size();

        // Create the HesabResi with an existing ID
        hesabResi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHesabResiMockMvc.perform(post("/api/hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hesabResi)))
            .andExpect(status().isBadRequest());

        // Validate the HesabResi in the database
        List<HesabResi> hesabResiList = hesabResiRepository.findAll();
        assertThat(hesabResiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllHesabResis() throws Exception {
        // Initialize the database
        hesabResiRepository.saveAndFlush(hesabResi);

        // Get all the hesabResiList
        restHesabResiMockMvc.perform(get("/api/hesab-resis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hesabResi.getId().intValue())))
            .andExpect(jsonPath("$.[*].sal").value(hasItem(DEFAULT_SAL)))
            .andExpect(jsonPath("$.[*].vaziateHesabResi").value(hasItem(DEFAULT_VAZIATE_HESAB_RESI.toString())));
    }
    
    @Test
    @Transactional
    public void getHesabResi() throws Exception {
        // Initialize the database
        hesabResiRepository.saveAndFlush(hesabResi);

        // Get the hesabResi
        restHesabResiMockMvc.perform(get("/api/hesab-resis/{id}", hesabResi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hesabResi.getId().intValue()))
            .andExpect(jsonPath("$.sal").value(DEFAULT_SAL))
            .andExpect(jsonPath("$.vaziateHesabResi").value(DEFAULT_VAZIATE_HESAB_RESI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHesabResi() throws Exception {
        // Get the hesabResi
        restHesabResiMockMvc.perform(get("/api/hesab-resis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHesabResi() throws Exception {
        // Initialize the database
        hesabResiRepository.saveAndFlush(hesabResi);

        int databaseSizeBeforeUpdate = hesabResiRepository.findAll().size();

        // Update the hesabResi
        HesabResi updatedHesabResi = hesabResiRepository.findById(hesabResi.getId()).get();
        // Disconnect from session so that the updates on updatedHesabResi are not directly saved in db
        em.detach(updatedHesabResi);
        updatedHesabResi
            .sal(UPDATED_SAL)
            .vaziateHesabResi(UPDATED_VAZIATE_HESAB_RESI);

        restHesabResiMockMvc.perform(put("/api/hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHesabResi)))
            .andExpect(status().isOk());

        // Validate the HesabResi in the database
        List<HesabResi> hesabResiList = hesabResiRepository.findAll();
        assertThat(hesabResiList).hasSize(databaseSizeBeforeUpdate);
        HesabResi testHesabResi = hesabResiList.get(hesabResiList.size() - 1);
        assertThat(testHesabResi.getSal()).isEqualTo(UPDATED_SAL);
        assertThat(testHesabResi.getVaziateHesabResi()).isEqualTo(UPDATED_VAZIATE_HESAB_RESI);
    }

    @Test
    @Transactional
    public void updateNonExistingHesabResi() throws Exception {
        int databaseSizeBeforeUpdate = hesabResiRepository.findAll().size();

        // Create the HesabResi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHesabResiMockMvc.perform(put("/api/hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hesabResi)))
            .andExpect(status().isBadRequest());

        // Validate the HesabResi in the database
        List<HesabResi> hesabResiList = hesabResiRepository.findAll();
        assertThat(hesabResiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHesabResi() throws Exception {
        // Initialize the database
        hesabResiRepository.saveAndFlush(hesabResi);

        int databaseSizeBeforeDelete = hesabResiRepository.findAll().size();

        // Delete the hesabResi
        restHesabResiMockMvc.perform(delete("/api/hesab-resis/{id}", hesabResi.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HesabResi> hesabResiList = hesabResiRepository.findAll();
        assertThat(hesabResiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
