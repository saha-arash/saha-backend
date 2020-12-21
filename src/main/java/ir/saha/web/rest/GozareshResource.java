package ir.saha.web.rest;

import ir.saha.domain.Gozaresh;
import ir.saha.repository.GozareshRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link ir.saha.domain.Gozaresh}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GozareshResource {

    private final Logger log = LoggerFactory.getLogger(GozareshResource.class);

    private static final String ENTITY_NAME = "gozaresh";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GozareshRepository gozareshRepository;

    public GozareshResource(GozareshRepository gozareshRepository) {
        this.gozareshRepository = gozareshRepository;
    }

    /**
     * {@code POST  /gozareshes} : Create a new gozaresh.
     *
     * @param gozaresh the gozaresh to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gozaresh, or with status {@code 400 (Bad Request)} if the gozaresh has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gozareshes")
    public ResponseEntity<Gozaresh> createGozaresh(@RequestBody Gozaresh gozaresh) throws URISyntaxException {
        log.debug("REST request to save Gozaresh : {}", gozaresh);
        if (gozaresh.getId() != null) {
            throw new BadRequestAlertException("A new gozaresh cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gozaresh result = gozareshRepository.save(gozaresh);
        return ResponseEntity.created(new URI("/api/gozareshes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gozareshes} : Updates an existing gozaresh.
     *
     * @param gozaresh the gozaresh to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gozaresh,
     * or with status {@code 400 (Bad Request)} if the gozaresh is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gozaresh couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gozareshes")
    public ResponseEntity<Gozaresh> updateGozaresh(@RequestBody Gozaresh gozaresh) throws URISyntaxException {
        log.debug("REST request to update Gozaresh : {}", gozaresh);
        if (gozaresh.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Gozaresh result = gozareshRepository.save(gozaresh);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gozaresh.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /gozareshes} : get all the gozareshes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gozareshes in body.
     */
    @GetMapping("/gozareshes")
    public List<Gozaresh> getAllGozareshes(@RequestParam(required = false) String filter) {
        if ("hesabresi-is-null".equals(filter)) {
            log.debug("REST request to get all Gozareshs where hesabResi is null");
            return StreamSupport
                .stream(gozareshRepository.findAll().spliterator(), false)
                .filter(gozaresh -> gozaresh.getHesabResi() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Gozareshes");
        return gozareshRepository.findAll();
    }

    /**
     * {@code GET  /gozareshes/:id} : get the "id" gozaresh.
     *
     * @param id the id of the gozaresh to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gozaresh, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gozareshes/{id}")
    public ResponseEntity<Gozaresh> getGozaresh(@PathVariable Long id) {
        log.debug("REST request to get Gozaresh : {}", id);
        Optional<Gozaresh> gozaresh = gozareshRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gozaresh);
    }

    /**
     * {@code DELETE  /gozareshes/:id} : delete the "id" gozaresh.
     *
     * @param id the id of the gozaresh to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gozareshes/{id}")
    public ResponseEntity<Void> deleteGozaresh(@PathVariable Long id) {
        log.debug("REST request to delete Gozaresh : {}", id);
        gozareshRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
