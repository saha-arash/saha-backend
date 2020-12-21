package ir.saha.web.rest;

import ir.saha.domain.Semat;
import ir.saha.repository.SematRepository;
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
 * REST controller for managing {@link ir.saha.domain.Semat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SematResource {

    private final Logger log = LoggerFactory.getLogger(SematResource.class);

    private static final String ENTITY_NAME = "semat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SematRepository sematRepository;

    public SematResource(SematRepository sematRepository) {
        this.sematRepository = sematRepository;
    }

    /**
     * {@code POST  /semats} : Create a new semat.
     *
     * @param semat the semat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semat, or with status {@code 400 (Bad Request)} if the semat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/semats")
    public ResponseEntity<Semat> createSemat(@RequestBody Semat semat) throws URISyntaxException {
        log.debug("REST request to save Semat : {}", semat);
        if (semat.getId() != null) {
            throw new BadRequestAlertException("A new semat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Semat result = sematRepository.save(semat);
        return ResponseEntity.created(new URI("/api/semats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /semats} : Updates an existing semat.
     *
     * @param semat the semat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semat,
     * or with status {@code 400 (Bad Request)} if the semat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/semats")
    public ResponseEntity<Semat> updateSemat(@RequestBody Semat semat) throws URISyntaxException {
        log.debug("REST request to update Semat : {}", semat);
        if (semat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Semat result = sematRepository.save(semat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semat.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /semats} : get all the semats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semats in body.
     */
    @GetMapping("/semats")
    public List<Semat> getAllSemats() {
        log.debug("REST request to get all Semats");
        return sematRepository.findAll();
    }

    /**
     * {@code GET  /semats/:id} : get the "id" semat.
     *
     * @param id the id of the semat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/semats/{id}")
    public ResponseEntity<Semat> getSemat(@PathVariable Long id) {
        log.debug("REST request to get Semat : {}", id);
        Optional<Semat> semat = sematRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(semat);
    }

    /**
     * {@code DELETE  /semats/:id} : delete the "id" semat.
     *
     * @param id the id of the semat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/semats/{id}")
    public ResponseEntity<Void> deleteSemat(@PathVariable Long id) {
        log.debug("REST request to delete Semat : {}", id);
        sematRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
