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
import { ICourseGoalRelations } from 'app/shared/model/course-goal-relations.model';
import { getEntities as getCourseGoalRelations } from 'app/entities/course-goal-relations/course-goal-relations.reducer';
import { ICourseGoalTree } from 'app/shared/model/course-goal-tree.model';
import { getEntity, updateEntity, createEntity, reset } from './course-goal-tree.reducer';

export const CourseGoalTreeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const courses = useAppSelector(state => state.course.entities);
  const courseGoalRelations = useAppSelector(state => state.courseGoalRelations.entities);
  const courseGoalTreeEntity = useAppSelector(state => state.courseGoalTree.entity);
  const loading = useAppSelector(state => state.courseGoalTree.loading);
  const updating = useAppSelector(state => state.courseGoalTree.updating);
  const updateSuccess = useAppSelector(state => state.courseGoalTree.updateSuccess);
  const handleClose = () => {
    props.history.push('/course-goal-tree');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCourses({}));
    dispatch(getCourseGoalRelations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...courseGoalTreeEntity,
      ...values,
      course: courses.find(it => it.id.toString() === values.course.toString()),
      coursegoal: courseGoalRelations.find(it => it.id.toString() === values.coursegoal.toString()),
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
          ...courseGoalTreeEntity,
          course: courseGoalTreeEntity?.course?.id,
          coursegoal: courseGoalTreeEntity?.coursegoal?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="deleapApp.courseGoalTree.home.createOrEditLabel" data-cy="CourseGoalTreeCreateUpdateHeading">
            <Translate contentKey="deleapApp.courseGoalTree.home.createOrEditLabel">Create or edit a CourseGoalTree</Translate>
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
                  id="course-goal-tree-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('deleapApp.courseGoalTree.goalValue')}
                id="course-goal-tree-goalValue"
                name="goalValue"
                data-cy="goalValue"
                type="text"
              />
              <ValidatedField
                id="course-goal-tree-course"
                name="course"
                data-cy="course"
                label={translate('deleapApp.courseGoalTree.course')}
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
                id="course-goal-tree-coursegoal"
                name="coursegoal"
                data-cy="coursegoal"
                label={translate('deleapApp.courseGoalTree.coursegoal')}
                type="select"
              >
                <option value="" key="0" />
                {courseGoalRelations
                  ? courseGoalRelations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/course-goal-tree" replace color="info">
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

export default CourseGoalTreeUpdate;
