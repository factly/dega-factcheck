/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RatingService } from 'app/entities/factcheck/rating/rating.service';
import { IRating, Rating } from 'app/shared/model/factcheck/rating.model';

describe('Service Tests', () => {
  describe('Rating Service', () => {
    let injector: TestBed;
    let service: RatingService;
    let httpMock: HttpTestingController;
    let elemDefault: IRating;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      injector = getTestBed();
      service = injector.get(RatingService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Rating('ID', 'AAAAAAA', 0, 'AAAAAAA', false, 'AAAAAAA', 'AAAAAAA', currentDate);
    });

    describe('Service methods', async () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Rating', async () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Rating(null))
          .pipe(take(1))
          .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(JSON.stringify(returnedFromService));
      });

      it('should update a Rating', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            numericValue: 1,
            iconURL: 'BBBBBB',
            isDefault: true,
            clientId: 'BBBBBB',
            slug: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate
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

      it('should return a list of Rating', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            numericValue: 1,
            iconURL: 'BBBBBB',
            isDefault: true,
            clientId: 'BBBBBB',
            slug: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdDate: currentDate
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

      it('should delete a Rating', async () => {
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
