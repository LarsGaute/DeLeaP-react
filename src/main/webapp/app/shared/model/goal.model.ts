export interface IGoal {
  id?: number;
  name?: string | null;
  goalFocus?: string | null;
  whyAchieveThis?: string | null;
  roadAhead?: string | null;
  whatToAchieve?: string | null;
  whatToLearn?: string | null;
  whyFocusOnThis?: string | null;
}

export const defaultValue: Readonly<IGoal> = {};
