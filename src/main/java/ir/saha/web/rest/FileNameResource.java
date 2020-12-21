package ir.saha.web.rest;

import ir.saha.domain.FileName;
import ir.saha.repository.FileNameRepository;
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
 * REST controller for managing {@link ir.saha.domain.FileName}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FileNameResource {

    private final Logger log = LoggerFactory.getLogger(FileNameResource.class);

    private static final String ENTITY_NAME = "fileName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileNameRepository fileNameRepository;

    public FileNameResource(FileNameRepository fileNameRepository) {
        this.fileNameRepository = fileNameRepository;
    }

    /**
     * {@code POST  /file-names} : Create a new fileName.
     *
     * @param fileName the fileName to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileName, or with status {@code 400 (Bad Request)} if the fileName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-names")
    public ResponseEntity<FileName> createFileName(@RequestBody FileName fileName) throws URISyntaxException {
        log.debug("REST request to save FileName : {}", fileName);
        if (fileName.getId() != null) {
            throw new BadRequestAlertException("A new fileName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileName result = fileNameRepository.save(fileName);
        return ResponseEntity.created(new URI("/api/file-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-names} : Updates an existing fileName.
     *
     * @param fileName the fileName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileName,
     * or with status {@code 400 (Bad Request)} if the fileName is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-names")
    public ResponseEntity<FileName> updateFileName(@RequestBody FileName fileName) throws URISyntaxException {
        log.debug("REST request to update FileName : {}", fileName);
        if (fileName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileName result = fileNameRepository.save(fileName);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileName.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /file-names} : get all the fileNames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileNames in body.
     */
    @GetMapping("/file-names")
    public List<FileName> getAllFileNames() {
        log.debug("REST request to get all FileNames");
        return fileNameRepository.findAll();
    }

    /**
     * {@code GET  /file-names/:id} : get the "id" fileName.
     *
     * @param id the id of the fileName to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileName, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-names/{id}")
    public ResponseEntity<FileName> getFileName(@PathVariable Long id) {
        log.debug("REST request to get FileName : {}", id);
        Optional<FileName> fileName = fileNameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fileName);
    }

    /**
     * {@code DELETE  /file-names/:id} : delete the "id" fileName.
     *
     * @param id the id of the fileName to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-names/{id}")
    public ResponseEntity<Void> deleteFileName(@PathVariable Long id) {
        log.debug("REST request to delete FileName : {}", id);
        fileNameRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
