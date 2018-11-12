import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRating } from 'app/shared/model/factcheck/rating.model';
import { RatingService } from './rating.service';

@Component({
  selector: 'jhi-rating-update',
  templateUrl: './rating-update.component.html'
})
export class RatingUpdateComponent implements OnInit {
  rating: IRating;
  isSaving: boolean;

  constructor(private ratingService: RatingService, private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rating }) => {
      this.rating = rating;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
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
