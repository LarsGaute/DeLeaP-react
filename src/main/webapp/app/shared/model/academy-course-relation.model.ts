import dayjs from 'dayjs';
import { IAcademy } from 'app/shared/model/academy.model';
import { ICourse } from 'app/shared/model/course.model';

export interface IAcademyCourseRelation {
  id?: number;
  start?: string;
  end?: string;
  adademy?: IAcademy | null;
  course?: ICourse | null;
}

export const defaultValue: Readonly<IAcademyCourseRelation> = {};
