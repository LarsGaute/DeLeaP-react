import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './academy-course-relation.reducer';

export const AcademyCourseRelationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const academyCourseRelationEntity = useAppSelector(state => state.academyCourseRelation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="academyCourseRelationDetailsHeading">
          <Translate contentKey="deleapApp.academyCourseRelation.detail.title">AcademyCourseRelation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{academyCourseRelationEntity.id}</dd>
          <dt>
            <span id="start">
              <Translate contentKey="deleapApp.academyCourseRelation.start">Start</Translate>
            </span>
          </dt>
          <dd>
            {academyCourseRelationEntity.start ? (
              <TextFormat value={academyCourseRelationEntity.start} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="end">
              <Translate contentKey="deleapApp.academyCourseRelation.end">End</Translate>
            </span>
          </dt>
          <dd>
            {academyCourseRelationEntity.end ? (
              <TextFormat value={academyCourseRelationEntity.end} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="deleapApp.academyCourseRelation.adademy">Adademy</Translate>
          </dt>
          <dd>{academyCourseRelationEntity.adademy ? academyCourseRelationEntity.adademy.name : ''}</dd>
          <dt>
            <Translate contentKey="deleapApp.academyCourseRelation.course">Course</Translate>
          </dt>
          <dd>{academyCourseRelationEntity.course ? academyCourseRelationEntity.course.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/academy-course-relation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/academy-course-relation/${academyCourseRelationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AcademyCourseRelationDetail;
