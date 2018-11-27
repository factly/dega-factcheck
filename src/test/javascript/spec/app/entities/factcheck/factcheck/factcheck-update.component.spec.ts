/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FactcheckTestModule } from '../../../../test.module';
import { FactcheckUpdateComponent } from 'app/entities/factcheck/factcheck/factcheck-update.component';
import { FactcheckService } from 'app/entities/factcheck/factcheck/factcheck.service';
import { Factcheck } from 'app/shared/model/factcheck/factcheck.model';

describe('Component Tests', () => {
  describe('Factcheck Management Update Component', () => {
    let comp: FactcheckUpdateComponent;
    let fixture: ComponentFixture<FactcheckUpdateComponent>;
    let service: FactcheckService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [FactcheckUpdateComponent]
      })
        .overrideTemplate(FactcheckUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FactcheckUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FactcheckService);
    });

    describe('save', () => {
      it(
        'Should call update service on save for existing entity',
        fakeAsync(() => {
          // GIVEN
          const entity = new Factcheck('123');
          spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
          comp.factcheck = entity;
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
          const entity = new Factcheck();
          spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
          comp.factcheck = entity;
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
