import { IGoal } from 'app/shared/model/goal.model';

export interface ICurriculum {
  id?: number;
  text?: string | null;
  url?: string | null;
  goal?: IGoal | null;
}

export const defaultValue: Readonly<ICurriculum> = {};
