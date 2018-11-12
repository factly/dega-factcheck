import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
    return this.http.post<IClaimant>(this.resourceUrl, claimant, { observe: 'response' });
  }

  update(claimant: IClaimant): Observable<EntityResponseType> {
    return this.http.put<IClaimant>(this.resourceUrl, claimant, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IClaimant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClaimant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClaimant[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
