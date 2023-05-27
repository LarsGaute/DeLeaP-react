package com.deleap.web.rest;

import com.deleap.domain.CourseGoalRelations;
import com.deleap.repository.CourseGoalRelationsRepository;
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
 * REST controller for managing {@link com.deleap.domain.CourseGoalRelations}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CourseGoalRelationsResource {

    private final Logger log = LoggerFactory.getLogger(CourseGoalRelationsResource.class);

    private static final String ENTITY_NAME = "courseGoalRelations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseGoalRelationsRepository courseGoalRelationsRepository;

    public CourseGoalRelationsResource(CourseGoalRelationsRepository courseGoalRelationsRepository) {
        this.courseGoalRelationsRepository = courseGoalRelationsRepository;
    }

    /**
     * {@code POST  /course-goal-relations} : Create a new courseGoalRelations.
     *
     * @param courseGoalRelations the courseGoalRelations to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseGoalRelations, or with status {@code 400 (Bad Request)} if the courseGoalRelations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-goal-relations")
    public ResponseEntity<CourseGoalRelations> createCourseGoalRelations(@RequestBody CourseGoalRelations courseGoalRelations)
        throws URISyntaxException {
        log.debug("REST request to save CourseGoalRelations : {}", courseGoalRelations);
        if (courseGoalRelations.getId() != null) {
            throw new BadRequestAlertException("A new courseGoalRelations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseGoalRelations result = courseGoalRelationsRepository.save(courseGoalRelations);
        return ResponseEntity
            .created(new URI("/api/course-goal-relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-goal-relations/:id} : Updates an existing courseGoalRelations.
     *
     * @param id the id of the courseGoalRelations to save.
     * @param courseGoalRelations the courseGoalRelations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseGoalRelations,
     * or with status {@code 400 (Bad Request)} if the courseGoalRelations is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseGoalRelations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-goal-relations/{id}")
    public ResponseEntity<CourseGoalRelations> updateCourseGoalRelations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseGoalRelations courseGoalRelations
    ) throws URISyntaxException {
        log.debug("REST request to update CourseGoalRelations : {}, {}", id, courseGoalRelations);
        if (courseGoalRelations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseGoalRelations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseGoalRelationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseGoalRelations result = courseGoalRelationsRepository.save(courseGoalRelations);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseGoalRelations.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-goal-relations/:id} : Partial updates given fields of an existing courseGoalRelations, field will ignore if it is null
     *
     * @param id the id of the courseGoalRelations to save.
     * @param courseGoalRelations the courseGoalRelations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseGoalRelations,
     * or with status {@code 400 (Bad Request)} if the courseGoalRelations is not valid,
     * or with status {@code 404 (Not Found)} if the courseGoalRelations is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseGoalRelations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-goal-relations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourseGoalRelations> partialUpdateCourseGoalRelations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseGoalRelations courseGoalRelations
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseGoalRelations partially : {}, {}", id, courseGoalRelations);
        if (courseGoalRelations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseGoalRelations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseGoalRelationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseGoalRelations> result = courseGoalRelationsRepository
            .findById(courseGoalRelations.getId())
            .map(existingCourseGoalRelations -> {
                if (courseGoalRelations.getGoalValue() != null) {
                    existingCourseGoalRelations.setGoalValue(courseGoalRelations.getGoalValue());
                }

                return existingCourseGoalRelations;
            })
            .map(courseGoalRelationsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseGoalRelations.getId().toString())
        );
    }

    /**
     * {@code GET  /course-goal-relations} : get all the courseGoalRelations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseGoalRelations in body.
     */
    @GetMapping("/course-goal-relations")
    public List<CourseGoalRelations> getAllCourseGoalRelations(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CourseGoalRelations");
        return courseGoalRelationsRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /course-goal-relations/:id} : get the "id" courseGoalRelations.
     *
     * @param id the id of the courseGoalRelations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseGoalRelations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-goal-relations/{id}")
    public ResponseEntity<CourseGoalRelations> getCourseGoalRelations(@PathVariable Long id) {
        log.debug("REST request to get CourseGoalRelations : {}", id);
        Optional<CourseGoalRelations> courseGoalRelations = courseGoalRelationsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(courseGoalRelations);
    }

    /**
     * {@code DELETE  /course-goal-relations/:id} : delete the "id" courseGoalRelations.
     *
     * @param id the id of the courseGoalRelations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-goal-relations/{id}")
    public ResponseEntity<Void> deleteCourseGoalRelations(@PathVariable Long id) {
        log.debug("REST request to delete CourseGoalRelations : {}", id);
        courseGoalRelationsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
