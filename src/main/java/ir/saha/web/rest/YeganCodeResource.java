package ir.saha.web.rest;

import ir.saha.domain.YeganCode;
import ir.saha.repository.YeganCodeRepository;
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
 * REST controller for managing {@link ir.saha.domain.YeganCode}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class YeganCodeResource {

    private final Logger log = LoggerFactory.getLogger(YeganCodeResource.class);

    private static final String ENTITY_NAME = "yeganCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YeganCodeRepository yeganCodeRepository;

    public YeganCodeResource(YeganCodeRepository yeganCodeRepository) {
        this.yeganCodeRepository = yeganCodeRepository;
    }

    /**
     * {@code POST  /yegan-codes} : Create a new yeganCode.
     *
     * @param yeganCode the yeganCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new yeganCode, or with status {@code 400 (Bad Request)} if the yeganCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/yegan-codes")
    public ResponseEntity<YeganCode> createYeganCode(@RequestBody YeganCode yeganCode) throws URISyntaxException {
        log.debug("REST request to save YeganCode : {}", yeganCode);
        if (yeganCode.getId() != null) {
            throw new BadRequestAlertException("A new yeganCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        YeganCode result = yeganCodeRepository.save(yeganCode);
        return ResponseEntity.created(new URI("/api/yegan-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /yegan-codes} : Updates an existing yeganCode.
     *
     * @param yeganCode the yeganCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yeganCode,
     * or with status {@code 400 (Bad Request)} if the yeganCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the yeganCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/yegan-codes")
    public ResponseEntity<YeganCode> updateYeganCode(@RequestBody YeganCode yeganCode) throws URISyntaxException {
        log.debug("REST request to update YeganCode : {}", yeganCode);
        if (yeganCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        YeganCode result = yeganCodeRepository.save(yeganCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, yeganCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /yegan-codes} : get all the yeganCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of yeganCodes in body.
     */
    @GetMapping("/yegan-codes")
    public List<YeganCode> getAllYeganCodes() {
        log.debug("REST request to get all YeganCodes");
        return yeganCodeRepository.findAll();
    }

    /**
     * {@code GET  /yegan-codes/:id} : get the "id" yeganCode.
     *
     * @param id the id of the yeganCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the yeganCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/yegan-codes/{id}")
    public ResponseEntity<YeganCode> getYeganCode(@PathVariable Long id) {
        log.debug("REST request to get YeganCode : {}", id);
        Optional<YeganCode> yeganCode = yeganCodeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(yeganCode);
    }

    /**
     * {@code DELETE  /yegan-codes/:id} : delete the "id" yeganCode.
     *
     * @param id the id of the yeganCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/yegan-codes/{id}")
    public ResponseEntity<Void> deleteYeganCode(@PathVariable Long id) {
        log.debug("REST request to delete YeganCode : {}", id);
        yeganCodeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
