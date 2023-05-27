import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './enrollment.reducer';

export const EnrollmentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const enrollmentEntity = useAppSelector(state => state.enrollment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="enrollmentDetailsHeading">
          <Translate contentKey="deleapApp.enrollment.detail.title">Enrollment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{enrollmentEntity.id}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="deleapApp.enrollment.role">Role</Translate>
            </span>
          </dt>
          <dd>{enrollmentEntity.role}</dd>
          <dt>
            <Translate contentKey="deleapApp.enrollment.user">User</Translate>
          </dt>
          <dd>{enrollmentEntity.user ? enrollmentEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="deleapApp.enrollment.adademy">Adademy</Translate>
          </dt>
          <dd>{enrollmentEntity.adademy ? enrollmentEntity.adademy.name : ''}</dd>
          <dt>
            <Translate contentKey="deleapApp.enrollment.course">Course</Translate>
          </dt>
          <dd>{enrollmentEntity.course ? enrollmentEntity.course.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/enrollment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/enrollment/${enrollmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EnrollmentDetail;
