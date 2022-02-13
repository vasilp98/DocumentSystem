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

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'folders', component: FoldersComponent },
  { path: 'folders/:id/documents', component: DocumentsComponent },
  { path: 'list/:id', component: DocumentsComponent },
  { path: 'list', component: ListComponent },
  { path: 'link/:token', component: DocumentLinksComponent },
  { path: 'test', component: TestComponent },
  { path: 'permissions/:folderId', component: PermissionsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
