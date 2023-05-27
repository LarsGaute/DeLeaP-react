package com.deleap.web.rest;

import com.deleap.domain.Goal;
import com.deleap.repository.GoalRepository;
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
 * REST controller for managing {@link com.deleap.domain.Goal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GoalResource {

    private final Logger log = LoggerFactory.getLogger(GoalResource.class);

    private static final String ENTITY_NAME = "goal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoalRepository goalRepository;

    public GoalResource(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    /**
     * {@code POST  /goals} : Create a new goal.
     *
     * @param goal the goal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goal, or with status {@code 400 (Bad Request)} if the goal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/goals")
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) throws URISyntaxException {
        log.debug("REST request to save Goal : {}", goal);
        if (goal.getId() != null) {
            throw new BadRequestAlertException("A new goal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Goal result = goalRepository.save(goal);
        return ResponseEntity
            .created(new URI("/api/goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /goals/:id} : Updates an existing goal.
     *
     * @param id the id of the goal to save.
     * @param goal the goal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goal,
     * or with status {@code 400 (Bad Request)} if the goal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/goals/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable(value = "id", required = false) final Long id, @RequestBody Goal goal)
        throws URISyntaxException {
        log.debug("REST request to update Goal : {}, {}", id, goal);
        if (goal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Goal result = goalRepository.save(goal);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goal.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /goals/:id} : Partial updates given fields of an existing goal, field will ignore if it is null
     *
     * @param id the id of the goal to save.
     * @param goal the goal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goal,
     * or with status {@code 400 (Bad Request)} if the goal is not valid,
     * or with status {@code 404 (Not Found)} if the goal is not found,
     * or with status {@code 500 (Internal Server Error)} if the goal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/goals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Goal> partialUpdateGoal(@PathVariable(value = "id", required = false) final Long id, @RequestBody Goal goal)
        throws URISyntaxException {
        log.debug("REST request to partial update Goal partially : {}, {}", id, goal);
        if (goal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Goal> result = goalRepository
            .findById(goal.getId())
            .map(existingGoal -> {
                if (goal.getName() != null) {
                    existingGoal.setName(goal.getName());
                }
                if (goal.getGoalFocus() != null) {
                    existingGoal.setGoalFocus(goal.getGoalFocus());
                }
                if (goal.getWhyAchieveThis() != null) {
                    existingGoal.setWhyAchieveThis(goal.getWhyAchieveThis());
                }
                if (goal.getRoadAhead() != null) {
                    existingGoal.setRoadAhead(goal.getRoadAhead());
                }
                if (goal.getWhatToAchieve() != null) {
                    existingGoal.setWhatToAchieve(goal.getWhatToAchieve());
                }
                if (goal.getWhatToLearn() != null) {
                    existingGoal.setWhatToLearn(goal.getWhatToLearn());
                }
                if (goal.getWhyFocusOnThis() != null) {
                    existingGoal.setWhyFocusOnThis(goal.getWhyFocusOnThis());
                }

                return existingGoal;
            })
            .map(goalRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goal.getId().toString())
        );
    }

    /**
     * {@code GET  /goals} : get all the goals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goals in body.
     */
    @GetMapping("/goals")
    public List<Goal> getAllGoals() {
        log.debug("REST request to get all Goals");
        return goalRepository.findAll();
    }

    /**
     * {@code GET  /goals/:id} : get the "id" goal.
     *
     * @param id the id of the goal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/goals/{id}")
    public ResponseEntity<Goal> getGoal(@PathVariable Long id) {
        log.debug("REST request to get Goal : {}", id);
        Optional<Goal> goal = goalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(goal);
    }

    /**
     * {@code DELETE  /goals/:id} : delete the "id" goal.
     *
     * @param id the id of the goal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/goals/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        log.debug("REST request to delete Goal : {}", id);
        goalRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
