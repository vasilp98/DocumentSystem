import { Component, OnInit } from '@angular/core';
import { DataService } from "@core/services/data.service";
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
    "folderName": new FormControl("", Validators.required),
    "location": new FormControl("", Validators.required),
  });

  constructor(private dataService: DataService, private route: Router) { }

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
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
