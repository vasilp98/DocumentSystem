import { Component, OnInit } from '@angular/core';
import { DataService } from "@core/services/data.service";
import {Data} from "@angular/router";
import { Router } from "@angular/router";

@Component({
  selector: 'app-folders',
  templateUrl: './folders.component.html',
  styleUrls: ['./folders.component.scss']
})
export class FoldersComponent implements OnInit {
  folders = null;
  showAddNewFolderModal: boolean = false;
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
}
