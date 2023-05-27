import course from 'app/entities/course/course.reducer';
import goal from 'app/entities/goal/goal.reducer';
import curriculum from 'app/entities/curriculum/curriculum.reducer';
import academy from 'app/entities/academy/academy.reducer';
import enrollment from 'app/entities/enrollment/enrollment.reducer';
import academyCourseRelation from 'app/entities/academy-course-relation/academy-course-relation.reducer';
import courseGoalRelations from 'app/entities/course-goal-relations/course-goal-relations.reducer';
import courseGoalTree from 'app/entities/course-goal-tree/course-goal-tree.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  course,
  goal,
  curriculum,
  academy,
  enrollment,
  academyCourseRelation,
  courseGoalRelations,
  courseGoalTree,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
