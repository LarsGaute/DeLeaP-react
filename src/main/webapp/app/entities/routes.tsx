import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Course from './course';
import Goal from './goal';
import Curriculum from './curriculum';
import Academy from './academy';
import Enrollment from './enrollment';
import AcademyCourseRelation from './academy-course-relation';
import CourseGoalRelations from './course-goal-relations';
import CourseGoalTree from './course-goal-tree';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}course`} component={Course} />
        <ErrorBoundaryRoute path={`${match.url}goal`} component={Goal} />
        <ErrorBoundaryRoute path={`${match.url}curriculum`} component={Curriculum} />
        <ErrorBoundaryRoute path={`${match.url}academy`} component={Academy} />
        <ErrorBoundaryRoute path={`${match.url}enrollment`} component={Enrollment} />
        <ErrorBoundaryRoute path={`${match.url}academy-course-relation`} component={AcademyCourseRelation} />
        <ErrorBoundaryRoute path={`${match.url}course-goal-relations`} component={CourseGoalRelations} />
        <ErrorBoundaryRoute path={`${match.url}course-goal-tree`} component={CourseGoalTree} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
