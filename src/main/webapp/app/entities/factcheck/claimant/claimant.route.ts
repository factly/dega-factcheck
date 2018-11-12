import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Claimant } from 'app/shared/model/factcheck/claimant.model';
import { ClaimantService } from './claimant.service';
import { ClaimantComponent } from './claimant.component';
import { ClaimantDetailComponent } from './claimant-detail.component';
import { ClaimantUpdateComponent } from './claimant-update.component';
import { ClaimantDeletePopupComponent } from './claimant-delete-dialog.component';
import { IClaimant } from 'app/shared/model/factcheck/claimant.model';

@Injectable({ providedIn: 'root' })
export class ClaimantResolve implements Resolve<IClaimant> {
  constructor(private service: ClaimantService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Claimant> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Claimant>) => response.ok),
        map((claimant: HttpResponse<Claimant>) => claimant.body)
      );
    }
    return of(new Claimant());
  }
}

export const claimantRoute: Routes = [
  {
    path: 'claimant',
    component: ClaimantComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'factcheckApp.factcheckClaimant.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'claimant/:id/view',
    component: ClaimantDetailComponent,
    resolve: {
      claimant: ClaimantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckClaimant.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'claimant/new',
    component: ClaimantUpdateComponent,
    resolve: {
      claimant: ClaimantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckClaimant.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'claimant/:id/edit',
    component: ClaimantUpdateComponent,
    resolve: {
      claimant: ClaimantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckClaimant.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const claimantPopupRoute: Routes = [
  {
    path: 'claimant/:id/delete',
    component: ClaimantDeletePopupComponent,
    resolve: {
      claimant: ClaimantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'factcheckApp.factcheckClaimant.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
