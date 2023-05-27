import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './curriculum.reducer';

export const CurriculumDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const curriculumEntity = useAppSelector(state => state.curriculum.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="curriculumDetailsHeading">
          <Translate contentKey="deleapApp.curriculum.detail.title">Curriculum</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{curriculumEntity.id}</dd>
          <dt>
            <span id="text">
              <Translate contentKey="deleapApp.curriculum.text">Text</Translate>
            </span>
          </dt>
          <dd>{curriculumEntity.text}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="deleapApp.curriculum.url">Url</Translate>
            </span>
          </dt>
          <dd>{curriculumEntity.url}</dd>
          <dt>
            <Translate contentKey="deleapApp.curriculum.goal">Goal</Translate>
          </dt>
          <dd>{curriculumEntity.goal ? curriculumEntity.goal.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/curriculum" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/curriculum/${curriculumEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CurriculumDetail;
