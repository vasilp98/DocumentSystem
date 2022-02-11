import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

// Clarity integration
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { HttpClientModule } from "@angular/common/http";

import { authInterceptorProviders } from "./core/interceptors/auth.interceptor";
import { HomeComponent } from "./modules/home/home.component";
import { LoginComponent } from "./modules/login/login.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ClarityModule } from "@clr/angular";
import { FoldersComponent } from './modules/folders/folders.component';
import { HeaderComponent } from './core/header/header.component';
import { FooterComponent } from './core/footer/footer.component';
import { PermissionsComponent } from './modules/permissions/permissions.component';
@NgModule({
  declarations: [AppComponent, LoginComponent, HomeComponent, FoldersComponent, HeaderComponent, FooterComponent, PermissionsComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ClarityModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent],
})
export class AppModule {}
