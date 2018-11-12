/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FactcheckTestModule } from '../../../../test.module';
import { RatingDeleteDialogComponent } from 'app/entities/factcheck/rating/rating-delete-dialog.component';
import { RatingService } from 'app/entities/factcheck/rating/rating.service';

describe('Component Tests', () => {
  describe('Rating Management Delete Component', () => {
    let comp: RatingDeleteDialogComponent;
    let fixture: ComponentFixture<RatingDeleteDialogComponent>;
    let service: RatingService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [RatingDeleteDialogComponent]
      })
        .overrideTemplate(RatingDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RatingDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RatingService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('123');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('123');
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
