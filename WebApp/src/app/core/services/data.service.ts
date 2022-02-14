import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpRequest} from '@angular/common/http';
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

    createFolder(name: string, location: string): Observable<any>{
        return this.http.post( API_URL + 'folder', {name: name, storageLocation: location});
    }

    deleteFolder(folder): Observable<any>{
        return this.http.delete( API_URL + `folder/${folder.id}`);
    }

    deleteDocument(document): Observable<any>{
        return this.http.delete( API_URL + `document/${document.id}`);
    }

    getDocuments(id: number): Observable<any>{
        return this.http.get( API_URL + `folder/${id}/documents`, {responseType: 'json'});
    }

    createDocument(payload, folderId): Observable<any>{
        return this.http.post( API_URL + 'document', {userFields: payload, folderId: folderId});
    }

    getFile(documentId: number, fileId: number): Observable<any>{
        return this.http.get( API_URL + `document/${documentId}/files/${fileId}`, {responseType: 'blob'});
    }

    uploadFile(file, documentId): Observable<any>{
        const formData = new FormData();
        formData.append("file", file, file.name);
        return this.http.post(API_URL + `document/${documentId}/files`, formData)

    }

    getFiles(documentId: number): Observable<any>{
        return this.http.get( API_URL + `document/${documentId}/files`, {responseType: 'json'});
    }

    getDocumentFromLink(token, password): Observable<any> {
        return this.http.post(API_URL + `link/${token}`, { password: password });
    }

    getLists(): Observable<any> {
        return this.http.get(API_URL + 'list');
    }

    getDocumentsFromList(id): Observable<any> {
        return this.http.get(API_URL + `list/${id}/documents`);
    }

    getFolder(id): Observable<any> {
        return this.http.get(API_URL + `folder/${id}`);
    }

    updateFields(documentId, payload): Observable<any>{
        return this.http.post( API_URL + `document/${documentId}/fields`, payload);
    }

    getAudits(documentId: number): Observable<any>{
        return this.http.get( API_URL + `audit/${documentId}/`, {responseType: 'json'});
    }

    getUsers(): Observable<any> {
        return this.http.get(API_URL + 'user');
    }

    createUser(data): Observable<any>{
        return this.http.post( API_URL + 'user', data);
    }

    getPermissions(): Observable<any> {
        return this.http.get(API_URL + 'permission');
    }

    createPermission(data): Observable<any>{
        return this.http.post( API_URL + 'permission', data);
    }
    
    createVersion(documentId): Observable<any>{
        return this.http.post( API_URL + `version/${documentId}`, {});
    }

    createComment(documentId, content): Observable<any>{
        return this.http.post( API_URL + `comment/${documentId}`, {content: content});
    }

    getVersionsOfDocument(documentId): Observable<any>{
        return this.http.get( API_URL + `document/${documentId}/versions`, {responseType: 'json'});
    }

    getCommentsOfDocument(documentId): Observable<any>{
        return this.http.get( API_URL + `comment/${documentId}`, {responseType: 'json'});
    }

    createLink(payload): Observable<any>{
        return this.http.post( API_URL + `link/`, payload);
    }

    updatePermissionUsers(permissionId: number, users: []): Observable<any> {
        return this.http.post(API_URL + `permission/${permissionId}/users`, users);
    }

    createList(data): Observable<any>{
        return this.http.post( API_URL + 'list', data);
    }

}
