import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CourseGoalTree from './course-goal-tree';
import CourseGoalTreeDetail from './course-goal-tree-detail';
import CourseGoalTreeUpdate from './course-goal-tree-update';
import CourseGoalTreeDeleteDialog from './course-goal-tree-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CourseGoalTreeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CourseGoalTreeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CourseGoalTreeDetail} />
      <ErrorBoundaryRoute path={match.url} component={CourseGoalTree} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CourseGoalTreeDeleteDialog} />
  </>
);

export default Routes;
