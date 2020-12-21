package ir.saha.web.rest;

import ir.saha.domain.FileGozaresh;
import ir.saha.repository.FileGozareshRepository;
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
 * REST controller for managing {@link ir.saha.domain.FileGozaresh}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FileGozareshResource {

    private final Logger log = LoggerFactory.getLogger(FileGozareshResource.class);

    private static final String ENTITY_NAME = "fileGozaresh";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileGozareshRepository fileGozareshRepository;

    public FileGozareshResource(FileGozareshRepository fileGozareshRepository) {
        this.fileGozareshRepository = fileGozareshRepository;
    }

    /**
     * {@code POST  /file-gozareshes} : Create a new fileGozaresh.
     *
     * @param fileGozaresh the fileGozaresh to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileGozaresh, or with status {@code 400 (Bad Request)} if the fileGozaresh has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-gozareshes")
    public ResponseEntity<FileGozaresh> createFileGozaresh(@RequestBody FileGozaresh fileGozaresh) throws URISyntaxException {
        log.debug("REST request to save FileGozaresh : {}", fileGozaresh);
        if (fileGozaresh.getId() != null) {
            throw new BadRequestAlertException("A new fileGozaresh cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileGozaresh result = fileGozareshRepository.save(fileGozaresh);
        return ResponseEntity.created(new URI("/api/file-gozareshes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-gozareshes} : Updates an existing fileGozaresh.
     *
     * @param fileGozaresh the fileGozaresh to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileGozaresh,
     * or with status {@code 400 (Bad Request)} if the fileGozaresh is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileGozaresh couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-gozareshes")
    public ResponseEntity<FileGozaresh> updateFileGozaresh(@RequestBody FileGozaresh fileGozaresh) throws URISyntaxException {
        log.debug("REST request to update FileGozaresh : {}", fileGozaresh);
        if (fileGozaresh.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileGozaresh result = fileGozareshRepository.save(fileGozaresh);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileGozaresh.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /file-gozareshes} : get all the fileGozareshes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileGozareshes in body.
     */
    @GetMapping("/file-gozareshes")
    public List<FileGozaresh> getAllFileGozareshes() {
        log.debug("REST request to get all FileGozareshes");
        return fileGozareshRepository.findAll();
    }

    /**
     * {@code GET  /file-gozareshes/:id} : get the "id" fileGozaresh.
     *
     * @param id the id of the fileGozaresh to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileGozaresh, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-gozareshes/{id}")
    public ResponseEntity<FileGozaresh> getFileGozaresh(@PathVariable Long id) {
        log.debug("REST request to get FileGozaresh : {}", id);
        Optional<FileGozaresh> fileGozaresh = fileGozareshRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fileGozaresh);
    }

    /**
     * {@code DELETE  /file-gozareshes/:id} : delete the "id" fileGozaresh.
     *
     * @param id the id of the fileGozaresh to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-gozareshes/{id}")
    public ResponseEntity<Void> deleteFileGozaresh(@PathVariable Long id) {
        log.debug("REST request to delete FileGozaresh : {}", id);
        fileGozareshRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
