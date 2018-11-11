import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactcheckSharedModule } from 'app/shared';
import {
  ClaimantComponent,
  ClaimantDetailComponent,
  ClaimantUpdateComponent,
  ClaimantDeletePopupComponent,
  ClaimantDeleteDialogComponent,
  claimantRoute,
  claimantPopupRoute
} from './';

const ENTITY_STATES = [...claimantRoute, ...claimantPopupRoute];

@NgModule({
  imports: [FactcheckSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ClaimantComponent,
    ClaimantDetailComponent,
    ClaimantUpdateComponent,
    ClaimantDeleteDialogComponent,
    ClaimantDeletePopupComponent
  ],
  entryComponents: [ClaimantComponent, ClaimantUpdateComponent, ClaimantDeleteDialogComponent, ClaimantDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactcheckClaimantModule {}
