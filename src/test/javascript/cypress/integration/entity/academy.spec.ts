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

describe('Academy e2e test', () => {
  const academyPageUrl = '/academy';
  const academyPageUrlPattern = new RegExp('/academy(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const academySample = {};

  let academy: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/academies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/academies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/academies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (academy) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/academies/${academy.id}`,
      }).then(() => {
        academy = undefined;
      });
    }
  });

  it('Academies menu should load Academies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('academy');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Academy').should('exist');
    cy.url().should('match', academyPageUrlPattern);
  });

  describe('Academy page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(academyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Academy page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/academy/new$'));
        cy.getEntityCreateUpdateHeading('Academy');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/academies',
          body: academySample,
        }).then(({ body }) => {
          academy = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/academies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [academy],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(academyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Academy page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('academy');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyPageUrlPattern);
      });

      it('edit button click should load edit Academy page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Academy');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyPageUrlPattern);
      });

      it('last delete button click should delete instance of Academy', () => {
        cy.intercept('GET', '/api/academies/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('academy').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academyPageUrlPattern);

        academy = undefined;
      });
    });
  });

  describe('new Academy page', () => {
    beforeEach(() => {
      cy.visit(`${academyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Academy');
    });

    it('should create an instance of Academy', () => {
      cy.get(`[data-cy="name"]`).type('enable Multi-channelled').should('have.value', 'enable Multi-channelled');

      cy.get(`[data-cy="type"]`).select('WebAcademy');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        academy = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', academyPageUrlPattern);
    });
  });
});
