import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAcademy } from 'app/shared/model/academy.model';
import { getEntities as getAcademies } from 'app/entities/academy/academy.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { IAcademyCourseRelation } from 'app/shared/model/academy-course-relation.model';
import { getEntity, updateEntity, createEntity, reset } from './academy-course-relation.reducer';

export const AcademyCourseRelationUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const academies = useAppSelector(state => state.academy.entities);
  const courses = useAppSelector(state => state.course.entities);
  const academyCourseRelationEntity = useAppSelector(state => state.academyCourseRelation.entity);
  const loading = useAppSelector(state => state.academyCourseRelation.loading);
  const updating = useAppSelector(state => state.academyCourseRelation.updating);
  const updateSuccess = useAppSelector(state => state.academyCourseRelation.updateSuccess);
  const handleClose = () => {
    props.history.push('/academy-course-relation');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAcademies({}));
    dispatch(getCourses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...academyCourseRelationEntity,
      ...values,
      adademy: academies.find(it => it.id.toString() === values.adademy.toString()),
      course: courses.find(it => it.id.toString() === values.course.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...academyCourseRelationEntity,
          adademy: academyCourseRelationEntity?.adademy?.id,
          course: academyCourseRelationEntity?.course?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="deleapApp.academyCourseRelation.home.createOrEditLabel" data-cy="AcademyCourseRelationCreateUpdateHeading">
            <Translate contentKey="deleapApp.academyCourseRelation.home.createOrEditLabel">
              Create or edit a AcademyCourseRelation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="academy-course-relation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('deleapApp.academyCourseRelation.start')}
                id="academy-course-relation-start"
                name="start"
                data-cy="start"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('deleapApp.academyCourseRelation.end')}
                id="academy-course-relation-end"
                name="end"
                data-cy="end"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="academy-course-relation-adademy"
                name="adademy"
                data-cy="adademy"
                label={translate('deleapApp.academyCourseRelation.adademy')}
                type="select"
              >
                <option value="" key="0" />
                {academies
                  ? academies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="academy-course-relation-course"
                name="course"
                data-cy="course"
                label={translate('deleapApp.academyCourseRelation.course')}
                type="select"
              >
                <option value="" key="0" />
                {courses
                  ? courses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/academy-course-relation" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AcademyCourseRelationUpdate;
