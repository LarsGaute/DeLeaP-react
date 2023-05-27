import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CourseGoalRelations from './course-goal-relations';
import CourseGoalRelationsDetail from './course-goal-relations-detail';
import CourseGoalRelationsUpdate from './course-goal-relations-update';
import CourseGoalRelationsDeleteDialog from './course-goal-relations-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CourseGoalRelationsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CourseGoalRelationsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CourseGoalRelationsDetail} />
      <ErrorBoundaryRoute path={match.url} component={CourseGoalRelations} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CourseGoalRelationsDeleteDialog} />
  </>
);

export default Routes;
