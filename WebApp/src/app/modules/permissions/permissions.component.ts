import { Component, OnInit } from "@angular/core";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute } from "@angular/router";
import { DataService } from "@core/services/data.service";

@Component({
  selector: "app-permissions",
  templateUrl: "./permissions.component.html",
  styleUrls: ["./permissions.component.scss"],
})
export class PermissionsComponent implements OnInit {
    permissions = null;
    showAddNewPermissionModal: boolean = false;
    showUsersModal: boolean = false;
    folders;
    users;
    fields = new Map([["Name", "name"],
        ["Document type", "documentType"],
        ["Company", "company"],
        ["Date", "date"],
        ["Contact", "contact"],
        ["Status", "status"],
        ["Amount", "amount"],
        ["Number", "number"]]);

    operations = new Map([["Equal", "EQUAL"],
        ["Not equal", "NOT_EQUAL"],
        ["Contains", "CONTAINS"],
        ["After", "AFTER"],
        ["Before", "BEFORE"]]);

    form = new FormGroup({
        "name": new FormControl("", Validators.required),
        "folder": new FormControl("", Validators.required),
        "area": new FormControl("FOLDER", Validators.required),
        "option-1": new FormControl(false,Validators.required),
        "option-2": new FormControl(false,Validators.required),
        "option-3": new FormControl(false,Validators.required),
        "field": new FormControl(null, Validators.required),
        "operation": new FormControl(null, Validators.required),
        "value": new FormControl(null, Validators.required),
    });

    constructor(private dataService: DataService) {}

    ngOnInit(): void {
        this.getPermissions();
        this.getUsers();
    }

    getPermissions(): void{
        this.dataService.getPermissions().subscribe({
            next: data => {
                this.permissions = data;
            },
            error: err => {
                console.log(err);
            }
        });
    }

    getUsers(): void{
        this.dataService.getUsers().subscribe({
            next: data => {
                this.users = data;
            },
            error: err => {
                console.log(err);
            }
        });
    }

    getUserName(userId: number): string {
        const user = this.users.filter(u => u.id === userId)[0];
        return user.username;
    }

    loadFolders() {
        this.dataService.getFolders().subscribe({
            next: data => {
                this.folders = data;
                this.form.controls['folder'].setValue(data[0]);
            },
            error: err => {
                console.log(err);
            }
        });
    }

    savePermissionUsers(permissionId: number, userIds: []) {
        this.dataService.updatePermissionUsers(permissionId, userIds).subscribe({
            next: data => {
                this.folders = data;
                this.form.controls['folder'].setValue(data[0]);
            },
            error: err => {
                console.log(err);
            }
        });
    }

    onSubmit(){
        let payload = this.form.getRawValue();

        payload.filter = null;

        if (this.form.controls['field'].value != null || this.form.controls['operation'].value != null || this.form.controls['value'].value != null) {
            payload.filter = {
                field: this.fields.get(this.form.controls['field'].value),
                operation: this.operations.get(this.form.controls['operation'].value),
                value: this.form.controls['value'].value,
            };
        }

        payload.permissions = [];
        if (this.form.controls['option-1'].value)
            payload.permissions.push('READ');

        if (this.form.controls['option-2'].value)
            payload.permissions.push('WRITE');

        if (this.form.controls['option-3'].value)
            payload.permissions.push('DELETE');

        payload.folderId = this.form.controls['folder'].value.id;

        payload.area = payload.area.toUpperCase();

        this.dataService.createPermission(payload).subscribe({
            next: data => {
                data.folderName = this.form.controls['folder'].value.name;
                this.permissions.push(data);
                this.showAddNewPermissionModal = false;
            },
            error: err => {
                console.log(err);
            }
        });
    }
}
