import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGoal } from 'app/shared/model/goal.model';
import { getEntities } from './goal.reducer';

export const Goal = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const goalList = useAppSelector(state => state.goal.entities);
  const loading = useAppSelector(state => state.goal.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="goal-heading" data-cy="GoalHeading">
        <Translate contentKey="deleapApp.goal.home.title">Goals</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="deleapApp.goal.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/goal/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="deleapApp.goal.home.createLabel">Create new Goal</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {goalList && goalList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="deleapApp.goal.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.parent">Parent</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.goalValue">Goal Value</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.goalFocus">Goal Focus</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.whyAchieveThis">Why Achieve This</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.roadAhead">Road Ahead</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.whatToAchieve">What To Achieve</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.whatToLearn">What To Learn</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.whyFocusOnThis">Why Focus On This</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.goaldone">Goaldone</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.goal.course">Course</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {goalList.map((goal, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/goal/${goal.id}`} color="link" size="sm">
                      {goal.id}
                    </Button>
                  </td>
                  <td>{goal.name}</td>
                  <td>{goal.parent}</td>
                  <td>{goal.goalValue}</td>
                  <td>{goal.goalFocus}</td>
                  <td>{goal.whyAchieveThis}</td>
                  <td>{goal.roadAhead}</td>
                  <td>{goal.whatToAchieve}</td>
                  <td>{goal.whatToLearn}</td>
                  <td>{goal.whyFocusOnThis}</td>
                  <td>{goal.goaldone ? 'true' : 'false'}</td>
                  <td>{goal.course ? <Link to={`/course/${goal.course.id}`}>{goal.course.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/goal/${goal.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/goal/${goal.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/goal/${goal.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="deleapApp.goal.home.notFound">No Goals found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Goal;
