package ir.saha.web.rest;

import ir.saha.domain.BarnameHesabResi;
import ir.saha.repository.BarnameHesabResiRepository;
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
 * REST controller for managing {@link ir.saha.domain.BarnameHesabResi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BarnameHesabResiResource {

    private final Logger log = LoggerFactory.getLogger(BarnameHesabResiResource.class);

    private static final String ENTITY_NAME = "barnameHesabResi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BarnameHesabResiRepository barnameHesabResiRepository;

    public BarnameHesabResiResource(BarnameHesabResiRepository barnameHesabResiRepository) {
        this.barnameHesabResiRepository = barnameHesabResiRepository;
    }

    /**
     * {@code POST  /barname-hesab-resis} : Create a new barnameHesabResi.
     *
     * @param barnameHesabResi the barnameHesabResi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new barnameHesabResi, or with status {@code 400 (Bad Request)} if the barnameHesabResi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/barname-hesab-resis")
    public ResponseEntity<BarnameHesabResi> createBarnameHesabResi(@RequestBody BarnameHesabResi barnameHesabResi) throws URISyntaxException {
        log.debug("REST request to save BarnameHesabResi : {}", barnameHesabResi);
        if (barnameHesabResi.getId() != null) {
            throw new BadRequestAlertException("A new barnameHesabResi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BarnameHesabResi result = barnameHesabResiRepository.save(barnameHesabResi);
        return ResponseEntity.created(new URI("/api/barname-hesab-resis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /barname-hesab-resis} : Updates an existing barnameHesabResi.
     *
     * @param barnameHesabResi the barnameHesabResi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated barnameHesabResi,
     * or with status {@code 400 (Bad Request)} if the barnameHesabResi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the barnameHesabResi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/barname-hesab-resis")
    public ResponseEntity<BarnameHesabResi> updateBarnameHesabResi(@RequestBody BarnameHesabResi barnameHesabResi) throws URISyntaxException {
        log.debug("REST request to update BarnameHesabResi : {}", barnameHesabResi);
        if (barnameHesabResi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BarnameHesabResi result = barnameHesabResiRepository.save(barnameHesabResi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, barnameHesabResi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /barname-hesab-resis} : get all the barnameHesabResis.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of barnameHesabResis in body.
     */
    @GetMapping("/barname-hesab-resis")
    public List<BarnameHesabResi> getAllBarnameHesabResis(@RequestParam(required = false) String filter) {
        if ("hesabresi-is-null".equals(filter)) {
            log.debug("REST request to get all BarnameHesabResis where hesabResi is null");
            return StreamSupport
                .stream(barnameHesabResiRepository.findAll().spliterator(), false)
                .filter(barnameHesabResi -> barnameHesabResi.getHesabResi() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all BarnameHesabResis");
        return barnameHesabResiRepository.findAll();
    }

    /**
     * {@code GET  /barname-hesab-resis/:id} : get the "id" barnameHesabResi.
     *
     * @param id the id of the barnameHesabResi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the barnameHesabResi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/barname-hesab-resis/{id}")
    public ResponseEntity<BarnameHesabResi> getBarnameHesabResi(@PathVariable Long id) {
        log.debug("REST request to get BarnameHesabResi : {}", id);
        Optional<BarnameHesabResi> barnameHesabResi = barnameHesabResiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(barnameHesabResi);
    }

    /**
     * {@code DELETE  /barname-hesab-resis/:id} : delete the "id" barnameHesabResi.
     *
     * @param id the id of the barnameHesabResi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/barname-hesab-resis/{id}")
    public ResponseEntity<Void> deleteBarnameHesabResi(@PathVariable Long id) {
        log.debug("REST request to delete BarnameHesabResi : {}", id);
        barnameHesabResiRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
