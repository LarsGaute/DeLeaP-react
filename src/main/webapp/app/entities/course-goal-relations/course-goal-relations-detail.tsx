import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './course-goal-relations.reducer';

export const CourseGoalRelationsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const courseGoalRelationsEntity = useAppSelector(state => state.courseGoalRelations.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courseGoalRelationsDetailsHeading">
          <Translate contentKey="deleapApp.courseGoalRelations.detail.title">CourseGoalRelations</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{courseGoalRelationsEntity.id}</dd>
          <dt>
            <span id="goalValue">
              <Translate contentKey="deleapApp.courseGoalRelations.goalValue">Goal Value</Translate>
            </span>
          </dt>
          <dd>{courseGoalRelationsEntity.goalValue}</dd>
          <dt>
            <Translate contentKey="deleapApp.courseGoalRelations.course">Course</Translate>
          </dt>
          <dd>{courseGoalRelationsEntity.course ? courseGoalRelationsEntity.course.name : ''}</dd>
          <dt>
            <Translate contentKey="deleapApp.courseGoalRelations.goal">Goal</Translate>
          </dt>
          <dd>{courseGoalRelationsEntity.goal ? courseGoalRelationsEntity.goal.name : ''}</dd>
          <dt>
            <Translate contentKey="deleapApp.courseGoalRelations.parent">Parent</Translate>
          </dt>
          <dd>{courseGoalRelationsEntity.parent ? courseGoalRelationsEntity.parent.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/course-goal-relations" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/course-goal-relations/${courseGoalRelationsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourseGoalRelationsDetail;
