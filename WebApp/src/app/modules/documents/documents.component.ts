import { Component, ViewChild, OnInit, Output, EventEmitter, ElementRef } from '@angular/core';
import { DataService } from "@core/services/data.service";
import {Data} from "@angular/router";
import {ActivatedRoute} from "@angular/router";
import { MessageService } from "@core/services/message.service";

@Component({
  selector: 'app-folders',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {
  public documents = null;
  public files = null;
  selectedFile;
  showDocument: boolean = false;
  showAddNewDocumentModal: boolean = false;

  selectedDocument;
  constructor(private dataService: DataService, private messageService: MessageService, private router: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.router.params.subscribe(params => {
      this.getDocuments(params['id']);
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
}
