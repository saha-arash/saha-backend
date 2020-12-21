package ir.saha.web.rest;

import ir.saha.domain.Ostan;
import ir.saha.repository.OstanRepository;
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
 * REST controller for managing {@link ir.saha.domain.Ostan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OstanResource {

    private final Logger log = LoggerFactory.getLogger(OstanResource.class);

    private static final String ENTITY_NAME = "ostan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OstanRepository ostanRepository;

    public OstanResource(OstanRepository ostanRepository) {
        this.ostanRepository = ostanRepository;
    }

    /**
     * {@code POST  /ostans} : Create a new ostan.
     *
     * @param ostan the ostan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ostan, or with status {@code 400 (Bad Request)} if the ostan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ostans")
    public ResponseEntity<Ostan> createOstan(@RequestBody Ostan ostan) throws URISyntaxException {
        log.debug("REST request to save Ostan : {}", ostan);
        if (ostan.getId() != null) {
            throw new BadRequestAlertException("A new ostan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ostan result = ostanRepository.save(ostan);
        return ResponseEntity.created(new URI("/api/ostans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ostans} : Updates an existing ostan.
     *
     * @param ostan the ostan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ostan,
     * or with status {@code 400 (Bad Request)} if the ostan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ostan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ostans")
    public ResponseEntity<Ostan> updateOstan(@RequestBody Ostan ostan) throws URISyntaxException {
        log.debug("REST request to update Ostan : {}", ostan);
        if (ostan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ostan result = ostanRepository.save(ostan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ostan.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ostans} : get all the ostans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ostans in body.
     */
    @GetMapping("/ostans")
    public List<Ostan> getAllOstans() {
        log.debug("REST request to get all Ostans");
        return ostanRepository.findAll();
    }

    /**
     * {@code GET  /ostans/:id} : get the "id" ostan.
     *
     * @param id the id of the ostan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ostan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ostans/{id}")
    public ResponseEntity<Ostan> getOstan(@PathVariable Long id) {
        log.debug("REST request to get Ostan : {}", id);
        Optional<Ostan> ostan = ostanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ostan);
    }

    /**
     * {@code DELETE  /ostans/:id} : delete the "id" ostan.
     *
     * @param id the id of the ostan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ostans/{id}")
    public ResponseEntity<Void> deleteOstan(@PathVariable Long id) {
        log.debug("REST request to delete Ostan : {}", id);
        ostanRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
