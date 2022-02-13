import { Component, OnInit } from '@angular/core';
import {DataService} from '@core/services/data.service';
import {ActivatedRoute, Router} from '@angular/router';
import {error} from 'protractor';
import {MessageService} from '@core/services/message.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-document-links',
  templateUrl: './document-links.component.html',
  styleUrls: ['./document-links.component.scss']
})
export class DocumentLinksComponent implements OnInit {
  public password;
  public files = null;
  file: File = null;
  showDocument: boolean = false;
  currentDocument;
  versionsToShow;
  isOnVersioned: boolean = false;
  originalDocument;
  documentForm = new FormGroup({
    "name": new FormControl("", Validators.required),
    "documentType": new FormControl("", Validators.required),
    "company": new FormControl("", Validators.required),
    "date": new FormControl("", Validators.required),
    "contact": new FormControl("", Validators.required),
    "status": new FormControl("", Validators.required),
    "amount": new FormControl("", Validators.required),
    "number": new FormControl("", Validators.required),
  });

  constructor(private dataService: DataService, private route: ActivatedRoute, private messageService: MessageService) { }

  ngOnInit(): void {

  }

  openDocumentFromLink(): void {
    this.route.params.subscribe(params => {
      const token = params['token'];
      this.dataService.getDocumentFromLink(token, this.password).subscribe({
        next: data => {
          this.originalDocument = data;
          this.openDocument(data);
        },
        error: err => {
          console.log(err);
        }
      });
    });
  }

  getFiles(document){
    this.currentDocument = document;
    this.dataService.getFiles(document.id).subscribe({
      next: data => {
        this.files = data;
        this.messageService.changeMessage({
          document: document,
          files: data
        });
        this.showDocument = true;
      },
      error: err => {
        console.log(err);
      }
    });
  }

  changeFile(fileId){
    this.messageService.changeFileId(fileId);
  }

  onFileChange(event) {
    this.file = event.target.files[0];
  }

  isCurrentDocumentVersion(){
    if(this.currentDocument){
      return this.currentDocument.id === this.currentDocument.currentDocumentId;
    }
    else
      return false;
  }

  openDocument(document, isVersioned = false){
    this.showDocument = false;
    this.isOnVersioned = isVersioned;
    this.getVersionsOfDocument(document.id);
    this.getFiles(document);
  }

  getVersionsOfDocument(documentId = this.currentDocument.id){
    this.dataService.getVersionsOfDocument(documentId).subscribe({
      next: data => {
        this.versionsToShow = data;
      },
      error: err => {
        console.log(err);
      }
    });
  }



}
