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

describe('AcademyCourseRelation e2e test', () => {
  const academyCourseRelationPageUrl = '/academy-course-relation';
  const academyCourseRelationPageUrlPattern = new RegExp('/academy-course-relation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const academyCourseRelationSample = { start: '2023-05-27', end: '2023-05-27' };

  let academyCourseRelation: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/academy-course-relations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/academy-course-relations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/academy-course-relations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (academyCourseRelation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/academy-course-relations/${academyCourseRelation.id}`,
      }).then(() => {
        academyCourseRelation = undefined;
      });
    }
  });

  it('AcademyCourseRelations menu should load AcademyCourseRelations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('academy-course-relation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AcademyCourseRelation').should('exist');
    cy.url().should('match', academyCourseRelationPageUrlPattern);
  });

  describe('AcademyCourseRelation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(academyCourseRelationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AcademyCourseRelation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/academy-course-relation/new$'));
        cy.getEntityCreateUpdateHeading('AcademyCourseRelation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyCourseRelationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/academy-course-relations',
          body: academyCourseRelationSample,
        }).then(({ body }) => {
          academyCourseRelation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/academy-course-relations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [academyCourseRelation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(academyCourseRelationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AcademyCourseRelation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('academyCourseRelation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyCourseRelationPageUrlPattern);
      });

      it('edit button click should load edit AcademyCourseRelation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AcademyCourseRelation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyCourseRelationPageUrlPattern);
      });

      it('last delete button click should delete instance of AcademyCourseRelation', () => {
        cy.intercept('GET', '/api/academy-course-relations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('academyCourseRelation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyCourseRelationPageUrlPattern);

        academyCourseRelation = undefined;
      });
    });
  });

  describe('new AcademyCourseRelation page', () => {
    beforeEach(() => {
      cy.visit(`${academyCourseRelationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AcademyCourseRelation');
    });

    it('should create an instance of AcademyCourseRelation', () => {
      cy.get(`[data-cy="start"]`).type('2023-05-27').should('have.value', '2023-05-27');

      cy.get(`[data-cy="end"]`).type('2023-05-26').should('have.value', '2023-05-26');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        academyCourseRelation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', academyCourseRelationPageUrlPattern);
    });
  });
});
