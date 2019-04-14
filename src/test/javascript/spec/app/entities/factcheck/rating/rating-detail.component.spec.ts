/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FactcheckTestModule } from '../../../../test.module';
import { RatingDetailComponent } from 'app/entities/factcheck/rating/rating-detail.component';
import { Rating } from 'app/shared/model/factcheck/rating.model';

describe('Component Tests', () => {
  describe('Rating Management Detail Component', () => {
    let comp: RatingDetailComponent;
    let fixture: ComponentFixture<RatingDetailComponent>;
    const route = ({ data: of({ rating: new Rating('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [RatingDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RatingDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RatingDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rating).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
