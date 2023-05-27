import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AcademyCourseRelation from './academy-course-relation';
import AcademyCourseRelationDetail from './academy-course-relation-detail';
import AcademyCourseRelationUpdate from './academy-course-relation-update';
import AcademyCourseRelationDeleteDialog from './academy-course-relation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AcademyCourseRelationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AcademyCourseRelationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AcademyCourseRelationDetail} />
      <ErrorBoundaryRoute path={match.url} component={AcademyCourseRelation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AcademyCourseRelationDeleteDialog} />
  </>
);

export default Routes;
