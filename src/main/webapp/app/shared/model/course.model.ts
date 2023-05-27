export interface ICourse {
  id?: number;
  name?: string | null;
  text?: string | null;
}

export const defaultValue: Readonly<ICourse> = {};
