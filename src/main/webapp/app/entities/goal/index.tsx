import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Goal from './goal';
import GoalDetail from './goal-detail';
import GoalUpdate from './goal-update';
import GoalDeleteDialog from './goal-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GoalUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GoalUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GoalDetail} />
      <ErrorBoundaryRoute path={match.url} component={Goal} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GoalDeleteDialog} />
  </>
);

export default Routes;
