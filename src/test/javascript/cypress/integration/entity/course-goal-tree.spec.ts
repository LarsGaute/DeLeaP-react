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

describe('CourseGoalTree e2e test', () => {
  const courseGoalTreePageUrl = '/course-goal-tree';
  const courseGoalTreePageUrlPattern = new RegExp('/course-goal-tree(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const courseGoalTreeSample = {};

  let courseGoalTree: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/course-goal-trees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/course-goal-trees').as('postEntityRequest');
    cy.intercept('DELETE', '/api/course-goal-trees/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (courseGoalTree) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/course-goal-trees/${courseGoalTree.id}`,
      }).then(() => {
        courseGoalTree = undefined;
      });
    }
  });

  it('CourseGoalTrees menu should load CourseGoalTrees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('course-goal-tree');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CourseGoalTree').should('exist');
    cy.url().should('match', courseGoalTreePageUrlPattern);
  });

  describe('CourseGoalTree page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(courseGoalTreePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CourseGoalTree page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/course-goal-tree/new$'));
        cy.getEntityCreateUpdateHeading('CourseGoalTree');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalTreePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/course-goal-trees',
          body: courseGoalTreeSample,
        }).then(({ body }) => {
          courseGoalTree = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/course-goal-trees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [courseGoalTree],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(courseGoalTreePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CourseGoalTree page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('courseGoalTree');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalTreePageUrlPattern);
      });

      it('edit button click should load edit CourseGoalTree page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CourseGoalTree');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalTreePageUrlPattern);
      });

      it('last delete button click should delete instance of CourseGoalTree', () => {
        cy.intercept('GET', '/api/course-goal-trees/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('courseGoalTree').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', courseGoalTreePageUrlPattern);

        courseGoalTree = undefined;
      });
    });
  });

  describe('new CourseGoalTree page', () => {
    beforeEach(() => {
      cy.visit(`${courseGoalTreePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CourseGoalTree');
    });

    it('should create an instance of CourseGoalTree', () => {
      cy.get(`[data-cy="goalValue"]`).type('Pizza withdrawal Jewelery').should('have.value', 'Pizza withdrawal Jewelery');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        courseGoalTree = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', courseGoalTreePageUrlPattern);
    });
  });
});
