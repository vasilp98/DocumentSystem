import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../core/services/token-storage.service";
import {DataService} from "../core/services/data.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    private roles: string[] = [];
    isLoggedIn = false;
    username?: string;
    content: string = '';
    hideAlert: boolean = true;
    alertType: string;
    constructor(private tokenStorageService: TokenStorageService, private dataService: DataService) {
    }

    ngOnInit(): void {
        this.isLoggedIn = !!this.tokenStorageService.getToken();

        if (this.isLoggedIn) {
            const user = this.tokenStorageService.getUser();
            this.username = user.sub;
        }
    }

    testCall(){
        this.dataService.getTestText().subscribe({
            next: data => {
                this.content = data;
                this.alertType = 'success';
                this.hideAlert = false;
            },
            error: err => {
                this.alertType = 'danger';
                this.content = 'Action forbidden';
                this.hideAlert = false;
            }
        });
    }

    signOut(){
        this.tokenStorageService.signOut();
        location.reload();
    }

}
