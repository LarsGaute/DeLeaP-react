import { ICourse } from 'app/shared/model/course.model';

export interface IGoal {
  id?: number;
  name?: string | null;
  parent?: number | null;
  goalValue?: string | null;
  goalFocus?: string | null;
  whyAchieveThis?: string | null;
  roadAhead?: string | null;
  whatToAchieve?: string | null;
  whatToLearn?: string | null;
  whyFocusOnThis?: string | null;
  goaldone?: boolean | null;
  course?: ICourse | null;
}

export const defaultValue: Readonly<IGoal> = {
  goaldone: false,
};
