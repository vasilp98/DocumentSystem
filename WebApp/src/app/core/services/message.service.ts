import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, ReplaySubject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class MessageService {

    private messageSource = new BehaviorSubject(<any>{});
    currentMessage = this.messageSource.asObservable();

    private fileIdSource = new BehaviorSubject(-1);
    currentFile = this.fileIdSource.asObservable();

    private alertSource = new BehaviorSubject({show: false, message: '', type: ''});
    currentAlert = this.alertSource.asObservable();

    private listSource = new ReplaySubject();
    list = this.listSource.asObservable();

    constructor() { }

    changeMessage(message: any) {
        this.messageSource.next(message)
    }

    changeFileId(message: number) {
        this.fileIdSource.next(message)
    }

    changeAlert(message: any){
        this.alertSource.next(message);
    }

    pushList(message: any) {
        this.listSource.next(message);
    }
}
