import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRating } from 'app/shared/model/factcheck/rating.model';
import { RatingService } from './rating.service';

@Component({
  selector: 'jhi-rating-update',
  templateUrl: './rating-update.component.html'
})
export class RatingUpdateComponent implements OnInit {
  rating: IRating;
  isSaving: boolean;
  createdDate: string;
  lastUpdatedDate: string;

  constructor(private ratingService: RatingService, private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rating }) => {
      this.rating = rating;
      this.createdDate = this.rating.createdDate != null ? this.rating.createdDate.format(DATE_TIME_FORMAT) : null;
      this.lastUpdatedDate = this.rating.lastUpdatedDate != null ? this.rating.lastUpdatedDate.format(DATE_TIME_FORMAT) : null;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.rating.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
    this.rating.lastUpdatedDate = this.lastUpdatedDate != null ? moment(this.lastUpdatedDate, DATE_TIME_FORMAT) : null;
    if (this.rating.id !== undefined) {
      this.subscribeToSaveResponse(this.ratingService.update(this.rating));
    } else {
      this.subscribeToSaveResponse(this.ratingService.create(this.rating));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IRating>>) {
    result.subscribe((res: HttpResponse<IRating>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }
}
