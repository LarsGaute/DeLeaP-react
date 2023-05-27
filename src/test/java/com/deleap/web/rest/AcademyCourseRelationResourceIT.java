package com.deleap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deleap.IntegrationTest;
import com.deleap.domain.AcademyCourseRelation;
import com.deleap.repository.AcademyCourseRelationRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AcademyCourseRelationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AcademyCourseRelationResourceIT {

    private static final LocalDate DEFAULT_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/academy-course-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcademyCourseRelationRepository academyCourseRelationRepository;

    @Mock
    private AcademyCourseRelationRepository academyCourseRelationRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcademyCourseRelationMockMvc;

    private AcademyCourseRelation academyCourseRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademyCourseRelation createEntity(EntityManager em) {
        AcademyCourseRelation academyCourseRelation = new AcademyCourseRelation().start(DEFAULT_START).end(DEFAULT_END);
        return academyCourseRelation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademyCourseRelation createUpdatedEntity(EntityManager em) {
        AcademyCourseRelation academyCourseRelation = new AcademyCourseRelation().start(UPDATED_START).end(UPDATED_END);
        return academyCourseRelation;
    }

    @BeforeEach
    public void initTest() {
        academyCourseRelation = createEntity(em);
    }

    @Test
    @Transactional
    void createAcademyCourseRelation() throws Exception {
        int databaseSizeBeforeCreate = academyCourseRelationRepository.findAll().size();
        // Create the AcademyCourseRelation
        restAcademyCourseRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isCreated());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeCreate + 1);
        AcademyCourseRelation testAcademyCourseRelation = academyCourseRelationList.get(academyCourseRelationList.size() - 1);
        assertThat(testAcademyCourseRelation.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testAcademyCourseRelation.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    void createAcademyCourseRelationWithExistingId() throws Exception {
        // Create the AcademyCourseRelation with an existing ID
        academyCourseRelation.setId(1L);

        int databaseSizeBeforeCreate = academyCourseRelationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcademyCourseRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = academyCourseRelationRepository.findAll().size();
        // set the field null
        academyCourseRelation.setStart(null);

        // Create the AcademyCourseRelation, which fails.

        restAcademyCourseRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isBadRequest());

        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = academyCourseRelationRepository.findAll().size();
        // set the field null
        academyCourseRelation.setEnd(null);

        // Create the AcademyCourseRelation, which fails.

        restAcademyCourseRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isBadRequest());

        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcademyCourseRelations() throws Exception {
        // Initialize the database
        academyCourseRelationRepository.saveAndFlush(academyCourseRelation);

        // Get all the academyCourseRelationList
        restAcademyCourseRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academyCourseRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAcademyCourseRelationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(academyCourseRelationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAcademyCourseRelationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(academyCourseRelationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAcademyCourseRelationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(academyCourseRelationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAcademyCourseRelationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(academyCourseRelationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAcademyCourseRelation() throws Exception {
        // Initialize the database
        academyCourseRelationRepository.saveAndFlush(academyCourseRelation);

        // Get the academyCourseRelation
        restAcademyCourseRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, academyCourseRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(academyCourseRelation.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAcademyCourseRelation() throws Exception {
        // Get the academyCourseRelation
        restAcademyCourseRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcademyCourseRelation() throws Exception {
        // Initialize the database
        academyCourseRelationRepository.saveAndFlush(academyCourseRelation);

        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();

        // Update the academyCourseRelation
        AcademyCourseRelation updatedAcademyCourseRelation = academyCourseRelationRepository.findById(academyCourseRelation.getId()).get();
        // Disconnect from session so that the updates on updatedAcademyCourseRelation are not directly saved in db
        em.detach(updatedAcademyCourseRelation);
        updatedAcademyCourseRelation.start(UPDATED_START).end(UPDATED_END);

        restAcademyCourseRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAcademyCourseRelation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAcademyCourseRelation))
            )
            .andExpect(status().isOk());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
        AcademyCourseRelation testAcademyCourseRelation = academyCourseRelationList.get(academyCourseRelationList.size() - 1);
        assertThat(testAcademyCourseRelation.getStart()).isEqualTo(UPDATED_START);
        assertThat(testAcademyCourseRelation.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    void putNonExistingAcademyCourseRelation() throws Exception {
        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();
        academyCourseRelation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademyCourseRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, academyCourseRelation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcademyCourseRelation() throws Exception {
        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();
        academyCourseRelation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyCourseRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcademyCourseRelation() throws Exception {
        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();
        academyCourseRelation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyCourseRelationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcademyCourseRelationWithPatch() throws Exception {
        // Initialize the database
        academyCourseRelationRepository.saveAndFlush(academyCourseRelation);

        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();

        // Update the academyCourseRelation using partial update
        AcademyCourseRelation partialUpdatedAcademyCourseRelation = new AcademyCourseRelation();
        partialUpdatedAcademyCourseRelation.setId(academyCourseRelation.getId());

        partialUpdatedAcademyCourseRelation.start(UPDATED_START);

        restAcademyCourseRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademyCourseRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademyCourseRelation))
            )
            .andExpect(status().isOk());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
        AcademyCourseRelation testAcademyCourseRelation = academyCourseRelationList.get(academyCourseRelationList.size() - 1);
        assertThat(testAcademyCourseRelation.getStart()).isEqualTo(UPDATED_START);
        assertThat(testAcademyCourseRelation.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    void fullUpdateAcademyCourseRelationWithPatch() throws Exception {
        // Initialize the database
        academyCourseRelationRepository.saveAndFlush(academyCourseRelation);

        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();

        // Update the academyCourseRelation using partial update
        AcademyCourseRelation partialUpdatedAcademyCourseRelation = new AcademyCourseRelation();
        partialUpdatedAcademyCourseRelation.setId(academyCourseRelation.getId());

        partialUpdatedAcademyCourseRelation.start(UPDATED_START).end(UPDATED_END);

        restAcademyCourseRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademyCourseRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademyCourseRelation))
            )
            .andExpect(status().isOk());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
        AcademyCourseRelation testAcademyCourseRelation = academyCourseRelationList.get(academyCourseRelationList.size() - 1);
        assertThat(testAcademyCourseRelation.getStart()).isEqualTo(UPDATED_START);
        assertThat(testAcademyCourseRelation.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    void patchNonExistingAcademyCourseRelation() throws Exception {
        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();
        academyCourseRelation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademyCourseRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, academyCourseRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcademyCourseRelation() throws Exception {
        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();
        academyCourseRelation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyCourseRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcademyCourseRelation() throws Exception {
        int databaseSizeBeforeUpdate = academyCourseRelationRepository.findAll().size();
        academyCourseRelation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyCourseRelationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academyCourseRelation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademyCourseRelation in the database
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcademyCourseRelation() throws Exception {
        // Initialize the database
        academyCourseRelationRepository.saveAndFlush(academyCourseRelation);

        int databaseSizeBeforeDelete = academyCourseRelationRepository.findAll().size();

        // Delete the academyCourseRelation
        restAcademyCourseRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, academyCourseRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcademyCourseRelation> academyCourseRelationList = academyCourseRelationRepository.findAll();
        assertThat(academyCourseRelationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
