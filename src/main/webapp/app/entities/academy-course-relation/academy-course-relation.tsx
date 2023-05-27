import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAcademyCourseRelation } from 'app/shared/model/academy-course-relation.model';
import { getEntities } from './academy-course-relation.reducer';

export const AcademyCourseRelation = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const academyCourseRelationList = useAppSelector(state => state.academyCourseRelation.entities);
  const loading = useAppSelector(state => state.academyCourseRelation.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="academy-course-relation-heading" data-cy="AcademyCourseRelationHeading">
        <Translate contentKey="deleapApp.academyCourseRelation.home.title">Academy Course Relations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="deleapApp.academyCourseRelation.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/academy-course-relation/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="deleapApp.academyCourseRelation.home.createLabel">Create new Academy Course Relation</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {academyCourseRelationList && academyCourseRelationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="deleapApp.academyCourseRelation.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.academyCourseRelation.start">Start</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.academyCourseRelation.end">End</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.academyCourseRelation.adademy">Adademy</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.academyCourseRelation.course">Course</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {academyCourseRelationList.map((academyCourseRelation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/academy-course-relation/${academyCourseRelation.id}`} color="link" size="sm">
                      {academyCourseRelation.id}
                    </Button>
                  </td>
                  <td>
                    {academyCourseRelation.start ? (
                      <TextFormat type="date" value={academyCourseRelation.start} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {academyCourseRelation.end ? (
                      <TextFormat type="date" value={academyCourseRelation.end} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {academyCourseRelation.adademy ? (
                      <Link to={`/academy/${academyCourseRelation.adademy.id}`}>{academyCourseRelation.adademy.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {academyCourseRelation.course ? (
                      <Link to={`/course/${academyCourseRelation.course.id}`}>{academyCourseRelation.course.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/academy-course-relation/${academyCourseRelation.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/academy-course-relation/${academyCourseRelation.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/academy-course-relation/${academyCourseRelation.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="deleapApp.academyCourseRelation.home.notFound">No Academy Course Relations found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AcademyCourseRelation;
