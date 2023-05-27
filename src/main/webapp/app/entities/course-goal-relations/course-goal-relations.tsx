import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourseGoalRelations } from 'app/shared/model/course-goal-relations.model';
import { getEntities } from './course-goal-relations.reducer';

export const CourseGoalRelations = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const courseGoalRelationsList = useAppSelector(state => state.courseGoalRelations.entities);
  const loading = useAppSelector(state => state.courseGoalRelations.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="course-goal-relations-heading" data-cy="CourseGoalRelationsHeading">
        <Translate contentKey="deleapApp.courseGoalRelations.home.title">Course Goal Relations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="deleapApp.courseGoalRelations.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/course-goal-relations/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="deleapApp.courseGoalRelations.home.createLabel">Create new Course Goal Relations</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {courseGoalRelationsList && courseGoalRelationsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="deleapApp.courseGoalRelations.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.courseGoalRelations.goalValue">Goal Value</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.courseGoalRelations.course">Course</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.courseGoalRelations.goal">Goal</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.courseGoalRelations.parent">Parent</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {courseGoalRelationsList.map((courseGoalRelations, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/course-goal-relations/${courseGoalRelations.id}`} color="link" size="sm">
                      {courseGoalRelations.id}
                    </Button>
                  </td>
                  <td>{courseGoalRelations.goalValue}</td>
                  <td>
                    {courseGoalRelations.course ? (
                      <Link to={`/course/${courseGoalRelations.course.id}`}>{courseGoalRelations.course.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {courseGoalRelations.goal ? (
                      <Link to={`/goal/${courseGoalRelations.goal.id}`}>{courseGoalRelations.goal.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {courseGoalRelations.parent ? (
                      <Link to={`/goal/${courseGoalRelations.parent.id}`}>{courseGoalRelations.parent.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/course-goal-relations/${courseGoalRelations.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/course-goal-relations/${courseGoalRelations.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/course-goal-relations/${courseGoalRelations.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="deleapApp.courseGoalRelations.home.notFound">No Course Goal Relations found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CourseGoalRelations;
