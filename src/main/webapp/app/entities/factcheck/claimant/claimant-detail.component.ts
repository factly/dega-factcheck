import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClaimant } from 'app/shared/model/factcheck/claimant.model';

@Component({
  selector: 'jhi-claimant-detail',
  templateUrl: './claimant-detail.component.html'
})
export class ClaimantDetailComponent implements OnInit {
  claimant: IClaimant;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ claimant }) => {
      this.claimant = claimant;
    });
  }

  previousState() {
    window.history.back();
  }
}
