import { IClaim } from 'app/shared/model/factcheck/claim.model';

export interface IClaimant {
  id?: string;
  name?: string;
  tagLine?: string;
  description?: string;
  imageURL?: string;
  clientId?: string;
  claims?: IClaim[];
}

export class Claimant implements IClaimant {
  constructor(
    public id?: string,
    public name?: string,
    public tagLine?: string,
    public description?: string,
    public imageURL?: string,
    public clientId?: string,
    public claims?: IClaim[]
  ) {}
}
