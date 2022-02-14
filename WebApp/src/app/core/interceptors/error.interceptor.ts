import { Injectable } from "@angular/core";
import { MessageService } from "@core/services/message.service";
import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest
} from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { Router } from "@angular/router";

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {
    constructor(private messageService: MessageService, public router: Router) {}

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError(error => {
                this.messageService.changeAlert({
                    show: true,
                    message: error.message,
                    type: 'danger'
                });
                return throwError(error.message);
            })
        );
    }
}
