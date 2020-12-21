package ir.saha.web.rest;

import ir.saha.domain.Karbar;
import ir.saha.repository.KarbarRepository;
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
 * REST controller for managing {@link ir.saha.domain.Karbar}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KarbarResource {

    private final Logger log = LoggerFactory.getLogger(KarbarResource.class);

    private static final String ENTITY_NAME = "karbar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KarbarRepository karbarRepository;

    public KarbarResource(KarbarRepository karbarRepository) {
        this.karbarRepository = karbarRepository;
    }

    /**
     * {@code POST  /karbars} : Create a new karbar.
     *
     * @param karbar the karbar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new karbar, or with status {@code 400 (Bad Request)} if the karbar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/karbars")
    public ResponseEntity<Karbar> createKarbar(@RequestBody Karbar karbar) throws URISyntaxException {
        log.debug("REST request to save Karbar : {}", karbar);
        if (karbar.getId() != null) {
            throw new BadRequestAlertException("A new karbar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Karbar result = karbarRepository.save(karbar);
        return ResponseEntity.created(new URI("/api/karbars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /karbars} : Updates an existing karbar.
     *
     * @param karbar the karbar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated karbar,
     * or with status {@code 400 (Bad Request)} if the karbar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the karbar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/karbars")
    public ResponseEntity<Karbar> updateKarbar(@RequestBody Karbar karbar) throws URISyntaxException {
        log.debug("REST request to update Karbar : {}", karbar);
        if (karbar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Karbar result = karbarRepository.save(karbar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, karbar.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /karbars} : get all the karbars.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of karbars in body.
     */
    @GetMapping("/karbars")
    public List<Karbar> getAllKarbars(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Karbars");
        return karbarRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /karbars/:id} : get the "id" karbar.
     *
     * @param id the id of the karbar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the karbar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/karbars/{id}")
    public ResponseEntity<Karbar> getKarbar(@PathVariable Long id) {
        log.debug("REST request to get Karbar : {}", id);
        Optional<Karbar> karbar = karbarRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(karbar);
    }

    /**
     * {@code DELETE  /karbars/:id} : delete the "id" karbar.
     *
     * @param id the id of the karbar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/karbars/{id}")
    public ResponseEntity<Void> deleteKarbar(@PathVariable Long id) {
        log.debug("REST request to delete Karbar : {}", id);
        karbarRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
