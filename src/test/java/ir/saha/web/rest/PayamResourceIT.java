package ir.saha.web.rest;

import ir.saha.SahaApp;
import ir.saha.domain.Payam;
import ir.saha.repository.PayamRepository;
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
 * Integration tests for the {@link PayamResource} REST controller.
 */
@SpringBootTest(classes = SahaApp.class)
public class PayamResourceIT {

    private static final String DEFAULT_ONVAN = "AAAAAAAAAA";
    private static final String UPDATED_ONVAN = "BBBBBBBBBB";

    private static final String DEFAULT_MATN = "AAAAAAAAAA";
    private static final String UPDATED_MATN = "BBBBBBBBBB";

    @Autowired
    private PayamRepository payamRepository;

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

    private MockMvc restPayamMockMvc;

    private Payam payam;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PayamResource payamResource = new PayamResource(payamRepository);
        this.restPayamMockMvc = MockMvcBuilders.standaloneSetup(payamResource)
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
    public static Payam createEntity(EntityManager em) {
        Payam payam = new Payam()
            .onvan(DEFAULT_ONVAN)
            .matn(DEFAULT_MATN);
        return payam;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payam createUpdatedEntity(EntityManager em) {
        Payam payam = new Payam()
            .onvan(UPDATED_ONVAN)
            .matn(UPDATED_MATN);
        return payam;
    }

    @BeforeEach
    public void initTest() {
        payam = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayam() throws Exception {
        int databaseSizeBeforeCreate = payamRepository.findAll().size();

        // Create the Payam
        restPayamMockMvc.perform(post("/api/payams")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payam)))
            .andExpect(status().isCreated());

        // Validate the Payam in the database
        List<Payam> payamList = payamRepository.findAll();
        assertThat(payamList).hasSize(databaseSizeBeforeCreate + 1);
        Payam testPayam = payamList.get(payamList.size() - 1);
        assertThat(testPayam.getOnvan()).isEqualTo(DEFAULT_ONVAN);
        assertThat(testPayam.getMatn()).isEqualTo(DEFAULT_MATN);
    }

    @Test
    @Transactional
    public void createPayamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payamRepository.findAll().size();

        // Create the Payam with an existing ID
        payam.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayamMockMvc.perform(post("/api/payams")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payam)))
            .andExpect(status().isBadRequest());

        // Validate the Payam in the database
        List<Payam> payamList = payamRepository.findAll();
        assertThat(payamList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPayams() throws Exception {
        // Initialize the database
        payamRepository.saveAndFlush(payam);

        // Get all the payamList
        restPayamMockMvc.perform(get("/api/payams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payam.getId().intValue())))
            .andExpect(jsonPath("$.[*].onvan").value(hasItem(DEFAULT_ONVAN)))
            .andExpect(jsonPath("$.[*].matn").value(hasItem(DEFAULT_MATN.toString())));
    }
    
    @Test
    @Transactional
    public void getPayam() throws Exception {
        // Initialize the database
        payamRepository.saveAndFlush(payam);

        // Get the payam
        restPayamMockMvc.perform(get("/api/payams/{id}", payam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payam.getId().intValue()))
            .andExpect(jsonPath("$.onvan").value(DEFAULT_ONVAN))
            .andExpect(jsonPath("$.matn").value(DEFAULT_MATN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPayam() throws Exception {
        // Get the payam
        restPayamMockMvc.perform(get("/api/payams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayam() throws Exception {
        // Initialize the database
        payamRepository.saveAndFlush(payam);

        int databaseSizeBeforeUpdate = payamRepository.findAll().size();

        // Update the payam
        Payam updatedPayam = payamRepository.findById(payam.getId()).get();
        // Disconnect from session so that the updates on updatedPayam are not directly saved in db
        em.detach(updatedPayam);
        updatedPayam
            .onvan(UPDATED_ONVAN)
            .matn(UPDATED_MATN);

        restPayamMockMvc.perform(put("/api/payams")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayam)))
            .andExpect(status().isOk());

        // Validate the Payam in the database
        List<Payam> payamList = payamRepository.findAll();
        assertThat(payamList).hasSize(databaseSizeBeforeUpdate);
        Payam testPayam = payamList.get(payamList.size() - 1);
        assertThat(testPayam.getOnvan()).isEqualTo(UPDATED_ONVAN);
        assertThat(testPayam.getMatn()).isEqualTo(UPDATED_MATN);
    }

    @Test
    @Transactional
    public void updateNonExistingPayam() throws Exception {
        int databaseSizeBeforeUpdate = payamRepository.findAll().size();

        // Create the Payam

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayamMockMvc.perform(put("/api/payams")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payam)))
            .andExpect(status().isBadRequest());

        // Validate the Payam in the database
        List<Payam> payamList = payamRepository.findAll();
        assertThat(payamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayam() throws Exception {
        // Initialize the database
        payamRepository.saveAndFlush(payam);

        int databaseSizeBeforeDelete = payamRepository.findAll().size();

        // Delete the payam
        restPayamMockMvc.perform(delete("/api/payams/{id}", payam.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payam> payamList = payamRepository.findAll();
        assertThat(payamList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
