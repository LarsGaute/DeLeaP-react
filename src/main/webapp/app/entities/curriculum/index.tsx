import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Curriculum from './curriculum';
import CurriculumDetail from './curriculum-detail';
import CurriculumUpdate from './curriculum-update';
import CurriculumDeleteDialog from './curriculum-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CurriculumUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CurriculumUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CurriculumDetail} />
      <ErrorBoundaryRoute path={match.url} component={Curriculum} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CurriculumDeleteDialog} />
  </>
);

export default Routes;
