import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = '/api/';

@Injectable({
    providedIn: 'root'
})
export class DataService {
    constructor(private http: HttpClient) { }

    getTestText(): Observable<any> {
        return this.http.get(API_URL + 'test', { responseType: 'text' });
    }
}