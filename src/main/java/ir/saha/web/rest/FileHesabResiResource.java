package ir.saha.web.rest;

import ir.saha.domain.FileHesabResi;
import ir.saha.repository.FileHesabResiRepository;
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
 * REST controller for managing {@link ir.saha.domain.FileHesabResi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FileHesabResiResource {

    private final Logger log = LoggerFactory.getLogger(FileHesabResiResource.class);

    private static final String ENTITY_NAME = "fileHesabResi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileHesabResiRepository fileHesabResiRepository;

    public FileHesabResiResource(FileHesabResiRepository fileHesabResiRepository) {
        this.fileHesabResiRepository = fileHesabResiRepository;
    }

    /**
     * {@code POST  /file-hesab-resis} : Create a new fileHesabResi.
     *
     * @param fileHesabResi the fileHesabResi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileHesabResi, or with status {@code 400 (Bad Request)} if the fileHesabResi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-hesab-resis")
    public ResponseEntity<FileHesabResi> createFileHesabResi(@RequestBody FileHesabResi fileHesabResi) throws URISyntaxException {
        log.debug("REST request to save FileHesabResi : {}", fileHesabResi);
        if (fileHesabResi.getId() != null) {
            throw new BadRequestAlertException("A new fileHesabResi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileHesabResi result = fileHesabResiRepository.save(fileHesabResi);
        return ResponseEntity.created(new URI("/api/file-hesab-resis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-hesab-resis} : Updates an existing fileHesabResi.
     *
     * @param fileHesabResi the fileHesabResi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileHesabResi,
     * or with status {@code 400 (Bad Request)} if the fileHesabResi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileHesabResi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-hesab-resis")
    public ResponseEntity<FileHesabResi> updateFileHesabResi(@RequestBody FileHesabResi fileHesabResi) throws URISyntaxException {
        log.debug("REST request to update FileHesabResi : {}", fileHesabResi);
        if (fileHesabResi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileHesabResi result = fileHesabResiRepository.save(fileHesabResi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileHesabResi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /file-hesab-resis} : get all the fileHesabResis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileHesabResis in body.
     */
    @GetMapping("/file-hesab-resis")
    public List<FileHesabResi> getAllFileHesabResis() {
        log.debug("REST request to get all FileHesabResis");
        return fileHesabResiRepository.findAll();
    }

    /**
     * {@code GET  /file-hesab-resis/:id} : get the "id" fileHesabResi.
     *
     * @param id the id of the fileHesabResi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileHesabResi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-hesab-resis/{id}")
    public ResponseEntity<FileHesabResi> getFileHesabResi(@PathVariable Long id) {
        log.debug("REST request to get FileHesabResi : {}", id);
        Optional<FileHesabResi> fileHesabResi = fileHesabResiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fileHesabResi);
    }

    /**
     * {@code DELETE  /file-hesab-resis/:id} : delete the "id" fileHesabResi.
     *
     * @param id the id of the fileHesabResi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-hesab-resis/{id}")
    public ResponseEntity<Void> deleteFileHesabResi(@PathVariable Long id) {
        log.debug("REST request to delete FileHesabResi : {}", id);
        fileHesabResiRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
