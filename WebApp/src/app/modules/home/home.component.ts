import {Component, OnInit} from '@angular/core';
import {DataService} from "../../core/services/data.service";
import {TokenStorageService} from '@core/services/token-storage.service';
import {Router} from "@angular/router";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    content: string = '';
    hideAlert: boolean = true;
    alertType: string;
    constructor(private router: Router, private tokenStorage: TokenStorageService, private dataService: DataService) {
    }

    ngOnInit(): void {
        if (this.tokenStorage.getToken()) {
            this.router.navigate(['/folders']);
        }
        else{
            this.router.navigate(['/login']);
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
}
