import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './goal.reducer';

export const GoalDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const goalEntity = useAppSelector(state => state.goal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="goalDetailsHeading">
          <Translate contentKey="deleapApp.goal.detail.title">Goal</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{goalEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="deleapApp.goal.name">Name</Translate>
            </span>
          </dt>
          <dd>{goalEntity.name}</dd>
          <dt>
            <span id="parent">
              <Translate contentKey="deleapApp.goal.parent">Parent</Translate>
            </span>
          </dt>
          <dd>{goalEntity.parent}</dd>
          <dt>
            <span id="goalValue">
              <Translate contentKey="deleapApp.goal.goalValue">Goal Value</Translate>
            </span>
          </dt>
          <dd>{goalEntity.goalValue}</dd>
          <dt>
            <span id="goalFocus">
              <Translate contentKey="deleapApp.goal.goalFocus">Goal Focus</Translate>
            </span>
          </dt>
          <dd>{goalEntity.goalFocus}</dd>
          <dt>
            <span id="whyAchieveThis">
              <Translate contentKey="deleapApp.goal.whyAchieveThis">Why Achieve This</Translate>
            </span>
          </dt>
          <dd>{goalEntity.whyAchieveThis}</dd>
          <dt>
            <span id="roadAhead">
              <Translate contentKey="deleapApp.goal.roadAhead">Road Ahead</Translate>
            </span>
          </dt>
          <dd>{goalEntity.roadAhead}</dd>
          <dt>
            <span id="whatToAchieve">
              <Translate contentKey="deleapApp.goal.whatToAchieve">What To Achieve</Translate>
            </span>
          </dt>
          <dd>{goalEntity.whatToAchieve}</dd>
          <dt>
            <span id="whatToLearn">
              <Translate contentKey="deleapApp.goal.whatToLearn">What To Learn</Translate>
            </span>
          </dt>
          <dd>{goalEntity.whatToLearn}</dd>
          <dt>
            <span id="whyFocusOnThis">
              <Translate contentKey="deleapApp.goal.whyFocusOnThis">Why Focus On This</Translate>
            </span>
          </dt>
          <dd>{goalEntity.whyFocusOnThis}</dd>
          <dt>
            <span id="goaldone">
              <Translate contentKey="deleapApp.goal.goaldone">Goaldone</Translate>
            </span>
          </dt>
          <dd>{goalEntity.goaldone ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="deleapApp.goal.course">Course</Translate>
          </dt>
          <dd>{goalEntity.course ? goalEntity.course.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/goal" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/goal/${goalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GoalDetail;
