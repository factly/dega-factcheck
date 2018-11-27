/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FactcheckTestModule } from '../../../../test.module';
import { FactcheckDeleteDialogComponent } from 'app/entities/factcheck/factcheck/factcheck-delete-dialog.component';
import { FactcheckService } from 'app/entities/factcheck/factcheck/factcheck.service';

describe('Component Tests', () => {
  describe('Factcheck Management Delete Component', () => {
    let comp: FactcheckDeleteDialogComponent;
    let fixture: ComponentFixture<FactcheckDeleteDialogComponent>;
    let service: FactcheckService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [FactcheckDeleteDialogComponent]
      })
        .overrideTemplate(FactcheckDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FactcheckDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FactcheckService);
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
