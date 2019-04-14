/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FactcheckTestModule } from '../../../../test.module';
import { ClaimantUpdateComponent } from 'app/entities/factcheck/claimant/claimant-update.component';
import { ClaimantService } from 'app/entities/factcheck/claimant/claimant.service';
import { Claimant } from 'app/shared/model/factcheck/claimant.model';

describe('Component Tests', () => {
  describe('Claimant Management Update Component', () => {
    let comp: ClaimantUpdateComponent;
    let fixture: ComponentFixture<ClaimantUpdateComponent>;
    let service: ClaimantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [ClaimantUpdateComponent]
      })
        .overrideTemplate(ClaimantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClaimantUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClaimantService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new Claimant('123');
          spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
          comp.claimant = entity;
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
          const entity = new Claimant();
          spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
          comp.claimant = entity;
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
