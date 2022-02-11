import { Component, OnInit } from "@angular/core";
import { FormArray, FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Folder } from "@core/models/folder.model";
import { DataService } from "@core/services/data.service";
import { Observable } from "rxjs";

@Component({
  selector: "app-permissions",
  templateUrl: "./permissions.component.html",
  styleUrls: ["./permissions.component.scss"],
})
export class PermissionsComponent implements OnInit {
  folder?: Folder;
  form: FormArray = this.fb.array([]);

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.getFolder(+params!.get("folderId")!);
    });
  }

  private getFolder(folderId: number) {
    this.dataService.getFolder(folderId).subscribe((data) => {
      this.folder = data;
      this.initForm();
    });
  }

  private initForm() {
    // load all existing permissions for a folder (make this readonly maybe)
    this.folder!.permissions.forEach((p) => {
      this.form.push(
        this.fb.group({
          area: [p.area],
          filter: [p.filter.value],
          filterValue: [p.filter.value],
          users: [p.users],
        })
      );
    });
  }

  addPermissionRule() {
    this.form.push(
      this.fb.group({
        area: [],
        filter: [],
        filterValue: [],
        users: [],
      })
    );
    console.log(this.form)
  }

  submitPermissions() {}
}
