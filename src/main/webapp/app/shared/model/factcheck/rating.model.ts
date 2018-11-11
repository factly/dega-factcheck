import { IClaim } from 'app/shared/model/factcheck/claim.model';

export interface IRating {
  id?: string;
  name?: string;
  numericValue?: number;
  iconURL?: string;
  isDefault?: boolean;
  clientId?: string;
  claims?: IClaim[];
}

export class Rating implements IRating {
  constructor(
    public id?: string,
    public name?: string,
    public numericValue?: number,
    public iconURL?: string,
    public isDefault?: boolean,
    public clientId?: string,
    public claims?: IClaim[]
  ) {
    this.isDefault = this.isDefault || false;
  }
}
