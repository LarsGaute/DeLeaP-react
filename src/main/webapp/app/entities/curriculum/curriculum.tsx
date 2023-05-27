import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICurriculum } from 'app/shared/model/curriculum.model';
import { getEntities } from './curriculum.reducer';

export const Curriculum = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const curriculumList = useAppSelector(state => state.curriculum.entities);
  const loading = useAppSelector(state => state.curriculum.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="curriculum-heading" data-cy="CurriculumHeading">
        <Translate contentKey="deleapApp.curriculum.home.title">Curricula</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="deleapApp.curriculum.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/curriculum/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="deleapApp.curriculum.home.createLabel">Create new Curriculum</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {curriculumList && curriculumList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="deleapApp.curriculum.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.curriculum.text">Text</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.curriculum.url">Url</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.curriculum.goal">Goal</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {curriculumList.map((curriculum, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/curriculum/${curriculum.id}`} color="link" size="sm">
                      {curriculum.id}
                    </Button>
                  </td>
                  <td>{curriculum.text}</td>
                  <td>{curriculum.url}</td>
                  <td>{curriculum.goal ? <Link to={`/goal/${curriculum.goal.id}`}>{curriculum.goal.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/curriculum/${curriculum.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/curriculum/${curriculum.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/curriculum/${curriculum.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="deleapApp.curriculum.home.notFound">No Curricula found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Curriculum;
