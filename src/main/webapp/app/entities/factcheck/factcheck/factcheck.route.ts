import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Factcheck } from 'app/shared/model/factcheck/factcheck.model';
import { FactcheckService } from './factcheck.service';
import { FactcheckComponent } from './factcheck.component';
import { FactcheckDetailComponent } from './factcheck-detail.component';
import { FactcheckUpdateComponent } from './factcheck-update.component';
import { FactcheckDeletePopupComponent } from './factcheck-delete-dialog.component';
import { IFactcheck } from 'app/shared/model/factcheck/factcheck.model';

@Injectable({ providedIn: 'root' })
export class FactcheckResolve implements Resolve<IFactcheck> {
  constructor(private service: FactcheckService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Factcheck> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Factcheck>) => response.ok),
        map((factcheck: HttpResponse<Factcheck>) => factcheck.body)
      );
    }
    return of(new Factcheck());
  }
}

export const factcheckRoute: Routes = [
  {
    path: 'factcheck',
    component: FactcheckComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'factcheckApp.factcheckFactcheck.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'factcheck/:id/view',
    component: FactcheckDetailComponent,
    resolve: {
      factcheck: FactcheckResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckFactcheck.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'factcheck/new',
    component: FactcheckUpdateComponent,
    resolve: {
      factcheck: FactcheckResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckFactcheck.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'factcheck/:id/edit',
    component: FactcheckUpdateComponent,
    resolve: {
      factcheck: FactcheckResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckFactcheck.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const factcheckPopupRoute: Routes = [
  {
    path: 'factcheck/:id/delete',
    component: FactcheckDeletePopupComponent,
    resolve: {
      factcheck: FactcheckResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckFactcheck.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
