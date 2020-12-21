package ir.saha.web.rest;

import ir.saha.domain.NirooCode;
import ir.saha.repository.NirooCodeRepository;
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
 * REST controller for managing {@link ir.saha.domain.NirooCode}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NirooCodeResource {

    private final Logger log = LoggerFactory.getLogger(NirooCodeResource.class);

    private static final String ENTITY_NAME = "nirooCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NirooCodeRepository nirooCodeRepository;

    public NirooCodeResource(NirooCodeRepository nirooCodeRepository) {
        this.nirooCodeRepository = nirooCodeRepository;
    }

    /**
     * {@code POST  /niroo-codes} : Create a new nirooCode.
     *
     * @param nirooCode the nirooCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nirooCode, or with status {@code 400 (Bad Request)} if the nirooCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/niroo-codes")
    public ResponseEntity<NirooCode> createNirooCode(@RequestBody NirooCode nirooCode) throws URISyntaxException {
        log.debug("REST request to save NirooCode : {}", nirooCode);
        if (nirooCode.getId() != null) {
            throw new BadRequestAlertException("A new nirooCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NirooCode result = nirooCodeRepository.save(nirooCode);
        return ResponseEntity.created(new URI("/api/niroo-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /niroo-codes} : Updates an existing nirooCode.
     *
     * @param nirooCode the nirooCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nirooCode,
     * or with status {@code 400 (Bad Request)} if the nirooCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nirooCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/niroo-codes")
    public ResponseEntity<NirooCode> updateNirooCode(@RequestBody NirooCode nirooCode) throws URISyntaxException {
        log.debug("REST request to update NirooCode : {}", nirooCode);
        if (nirooCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NirooCode result = nirooCodeRepository.save(nirooCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nirooCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /niroo-codes} : get all the nirooCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nirooCodes in body.
     */
    @GetMapping("/niroo-codes")
    public List<NirooCode> getAllNirooCodes() {
        log.debug("REST request to get all NirooCodes");
        return nirooCodeRepository.findAll();
    }

    /**
     * {@code GET  /niroo-codes/:id} : get the "id" nirooCode.
     *
     * @param id the id of the nirooCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nirooCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/niroo-codes/{id}")
    public ResponseEntity<NirooCode> getNirooCode(@PathVariable Long id) {
        log.debug("REST request to get NirooCode : {}", id);
        Optional<NirooCode> nirooCode = nirooCodeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nirooCode);
    }

    /**
     * {@code DELETE  /niroo-codes/:id} : delete the "id" nirooCode.
     *
     * @param id the id of the nirooCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/niroo-codes/{id}")
    public ResponseEntity<Void> deleteNirooCode(@PathVariable Long id) {
        log.debug("REST request to delete NirooCode : {}", id);
        nirooCodeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
