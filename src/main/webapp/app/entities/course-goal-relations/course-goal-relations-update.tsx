import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { IGoal } from 'app/shared/model/goal.model';
import { getEntities as getGoals } from 'app/entities/goal/goal.reducer';
import { ICourseGoalRelations } from 'app/shared/model/course-goal-relations.model';
import { getEntity, updateEntity, createEntity, reset } from './course-goal-relations.reducer';

export const CourseGoalRelationsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const courses = useAppSelector(state => state.course.entities);
  const goals = useAppSelector(state => state.goal.entities);
  const courseGoalRelationsEntity = useAppSelector(state => state.courseGoalRelations.entity);
  const loading = useAppSelector(state => state.courseGoalRelations.loading);
  const updating = useAppSelector(state => state.courseGoalRelations.updating);
  const updateSuccess = useAppSelector(state => state.courseGoalRelations.updateSuccess);
  const handleClose = () => {
    props.history.push('/course-goal-relations');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCourses({}));
    dispatch(getGoals({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...courseGoalRelationsEntity,
      ...values,
      course: courses.find(it => it.id.toString() === values.course.toString()),
      goal: goals.find(it => it.id.toString() === values.goal.toString()),
      parent: goals.find(it => it.id.toString() === values.parent.toString()),
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
          ...courseGoalRelationsEntity,
          course: courseGoalRelationsEntity?.course?.id,
          goal: courseGoalRelationsEntity?.goal?.id,
          parent: courseGoalRelationsEntity?.parent?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="deleapApp.courseGoalRelations.home.createOrEditLabel" data-cy="CourseGoalRelationsCreateUpdateHeading">
            <Translate contentKey="deleapApp.courseGoalRelations.home.createOrEditLabel">Create or edit a CourseGoalRelations</Translate>
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
                  id="course-goal-relations-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('deleapApp.courseGoalRelations.goalValue')}
                id="course-goal-relations-goalValue"
                name="goalValue"
                data-cy="goalValue"
                type="text"
              />
              <ValidatedField
                id="course-goal-relations-course"
                name="course"
                data-cy="course"
                label={translate('deleapApp.courseGoalRelations.course')}
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
              <ValidatedField
                id="course-goal-relations-goal"
                name="goal"
                data-cy="goal"
                label={translate('deleapApp.courseGoalRelations.goal')}
                type="select"
              >
                <option value="" key="0" />
                {goals
                  ? goals.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="course-goal-relations-parent"
                name="parent"
                data-cy="parent"
                label={translate('deleapApp.courseGoalRelations.parent')}
                type="select"
              >
                <option value="" key="0" />
                {goals
                  ? goals.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/course-goal-relations" replace color="info">
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

export default CourseGoalRelationsUpdate;
