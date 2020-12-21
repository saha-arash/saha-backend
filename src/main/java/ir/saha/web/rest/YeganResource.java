package ir.saha.web.rest;

import ir.saha.domain.Yegan;
import ir.saha.repository.YeganRepository;
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
 * REST controller for managing {@link ir.saha.domain.Yegan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class YeganResource {

    private final Logger log = LoggerFactory.getLogger(YeganResource.class);

    private static final String ENTITY_NAME = "yegan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YeganRepository yeganRepository;

    public YeganResource(YeganRepository yeganRepository) {
        this.yeganRepository = yeganRepository;
    }

    /**
     * {@code POST  /yegans} : Create a new yegan.
     *
     * @param yegan the yegan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new yegan, or with status {@code 400 (Bad Request)} if the yegan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/yegans")
    public ResponseEntity<Yegan> createYegan(@RequestBody Yegan yegan) throws URISyntaxException {
        log.debug("REST request to save Yegan : {}", yegan);
        if (yegan.getId() != null) {
            throw new BadRequestAlertException("A new yegan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Yegan result = yeganRepository.save(yegan);
        return ResponseEntity.created(new URI("/api/yegans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /yegans} : Updates an existing yegan.
     *
     * @param yegan the yegan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yegan,
     * or with status {@code 400 (Bad Request)} if the yegan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the yegan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/yegans")
    public ResponseEntity<Yegan> updateYegan(@RequestBody Yegan yegan) throws URISyntaxException {
        log.debug("REST request to update Yegan : {}", yegan);
        if (yegan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Yegan result = yeganRepository.save(yegan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, yegan.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /yegans} : get all the yegans.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of yegans in body.
     */
    @GetMapping("/yegans")
    public List<Yegan> getAllYegans(@RequestParam(required = false) String filter,@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        if ("yegancode-is-null".equals(filter)) {
            log.debug("REST request to get all Yegans where yeganCode is null");
            return StreamSupport
                .stream(yeganRepository.findAll().spliterator(), false)
                .filter(yegan -> yegan.getYeganCode() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Yegans");
        return yeganRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /yegans/:id} : get the "id" yegan.
     *
     * @param id the id of the yegan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the yegan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/yegans/{id}")
    public ResponseEntity<Yegan> getYegan(@PathVariable Long id) {
        log.debug("REST request to get Yegan : {}", id);
        Optional<Yegan> yegan = yeganRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(yegan);
    }

    /**
     * {@code DELETE  /yegans/:id} : delete the "id" yegan.
     *
     * @param id the id of the yegan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/yegans/{id}")
    public ResponseEntity<Void> deleteYegan(@PathVariable Long id) {
        log.debug("REST request to delete Yegan : {}", id);
        yeganRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
