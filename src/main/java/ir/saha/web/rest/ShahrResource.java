package ir.saha.web.rest;

import ir.saha.domain.Shahr;
import ir.saha.repository.ShahrRepository;
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
 * REST controller for managing {@link ir.saha.domain.Shahr}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ShahrResource {

    private final Logger log = LoggerFactory.getLogger(ShahrResource.class);

    private static final String ENTITY_NAME = "shahr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShahrRepository shahrRepository;

    public ShahrResource(ShahrRepository shahrRepository) {
        this.shahrRepository = shahrRepository;
    }

    /**
     * {@code POST  /shahrs} : Create a new shahr.
     *
     * @param shahr the shahr to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shahr, or with status {@code 400 (Bad Request)} if the shahr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shahrs")
    public ResponseEntity<Shahr> createShahr(@RequestBody Shahr shahr) throws URISyntaxException {
        log.debug("REST request to save Shahr : {}", shahr);
        if (shahr.getId() != null) {
            throw new BadRequestAlertException("A new shahr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Shahr result = shahrRepository.save(shahr);
        return ResponseEntity.created(new URI("/api/shahrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shahrs} : Updates an existing shahr.
     *
     * @param shahr the shahr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shahr,
     * or with status {@code 400 (Bad Request)} if the shahr is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shahr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shahrs")
    public ResponseEntity<Shahr> updateShahr(@RequestBody Shahr shahr) throws URISyntaxException {
        log.debug("REST request to update Shahr : {}", shahr);
        if (shahr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Shahr result = shahrRepository.save(shahr);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shahr.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shahrs} : get all the shahrs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shahrs in body.
     */
    @GetMapping("/shahrs")
    public List<Shahr> getAllShahrs() {
        log.debug("REST request to get all Shahrs");
        return shahrRepository.findAll();
    }

    /**
     * {@code GET  /shahrs/:id} : get the "id" shahr.
     *
     * @param id the id of the shahr to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shahr, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shahrs/{id}")
    public ResponseEntity<Shahr> getShahr(@PathVariable Long id) {
        log.debug("REST request to get Shahr : {}", id);
        Optional<Shahr> shahr = shahrRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shahr);
    }

    /**
     * {@code DELETE  /shahrs/:id} : delete the "id" shahr.
     *
     * @param id the id of the shahr to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shahrs/{id}")
    public ResponseEntity<Void> deleteShahr(@PathVariable Long id) {
        log.debug("REST request to delete Shahr : {}", id);
        shahrRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
