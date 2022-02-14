import { Component, OnInit } from '@angular/core';
import {TokenStorageService} from "../../core/services/token-storage.service";
import {DataService} from '@core/services/data.service';
import {Router} from '@angular/router';

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

  constructor(private tokenStorageService: TokenStorageService, private dataService: DataService, private router: Router) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.username = user.sub;
    }
  }

  signOut(){
    this.tokenStorageService.signOut();
    location.reload();
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
}
