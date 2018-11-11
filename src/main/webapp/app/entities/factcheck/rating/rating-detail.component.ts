import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRating } from 'app/shared/model/factcheck/rating.model';

@Component({
  selector: 'jhi-rating-detail',
  templateUrl: './rating-detail.component.html'
})
export class RatingDetailComponent implements OnInit {
  rating: IRating;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ rating }) => {
      this.rating = rating;
    });
  }

  previousState() {
    window.history.back();
  }
}
