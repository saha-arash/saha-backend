package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.BarnameHesabResi;
import ir.saha.repository.BarnameHesabResiRepository;
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

import ir.saha.domain.enumeration.NoeBarnameHesabResi;
/**
 * Integration tests for the {@link BarnameHesabResiResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class BarnameHesabResiResourceIT {

    private static final NoeBarnameHesabResi DEFAULT_NOE_BARNAME_HESAB_RESI = NoeBarnameHesabResi.HESABRESI_BARNAMEE;
    private static final NoeBarnameHesabResi UPDATED_NOE_BARNAME_HESAB_RESI = NoeBarnameHesabResi.HESABRESI_PEYGIRI;

    @Autowired
    private BarnameHesabResiRepository barnameHesabResiRepository;

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

    private MockMvc restBarnameHesabResiMockMvc;

    private BarnameHesabResi barnameHesabResi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BarnameHesabResiResource barnameHesabResiResource = new BarnameHesabResiResource(barnameHesabResiRepository);
        this.restBarnameHesabResiMockMvc = MockMvcBuilders.standaloneSetup(barnameHesabResiResource)
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
    public static BarnameHesabResi createEntity(EntityManager em) {
        BarnameHesabResi barnameHesabResi = new BarnameHesabResi()
            .noeBarnameHesabResi(DEFAULT_NOE_BARNAME_HESAB_RESI);
        return barnameHesabResi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BarnameHesabResi createUpdatedEntity(EntityManager em) {
        BarnameHesabResi barnameHesabResi = new BarnameHesabResi()
            .noeBarnameHesabResi(UPDATED_NOE_BARNAME_HESAB_RESI);
        return barnameHesabResi;
    }

    @BeforeEach
    public void initTest() {
        barnameHesabResi = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarnameHesabResi() throws Exception {
        int databaseSizeBeforeCreate = barnameHesabResiRepository.findAll().size();

        // Create the BarnameHesabResi
        restBarnameHesabResiMockMvc.perform(post("/api/barname-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(barnameHesabResi)))
            .andExpect(status().isCreated());

        // Validate the BarnameHesabResi in the database
        List<BarnameHesabResi> barnameHesabResiList = barnameHesabResiRepository.findAll();
        assertThat(barnameHesabResiList).hasSize(databaseSizeBeforeCreate + 1);
        BarnameHesabResi testBarnameHesabResi = barnameHesabResiList.get(barnameHesabResiList.size() - 1);
        assertThat(testBarnameHesabResi.getNoeBarnameHesabResi()).isEqualTo(DEFAULT_NOE_BARNAME_HESAB_RESI);
    }

    @Test
    @Transactional
    public void createBarnameHesabResiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barnameHesabResiRepository.findAll().size();

        // Create the BarnameHesabResi with an existing ID
        barnameHesabResi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarnameHesabResiMockMvc.perform(post("/api/barname-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(barnameHesabResi)))
            .andExpect(status().isBadRequest());

        // Validate the BarnameHesabResi in the database
        List<BarnameHesabResi> barnameHesabResiList = barnameHesabResiRepository.findAll();
        assertThat(barnameHesabResiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBarnameHesabResis() throws Exception {
        // Initialize the database
        barnameHesabResiRepository.saveAndFlush(barnameHesabResi);

        // Get all the barnameHesabResiList
        restBarnameHesabResiMockMvc.perform(get("/api/barname-hesab-resis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barnameHesabResi.getId().intValue())))
            .andExpect(jsonPath("$.[*].noeBarnameHesabResi").value(hasItem(DEFAULT_NOE_BARNAME_HESAB_RESI.toString())));
    }
    
    @Test
    @Transactional
    public void getBarnameHesabResi() throws Exception {
        // Initialize the database
        barnameHesabResiRepository.saveAndFlush(barnameHesabResi);

        // Get the barnameHesabResi
        restBarnameHesabResiMockMvc.perform(get("/api/barname-hesab-resis/{id}", barnameHesabResi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(barnameHesabResi.getId().intValue()))
            .andExpect(jsonPath("$.noeBarnameHesabResi").value(DEFAULT_NOE_BARNAME_HESAB_RESI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarnameHesabResi() throws Exception {
        // Get the barnameHesabResi
        restBarnameHesabResiMockMvc.perform(get("/api/barname-hesab-resis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarnameHesabResi() throws Exception {
        // Initialize the database
        barnameHesabResiRepository.saveAndFlush(barnameHesabResi);

        int databaseSizeBeforeUpdate = barnameHesabResiRepository.findAll().size();

        // Update the barnameHesabResi
        BarnameHesabResi updatedBarnameHesabResi = barnameHesabResiRepository.findById(barnameHesabResi.getId()).get();
        // Disconnect from session so that the updates on updatedBarnameHesabResi are not directly saved in db
        em.detach(updatedBarnameHesabResi);
        updatedBarnameHesabResi
            .noeBarnameHesabResi(UPDATED_NOE_BARNAME_HESAB_RESI);

        restBarnameHesabResiMockMvc.perform(put("/api/barname-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBarnameHesabResi)))
            .andExpect(status().isOk());

        // Validate the BarnameHesabResi in the database
        List<BarnameHesabResi> barnameHesabResiList = barnameHesabResiRepository.findAll();
        assertThat(barnameHesabResiList).hasSize(databaseSizeBeforeUpdate);
        BarnameHesabResi testBarnameHesabResi = barnameHesabResiList.get(barnameHesabResiList.size() - 1);
        assertThat(testBarnameHesabResi.getNoeBarnameHesabResi()).isEqualTo(UPDATED_NOE_BARNAME_HESAB_RESI);
    }

    @Test
    @Transactional
    public void updateNonExistingBarnameHesabResi() throws Exception {
        int databaseSizeBeforeUpdate = barnameHesabResiRepository.findAll().size();

        // Create the BarnameHesabResi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBarnameHesabResiMockMvc.perform(put("/api/barname-hesab-resis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(barnameHesabResi)))
            .andExpect(status().isBadRequest());

        // Validate the BarnameHesabResi in the database
        List<BarnameHesabResi> barnameHesabResiList = barnameHesabResiRepository.findAll();
        assertThat(barnameHesabResiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBarnameHesabResi() throws Exception {
        // Initialize the database
        barnameHesabResiRepository.saveAndFlush(barnameHesabResi);

        int databaseSizeBeforeDelete = barnameHesabResiRepository.findAll().size();

        // Delete the barnameHesabResi
        restBarnameHesabResiMockMvc.perform(delete("/api/barname-hesab-resis/{id}", barnameHesabResi.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BarnameHesabResi> barnameHesabResiList = barnameHesabResiRepository.findAll();
        assertThat(barnameHesabResiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
