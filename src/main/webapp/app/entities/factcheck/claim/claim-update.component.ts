import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IClaim } from 'app/shared/model/factcheck/claim.model';
import { ClaimService } from './claim.service';
import { IRating } from 'app/shared/model/factcheck/rating.model';
import { RatingService } from 'app/entities/factcheck/rating';
import { IClaimant } from 'app/shared/model/factcheck/claimant.model';
import { ClaimantService } from 'app/entities/factcheck/claimant';
import { IFactcheck } from 'app/shared/model/factcheck/fact-check.model';
import { FactcheckService } from 'app/entities/factcheck/fact-check';

@Component({
  selector: 'jhi-claim-update',
  templateUrl: './claim-update.component.html'
})
export class ClaimUpdateComponent implements OnInit {
  claim: IClaim;
  isSaving: boolean;

  ratings: IRating[];

  claimants: IClaimant[];

  factchecks: IFactcheck[];
  claimDateDp: any;
  checkedDateDp: any;
  createdDate: string;

  constructor(
    private jhiAlertService: JhiAlertService,
    private claimService: ClaimService,
    private ratingService: RatingService,
    private claimantService: ClaimantService,
    private factcheckService: FactcheckService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ claim }) => {
      this.claim = claim;
      this.createdDate = this.claim.createdDate != null ? this.claim.createdDate.format(DATE_TIME_FORMAT) : null;
    });
    this.ratingService.query().subscribe(
      (res: HttpResponse<IRating[]>) => {
        this.ratings = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.claimantService.query().subscribe(
      (res: HttpResponse<IClaimant[]>) => {
        this.claimants = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.factcheckService.query().subscribe(
      (res: HttpResponse<IFactcheck[]>) => {
        this.factchecks = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.claim.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
    if (this.claim.id !== undefined) {
      this.subscribeToSaveResponse(this.claimService.update(this.claim));
    } else {
      this.subscribeToSaveResponse(this.claimService.create(this.claim));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IClaim>>) {
    result.subscribe((res: HttpResponse<IClaim>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackRatingById(index: number, item: IRating) {
    return item.id;
  }

  trackClaimantById(index: number, item: IClaimant) {
    return item.id;
  }

  trackFactcheckById(index: number, item: IFactcheck) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
