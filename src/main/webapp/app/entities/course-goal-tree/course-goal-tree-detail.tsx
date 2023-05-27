import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './course-goal-tree.reducer';

export const CourseGoalTreeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const courseGoalTreeEntity = useAppSelector(state => state.courseGoalTree.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courseGoalTreeDetailsHeading">
          <Translate contentKey="deleapApp.courseGoalTree.detail.title">CourseGoalTree</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{courseGoalTreeEntity.id}</dd>
          <dt>
            <span id="goalValue">
              <Translate contentKey="deleapApp.courseGoalTree.goalValue">Goal Value</Translate>
            </span>
          </dt>
          <dd>{courseGoalTreeEntity.goalValue}</dd>
          <dt>
            <Translate contentKey="deleapApp.courseGoalTree.course">Course</Translate>
          </dt>
          <dd>{courseGoalTreeEntity.course ? courseGoalTreeEntity.course.name : ''}</dd>
          <dt>
            <Translate contentKey="deleapApp.courseGoalTree.coursegoal">Coursegoal</Translate>
          </dt>
          <dd>{courseGoalTreeEntity.coursegoal ? courseGoalTreeEntity.coursegoal.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/course-goal-tree" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/course-goal-tree/${courseGoalTreeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourseGoalTreeDetail;
