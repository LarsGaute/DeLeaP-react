package com.deleap.web.rest;

import com.deleap.domain.Curriculum;
import com.deleap.repository.CurriculumRepository;
import com.deleap.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.deleap.domain.Curriculum}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CurriculumResource {

    private final Logger log = LoggerFactory.getLogger(CurriculumResource.class);

    private static final String ENTITY_NAME = "curriculum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurriculumRepository curriculumRepository;

    public CurriculumResource(CurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    /**
     * {@code POST  /curricula} : Create a new curriculum.
     *
     * @param curriculum the curriculum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new curriculum, or with status {@code 400 (Bad Request)} if the curriculum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/curricula")
    public ResponseEntity<Curriculum> createCurriculum(@RequestBody Curriculum curriculum) throws URISyntaxException {
        log.debug("REST request to save Curriculum : {}", curriculum);
        if (curriculum.getId() != null) {
            throw new BadRequestAlertException("A new curriculum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Curriculum result = curriculumRepository.save(curriculum);
        return ResponseEntity
            .created(new URI("/api/curricula/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /curricula/:id} : Updates an existing curriculum.
     *
     * @param id the id of the curriculum to save.
     * @param curriculum the curriculum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculum,
     * or with status {@code 400 (Bad Request)} if the curriculum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the curriculum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/curricula/{id}")
    public ResponseEntity<Curriculum> updateCurriculum(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Curriculum curriculum
    ) throws URISyntaxException {
        log.debug("REST request to update Curriculum : {}, {}", id, curriculum);
        if (curriculum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Curriculum result = curriculumRepository.save(curriculum);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculum.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /curricula/:id} : Partial updates given fields of an existing curriculum, field will ignore if it is null
     *
     * @param id the id of the curriculum to save.
     * @param curriculum the curriculum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculum,
     * or with status {@code 400 (Bad Request)} if the curriculum is not valid,
     * or with status {@code 404 (Not Found)} if the curriculum is not found,
     * or with status {@code 500 (Internal Server Error)} if the curriculum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/curricula/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Curriculum> partialUpdateCurriculum(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Curriculum curriculum
    ) throws URISyntaxException {
        log.debug("REST request to partial update Curriculum partially : {}, {}", id, curriculum);
        if (curriculum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Curriculum> result = curriculumRepository
            .findById(curriculum.getId())
            .map(existingCurriculum -> {
                if (curriculum.getText() != null) {
                    existingCurriculum.setText(curriculum.getText());
                }
                if (curriculum.getUrl() != null) {
                    existingCurriculum.setUrl(curriculum.getUrl());
                }

                return existingCurriculum;
            })
            .map(curriculumRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculum.getId().toString())
        );
    }

    /**
     * {@code GET  /curricula} : get all the curricula.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of curricula in body.
     */
    @GetMapping("/curricula")
    public List<Curriculum> getAllCurricula(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Curricula");
        return curriculumRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /curricula/:id} : get the "id" curriculum.
     *
     * @param id the id of the curriculum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the curriculum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/curricula/{id}")
    public ResponseEntity<Curriculum> getCurriculum(@PathVariable Long id) {
        log.debug("REST request to get Curriculum : {}", id);
        Optional<Curriculum> curriculum = curriculumRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(curriculum);
    }

    /**
     * {@code DELETE  /curricula/:id} : delete the "id" curriculum.
     *
     * @param id the id of the curriculum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/curricula/{id}")
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
        log.debug("REST request to delete Curriculum : {}", id);
        curriculumRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
