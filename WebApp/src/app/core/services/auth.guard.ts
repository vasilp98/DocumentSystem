import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { TokenStorageService} from '../services/token-storage.service';

@Injectable()
export class AuthGuardService implements CanActivate {
    constructor(private router: Router, private tokenStorage: TokenStorageService) {}
    canActivate(): boolean {
        if (!this.tokenStorage.getToken()) {
            this.router.navigate(['login']);
            return false;
        }
        else{
            return true;
        }
    }
}