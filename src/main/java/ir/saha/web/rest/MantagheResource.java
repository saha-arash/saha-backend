package ir.saha.web.rest;

import ir.saha.domain.Mantaghe;
import ir.saha.repository.MantagheRepository;
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
 * REST controller for managing {@link ir.saha.domain.Mantaghe}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MantagheResource {

    private final Logger log = LoggerFactory.getLogger(MantagheResource.class);

    private static final String ENTITY_NAME = "mantaghe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MantagheRepository mantagheRepository;

    public MantagheResource(MantagheRepository mantagheRepository) {
        this.mantagheRepository = mantagheRepository;
    }

    /**
     * {@code POST  /mantaghes} : Create a new mantaghe.
     *
     * @param mantaghe the mantaghe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mantaghe, or with status {@code 400 (Bad Request)} if the mantaghe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mantaghes")
    public ResponseEntity<Mantaghe> createMantaghe(@RequestBody Mantaghe mantaghe) throws URISyntaxException {
        log.debug("REST request to save Mantaghe : {}", mantaghe);
        if (mantaghe.getId() != null) {
            throw new BadRequestAlertException("A new mantaghe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mantaghe result = mantagheRepository.save(mantaghe);
        return ResponseEntity.created(new URI("/api/mantaghes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mantaghes} : Updates an existing mantaghe.
     *
     * @param mantaghe the mantaghe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mantaghe,
     * or with status {@code 400 (Bad Request)} if the mantaghe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mantaghe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mantaghes")
    public ResponseEntity<Mantaghe> updateMantaghe(@RequestBody Mantaghe mantaghe) throws URISyntaxException {
        log.debug("REST request to update Mantaghe : {}", mantaghe);
        if (mantaghe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Mantaghe result = mantagheRepository.save(mantaghe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mantaghe.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mantaghes} : get all the mantaghes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mantaghes in body.
     */
    @GetMapping("/mantaghes")
    public List<Mantaghe> getAllMantaghes() {
        log.debug("REST request to get all Mantaghes");
        return mantagheRepository.findAll();
    }

    /**
     * {@code GET  /mantaghes/:id} : get the "id" mantaghe.
     *
     * @param id the id of the mantaghe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mantaghe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mantaghes/{id}")
    public ResponseEntity<Mantaghe> getMantaghe(@PathVariable Long id) {
        log.debug("REST request to get Mantaghe : {}", id);
        Optional<Mantaghe> mantaghe = mantagheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mantaghe);
    }

    /**
     * {@code DELETE  /mantaghes/:id} : delete the "id" mantaghe.
     *
     * @param id the id of the mantaghe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mantaghes/{id}")
    public ResponseEntity<Void> deleteMantaghe(@PathVariable Long id) {
        log.debug("REST request to delete Mantaghe : {}", id);
        mantagheRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
