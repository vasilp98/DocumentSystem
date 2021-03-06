import { Component, OnInit } from '@angular/core';
import { DataService } from "@core/services/data.service";
import { MessageService } from "@core/services/message.service";
import { Router } from "@angular/router";
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-folders',
  templateUrl: './folders.component.html',
  styleUrls: ['./folders.component.scss']
})
export class FoldersComponent implements OnInit {
  folders = null;
  showAddNewFolderModal: boolean = false;
  form = new FormGroup({
    "folderName": new FormControl(null, Validators.required),
    "location": new FormControl(null, Validators.required),
  });

  constructor(private messageService: MessageService,private dataService: DataService, private route: Router) { }

  ngOnInit(): void {
    this.getFolders();
  }

  openFolder(id){
    this.route.navigate([`folders/${id}/documents`]);
  }


  getFolders(): void{
      this.dataService.getFolders().subscribe({
        next: data => {
          this.folders = data;
        },
        error: err => {
          console.log(err);
        }
      });
  }

  onSubmit(){
    this.dataService.createFolder(this.form.controls['folderName'].value, this.form.controls['location'].value).subscribe({
      next: data => {
        this.messageService.changeAlert({
          show: true,
          message: 'Folder successfully created',
          type: 'success'
        });
        this.getFolders();
        this.showAddNewFolderModal = false;
      },
      error: err => {
        console.log(err);
      }
    });
  }

  deleteFolder(folder){
    this.dataService.deleteFolder(folder).subscribe({
      next: data => {
        this.folders = this.folders.filter(f => f.id !== data);
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
