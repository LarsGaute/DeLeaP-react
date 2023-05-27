import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Academy from './academy';
import AcademyDetail from './academy-detail';
import AcademyUpdate from './academy-update';
import AcademyDeleteDialog from './academy-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AcademyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AcademyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AcademyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Academy} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AcademyDeleteDialog} />
  </>
);

export default Routes;
