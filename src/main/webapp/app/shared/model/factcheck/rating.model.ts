export interface IRating {
  id?: string;
  name?: string;
  numericValue?: number;
  iconURL?: string;
  isDefault?: boolean;
  clientId?: string;
}

export class Rating implements IRating {
  constructor(
    public id?: string,
    public name?: string,
    public numericValue?: number,
    public iconURL?: string,
    public isDefault?: boolean,
    public clientId?: string
  ) {
    this.isDefault = this.isDefault || false;
  }
}
