import { Component, ViewChild, OnInit, Output, EventEmitter, ElementRef } from '@angular/core';
import { DataService } from "@core/services/data.service";
import {Data} from "@angular/router";
import {ActivatedRoute} from "@angular/router";
import { MessageService } from "@core/services/message.service";
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import {Subscription} from "rxjs";
import '@cds/core/icon/register.js';
import '@cds/core/button/register.js';

@Component({
  selector: 'app-folders',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {
  public documents = null;
  public files = null;
  private valueChangeSubscription:Subscription;
  showDocument: boolean = false;
  showViewer: boolean = false;
  showAddNewDocumentModal: boolean = false;
  showAuditsModalFlag: boolean = false;
  formHasChanged: boolean = false;
  form = new FormGroup({
    "name": new FormControl("", Validators.required),
    "documentType": new FormControl("", Validators.required),
    "company": new FormControl("", Validators.required),
    "date": new FormControl(Date.now(), Validators.required),
    "contact": new FormControl("", Validators.required),
    "status": new FormControl("", Validators.required),
    "amount": new FormControl("", Validators.required),
    "number": new FormControl("", Validators.required),
  });
  file: File = null;
  currentFolderId = null;
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

  selectedDocument;
  auditsToShow;
  versionsToShow;
  currentDocument;
  constructor(private dataService: DataService, private messageService: MessageService, private router: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.router.params.subscribe(params => {
      this.getDocuments(params['id']);
      this.currentFolderId = params['id'];
    });
  }


  getDocuments(id: number): void{
    this.router.url.subscribe(segments => {
      if (segments[0].path === 'list') {
        this.dataService.getDocumentsFromList(id).subscribe({
          next: data => {
            this.documents = data;
          },
          error: err => {
            console.log(err);
          }
        });
      } else {
        this.dataService.getDocuments(id).subscribe({
          next: data => {
            this.documents = data;
          },
          error: err => {
            console.log(err);
          }
        });
      }
    });
  }

  getFiles(document){
    this.showViewer = false;
    this.currentDocument = document;
    this.dataService.getFiles(document.id).subscribe({
      next: data => {
        this.files = data;
        this.showViewer = true;
        this.messageService.changeMessage({
          files: data,
          document: document
        });
      },
      error: err => {
        console.log(err);
      }
    });
  }

  changeFile(fileId){
    this.messageService.changeFileId(fileId);
  }


  showModal(document){
    if(this.valueChangeSubscription)
      this.valueChangeSubscription.unsubscribe();
    this.formHasChanged = false;
    this.showDocument = true;
    this.selectedDocument  = document.id;
    this.documentForm.patchValue(document.userFields);

    this.valueChangeSubscription = this.documentForm.valueChanges.subscribe(x => {
      this.formHasChanged = true;
    })
    this.getVersionsOfDocument(document.id);
    this.getFiles(document);

  }

  openDocumentVersion(version){
    this.getVersionsOfDocument(version.id);
    this.getFiles(version);
  }


  onFileChange(event) {
    this.file = event.target.files[0];
  }

  uploadFileToDocument(documentId = this.currentDocument.id) {
    this.dataService.uploadFile(this.file, documentId).subscribe(
        (event: any) => {
          this.dataService.getFiles(documentId).subscribe({
            next: data => {
              this.files = data;
            },
            error: err => {
              console.log(err);
            }
          });
        }
    );
  }

  onSubmit(){
    this.dataService.createDocument(this.form.getRawValue(), this.currentFolderId).subscribe({
      next: data => {
        this.selectedDocument = data.id;
        this.uploadFileToDocument(data.id);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  updateFields(){
    this.dataService.updateFields(this.selectedDocument, this.documentForm.getRawValue()).subscribe(
        (event: any) => {
            this.formHasChanged = false;
        }
    );
  }

  showAuditsModal(document){
    this.showAuditsModalFlag = true;
    this.dataService.getAudits(document.id).subscribe({
      next: data => {
        this.auditsToShow = data;
      },
      error: err => {
        console.log(err);
      }
    });
  }

  getVersionsOfDocument(documentId = this.selectedDocument){
    this.dataService.getVersionsOfDocument(documentId).subscribe({
      next: data => {
        console.log(data);
        this.versionsToShow = data;
      },
      error: err => {
        console.log(err);
      }
    });
  }

  parseDateToString(date){
    return new Date(date).toLocaleDateString();
  }

  createNewVersion(){
    this.dataService.createVersion(this.selectedDocument).subscribe({
      next: data => {
        this.versionsToShow.unshift(data);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  isCurrentDocumentVersion(){
    if(this.currentDocument){
      return this.currentDocument.id === this.currentDocument.currentDocumentId;
    }
    else
      return false;
  }

}
