package com.deleap.web.rest;

import com.deleap.domain.CourseGoalTree;
import com.deleap.repository.CourseGoalTreeRepository;
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
 * REST controller for managing {@link com.deleap.domain.CourseGoalTree}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CourseGoalTreeResource {

    private final Logger log = LoggerFactory.getLogger(CourseGoalTreeResource.class);

    private static final String ENTITY_NAME = "courseGoalTree";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseGoalTreeRepository courseGoalTreeRepository;

    public CourseGoalTreeResource(CourseGoalTreeRepository courseGoalTreeRepository) {
        this.courseGoalTreeRepository = courseGoalTreeRepository;
    }

    /**
     * {@code POST  /course-goal-trees} : Create a new courseGoalTree.
     *
     * @param courseGoalTree the courseGoalTree to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseGoalTree, or with status {@code 400 (Bad Request)} if the courseGoalTree has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-goal-trees")
    public ResponseEntity<CourseGoalTree> createCourseGoalTree(@RequestBody CourseGoalTree courseGoalTree) throws URISyntaxException {
        log.debug("REST request to save CourseGoalTree : {}", courseGoalTree);
        if (courseGoalTree.getId() != null) {
            throw new BadRequestAlertException("A new courseGoalTree cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseGoalTree result = courseGoalTreeRepository.save(courseGoalTree);
        return ResponseEntity
            .created(new URI("/api/course-goal-trees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-goal-trees/:id} : Updates an existing courseGoalTree.
     *
     * @param id the id of the courseGoalTree to save.
     * @param courseGoalTree the courseGoalTree to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseGoalTree,
     * or with status {@code 400 (Bad Request)} if the courseGoalTree is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseGoalTree couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-goal-trees/{id}")
    public ResponseEntity<CourseGoalTree> updateCourseGoalTree(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseGoalTree courseGoalTree
    ) throws URISyntaxException {
        log.debug("REST request to update CourseGoalTree : {}, {}", id, courseGoalTree);
        if (courseGoalTree.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseGoalTree.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseGoalTreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseGoalTree result = courseGoalTreeRepository.save(courseGoalTree);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseGoalTree.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-goal-trees/:id} : Partial updates given fields of an existing courseGoalTree, field will ignore if it is null
     *
     * @param id the id of the courseGoalTree to save.
     * @param courseGoalTree the courseGoalTree to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseGoalTree,
     * or with status {@code 400 (Bad Request)} if the courseGoalTree is not valid,
     * or with status {@code 404 (Not Found)} if the courseGoalTree is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseGoalTree couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-goal-trees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourseGoalTree> partialUpdateCourseGoalTree(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseGoalTree courseGoalTree
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseGoalTree partially : {}, {}", id, courseGoalTree);
        if (courseGoalTree.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseGoalTree.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseGoalTreeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseGoalTree> result = courseGoalTreeRepository
            .findById(courseGoalTree.getId())
            .map(existingCourseGoalTree -> {
                if (courseGoalTree.getGoalValue() != null) {
                    existingCourseGoalTree.setGoalValue(courseGoalTree.getGoalValue());
                }

                return existingCourseGoalTree;
            })
            .map(courseGoalTreeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseGoalTree.getId().toString())
        );
    }

    /**
     * {@code GET  /course-goal-trees} : get all the courseGoalTrees.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseGoalTrees in body.
     */
    @GetMapping("/course-goal-trees")
    public List<CourseGoalTree> getAllCourseGoalTrees(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CourseGoalTrees");
        return courseGoalTreeRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /course-goal-trees/:id} : get the "id" courseGoalTree.
     *
     * @param id the id of the courseGoalTree to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseGoalTree, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-goal-trees/{id}")
    public ResponseEntity<CourseGoalTree> getCourseGoalTree(@PathVariable Long id) {
        log.debug("REST request to get CourseGoalTree : {}", id);
        Optional<CourseGoalTree> courseGoalTree = courseGoalTreeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(courseGoalTree);
    }

    /**
     * {@code DELETE  /course-goal-trees/:id} : delete the "id" courseGoalTree.
     *
     * @param id the id of the courseGoalTree to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-goal-trees/{id}")
    public ResponseEntity<Void> deleteCourseGoalTree(@PathVariable Long id) {
        log.debug("REST request to delete CourseGoalTree : {}", id);
        courseGoalTreeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
