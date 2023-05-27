package com.deleap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deleap.IntegrationTest;
import com.deleap.domain.Academy;
import com.deleap.domain.enumeration.AcademyType;
import com.deleap.repository.AcademyRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AcademyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcademyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AcademyType DEFAULT_TYPE = AcademyType.Universitet;
    private static final AcademyType UPDATED_TYPE = AcademyType.Hogskule;

    private static final String ENTITY_API_URL = "/api/academies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcademyMockMvc;

    private Academy academy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Academy createEntity(EntityManager em) {
        Academy academy = new Academy().name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return academy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Academy createUpdatedEntity(EntityManager em) {
        Academy academy = new Academy().name(UPDATED_NAME).type(UPDATED_TYPE);
        return academy;
    }

    @BeforeEach
    public void initTest() {
        academy = createEntity(em);
    }

    @Test
    @Transactional
    void createAcademy() throws Exception {
        int databaseSizeBeforeCreate = academyRepository.findAll().size();
        // Create the Academy
        restAcademyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academy)))
            .andExpect(status().isCreated());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeCreate + 1);
        Academy testAcademy = academyList.get(academyList.size() - 1);
        assertThat(testAcademy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAcademy.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createAcademyWithExistingId() throws Exception {
        // Create the Academy with an existing ID
        academy.setId(1L);

        int databaseSizeBeforeCreate = academyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcademyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academy)))
            .andExpect(status().isBadRequest());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAcademies() throws Exception {
        // Initialize the database
        academyRepository.saveAndFlush(academy);

        // Get all the academyList
        restAcademyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAcademy() throws Exception {
        // Initialize the database
        academyRepository.saveAndFlush(academy);

        // Get the academy
        restAcademyMockMvc
            .perform(get(ENTITY_API_URL_ID, academy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(academy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAcademy() throws Exception {
        // Get the academy
        restAcademyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcademy() throws Exception {
        // Initialize the database
        academyRepository.saveAndFlush(academy);

        int databaseSizeBeforeUpdate = academyRepository.findAll().size();

        // Update the academy
        Academy updatedAcademy = academyRepository.findById(academy.getId()).get();
        // Disconnect from session so that the updates on updatedAcademy are not directly saved in db
        em.detach(updatedAcademy);
        updatedAcademy.name(UPDATED_NAME).type(UPDATED_TYPE);

        restAcademyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAcademy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAcademy))
            )
            .andExpect(status().isOk());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
        Academy testAcademy = academyList.get(academyList.size() - 1);
        assertThat(testAcademy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcademy.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAcademy() throws Exception {
        int databaseSizeBeforeUpdate = academyRepository.findAll().size();
        academy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, academy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcademy() throws Exception {
        int databaseSizeBeforeUpdate = academyRepository.findAll().size();
        academy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcademy() throws Exception {
        int databaseSizeBeforeUpdate = academyRepository.findAll().size();
        academy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcademyWithPatch() throws Exception {
        // Initialize the database
        academyRepository.saveAndFlush(academy);

        int databaseSizeBeforeUpdate = academyRepository.findAll().size();

        // Update the academy using partial update
        Academy partialUpdatedAcademy = new Academy();
        partialUpdatedAcademy.setId(academy.getId());

        partialUpdatedAcademy.name(UPDATED_NAME).type(UPDATED_TYPE);

        restAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademy))
            )
            .andExpect(status().isOk());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
        Academy testAcademy = academyList.get(academyList.size() - 1);
        assertThat(testAcademy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcademy.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAcademyWithPatch() throws Exception {
        // Initialize the database
        academyRepository.saveAndFlush(academy);

        int databaseSizeBeforeUpdate = academyRepository.findAll().size();

        // Update the academy using partial update
        Academy partialUpdatedAcademy = new Academy();
        partialUpdatedAcademy.setId(academy.getId());

        partialUpdatedAcademy.name(UPDATED_NAME).type(UPDATED_TYPE);

        restAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademy))
            )
            .andExpect(status().isOk());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
        Academy testAcademy = academyList.get(academyList.size() - 1);
        assertThat(testAcademy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAcademy.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAcademy() throws Exception {
        int databaseSizeBeforeUpdate = academyRepository.findAll().size();
        academy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, academy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcademy() throws Exception {
        int databaseSizeBeforeUpdate = academyRepository.findAll().size();
        academy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcademy() throws Exception {
        int databaseSizeBeforeUpdate = academyRepository.findAll().size();
        academy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(academy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Academy in the database
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcademy() throws Exception {
        // Initialize the database
        academyRepository.saveAndFlush(academy);

        int databaseSizeBeforeDelete = academyRepository.findAll().size();

        // Delete the academy
        restAcademyMockMvc
            .perform(delete(ENTITY_API_URL_ID, academy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Academy> academyList = academyRepository.findAll();
        assertThat(academyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
