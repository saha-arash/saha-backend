package ir.saha.web.rest;

import ir.saha.domain.BargeMamooriat;
import ir.saha.repository.BargeMamooriatRepository;
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
 * REST controller for managing {@link ir.saha.domain.BargeMamooriat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BargeMamooriatResource {

    private final Logger log = LoggerFactory.getLogger(BargeMamooriatResource.class);

    private static final String ENTITY_NAME = "bargeMamooriat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BargeMamooriatRepository bargeMamooriatRepository;

    public BargeMamooriatResource(BargeMamooriatRepository bargeMamooriatRepository) {
        this.bargeMamooriatRepository = bargeMamooriatRepository;
    }

    /**
     * {@code POST  /barge-mamooriats} : Create a new bargeMamooriat.
     *
     * @param bargeMamooriat the bargeMamooriat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bargeMamooriat, or with status {@code 400 (Bad Request)} if the bargeMamooriat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/barge-mamooriats")
    public ResponseEntity<BargeMamooriat> createBargeMamooriat(@RequestBody BargeMamooriat bargeMamooriat) throws URISyntaxException {
        log.debug("REST request to save BargeMamooriat : {}", bargeMamooriat);
        if (bargeMamooriat.getId() != null) {
            throw new BadRequestAlertException("A new bargeMamooriat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BargeMamooriat result = bargeMamooriatRepository.save(bargeMamooriat);
        return ResponseEntity.created(new URI("/api/barge-mamooriats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /barge-mamooriats} : Updates an existing bargeMamooriat.
     *
     * @param bargeMamooriat the bargeMamooriat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bargeMamooriat,
     * or with status {@code 400 (Bad Request)} if the bargeMamooriat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bargeMamooriat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/barge-mamooriats")
    public ResponseEntity<BargeMamooriat> updateBargeMamooriat(@RequestBody BargeMamooriat bargeMamooriat) throws URISyntaxException {
        log.debug("REST request to update BargeMamooriat : {}", bargeMamooriat);
        if (bargeMamooriat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BargeMamooriat result = bargeMamooriatRepository.save(bargeMamooriat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bargeMamooriat.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /barge-mamooriats} : get all the bargeMamooriats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bargeMamooriats in body.
     */
    @GetMapping("/barge-mamooriats")
    public List<BargeMamooriat> getAllBargeMamooriats() {
        log.debug("REST request to get all BargeMamooriats");
        return bargeMamooriatRepository.findAll();
    }

    /**
     * {@code GET  /barge-mamooriats/:id} : get the "id" bargeMamooriat.
     *
     * @param id the id of the bargeMamooriat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bargeMamooriat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/barge-mamooriats/{id}")
    public ResponseEntity<BargeMamooriat> getBargeMamooriat(@PathVariable Long id) {
        log.debug("REST request to get BargeMamooriat : {}", id);
        Optional<BargeMamooriat> bargeMamooriat = bargeMamooriatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bargeMamooriat);
    }

    /**
     * {@code DELETE  /barge-mamooriats/:id} : delete the "id" bargeMamooriat.
     *
     * @param id the id of the bargeMamooriat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/barge-mamooriats/{id}")
    public ResponseEntity<Void> deleteBargeMamooriat(@PathVariable Long id) {
        log.debug("REST request to delete BargeMamooriat : {}", id);
        bargeMamooriatRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
