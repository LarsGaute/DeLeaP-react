import { IUser } from 'app/shared/model/user.model';

export interface ICourse {
  id?: number;
  name?: string | null;
  text?: string | null;
  initialGoalId?: number;
  creator?: IUser | null;
}

export const defaultValue: Readonly<ICourse> = {};
