package ir.saha.web.rest;

import ir.saha.domain.HesabResi;
import ir.saha.repository.HesabResiRepository;
import ir.saha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ir.saha.domain.HesabResi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HesabResiResource {

    private final Logger log = LoggerFactory.getLogger(HesabResiResource.class);

    private static final String ENTITY_NAME = "hesabResi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HesabResiRepository hesabResiRepository;

    public HesabResiResource(HesabResiRepository hesabResiRepository) {
        this.hesabResiRepository = hesabResiRepository;
    }

    /**
     * {@code POST  /hesab-resis} : Create a new hesabResi.
     *
     * @param hesabResi the hesabResi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hesabResi, or with status {@code 400 (Bad Request)} if the hesabResi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hesab-resis")
    public ResponseEntity<HesabResi> createHesabResi(@RequestBody HesabResi hesabResi) throws URISyntaxException {
        log.debug("REST request to save HesabResi : {}", hesabResi);
        if (hesabResi.getId() != null) {
            throw new BadRequestAlertException("A new hesabResi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HesabResi result = hesabResiRepository.save(hesabResi);
        return ResponseEntity.created(new URI("/api/hesab-resis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hesab-resis} : Updates an existing hesabResi.
     *
     * @param hesabResi the hesabResi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hesabResi,
     * or with status {@code 400 (Bad Request)} if the hesabResi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hesabResi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hesab-resis")
    public ResponseEntity<HesabResi> updateHesabResi(@RequestBody HesabResi hesabResi) throws URISyntaxException {
        log.debug("REST request to update HesabResi : {}", hesabResi);
        if (hesabResi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HesabResi result = hesabResiRepository.save(hesabResi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hesabResi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hesab-resis} : get all the hesabResis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hesabResis in body.
     */
    @GetMapping("/hesab-resis")
    public List<HesabResi> getAllHesabResis() {
        log.debug("REST request to get all HesabResis");
        return hesabResiRepository.findAll();
    }

    /**
     * {@code GET  /hesab-resis/:id} : get the "id" hesabResi.
     *
     * @param id the id of the hesabResi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hesabResi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hesab-resis/{id}")
    public ResponseEntity<HesabResi> getHesabResi(@PathVariable Long id) {
        log.debug("REST request to get HesabResi : {}", id);
        Optional<HesabResi> hesabResi = hesabResiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hesabResi);
    }

    /**
     * {@code DELETE  /hesab-resis/:id} : delete the "id" hesabResi.
     *
     * @param id the id of the hesabResi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hesab-resis/{id}")
    public ResponseEntity<Void> deleteHesabResi(@PathVariable Long id) {
        log.debug("REST request to delete HesabResi : {}", id);
        hesabResiRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
