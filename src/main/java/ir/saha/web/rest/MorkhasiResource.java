package ir.saha.web.rest;

import ir.saha.domain.Morkhasi;
import ir.saha.repository.MorkhasiRepository;
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
 * REST controller for managing {@link ir.saha.domain.Morkhasi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MorkhasiResource {

    private final Logger log = LoggerFactory.getLogger(MorkhasiResource.class);

    private static final String ENTITY_NAME = "morkhasi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MorkhasiRepository morkhasiRepository;

    public MorkhasiResource(MorkhasiRepository morkhasiRepository) {
        this.morkhasiRepository = morkhasiRepository;
    }

    /**
     * {@code POST  /morkhasis} : Create a new morkhasi.
     *
     * @param morkhasi the morkhasi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new morkhasi, or with status {@code 400 (Bad Request)} if the morkhasi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/morkhasis")
    public ResponseEntity<Morkhasi> createMorkhasi(@RequestBody Morkhasi morkhasi) throws URISyntaxException {
        log.debug("REST request to save Morkhasi : {}", morkhasi);
        if (morkhasi.getId() != null) {
            throw new BadRequestAlertException("A new morkhasi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Morkhasi result = morkhasiRepository.save(morkhasi);
        return ResponseEntity.created(new URI("/api/morkhasis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /morkhasis} : Updates an existing morkhasi.
     *
     * @param morkhasi the morkhasi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated morkhasi,
     * or with status {@code 400 (Bad Request)} if the morkhasi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the morkhasi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/morkhasis")
    public ResponseEntity<Morkhasi> updateMorkhasi(@RequestBody Morkhasi morkhasi) throws URISyntaxException {
        log.debug("REST request to update Morkhasi : {}", morkhasi);
        if (morkhasi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Morkhasi result = morkhasiRepository.save(morkhasi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, morkhasi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /morkhasis} : get all the morkhasis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of morkhasis in body.
     */
    @GetMapping("/morkhasis")
    public List<Morkhasi> getAllMorkhasis() {
        log.debug("REST request to get all Morkhasis");
        return morkhasiRepository.findAll();
    }

    /**
     * {@code GET  /morkhasis/:id} : get the "id" morkhasi.
     *
     * @param id the id of the morkhasi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the morkhasi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/morkhasis/{id}")
    public ResponseEntity<Morkhasi> getMorkhasi(@PathVariable Long id) {
        log.debug("REST request to get Morkhasi : {}", id);
        Optional<Morkhasi> morkhasi = morkhasiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(morkhasi);
    }

    /**
     * {@code DELETE  /morkhasis/:id} : delete the "id" morkhasi.
     *
     * @param id the id of the morkhasi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/morkhasis/{id}")
    public ResponseEntity<Void> deleteMorkhasi(@PathVariable Long id) {
        log.debug("REST request to delete Morkhasi : {}", id);
        morkhasiRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
