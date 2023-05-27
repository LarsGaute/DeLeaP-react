package com.deleap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deleap.IntegrationTest;
import com.deleap.domain.Goal;
import com.deleap.repository.GoalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GoalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GoalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT = 1L;
    private static final Long UPDATED_PARENT = 2L;

    private static final String DEFAULT_GOAL_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_GOAL_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_GOAL_FOCUS = "AAAAAAAAAA";
    private static final String UPDATED_GOAL_FOCUS = "BBBBBBBBBB";

    private static final String DEFAULT_WHY_ACHIEVE_THIS = "AAAAAAAAAA";
    private static final String UPDATED_WHY_ACHIEVE_THIS = "BBBBBBBBBB";

    private static final String DEFAULT_ROAD_AHEAD = "AAAAAAAAAA";
    private static final String UPDATED_ROAD_AHEAD = "BBBBBBBBBB";

    private static final String DEFAULT_WHAT_TO_ACHIEVE = "AAAAAAAAAA";
    private static final String UPDATED_WHAT_TO_ACHIEVE = "BBBBBBBBBB";

    private static final String DEFAULT_WHAT_TO_LEARN = "AAAAAAAAAA";
    private static final String UPDATED_WHAT_TO_LEARN = "BBBBBBBBBB";

    private static final String DEFAULT_WHY_FOCUS_ON_THIS = "AAAAAAAAAA";
    private static final String UPDATED_WHY_FOCUS_ON_THIS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GOALDONE = false;
    private static final Boolean UPDATED_GOALDONE = true;

    private static final String ENTITY_API_URL = "/api/goals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GoalRepository goalRepository;

    @Mock
    private GoalRepository goalRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGoalMockMvc;

    private Goal goal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goal createEntity(EntityManager em) {
        Goal goal = new Goal()
            .name(DEFAULT_NAME)
            .parent(DEFAULT_PARENT)
            .goalValue(DEFAULT_GOAL_VALUE)
            .goalFocus(DEFAULT_GOAL_FOCUS)
            .whyAchieveThis(DEFAULT_WHY_ACHIEVE_THIS)
            .roadAhead(DEFAULT_ROAD_AHEAD)
            .whatToAchieve(DEFAULT_WHAT_TO_ACHIEVE)
            .whatToLearn(DEFAULT_WHAT_TO_LEARN)
            .whyFocusOnThis(DEFAULT_WHY_FOCUS_ON_THIS)
            .goaldone(DEFAULT_GOALDONE);
        return goal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goal createUpdatedEntity(EntityManager em) {
        Goal goal = new Goal()
            .name(UPDATED_NAME)
            .parent(UPDATED_PARENT)
            .goalValue(UPDATED_GOAL_VALUE)
            .goalFocus(UPDATED_GOAL_FOCUS)
            .whyAchieveThis(UPDATED_WHY_ACHIEVE_THIS)
            .roadAhead(UPDATED_ROAD_AHEAD)
            .whatToAchieve(UPDATED_WHAT_TO_ACHIEVE)
            .whatToLearn(UPDATED_WHAT_TO_LEARN)
            .whyFocusOnThis(UPDATED_WHY_FOCUS_ON_THIS)
            .goaldone(UPDATED_GOALDONE);
        return goal;
    }

    @BeforeEach
    public void initTest() {
        goal = createEntity(em);
    }

    @Test
    @Transactional
    void createGoal() throws Exception {
        int databaseSizeBeforeCreate = goalRepository.findAll().size();
        // Create the Goal
        restGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isCreated());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeCreate + 1);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoal.getParent()).isEqualTo(DEFAULT_PARENT);
        assertThat(testGoal.getGoalValue()).isEqualTo(DEFAULT_GOAL_VALUE);
        assertThat(testGoal.getGoalFocus()).isEqualTo(DEFAULT_GOAL_FOCUS);
        assertThat(testGoal.getWhyAchieveThis()).isEqualTo(DEFAULT_WHY_ACHIEVE_THIS);
        assertThat(testGoal.getRoadAhead()).isEqualTo(DEFAULT_ROAD_AHEAD);
        assertThat(testGoal.getWhatToAchieve()).isEqualTo(DEFAULT_WHAT_TO_ACHIEVE);
        assertThat(testGoal.getWhatToLearn()).isEqualTo(DEFAULT_WHAT_TO_LEARN);
        assertThat(testGoal.getWhyFocusOnThis()).isEqualTo(DEFAULT_WHY_FOCUS_ON_THIS);
        assertThat(testGoal.getGoaldone()).isEqualTo(DEFAULT_GOALDONE);
    }

    @Test
    @Transactional
    void createGoalWithExistingId() throws Exception {
        // Create the Goal with an existing ID
        goal.setId(1L);

        int databaseSizeBeforeCreate = goalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGoals() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList
        restGoalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goal.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.intValue())))
            .andExpect(jsonPath("$.[*].goalValue").value(hasItem(DEFAULT_GOAL_VALUE)))
            .andExpect(jsonPath("$.[*].goalFocus").value(hasItem(DEFAULT_GOAL_FOCUS)))
            .andExpect(jsonPath("$.[*].whyAchieveThis").value(hasItem(DEFAULT_WHY_ACHIEVE_THIS)))
            .andExpect(jsonPath("$.[*].roadAhead").value(hasItem(DEFAULT_ROAD_AHEAD)))
            .andExpect(jsonPath("$.[*].whatToAchieve").value(hasItem(DEFAULT_WHAT_TO_ACHIEVE)))
            .andExpect(jsonPath("$.[*].whatToLearn").value(hasItem(DEFAULT_WHAT_TO_LEARN)))
            .andExpect(jsonPath("$.[*].whyFocusOnThis").value(hasItem(DEFAULT_WHY_FOCUS_ON_THIS)))
            .andExpect(jsonPath("$.[*].goaldone").value(hasItem(DEFAULT_GOALDONE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGoalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(goalRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGoalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(goalRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGoalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(goalRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGoalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(goalRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get the goal
        restGoalMockMvc
            .perform(get(ENTITY_API_URL_ID, goal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(goal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.intValue()))
            .andExpect(jsonPath("$.goalValue").value(DEFAULT_GOAL_VALUE))
            .andExpect(jsonPath("$.goalFocus").value(DEFAULT_GOAL_FOCUS))
            .andExpect(jsonPath("$.whyAchieveThis").value(DEFAULT_WHY_ACHIEVE_THIS))
            .andExpect(jsonPath("$.roadAhead").value(DEFAULT_ROAD_AHEAD))
            .andExpect(jsonPath("$.whatToAchieve").value(DEFAULT_WHAT_TO_ACHIEVE))
            .andExpect(jsonPath("$.whatToLearn").value(DEFAULT_WHAT_TO_LEARN))
            .andExpect(jsonPath("$.whyFocusOnThis").value(DEFAULT_WHY_FOCUS_ON_THIS))
            .andExpect(jsonPath("$.goaldone").value(DEFAULT_GOALDONE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGoal() throws Exception {
        // Get the goal
        restGoalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal
        Goal updatedGoal = goalRepository.findById(goal.getId()).get();
        // Disconnect from session so that the updates on updatedGoal are not directly saved in db
        em.detach(updatedGoal);
        updatedGoal
            .name(UPDATED_NAME)
            .parent(UPDATED_PARENT)
            .goalValue(UPDATED_GOAL_VALUE)
            .goalFocus(UPDATED_GOAL_FOCUS)
            .whyAchieveThis(UPDATED_WHY_ACHIEVE_THIS)
            .roadAhead(UPDATED_ROAD_AHEAD)
            .whatToAchieve(UPDATED_WHAT_TO_ACHIEVE)
            .whatToLearn(UPDATED_WHAT_TO_LEARN)
            .whyFocusOnThis(UPDATED_WHY_FOCUS_ON_THIS)
            .goaldone(UPDATED_GOALDONE);

        restGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGoal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoal.getParent()).isEqualTo(UPDATED_PARENT);
        assertThat(testGoal.getGoalValue()).isEqualTo(UPDATED_GOAL_VALUE);
        assertThat(testGoal.getGoalFocus()).isEqualTo(UPDATED_GOAL_FOCUS);
        assertThat(testGoal.getWhyAchieveThis()).isEqualTo(UPDATED_WHY_ACHIEVE_THIS);
        assertThat(testGoal.getRoadAhead()).isEqualTo(UPDATED_ROAD_AHEAD);
        assertThat(testGoal.getWhatToAchieve()).isEqualTo(UPDATED_WHAT_TO_ACHIEVE);
        assertThat(testGoal.getWhatToLearn()).isEqualTo(UPDATED_WHAT_TO_LEARN);
        assertThat(testGoal.getWhyFocusOnThis()).isEqualTo(UPDATED_WHY_FOCUS_ON_THIS);
        assertThat(testGoal.getGoaldone()).isEqualTo(UPDATED_GOALDONE);
    }

    @Test
    @Transactional
    void putNonExistingGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, goal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGoalWithPatch() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal using partial update
        Goal partialUpdatedGoal = new Goal();
        partialUpdatedGoal.setId(goal.getId());

        partialUpdatedGoal
            .goalFocus(UPDATED_GOAL_FOCUS)
            .roadAhead(UPDATED_ROAD_AHEAD)
            .whatToAchieve(UPDATED_WHAT_TO_ACHIEVE)
            .whyFocusOnThis(UPDATED_WHY_FOCUS_ON_THIS);

        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoal.getParent()).isEqualTo(DEFAULT_PARENT);
        assertThat(testGoal.getGoalValue()).isEqualTo(DEFAULT_GOAL_VALUE);
        assertThat(testGoal.getGoalFocus()).isEqualTo(UPDATED_GOAL_FOCUS);
        assertThat(testGoal.getWhyAchieveThis()).isEqualTo(DEFAULT_WHY_ACHIEVE_THIS);
        assertThat(testGoal.getRoadAhead()).isEqualTo(UPDATED_ROAD_AHEAD);
        assertThat(testGoal.getWhatToAchieve()).isEqualTo(UPDATED_WHAT_TO_ACHIEVE);
        assertThat(testGoal.getWhatToLearn()).isEqualTo(DEFAULT_WHAT_TO_LEARN);
        assertThat(testGoal.getWhyFocusOnThis()).isEqualTo(UPDATED_WHY_FOCUS_ON_THIS);
        assertThat(testGoal.getGoaldone()).isEqualTo(DEFAULT_GOALDONE);
    }

    @Test
    @Transactional
    void fullUpdateGoalWithPatch() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal using partial update
        Goal partialUpdatedGoal = new Goal();
        partialUpdatedGoal.setId(goal.getId());

        partialUpdatedGoal
            .name(UPDATED_NAME)
            .parent(UPDATED_PARENT)
            .goalValue(UPDATED_GOAL_VALUE)
            .goalFocus(UPDATED_GOAL_FOCUS)
            .whyAchieveThis(UPDATED_WHY_ACHIEVE_THIS)
            .roadAhead(UPDATED_ROAD_AHEAD)
            .whatToAchieve(UPDATED_WHAT_TO_ACHIEVE)
            .whatToLearn(UPDATED_WHAT_TO_LEARN)
            .whyFocusOnThis(UPDATED_WHY_FOCUS_ON_THIS)
            .goaldone(UPDATED_GOALDONE);

        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goalList.get(goalList.size() - 1);
        assertThat(testGoal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoal.getParent()).isEqualTo(UPDATED_PARENT);
        assertThat(testGoal.getGoalValue()).isEqualTo(UPDATED_GOAL_VALUE);
        assertThat(testGoal.getGoalFocus()).isEqualTo(UPDATED_GOAL_FOCUS);
        assertThat(testGoal.getWhyAchieveThis()).isEqualTo(UPDATED_WHY_ACHIEVE_THIS);
        assertThat(testGoal.getRoadAhead()).isEqualTo(UPDATED_ROAD_AHEAD);
        assertThat(testGoal.getWhatToAchieve()).isEqualTo(UPDATED_WHAT_TO_ACHIEVE);
        assertThat(testGoal.getWhatToLearn()).isEqualTo(UPDATED_WHAT_TO_LEARN);
        assertThat(testGoal.getWhyFocusOnThis()).isEqualTo(UPDATED_WHY_FOCUS_ON_THIS);
        assertThat(testGoal.getGoaldone()).isEqualTo(UPDATED_GOALDONE);
    }

    @Test
    @Transactional
    void patchNonExistingGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, goal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(goal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGoal() throws Exception {
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();
        goal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(goal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Goal in the database
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        int databaseSizeBeforeDelete = goalRepository.findAll().size();

        // Delete the goal
        restGoalMockMvc
            .perform(delete(ENTITY_API_URL_ID, goal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Goal> goalList = goalRepository.findAll();
        assertThat(goalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
