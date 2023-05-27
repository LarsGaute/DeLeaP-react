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

describe('Curriculum e2e test', () => {
  const curriculumPageUrl = '/curriculum';
  const curriculumPageUrlPattern = new RegExp('/curriculum(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const curriculumSample = {};

  let curriculum: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/curricula+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/curricula').as('postEntityRequest');
    cy.intercept('DELETE', '/api/curricula/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (curriculum) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/curricula/${curriculum.id}`,
      }).then(() => {
        curriculum = undefined;
      });
    }
  });

  it('Curricula menu should load Curricula page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('curriculum');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Curriculum').should('exist');
    cy.url().should('match', curriculumPageUrlPattern);
  });

  describe('Curriculum page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(curriculumPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Curriculum page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/curriculum/new$'));
        cy.getEntityCreateUpdateHeading('Curriculum');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', curriculumPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/curricula',
          body: curriculumSample,
        }).then(({ body }) => {
          curriculum = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/curricula+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [curriculum],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(curriculumPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Curriculum page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('curriculum');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', curriculumPageUrlPattern);
      });

      it('edit button click should load edit Curriculum page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Curriculum');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', curriculumPageUrlPattern);
      });

      it('last delete button click should delete instance of Curriculum', () => {
        cy.intercept('GET', '/api/curricula/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('curriculum').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', curriculumPageUrlPattern);

        curriculum = undefined;
      });
    });
  });

  describe('new Curriculum page', () => {
    beforeEach(() => {
      cy.visit(`${curriculumPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Curriculum');
    });

    it('should create an instance of Curriculum', () => {
      cy.get(`[data-cy="text"]`).type('partnerships').should('have.value', 'partnerships');

      cy.get(`[data-cy="url"]`).type('http://gretchen.biz').should('have.value', 'http://gretchen.biz');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        curriculum = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', curriculumPageUrlPattern);
    });
  });
});
