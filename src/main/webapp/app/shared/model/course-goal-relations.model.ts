import { ICourse } from 'app/shared/model/course.model';
import { IGoal } from 'app/shared/model/goal.model';

export interface ICourseGoalRelations {
  id?: number;
  goalValue?: string | null;
  course?: ICourse | null;
  goal?: IGoal | null;
  parent?: IGoal | null;
}

export const defaultValue: Readonly<ICourseGoalRelations> = {};
