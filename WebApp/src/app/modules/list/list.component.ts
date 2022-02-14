import { Component, OnInit } from '@angular/core';
import {DataService} from '@core/services/data.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MessageService} from '@core/services/message.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  folders;

  fields = new Map([["Name", "name"],
    ["Document type", "documentType"],
    ["Company", "company"],
    ["Date", "date"],
    ["Contact", "contact"],
    ["Status", "status"],
    ["Amount", "amount"],
    ["Number", "number"]]);

  operations = new Map([["Equal", "EQUAL"],
    ["Not equal", "NOT_EQUAL"],
    ["Contains", "CONTAINS"],
    ["After", "AFTER"],
    ["Before", "BEFORE"]]);

  form = new FormGroup({
    "name": new FormControl("", Validators.required),
    "folder": new FormControl("", Validators.required),
    "field": new FormControl("",Validators.required),
    "operation": new FormControl("",Validators.required),
    "value": new FormControl("",Validators.required),
  });


  constructor(private dataService: DataService, private messageService: MessageService) { }

  ngOnInit(): void {

  }

  loadFolders() {
    this.dataService.getFolders().subscribe({
      next: data => {
        this.folders = data;
        this.form.controls['folder'].setValue(data[0]);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  onSubmit(){
    let payload = this.form.getRawValue();

    payload.filters = [{
      field: this.fields.get(this.form.controls['field'].value),
      operation: this.operations.get(this.form.controls['operation'].value),
      value: this.form.controls['value'].value,
    }];

    payload.folderId = this.form.controls['folder'].value.id;

    this.dataService.createList(payload).subscribe({
      next: data => {
        this.form.controls['name'].setValue("");
        this.form.controls['value'].setValue("");

        this.messageService.pushList(data);
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
