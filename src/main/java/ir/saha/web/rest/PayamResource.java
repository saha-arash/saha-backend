package ir.saha.web.rest;

import ir.saha.domain.Payam;
import ir.saha.repository.PayamRepository;
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
 * REST controller for managing {@link ir.saha.domain.Payam}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PayamResource {

    private final Logger log = LoggerFactory.getLogger(PayamResource.class);

    private static final String ENTITY_NAME = "payam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayamRepository payamRepository;

    public PayamResource(PayamRepository payamRepository) {
        this.payamRepository = payamRepository;
    }

    /**
     * {@code POST  /payams} : Create a new payam.
     *
     * @param payam the payam to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payam, or with status {@code 400 (Bad Request)} if the payam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payams")
    public ResponseEntity<Payam> createPayam(@RequestBody Payam payam) throws URISyntaxException {
        log.debug("REST request to save Payam : {}", payam);
        if (payam.getId() != null) {
            throw new BadRequestAlertException("A new payam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Payam result = payamRepository.save(payam);
        return ResponseEntity.created(new URI("/api/payams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payams} : Updates an existing payam.
     *
     * @param payam the payam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payam,
     * or with status {@code 400 (Bad Request)} if the payam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payams")
    public ResponseEntity<Payam> updatePayam(@RequestBody Payam payam) throws URISyntaxException {
        log.debug("REST request to update Payam : {}", payam);
        if (payam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Payam result = payamRepository.save(payam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payam.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payams} : get all the payams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payams in body.
     */
    @GetMapping("/payams")
    public List<Payam> getAllPayams() {
        log.debug("REST request to get all Payams");
        return payamRepository.findAll();
    }

    /**
     * {@code GET  /payams/:id} : get the "id" payam.
     *
     * @param id the id of the payam to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payam, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payams/{id}")
    public ResponseEntity<Payam> getPayam(@PathVariable Long id) {
        log.debug("REST request to get Payam : {}", id);
        Optional<Payam> payam = payamRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(payam);
    }

    /**
     * {@code DELETE  /payams/:id} : delete the "id" payam.
     *
     * @param id the id of the payam to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payams/{id}")
    public ResponseEntity<Void> deletePayam(@PathVariable Long id) {
        log.debug("REST request to delete Payam : {}", id);
        payamRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
