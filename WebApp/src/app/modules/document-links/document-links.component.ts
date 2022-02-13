import { Component, OnInit } from '@angular/core';
import {DataService} from '@core/services/data.service';
import {ActivatedRoute, Router} from '@angular/router';
import {error} from 'protractor';
import {MessageService} from '@core/services/message.service';

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

  constructor(private dataService: DataService, private route: ActivatedRoute, private messageService: MessageService) { }

  ngOnInit(): void {

  }

  openDocumentFromLink(): void {
    this.route.params.subscribe(params => {
      const token = params['token'];
      this.dataService.getDocumentIdFromLink(token, this.password).subscribe({
        next: data => {
          console.log(data);
          this.getFiles(data);
        },
        error: err => {
          console.log(err);
        }
      });
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

}
