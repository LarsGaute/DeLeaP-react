import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAcademy } from 'app/shared/model/academy.model';
import { getEntities as getAcademies } from 'app/entities/academy/academy.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { IEnrollment } from 'app/shared/model/enrollment.model';
import { AcademicRoles } from 'app/shared/model/enumerations/academic-roles.model';
import { getEntity, updateEntity, createEntity, reset } from './enrollment.reducer';

export const EnrollmentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const academies = useAppSelector(state => state.academy.entities);
  const courses = useAppSelector(state => state.course.entities);
  const enrollmentEntity = useAppSelector(state => state.enrollment.entity);
  const loading = useAppSelector(state => state.enrollment.loading);
  const updating = useAppSelector(state => state.enrollment.updating);
  const updateSuccess = useAppSelector(state => state.enrollment.updateSuccess);
  const academicRolesValues = Object.keys(AcademicRoles);
  const handleClose = () => {
    props.history.push('/enrollment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
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
      ...enrollmentEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          role: 'AcademyAdmin',
          ...enrollmentEntity,
          user: enrollmentEntity?.user?.id,
          adademy: enrollmentEntity?.adademy?.id,
          course: enrollmentEntity?.course?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="deleapApp.enrollment.home.createOrEditLabel" data-cy="EnrollmentCreateUpdateHeading">
            <Translate contentKey="deleapApp.enrollment.home.createOrEditLabel">Create or edit a Enrollment</Translate>
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
                  id="enrollment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('deleapApp.enrollment.role')} id="enrollment-role" name="role" data-cy="role" type="select">
                {academicRolesValues.map(academicRoles => (
                  <option value={academicRoles} key={academicRoles}>
                    {translate('deleapApp.AcademicRoles.' + academicRoles)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="enrollment-user" name="user" data-cy="user" label={translate('deleapApp.enrollment.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="enrollment-adademy"
                name="adademy"
                data-cy="adademy"
                label={translate('deleapApp.enrollment.adademy')}
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
                id="enrollment-course"
                name="course"
                data-cy="course"
                label={translate('deleapApp.enrollment.course')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/enrollment" replace color="info">
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

export default EnrollmentUpdate;
