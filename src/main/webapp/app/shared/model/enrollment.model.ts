import { IUser } from 'app/shared/model/user.model';
import { IAcademy } from 'app/shared/model/academy.model';
import { ICourse } from 'app/shared/model/course.model';
import { AcademicRoles } from 'app/shared/model/enumerations/academic-roles.model';

export interface IEnrollment {
  id?: number;
  role?: AcademicRoles;
  user?: IUser | null;
  adademy?: IAcademy | null;
  course?: ICourse | null;
}

export const defaultValue: Readonly<IEnrollment> = {};
