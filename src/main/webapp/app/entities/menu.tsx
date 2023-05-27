import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/course">
        <Translate contentKey="global.menu.entities.course" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/goal">
        <Translate contentKey="global.menu.entities.goal" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/curriculum">
        <Translate contentKey="global.menu.entities.curriculum" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/academy">
        <Translate contentKey="global.menu.entities.academy" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/enrollment">
        <Translate contentKey="global.menu.entities.enrollment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/academy-course-relation">
        <Translate contentKey="global.menu.entities.academyCourseRelation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/course-goal-relations">
        <Translate contentKey="global.menu.entities.courseGoalRelations" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/course-goal-tree">
        <Translate contentKey="global.menu.entities.courseGoalTree" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
