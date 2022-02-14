import { Component, OnInit } from '@angular/core';
import {DataService} from '@core/services/data.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  users = null;
  showAddNewUserModal: boolean = false;
  form = new FormGroup({
    "username": new FormControl(null, Validators.required),
    "password": new FormControl(null, Validators.required),
    "email": new FormControl(null,[Validators.required, Validators.email]),
  });
  constructor(private dataService: DataService) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void{
    this.dataService.getUsers().subscribe({
      next: data => {
        this.users = data;
      },
      error: err => {
        console.log(err);
      }
    });
  }

  onSubmit(){
    this.dataService.createUser(this.form.getRawValue()).subscribe({
      next: data => {
        this.users.push(data);
        this.showAddNewUserModal = false;
      },
      error: err => {
        console.log(err);
      }
    });
  }

}
