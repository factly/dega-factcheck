/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { FactcheckService } from 'app/entities/factcheck/factcheck/factcheck.service';
import { IFactcheck, Factcheck } from 'app/shared/model/factcheck/factcheck.model';

describe('Service Tests', () => {
  describe('Factcheck Service', () => {
    let injector: TestBed;
    let service: FactcheckService;
    let httpMock: HttpTestingController;
    let elemDefault: IFactcheck;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      injector = getTestBed();
      service = injector.get(FactcheckService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Factcheck(
        'ID',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        false,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate
      );
    });

    describe('Service methods', async () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            publishedDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdatedDate: currentDate.format(DATE_TIME_FORMAT),
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

      it('should create a Factcheck', async () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            publishedDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdatedDate: currentDate.format(DATE_TIME_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            publishedDate: currentDate,
            lastUpdatedDate: currentDate,
            createdDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Factcheck(null))
          .pipe(take(1))
          .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(JSON.stringify(returnedFromService));
      });

      it('should update a Factcheck', async () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            clientId: 'BBBBBB',
            introduction: 'BBBBBB',
            summary: 'BBBBBB',
            excerpt: 'BBBBBB',
            publishedDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdatedDate: currentDate.format(DATE_TIME_FORMAT),
            featured: true,
            sticky: true,
            updates: 'BBBBBB',
            slug: 'BBBBBB',
            password: 'BBBBBB',
            featuredMedia: 'BBBBBB',
            subTitle: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            publishedDate: currentDate,
            lastUpdatedDate: currentDate,
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

      it('should return a list of Factcheck', async () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            clientId: 'BBBBBB',
            introduction: 'BBBBBB',
            summary: 'BBBBBB',
            excerpt: 'BBBBBB',
            publishedDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdatedDate: currentDate.format(DATE_TIME_FORMAT),
            featured: true,
            sticky: true,
            updates: 'BBBBBB',
            slug: 'BBBBBB',
            password: 'BBBBBB',
            featuredMedia: 'BBBBBB',
            subTitle: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            publishedDate: currentDate,
            lastUpdatedDate: currentDate,
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

      it('should delete a Factcheck', async () => {
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
