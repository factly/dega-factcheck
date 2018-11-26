import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFactcheck } from 'app/shared/model/factcheck/factcheck.model';

type EntityResponseType = HttpResponse<IFactcheck>;
type EntityArrayResponseType = HttpResponse<IFactcheck[]>;

@Injectable({ providedIn: 'root' })
export class FactcheckService {
  public resourceUrl = SERVER_API_URL + 'api/factchecks';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/factchecks';

  constructor(private http: HttpClient) {}

  create(factcheck: IFactcheck): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factcheck);
    return this.http
      .post<IFactcheck>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(factcheck: IFactcheck): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factcheck);
    return this.http
      .put<IFactcheck>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IFactcheck>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactcheck[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactcheck[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(factcheck: IFactcheck): IFactcheck {
    const copy: IFactcheck = Object.assign({}, factcheck, {
      publishedDate: factcheck.publishedDate != null && factcheck.publishedDate.isValid() ? factcheck.publishedDate.toJSON() : null,
      lastUpdatedDate: factcheck.lastUpdatedDate != null && factcheck.lastUpdatedDate.isValid() ? factcheck.lastUpdatedDate.toJSON() : null,
      createdDate: factcheck.createdDate != null && factcheck.createdDate.isValid() ? factcheck.createdDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.publishedDate = res.body.publishedDate != null ? moment(res.body.publishedDate) : null;
      res.body.lastUpdatedDate = res.body.lastUpdatedDate != null ? moment(res.body.lastUpdatedDate) : null;
      res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((factcheck: IFactcheck) => {
        factcheck.publishedDate = factcheck.publishedDate != null ? moment(factcheck.publishedDate) : null;
        factcheck.lastUpdatedDate = factcheck.lastUpdatedDate != null ? moment(factcheck.lastUpdatedDate) : null;
        factcheck.createdDate = factcheck.createdDate != null ? moment(factcheck.createdDate) : null;
      });
    }
    return res;
  }
}
