import { Component, ViewChild, OnInit, Output, EventEmitter, ElementRef } from '@angular/core';
import { DataService } from "@core/services/data.service";
import {Data} from "@angular/router";
import {ActivatedRoute} from "@angular/router";
import { MessageService } from "@core/services/message.service";
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-folders',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {
  public documents = null;
  public files = null;
  showDocument: boolean = false;
  showAddNewDocumentModal: boolean = false;
  form = new FormGroup({
    "name": new FormControl("", Validators.required),
    "documentType": new FormControl("", Validators.required),
    "company": new FormControl("", Validators.required),
    "date": new FormControl("", Validators.required),
    "contact": new FormControl("", Validators.required),
    "status": new FormControl("", Validators.required),
    "amount": new FormControl("", Validators.required),
    "number": new FormControl("", Validators.required),
  });
  file: File = null; // Variable to store file
  currentFolderId = null;

  selectedDocument;
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

  getFiles(id){
    this.dataService.getFiles(id).subscribe({
      next: data => {
        this.files = data;
        this.messageService.changeMessage({
          id: id,
          files: data
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


  showModal(id){
    this.showDocument = true;
    this.selectedDocument  = id;
    this.getFiles(id);
  }


  onFileChange(event) {
    this.file = event.target.files[0];
  }

  uploadFileToDocument(documentId = this.selectedDocument) {
    this.dataService.uploadFile(this.file, documentId).subscribe(
        (event: any) => {
          console.log(event)
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

}
