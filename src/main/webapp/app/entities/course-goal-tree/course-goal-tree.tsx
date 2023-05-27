import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourseGoalTree } from 'app/shared/model/course-goal-tree.model';
import { getEntities } from './course-goal-tree.reducer';

export const CourseGoalTree = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const courseGoalTreeList = useAppSelector(state => state.courseGoalTree.entities);
  const loading = useAppSelector(state => state.courseGoalTree.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="course-goal-tree-heading" data-cy="CourseGoalTreeHeading">
        <Translate contentKey="deleapApp.courseGoalTree.home.title">Course Goal Trees</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="deleapApp.courseGoalTree.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/course-goal-tree/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="deleapApp.courseGoalTree.home.createLabel">Create new Course Goal Tree</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {courseGoalTreeList && courseGoalTreeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="deleapApp.courseGoalTree.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.courseGoalTree.goalValue">Goal Value</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.courseGoalTree.course">Course</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.courseGoalTree.coursegoal">Coursegoal</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {courseGoalTreeList.map((courseGoalTree, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/course-goal-tree/${courseGoalTree.id}`} color="link" size="sm">
                      {courseGoalTree.id}
                    </Button>
                  </td>
                  <td>{courseGoalTree.goalValue}</td>
                  <td>
                    {courseGoalTree.course ? <Link to={`/course/${courseGoalTree.course.id}`}>{courseGoalTree.course.name}</Link> : ''}
                  </td>
                  <td>
                    {courseGoalTree.coursegoal ? (
                      <Link to={`/course-goal-relations/${courseGoalTree.coursegoal.id}`}>{courseGoalTree.coursegoal.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/course-goal-tree/${courseGoalTree.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/course-goal-tree/${courseGoalTree.id}/edit`}
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
                        to={`/course-goal-tree/${courseGoalTree.id}/delete`}
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
              <Translate contentKey="deleapApp.courseGoalTree.home.notFound">No Course Goal Trees found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CourseGoalTree;
