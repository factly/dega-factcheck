import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClaimant } from 'app/shared/model/factcheck/claimant.model';

type EntityResponseType = HttpResponse<IClaimant>;
type EntityArrayResponseType = HttpResponse<IClaimant[]>;

@Injectable({ providedIn: 'root' })
export class ClaimantService {
  public resourceUrl = SERVER_API_URL + 'api/claimants';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/claimants';

  constructor(private http: HttpClient) {}

  create(claimant: IClaimant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claimant);
    return this.http
      .post<IClaimant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(claimant: IClaimant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claimant);
    return this.http
      .put<IClaimant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IClaimant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClaimant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClaimant[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(claimant: IClaimant): IClaimant {
    const copy: IClaimant = Object.assign({}, claimant, {
      createdDate: claimant.createdDate != null && claimant.createdDate.isValid() ? claimant.createdDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((claimant: IClaimant) => {
        claimant.createdDate = claimant.createdDate != null ? moment(claimant.createdDate) : null;
      });
    }
    return res;
  }
}
