import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('CourseGoalRelations e2e test', () => {
  const courseGoalRelationsPageUrl = '/course-goal-relations';
  const courseGoalRelationsPageUrlPattern = new RegExp('/course-goal-relations(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const courseGoalRelationsSample = {};

  let courseGoalRelations: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/course-goal-relations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/course-goal-relations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/course-goal-relations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (courseGoalRelations) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/course-goal-relations/${courseGoalRelations.id}`,
      }).then(() => {
        courseGoalRelations = undefined;
      });
    }
  });

  it('CourseGoalRelations menu should load CourseGoalRelations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('course-goal-relations');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CourseGoalRelations').should('exist');
    cy.url().should('match', courseGoalRelationsPageUrlPattern);
  });

  describe('CourseGoalRelations page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(courseGoalRelationsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CourseGoalRelations page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/course-goal-relations/new$'));
        cy.getEntityCreateUpdateHeading('CourseGoalRelations');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalRelationsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/course-goal-relations',
          body: courseGoalRelationsSample,
        }).then(({ body }) => {
          courseGoalRelations = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/course-goal-relations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [courseGoalRelations],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(courseGoalRelationsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CourseGoalRelations page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('courseGoalRelations');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalRelationsPageUrlPattern);
      });

      it('edit button click should load edit CourseGoalRelations page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CourseGoalRelations');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalRelationsPageUrlPattern);
      });

      it('last delete button click should delete instance of CourseGoalRelations', () => {
        cy.intercept('GET', '/api/course-goal-relations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('courseGoalRelations').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalRelationsPageUrlPattern);

        courseGoalRelations = undefined;
      });
    });
  });

  describe('new CourseGoalRelations page', () => {
    beforeEach(() => {
      cy.visit(`${courseGoalRelationsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CourseGoalRelations');
    });

    it('should create an instance of CourseGoalRelations', () => {
      cy.get(`[data-cy="goalValue"]`).type('Administrator Awesome').should('have.value', 'Administrator Awesome');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        courseGoalRelations = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', courseGoalRelationsPageUrlPattern);
    });
  });
});
