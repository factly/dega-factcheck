import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IClaimant } from 'app/shared/model/factcheck/claimant.model';
import { ClaimantService } from './claimant.service';

@Component({
  selector: 'jhi-claimant-update',
  templateUrl: './claimant-update.component.html'
})
export class ClaimantUpdateComponent implements OnInit {
  claimant: IClaimant;
  isSaving: boolean;

  constructor(private claimantService: ClaimantService, private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ claimant }) => {
      this.claimant = claimant;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.claimant.id !== undefined) {
      this.subscribeToSaveResponse(this.claimantService.update(this.claimant));
    } else {
      this.subscribeToSaveResponse(this.claimantService.create(this.claimant));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IClaimant>>) {
    result.subscribe((res: HttpResponse<IClaimant>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }
}
