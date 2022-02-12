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

    getFolders(): Observable<any>{
        return this.http.get( API_URL + 'folder', {responseType: 'json'});
    }

    getDocuments(id: number): Observable<any>{
        return this.http.get( API_URL + `folder/${id}/documents`, {responseType: 'json'});
    }
    getFile(documentId: number, fileId: number): Observable<any>{
        return this.http.get( API_URL + `document/${documentId}/files/${fileId}`, {responseType: 'blob'});
    }

}