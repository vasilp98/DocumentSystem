import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./modules/home/home.component";
import { LoginComponent} from "./modules/login/login.component";
import { FoldersComponent} from "./modules/folders/folders.component";
import { DocumentsComponent} from "./modules/documents/documents.component";
import { TestComponent} from "./modules/test/test.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'folders', component: FoldersComponent },
  { path: 'folders/:id/documents', component: DocumentsComponent },
  { path: 'test', component: TestComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
