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

describe('Goal e2e test', () => {
  const goalPageUrl = '/goal';
  const goalPageUrlPattern = new RegExp('/goal(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const goalSample = {};

  let goal: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/goals+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/goals').as('postEntityRequest');
    cy.intercept('DELETE', '/api/goals/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (goal) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/goals/${goal.id}`,
      }).then(() => {
        goal = undefined;
      });
    }
  });

  it('Goals menu should load Goals page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('goal');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Goal').should('exist');
    cy.url().should('match', goalPageUrlPattern);
  });

  describe('Goal page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(goalPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Goal page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/goal/new$'));
        cy.getEntityCreateUpdateHeading('Goal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', goalPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/goals',
          body: goalSample,
        }).then(({ body }) => {
          goal = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/goals+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [goal],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(goalPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Goal page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('goal');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', goalPageUrlPattern);
      });

      it('edit button click should load edit Goal page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Goal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', goalPageUrlPattern);
      });

      it('last delete button click should delete instance of Goal', () => {
        cy.intercept('GET', '/api/goals/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('goal').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', goalPageUrlPattern);

        goal = undefined;
      });
    });
  });

  describe('new Goal page', () => {
    beforeEach(() => {
      cy.visit(`${goalPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Goal');
    });

    it('should create an instance of Goal', () => {
      cy.get(`[data-cy="name"]`).type('Island back-end').should('have.value', 'Island back-end');

      cy.get(`[data-cy="goalFocus"]`).type('transform turn-key').should('have.value', 'transform turn-key');

      cy.get(`[data-cy="whyAchieveThis"]`).type('Montana').should('have.value', 'Montana');

      cy.get(`[data-cy="roadAhead"]`).type('Games Rue').should('have.value', 'Games Rue');

      cy.get(`[data-cy="whatToAchieve"]`).type('focus 24/7').should('have.value', 'focus 24/7');

      cy.get(`[data-cy="whatToLearn"]`).type('Kroon').should('have.value', 'Kroon');

      cy.get(`[data-cy="whyFocusOnThis"]`).type('neural haptic').should('have.value', 'neural haptic');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        goal = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', goalPageUrlPattern);
    });
  });
});
