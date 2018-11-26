import { Moment } from 'moment';
import { IClaim } from 'app/shared/model/factcheck/claim.model';

export interface IFactcheck {
  id?: string;
  title?: string;
  clientId?: string;
  introduction?: string;
  summary?: string;
  excerpt?: string;
  publishedDate?: Moment;
  lastUpdatedDate?: Moment;
  featured?: boolean;
  sticky?: boolean;
  updates?: string;
  slug?: string;
  password?: string;
  featuredMedia?: string;
  subTitle?: string;
  createdDate?: Moment;
  claims?: IClaim[];
}

export class Factcheck implements IFactcheck {
  constructor(
    public id?: string,
    public title?: string,
    public clientId?: string,
    public introduction?: string,
    public summary?: string,
    public excerpt?: string,
    public publishedDate?: Moment,
    public lastUpdatedDate?: Moment,
    public featured?: boolean,
    public sticky?: boolean,
    public updates?: string,
    public slug?: string,
    public password?: string,
    public featuredMedia?: string,
    public subTitle?: string,
    public createdDate?: Moment,
    public claims?: IClaim[]
  ) {
    this.featured = this.featured || false;
    this.sticky = this.sticky || false;
  }
}
