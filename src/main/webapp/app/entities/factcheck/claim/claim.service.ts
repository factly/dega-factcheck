import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClaim } from 'app/shared/model/factcheck/claim.model';

type EntityResponseType = HttpResponse<IClaim>;
type EntityArrayResponseType = HttpResponse<IClaim[]>;

@Injectable({ providedIn: 'root' })
export class ClaimService {
  public resourceUrl = SERVER_API_URL + 'api/claims';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/claims';

  constructor(private http: HttpClient) {}

  create(claim: IClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claim);
    return this.http
      .post<IClaim>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(claim: IClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claim);
    return this.http
      .put<IClaim>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IClaim>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClaim[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClaim[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(claim: IClaim): IClaim {
    const copy: IClaim = Object.assign({}, claim, {
      claimDate: claim.claimDate != null && claim.claimDate.isValid() ? claim.claimDate.format(DATE_FORMAT) : null,
      checkedDate: claim.checkedDate != null && claim.checkedDate.isValid() ? claim.checkedDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.claimDate = res.body.claimDate != null ? moment(res.body.claimDate) : null;
      res.body.checkedDate = res.body.checkedDate != null ? moment(res.body.checkedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((claim: IClaim) => {
        claim.claimDate = claim.claimDate != null ? moment(claim.claimDate) : null;
        claim.checkedDate = claim.checkedDate != null ? moment(claim.checkedDate) : null;
      });
    }
    return res;
  }
}
