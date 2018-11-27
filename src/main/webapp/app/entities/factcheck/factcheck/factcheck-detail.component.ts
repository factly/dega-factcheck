import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFactcheck } from 'app/shared/model/factcheck/factcheck.model';

@Component({
  selector: 'jhi-factcheck-detail',
  templateUrl: './factcheck-detail.component.html'
})
export class FactcheckDetailComponent implements OnInit {
  factcheck: IFactcheck;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ factcheck }) => {
      this.factcheck = factcheck;
    });
  }

  previousState() {
    window.history.back();
  }
}
