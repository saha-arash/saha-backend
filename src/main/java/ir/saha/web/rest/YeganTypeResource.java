package ir.saha.web.rest;

import ir.saha.domain.YeganType;
import ir.saha.repository.YeganTypeRepository;
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
 * REST controller for managing {@link ir.saha.domain.YeganType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class YeganTypeResource {

    private final Logger log = LoggerFactory.getLogger(YeganTypeResource.class);

    private static final String ENTITY_NAME = "yeganType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YeganTypeRepository yeganTypeRepository;

    public YeganTypeResource(YeganTypeRepository yeganTypeRepository) {
        this.yeganTypeRepository = yeganTypeRepository;
    }

    /**
     * {@code POST  /yegan-types} : Create a new yeganType.
     *
     * @param yeganType the yeganType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new yeganType, or with status {@code 400 (Bad Request)} if the yeganType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/yegan-types")
    public ResponseEntity<YeganType> createYeganType(@RequestBody YeganType yeganType) throws URISyntaxException {
        log.debug("REST request to save YeganType : {}", yeganType);
        if (yeganType.getId() != null) {
            throw new BadRequestAlertException("A new yeganType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        YeganType result = yeganTypeRepository.save(yeganType);
        return ResponseEntity.created(new URI("/api/yegan-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /yegan-types} : Updates an existing yeganType.
     *
     * @param yeganType the yeganType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yeganType,
     * or with status {@code 400 (Bad Request)} if the yeganType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the yeganType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/yegan-types")
    public ResponseEntity<YeganType> updateYeganType(@RequestBody YeganType yeganType) throws URISyntaxException {
        log.debug("REST request to update YeganType : {}", yeganType);
        if (yeganType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        YeganType result = yeganTypeRepository.save(yeganType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, yeganType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /yegan-types} : get all the yeganTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of yeganTypes in body.
     */
    @GetMapping("/yegan-types")
    public List<YeganType> getAllYeganTypes() {
        log.debug("REST request to get all YeganTypes");
        return yeganTypeRepository.findAll();
    }

    /**
     * {@code GET  /yegan-types/:id} : get the "id" yeganType.
     *
     * @param id the id of the yeganType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the yeganType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/yegan-types/{id}")
    public ResponseEntity<YeganType> getYeganType(@PathVariable Long id) {
        log.debug("REST request to get YeganType : {}", id);
        Optional<YeganType> yeganType = yeganTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(yeganType);
    }

    /**
     * {@code DELETE  /yegan-types/:id} : delete the "id" yeganType.
     *
     * @param id the id of the yeganType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/yegan-types/{id}")
    public ResponseEntity<Void> deleteYeganType(@PathVariable Long id) {
        log.debug("REST request to delete YeganType : {}", id);
        yeganTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
