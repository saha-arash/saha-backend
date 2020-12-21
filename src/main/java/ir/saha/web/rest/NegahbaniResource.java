package ir.saha.web.rest;

import ir.saha.domain.Negahbani;
import ir.saha.repository.NegahbaniRepository;
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
 * REST controller for managing {@link ir.saha.domain.Negahbani}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NegahbaniResource {

    private final Logger log = LoggerFactory.getLogger(NegahbaniResource.class);

    private static final String ENTITY_NAME = "negahbani";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NegahbaniRepository negahbaniRepository;

    public NegahbaniResource(NegahbaniRepository negahbaniRepository) {
        this.negahbaniRepository = negahbaniRepository;
    }

    /**
     * {@code POST  /negahbanis} : Create a new negahbani.
     *
     * @param negahbani the negahbani to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new negahbani, or with status {@code 400 (Bad Request)} if the negahbani has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/negahbanis")
    public ResponseEntity<Negahbani> createNegahbani(@RequestBody Negahbani negahbani) throws URISyntaxException {
        log.debug("REST request to save Negahbani : {}", negahbani);
        if (negahbani.getId() != null) {
            throw new BadRequestAlertException("A new negahbani cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Negahbani result = negahbaniRepository.save(negahbani);
        return ResponseEntity.created(new URI("/api/negahbanis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /negahbanis} : Updates an existing negahbani.
     *
     * @param negahbani the negahbani to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated negahbani,
     * or with status {@code 400 (Bad Request)} if the negahbani is not valid,
     * or with status {@code 500 (Internal Server Error)} if the negahbani couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/negahbanis")
    public ResponseEntity<Negahbani> updateNegahbani(@RequestBody Negahbani negahbani) throws URISyntaxException {
        log.debug("REST request to update Negahbani : {}", negahbani);
        if (negahbani.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Negahbani result = negahbaniRepository.save(negahbani);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, negahbani.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /negahbanis} : get all the negahbanis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of negahbanis in body.
     */
    @GetMapping("/negahbanis")
    public List<Negahbani> getAllNegahbanis() {
        log.debug("REST request to get all Negahbanis");
        return negahbaniRepository.findAll();
    }

    /**
     * {@code GET  /negahbanis/:id} : get the "id" negahbani.
     *
     * @param id the id of the negahbani to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the negahbani, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/negahbanis/{id}")
    public ResponseEntity<Negahbani> getNegahbani(@PathVariable Long id) {
        log.debug("REST request to get Negahbani : {}", id);
        Optional<Negahbani> negahbani = negahbaniRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(negahbani);
    }

    /**
     * {@code DELETE  /negahbanis/:id} : delete the "id" negahbani.
     *
     * @param id the id of the negahbani to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/negahbanis/{id}")
    public ResponseEntity<Void> deleteNegahbani(@PathVariable Long id) {
        log.debug("REST request to delete Negahbani : {}", id);
        negahbaniRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
