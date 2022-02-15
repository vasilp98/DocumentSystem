import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from "../../core/services/token-storage.service";
import {DataService} from '@core/services/data.service';
import {NavigationStart, Router} from '@angular/router';
import {MessageService} from "@core/services/message.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  private roles: string[] = [];
  isLoggedIn = false;
  username?: string;
  lists = null;
  alert = {show: false, message: '', type: ''};
  currentRoute = '';

  constructor(private messageService: MessageService, private tokenStorageService: TokenStorageService, private dataService: DataService, private router: Router) { }

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if(event instanceof NavigationStart) {
          this.currentRoute = event.url;
      }
    });


    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.username = user.sub;
    }

    this.messageService.currentAlert.subscribe(message => {
      this.alert = message;
    });

    this.messageService.list.subscribe(message => {
      if (this.lists == null)
        this.getLists();

      this.lists.push(message);
    });
  }

  closeAlert(){
    this.messageService.changeAlert({show: false, type: '', message:''})
  }

  signOut(){
    this.tokenStorageService.signOut();
    location.reload();
  }

  isAdmin (): boolean {
    return this.username == 'admin';
}

  getLists() {
    if (this.lists == null) {
      this.dataService.getLists().subscribe({
        next: data => {
          this.lists = data;
        },
        error: err => {
          console.log(err);
        }
      });
    }
  }

  openList(id) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };

    this.router.navigate(['/lists/' + id]);
  }

  addList() {
    this.router.navigate(['lists']);
  }

  openUsers() {
    this.router.navigate(['users']);
  }

  openPermissions() {
    this.router.navigate(['permissions']);
  }

  openFolder() {
    this.router.navigate(['folders']);
  }
}
