package com.deleap.web.rest;

import com.deleap.domain.Academy;
import com.deleap.repository.AcademyRepository;
import com.deleap.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.deleap.domain.Academy}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AcademyResource {

    private final Logger log = LoggerFactory.getLogger(AcademyResource.class);

    private static final String ENTITY_NAME = "academy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcademyRepository academyRepository;

    public AcademyResource(AcademyRepository academyRepository) {
        this.academyRepository = academyRepository;
    }

    /**
     * {@code POST  /academies} : Create a new academy.
     *
     * @param academy the academy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new academy, or with status {@code 400 (Bad Request)} if the academy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/academies")
    public ResponseEntity<Academy> createAcademy(@Valid @RequestBody Academy academy) throws URISyntaxException {
        log.debug("REST request to save Academy : {}", academy);
        if (academy.getId() != null) {
            throw new BadRequestAlertException("A new academy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Academy result = academyRepository.save(academy);
        return ResponseEntity
            .created(new URI("/api/academies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /academies/:id} : Updates an existing academy.
     *
     * @param id the id of the academy to save.
     * @param academy the academy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academy,
     * or with status {@code 400 (Bad Request)} if the academy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the academy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/academies/{id}")
    public ResponseEntity<Academy> updateAcademy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Academy academy
    ) throws URISyntaxException {
        log.debug("REST request to update Academy : {}, {}", id, academy);
        if (academy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Academy result = academyRepository.save(academy);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academy.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /academies/:id} : Partial updates given fields of an existing academy, field will ignore if it is null
     *
     * @param id the id of the academy to save.
     * @param academy the academy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academy,
     * or with status {@code 400 (Bad Request)} if the academy is not valid,
     * or with status {@code 404 (Not Found)} if the academy is not found,
     * or with status {@code 500 (Internal Server Error)} if the academy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/academies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Academy> partialUpdateAcademy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Academy academy
    ) throws URISyntaxException {
        log.debug("REST request to partial update Academy partially : {}, {}", id, academy);
        if (academy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Academy> result = academyRepository
            .findById(academy.getId())
            .map(existingAcademy -> {
                if (academy.getName() != null) {
                    existingAcademy.setName(academy.getName());
                }
                if (academy.getType() != null) {
                    existingAcademy.setType(academy.getType());
                }

                return existingAcademy;
            })
            .map(academyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academy.getId().toString())
        );
    }

    /**
     * {@code GET  /academies} : get all the academies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of academies in body.
     */
    @GetMapping("/academies")
    public List<Academy> getAllAcademies() {
        log.debug("REST request to get all Academies");
        return academyRepository.findAll();
    }

    /**
     * {@code GET  /academies/:id} : get the "id" academy.
     *
     * @param id the id of the academy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the academy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/academies/{id}")
    public ResponseEntity<Academy> getAcademy(@PathVariable Long id) {
        log.debug("REST request to get Academy : {}", id);
        Optional<Academy> academy = academyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(academy);
    }

    /**
     * {@code DELETE  /academies/:id} : delete the "id" academy.
     *
     * @param id the id of the academy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/academies/{id}")
    public ResponseEntity<Void> deleteAcademy(@PathVariable Long id) {
        log.debug("REST request to delete Academy : {}", id);
        academyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
