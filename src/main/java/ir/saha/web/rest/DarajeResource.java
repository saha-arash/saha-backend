package ir.saha.web.rest;

import ir.saha.domain.Daraje;
import ir.saha.repository.DarajeRepository;
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
 * REST controller for managing {@link ir.saha.domain.Daraje}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DarajeResource {

    private final Logger log = LoggerFactory.getLogger(DarajeResource.class);

    private static final String ENTITY_NAME = "daraje";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DarajeRepository darajeRepository;

    public DarajeResource(DarajeRepository darajeRepository) {
        this.darajeRepository = darajeRepository;
    }

    /**
     * {@code POST  /darajes} : Create a new daraje.
     *
     * @param daraje the daraje to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new daraje, or with status {@code 400 (Bad Request)} if the daraje has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/darajes")
    public ResponseEntity<Daraje> createDaraje(@RequestBody Daraje daraje) throws URISyntaxException {
        log.debug("REST request to save Daraje : {}", daraje);
        if (daraje.getId() != null) {
            throw new BadRequestAlertException("A new daraje cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Daraje result = darajeRepository.save(daraje);
        return ResponseEntity.created(new URI("/api/darajes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /darajes} : Updates an existing daraje.
     *
     * @param daraje the daraje to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated daraje,
     * or with status {@code 400 (Bad Request)} if the daraje is not valid,
     * or with status {@code 500 (Internal Server Error)} if the daraje couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/darajes")
    public ResponseEntity<Daraje> updateDaraje(@RequestBody Daraje daraje) throws URISyntaxException {
        log.debug("REST request to update Daraje : {}", daraje);
        if (daraje.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Daraje result = darajeRepository.save(daraje);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, daraje.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /darajes} : get all the darajes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of darajes in body.
     */
    @GetMapping("/darajes")
    public List<Daraje> getAllDarajes() {
        log.debug("REST request to get all Darajes");
        return darajeRepository.findAll();
    }

    /**
     * {@code GET  /darajes/:id} : get the "id" daraje.
     *
     * @param id the id of the daraje to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the daraje, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/darajes/{id}")
    public ResponseEntity<Daraje> getDaraje(@PathVariable Long id) {
        log.debug("REST request to get Daraje : {}", id);
        Optional<Daraje> daraje = darajeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(daraje);
    }

    /**
     * {@code DELETE  /darajes/:id} : delete the "id" daraje.
     *
     * @param id the id of the daraje to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/darajes/{id}")
    public ResponseEntity<Void> deleteDaraje(@PathVariable Long id) {
        log.debug("REST request to delete Daraje : {}", id);
        darajeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
