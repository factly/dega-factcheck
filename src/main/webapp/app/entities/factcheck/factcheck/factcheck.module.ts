import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactcheckSharedModule } from 'app/shared';
import {
  FactcheckComponent,
  FactcheckDetailComponent,
  FactcheckUpdateComponent,
  FactcheckDeletePopupComponent,
  FactcheckDeleteDialogComponent,
  factcheckRoute,
  factcheckPopupRoute
} from './';

const ENTITY_STATES = [...factcheckRoute, ...factcheckPopupRoute];

@NgModule({
  imports: [FactcheckSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FactcheckComponent,
    FactcheckDetailComponent,
    FactcheckUpdateComponent,
    FactcheckDeleteDialogComponent,
    FactcheckDeletePopupComponent
  ],
  entryComponents: [FactcheckComponent, FactcheckUpdateComponent, FactcheckDeleteDialogComponent, FactcheckDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactcheckFactcheckModule {}
