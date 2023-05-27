package com.deleap.web.rest;

import com.deleap.domain.AcademyCourseRelation;
import com.deleap.repository.AcademyCourseRelationRepository;
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
 * REST controller for managing {@link com.deleap.domain.AcademyCourseRelation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AcademyCourseRelationResource {

    private final Logger log = LoggerFactory.getLogger(AcademyCourseRelationResource.class);

    private static final String ENTITY_NAME = "academyCourseRelation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcademyCourseRelationRepository academyCourseRelationRepository;

    public AcademyCourseRelationResource(AcademyCourseRelationRepository academyCourseRelationRepository) {
        this.academyCourseRelationRepository = academyCourseRelationRepository;
    }

    /**
     * {@code POST  /academy-course-relations} : Create a new academyCourseRelation.
     *
     * @param academyCourseRelation the academyCourseRelation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new academyCourseRelation, or with status {@code 400 (Bad Request)} if the academyCourseRelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/academy-course-relations")
    public ResponseEntity<AcademyCourseRelation> createAcademyCourseRelation(
        @Valid @RequestBody AcademyCourseRelation academyCourseRelation
    ) throws URISyntaxException {
        log.debug("REST request to save AcademyCourseRelation : {}", academyCourseRelation);
        if (academyCourseRelation.getId() != null) {
            throw new BadRequestAlertException("A new academyCourseRelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcademyCourseRelation result = academyCourseRelationRepository.save(academyCourseRelation);
        return ResponseEntity
            .created(new URI("/api/academy-course-relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /academy-course-relations/:id} : Updates an existing academyCourseRelation.
     *
     * @param id the id of the academyCourseRelation to save.
     * @param academyCourseRelation the academyCourseRelation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academyCourseRelation,
     * or with status {@code 400 (Bad Request)} if the academyCourseRelation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the academyCourseRelation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/academy-course-relations/{id}")
    public ResponseEntity<AcademyCourseRelation> updateAcademyCourseRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcademyCourseRelation academyCourseRelation
    ) throws URISyntaxException {
        log.debug("REST request to update AcademyCourseRelation : {}, {}", id, academyCourseRelation);
        if (academyCourseRelation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academyCourseRelation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academyCourseRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcademyCourseRelation result = academyCourseRelationRepository.save(academyCourseRelation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academyCourseRelation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /academy-course-relations/:id} : Partial updates given fields of an existing academyCourseRelation, field will ignore if it is null
     *
     * @param id the id of the academyCourseRelation to save.
     * @param academyCourseRelation the academyCourseRelation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academyCourseRelation,
     * or with status {@code 400 (Bad Request)} if the academyCourseRelation is not valid,
     * or with status {@code 404 (Not Found)} if the academyCourseRelation is not found,
     * or with status {@code 500 (Internal Server Error)} if the academyCourseRelation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/academy-course-relations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcademyCourseRelation> partialUpdateAcademyCourseRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcademyCourseRelation academyCourseRelation
    ) throws URISyntaxException {
        log.debug("REST request to partial update AcademyCourseRelation partially : {}, {}", id, academyCourseRelation);
        if (academyCourseRelation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academyCourseRelation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academyCourseRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcademyCourseRelation> result = academyCourseRelationRepository
            .findById(academyCourseRelation.getId())
            .map(existingAcademyCourseRelation -> {
                if (academyCourseRelation.getStart() != null) {
                    existingAcademyCourseRelation.setStart(academyCourseRelation.getStart());
                }
                if (academyCourseRelation.getEnd() != null) {
                    existingAcademyCourseRelation.setEnd(academyCourseRelation.getEnd());
                }

                return existingAcademyCourseRelation;
            })
            .map(academyCourseRelationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academyCourseRelation.getId().toString())
        );
    }

    /**
     * {@code GET  /academy-course-relations} : get all the academyCourseRelations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of academyCourseRelations in body.
     */
    @GetMapping("/academy-course-relations")
    public List<AcademyCourseRelation> getAllAcademyCourseRelations(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all AcademyCourseRelations");
        return academyCourseRelationRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /academy-course-relations/:id} : get the "id" academyCourseRelation.
     *
     * @param id the id of the academyCourseRelation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the academyCourseRelation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/academy-course-relations/{id}")
    public ResponseEntity<AcademyCourseRelation> getAcademyCourseRelation(@PathVariable Long id) {
        log.debug("REST request to get AcademyCourseRelation : {}", id);
        Optional<AcademyCourseRelation> academyCourseRelation = academyCourseRelationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(academyCourseRelation);
    }

    /**
     * {@code DELETE  /academy-course-relations/:id} : delete the "id" academyCourseRelation.
     *
     * @param id the id of the academyCourseRelation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/academy-course-relations/{id}")
    public ResponseEntity<Void> deleteAcademyCourseRelation(@PathVariable Long id) {
        log.debug("REST request to delete AcademyCourseRelation : {}", id);
        academyCourseRelationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
