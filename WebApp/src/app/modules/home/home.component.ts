import {Component, OnInit} from '@angular/core';
import {DataService} from "../../core/services/data.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    content: string = '';
    hideAlert: boolean = true;
    alertType: string;
    constructor(private dataService: DataService) {
    }

    ngOnInit(): void {

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
