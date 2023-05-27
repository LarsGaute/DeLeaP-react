import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAcademy } from 'app/shared/model/academy.model';
import { getEntities } from './academy.reducer';

export const Academy = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const academyList = useAppSelector(state => state.academy.entities);
  const loading = useAppSelector(state => state.academy.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="academy-heading" data-cy="AcademyHeading">
        <Translate contentKey="deleapApp.academy.home.title">Academies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="deleapApp.academy.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/academy/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="deleapApp.academy.home.createLabel">Create new Academy</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {academyList && academyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="deleapApp.academy.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.academy.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="deleapApp.academy.type">Type</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {academyList.map((academy, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/academy/${academy.id}`} color="link" size="sm">
                      {academy.id}
                    </Button>
                  </td>
                  <td>{academy.name}</td>
                  <td>
                    <Translate contentKey={`deleapApp.AcademyType.${academy.type}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/academy/${academy.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/academy/${academy.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/academy/${academy.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="deleapApp.academy.home.notFound">No Academies found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Academy;
