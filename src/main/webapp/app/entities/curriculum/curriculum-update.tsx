import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGoal } from 'app/shared/model/goal.model';
import { getEntities as getGoals } from 'app/entities/goal/goal.reducer';
import { ICurriculum } from 'app/shared/model/curriculum.model';
import { getEntity, updateEntity, createEntity, reset } from './curriculum.reducer';

export const CurriculumUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const goals = useAppSelector(state => state.goal.entities);
  const curriculumEntity = useAppSelector(state => state.curriculum.entity);
  const loading = useAppSelector(state => state.curriculum.loading);
  const updating = useAppSelector(state => state.curriculum.updating);
  const updateSuccess = useAppSelector(state => state.curriculum.updateSuccess);
  const handleClose = () => {
    props.history.push('/curriculum');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGoals({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...curriculumEntity,
      ...values,
      goal: goals.find(it => it.id.toString() === values.goal.toString()),
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
          ...curriculumEntity,
          goal: curriculumEntity?.goal?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="deleapApp.curriculum.home.createOrEditLabel" data-cy="CurriculumCreateUpdateHeading">
            <Translate contentKey="deleapApp.curriculum.home.createOrEditLabel">Create or edit a Curriculum</Translate>
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
                  id="curriculum-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('deleapApp.curriculum.text')} id="curriculum-text" name="text" data-cy="text" type="text" />
              <ValidatedField label={translate('deleapApp.curriculum.url')} id="curriculum-url" name="url" data-cy="url" type="text" />
              <ValidatedField id="curriculum-goal" name="goal" data-cy="goal" label={translate('deleapApp.curriculum.goal')} type="select">
                <option value="" key="0" />
                {goals
                  ? goals.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/curriculum" replace color="info">
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

export default CurriculumUpdate;
