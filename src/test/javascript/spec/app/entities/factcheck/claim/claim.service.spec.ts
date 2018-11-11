/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { ClaimService } from 'app/entities/factcheck/claim/claim.service';
import { IClaim, Claim } from 'app/shared/model/factcheck/claim.model';

describe('Service Tests', () => {
  describe('Claim Service', () => {
    let injector: TestBed;
    let service: ClaimService;
    let httpMock: HttpTestingController;
    let elemDefault: IClaim;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      injector = getTestBed();
      service = injector.get(ClaimService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Claim('ID', 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', async () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            claimDate: currentDate.format(DATE_FORMAT),
            checkedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find('123')
          .pipe(take(1))
          .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(JSON.stringify(returnedFromService));
      });

      it('should create a Claim', async () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            claimDate: currentDate.format(DATE_FORMAT),
            checkedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            claimDate: currentDate,
            checkedDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Claim(null))
          .pipe(take(1))
          .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(JSON.stringify(returnedFromService));
      });

      it('should update a Claim', async () => {
        const returnedFromService = Object.assign(
          {
            claim: 'BBBBBB',
            description: 'BBBBBB',
            claimDate: currentDate.format(DATE_FORMAT),
            claimSource: 'BBBBBB',
            checkedDate: currentDate.format(DATE_FORMAT),
            reviewSources: 'BBBBBB',
            review: 'BBBBBB',
            reviewTagLine: 'BBBBBB',
            clientId: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            claimDate: currentDate,
            checkedDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(JSON.stringify(returnedFromService));
      });

      it('should return a list of Claim', async () => {
        const returnedFromService = Object.assign(
          {
            claim: 'BBBBBB',
            description: 'BBBBBB',
            claimDate: currentDate.format(DATE_FORMAT),
            claimSource: 'BBBBBB',
            checkedDate: currentDate.format(DATE_FORMAT),
            reviewSources: 'BBBBBB',
            review: 'BBBBBB',
            reviewTagLine: 'BBBBBB',
            clientId: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            claimDate: currentDate,
            checkedDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => expect(body).toContainEqual(expected));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(JSON.stringify([returnedFromService]));
        httpMock.verify();
      });

      it('should delete a Claim', async () => {
        const rxPromise = service.delete('123').subscribe(resp => expect(resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
