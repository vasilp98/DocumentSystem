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
  constructor(private dataService: DataService, private messageService: MessageService, private router: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.router.params.subscribe(params => {
      this.getDocuments(params['id']);
      this.currentFolderId = params['id'];
    });
  }


  getDocuments(id: number): void{
      this.dataService.getDocuments(id).subscribe({
        next: data => {
          this.documents = data;
        },
        error: err => {
          console.log(err);
        }
      });
  }

  getFiles(document){
    this.dataService.getFiles(document.id).subscribe({
      next: data => {
        this.files = data;
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
    this.getFiles(document);
  }


  onFileChange(event) {
    this.file = event.target.files[0];
  }

  uploadFileToDocument(documentId = this.selectedDocument) {
    this.dataService.uploadFile(this.file, documentId).subscribe(
        (event: any) => {
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

  parseDateToString(date){
    return new Date(date).toLocaleDateString();
  }

  createNewVersion(){
    console.log('s')
  }

}
