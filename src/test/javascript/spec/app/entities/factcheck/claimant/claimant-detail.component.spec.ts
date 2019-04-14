/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FactcheckTestModule } from '../../../../test.module';
import { ClaimantDetailComponent } from 'app/entities/factcheck/claimant/claimant-detail.component';
import { Claimant } from 'app/shared/model/factcheck/claimant.model';

describe('Component Tests', () => {
  describe('Claimant Management Detail Component', () => {
    let comp: ClaimantDetailComponent;
    let fixture: ComponentFixture<ClaimantDetailComponent>;
    const route = ({ data: of({ claimant: new Claimant('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FactcheckTestModule],
        declarations: [ClaimantDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ClaimantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClaimantDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.claimant).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
