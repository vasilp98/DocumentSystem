import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Folder } from "@core/models/folder.model";

const API_URL = "http://localhost:8080/api/";

@Injectable({
  providedIn: "root",
})
export class DataService {
  constructor(private http: HttpClient) {}

  getTestText(): Observable<any> {
    return this.http.get(API_URL + "test", { responseType: "text" });
  }

  getUsers() {
    return this.http.get(`${API_URL}user`);
  }

  getFolder(id: number): Observable<Folder> {
    return this.http.get<Folder>(`${API_URL}folder/${id}`);
  }
}
