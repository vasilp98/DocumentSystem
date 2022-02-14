import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./modules/home/home.component";
import { LoginComponent} from "./modules/login/login.component";
import { FoldersComponent} from "./modules/folders/folders.component";
import { DocumentsComponent} from "./modules/documents/documents.component";
import { TestComponent} from "./modules/test/test.component";
import {DocumentLinksComponent} from './modules/document-links/document-links.component';
import {ListComponent} from './modules/list/list.component';
import {PermissionsComponent} from './modules/permissions/permissions.component';
import {UsersComponent} from './modules/users/users.component';
import {AuthGuardService as AuthGuard} from "@core/services/auth.guard";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'folders', component: FoldersComponent, canActivate:[AuthGuard] },
  { path: 'folders/:id/documents', component: DocumentsComponent, canActivate:[AuthGuard] },
  { path: 'lists/:id', component: DocumentsComponent, canActivate:[AuthGuard] },
  { path: 'lists', component: ListComponent, canActivate:[AuthGuard] },
  { path: 'links/:token', component: DocumentLinksComponent },
  { path: 'test', component: TestComponent, canActivate:[AuthGuard] },
  { path: 'permissions', component: PermissionsComponent, canActivate:[AuthGuard] },
  { path: 'users', component: UsersComponent, canActivate:[AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
