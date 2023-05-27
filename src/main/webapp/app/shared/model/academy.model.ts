import { AcademyType } from 'app/shared/model/enumerations/academy-type.model';

export interface IAcademy {
  id?: number;
  name?: string | null;
  type?: AcademyType | null;
}

export const defaultValue: Readonly<IAcademy> = {};
