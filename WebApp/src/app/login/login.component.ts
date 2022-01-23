import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../core/services/authentication.service';
import {TokenStorageService} from '../core/services/token-storage.service';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import jwt_decode from "jwt-decode";
import {Router} from "@angular/router";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    loginForm = new FormGroup({
        username: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required),
    });
    isLoggedIn = false;
    isLoginFailed = false;
    errorMessage = '';
    roles: string[] = [];

    constructor(private router: Router, private fb:FormBuilder, private authService: AuthenticationService, private tokenStorage: TokenStorageService) {
    }

    ngOnInit(): void {
        if (this.tokenStorage.getToken()) {
            console.log(this.tokenStorage.getUser())
            console.log(this.tokenStorage.getToken())
            this.isLoggedIn = true;
            this.roles = this.tokenStorage.getUser().roles;
        }
    }

    onSubmit(): void {

        const val = this.loginForm.value;

        this.authService.login(val.username, val.password).subscribe({
            next: data => {
                let decodedJWT = jwt_decode(data.jwt);
                this.tokenStorage.saveToken(data.jwt);
                this.tokenStorage.saveUser(decodedJWT);

                this.isLoginFailed = false;
                this.isLoggedIn = true;
                this.roles = this.tokenStorage.getUser().roles;
                this.router.navigate(['/']);

            },
            error: err => {
                this.isLoginFailed = true;
            }
        });
    }
}
