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
  showLinksModalFlag: boolean = false;
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
  commentsToShow;
  currentDocument;
  linksDateModel;
  commentValueModel;
  linksPasswordModel;
  originalDocument;
  initalDocument;
  constructor(private dataService: DataService, private messageService: MessageService, private router: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.router.params.subscribe(params => {
      this.initalDocument = params['id'];
      this.getDocuments(params['id']);
      this.currentFolderId = params['id'];
    });
  }


  getDocuments(id: number): void{
    this.router.url.subscribe(segments => {
      if (segments[0].path === 'lists') {
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
    this.documentForm.patchValue(document.userFields);
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
    this.originalDocument = document;

    this.valueChangeSubscription = this.documentForm.valueChanges.subscribe(x => {
      this.formHasChanged = true;
    })
    this.openDocument(document)

  }

  openDocument(document){
    this.getVersionsOfDocument(document.id);
    this.getCommentsOfDocument(document.id);
    this.getFiles(document);
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
    if(this.file){
      this.form.controls['date'].setValue(new Date(this.form.controls['date'].value).toISOString().split('T')[0]);
      this.dataService.createDocument(this.form.getRawValue(), this.currentFolderId).subscribe({
        next: data => {
          this.selectedDocument = data.id;
          this.uploadFileToDocument(data.id);
          this.messageService.changeAlert({
            show: true,
            message: 'Document successfully created',
            type: 'success'
          });
          this.showAddNewDocumentModal = false;
          this.getDocuments(this.initalDocument);
        },
        error: err => {
          console.log(err);
        }
      });
    }else{
      this.messageService.changeAlert({
        show: true,
        message: 'No file selected',
        type: 'danger'
      });
    }



  }

  removeDocument(document){
    this.dataService.deleteDocument(document).subscribe({
      next: data => {
        this.getDocuments(this.initalDocument);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  updateFields(){
    this.dataService.updateFields(this.currentDocument.id, this.documentForm.getRawValue()).subscribe(
        (data: any) => {
            this.formHasChanged = false;
            const index = this.documents.indexOf(this.currentDocument);
            if (index !== -1) {
                this.documents[index] = data;
            }
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

  getCommentsOfDocument(documentId = this.currentDocument.id){
    this.dataService.getCommentsOfDocument(documentId).subscribe({
      next: data => {
        this.commentsToShow = data;
        this.commentValueModel = '';
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

  showLinksModal(document){
    this.showLinksModalFlag = true;
    this.currentDocument = document;
  }

  postComment(){
    this.dataService.createComment(this.currentDocument.id, this.commentValueModel).subscribe({
      next: data => {
        this.getCommentsOfDocument(this.currentDocument.id);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  createLink(){
    let selectedDateObject = new Date(this.linksDateModel);
    let payload = {
      documentId: this.currentDocument.id,
      validUntil: selectedDateObject.toISOString().split('T')[0],
      password: this.linksPasswordModel
    };

    this.dataService.createLink(payload).subscribe({
      next: data => {
        console.log(data);
        this.messageService.changeAlert({
          show: true,
          message: `${window.location.host}/links/${data.token}`,
          type: 'info'
        })
      },
      error: err => {
        console.log(err);
      }
    });
  }

}
