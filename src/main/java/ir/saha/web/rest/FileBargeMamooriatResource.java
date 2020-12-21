package ir.saha.web.rest;

import ir.saha.domain.FileBargeMamooriat;
import ir.saha.repository.FileBargeMamooriatRepository;
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
 * REST controller for managing {@link ir.saha.domain.FileBargeMamooriat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FileBargeMamooriatResource {

    private final Logger log = LoggerFactory.getLogger(FileBargeMamooriatResource.class);

    private static final String ENTITY_NAME = "fileBargeMamooriat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileBargeMamooriatRepository fileBargeMamooriatRepository;

    public FileBargeMamooriatResource(FileBargeMamooriatRepository fileBargeMamooriatRepository) {
        this.fileBargeMamooriatRepository = fileBargeMamooriatRepository;
    }

    /**
     * {@code POST  /file-barge-mamooriats} : Create a new fileBargeMamooriat.
     *
     * @param fileBargeMamooriat the fileBargeMamooriat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileBargeMamooriat, or with status {@code 400 (Bad Request)} if the fileBargeMamooriat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-barge-mamooriats")
    public ResponseEntity<FileBargeMamooriat> createFileBargeMamooriat(@RequestBody FileBargeMamooriat fileBargeMamooriat) throws URISyntaxException {
        log.debug("REST request to save FileBargeMamooriat : {}", fileBargeMamooriat);
        if (fileBargeMamooriat.getId() != null) {
            throw new BadRequestAlertException("A new fileBargeMamooriat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileBargeMamooriat result = fileBargeMamooriatRepository.save(fileBargeMamooriat);
        return ResponseEntity.created(new URI("/api/file-barge-mamooriats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-barge-mamooriats} : Updates an existing fileBargeMamooriat.
     *
     * @param fileBargeMamooriat the fileBargeMamooriat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileBargeMamooriat,
     * or with status {@code 400 (Bad Request)} if the fileBargeMamooriat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileBargeMamooriat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-barge-mamooriats")
    public ResponseEntity<FileBargeMamooriat> updateFileBargeMamooriat(@RequestBody FileBargeMamooriat fileBargeMamooriat) throws URISyntaxException {
        log.debug("REST request to update FileBargeMamooriat : {}", fileBargeMamooriat);
        if (fileBargeMamooriat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileBargeMamooriat result = fileBargeMamooriatRepository.save(fileBargeMamooriat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileBargeMamooriat.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /file-barge-mamooriats} : get all the fileBargeMamooriats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileBargeMamooriats in body.
     */
    @GetMapping("/file-barge-mamooriats")
    public List<FileBargeMamooriat> getAllFileBargeMamooriats() {
        log.debug("REST request to get all FileBargeMamooriats");
        return fileBargeMamooriatRepository.findAll();
    }

    /**
     * {@code GET  /file-barge-mamooriats/:id} : get the "id" fileBargeMamooriat.
     *
     * @param id the id of the fileBargeMamooriat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileBargeMamooriat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-barge-mamooriats/{id}")
    public ResponseEntity<FileBargeMamooriat> getFileBargeMamooriat(@PathVariable Long id) {
        log.debug("REST request to get FileBargeMamooriat : {}", id);
        Optional<FileBargeMamooriat> fileBargeMamooriat = fileBargeMamooriatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fileBargeMamooriat);
    }

    /**
     * {@code DELETE  /file-barge-mamooriats/:id} : delete the "id" fileBargeMamooriat.
     *
     * @param id the id of the fileBargeMamooriat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-barge-mamooriats/{id}")
    public ResponseEntity<Void> deleteFileBargeMamooriat(@PathVariable Long id) {
        log.debug("REST request to delete FileBargeMamooriat : {}", id);
        fileBargeMamooriatRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
