/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FactcheckTestModule } from '../../../../test.module';
import { RatingUpdateComponent } from 'app/entities/factcheck/rating/rating-update.component';
import { RatingService } from 'app/entities/factcheck/rating/rating.service';
import { Rating } from 'app/shared/model/factcheck/rating.model';

describe('Component Tests', () => {
  describe('Rating Management Update Component', () => {
    let comp: RatingUpdateComponent;
    let fixture: ComponentFixture<RatingUpdateComponent>;
    let service: RatingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [RatingUpdateComponent]
      })
        .overrideTemplate(RatingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RatingUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RatingService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new Rating('123');
          spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
          comp.rating = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );

      it(
        'Should call create service on save for new entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new Rating();
          spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
          comp.rating = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );
    });
  });
});
