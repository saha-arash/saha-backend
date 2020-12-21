package ir.saha.web.rest;

import ir.saha.domain.Dore;
import ir.saha.repository.DoreRepository;
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
 * REST controller for managing {@link ir.saha.domain.Dore}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DoreResource {

    private final Logger log = LoggerFactory.getLogger(DoreResource.class);

    private static final String ENTITY_NAME = "dore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoreRepository doreRepository;

    public DoreResource(DoreRepository doreRepository) {
        this.doreRepository = doreRepository;
    }

    /**
     * {@code POST  /dores} : Create a new dore.
     *
     * @param dore the dore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dore, or with status {@code 400 (Bad Request)} if the dore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dores")
    public ResponseEntity<Dore> createDore(@RequestBody Dore dore) throws URISyntaxException {
        log.debug("REST request to save Dore : {}", dore);
        if (dore.getId() != null) {
            throw new BadRequestAlertException("A new dore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dore result = doreRepository.save(dore);
        return ResponseEntity.created(new URI("/api/dores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dores} : Updates an existing dore.
     *
     * @param dore the dore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dore,
     * or with status {@code 400 (Bad Request)} if the dore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dores")
    public ResponseEntity<Dore> updateDore(@RequestBody Dore dore) throws URISyntaxException {
        log.debug("REST request to update Dore : {}", dore);
        if (dore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dore result = doreRepository.save(dore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dore.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dores} : get all the dores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dores in body.
     */
    @GetMapping("/dores")
    public List<Dore> getAllDores() {
        log.debug("REST request to get all Dores");
        return doreRepository.findAll();
    }

    /**
     * {@code GET  /dores/:id} : get the "id" dore.
     *
     * @param id the id of the dore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dores/{id}")
    public ResponseEntity<Dore> getDore(@PathVariable Long id) {
        log.debug("REST request to get Dore : {}", id);
        Optional<Dore> dore = doreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dore);
    }

    /**
     * {@code DELETE  /dores/:id} : delete the "id" dore.
     *
     * @param id the id of the dore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dores/{id}")
    public ResponseEntity<Void> deleteDore(@PathVariable Long id) {
        log.debug("REST request to delete Dore : {}", id);
        doreRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
