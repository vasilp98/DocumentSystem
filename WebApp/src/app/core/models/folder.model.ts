import { Permission } from "./permission.model";

export interface Folder {
    id: number;
    name: string;
    created: string;
    storageLocation: string;
    customField1: string;
    customField2: string;
    customField3: string;
    customField4: string;
    customField5: string;
    permissions: Permission[];
}