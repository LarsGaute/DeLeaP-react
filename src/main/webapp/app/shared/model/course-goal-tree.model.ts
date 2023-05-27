import { ICourse } from 'app/shared/model/course.model';
import { ICourseGoalRelations } from 'app/shared/model/course-goal-relations.model';

export interface ICourseGoalTree {
  id?: number;
  goalValue?: string | null;
  course?: ICourse | null;
  coursegoal?: ICourseGoalRelations | null;
}

export const defaultValue: Readonly<ICourseGoalTree> = {};
