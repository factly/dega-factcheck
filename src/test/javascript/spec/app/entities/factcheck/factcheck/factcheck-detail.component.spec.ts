/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FactcheckTestModule } from '../../../../test.module';
import { FactcheckDetailComponent } from 'app/entities/factcheck/factcheck/factcheck-detail.component';
import { Factcheck } from 'app/shared/model/factcheck/factcheck.model';

describe('Component Tests', () => {
  describe('Factcheck Management Detail Component', () => {
    let comp: FactcheckDetailComponent;
    let fixture: ComponentFixture<FactcheckDetailComponent>;
    const route = ({ data: of({ factcheck: new Factcheck('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [FactcheckDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FactcheckDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FactcheckDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.factcheck).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
