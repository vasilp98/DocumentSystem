export interface Permission {
  id: number;
  users: any[];
  folderId: number;
  area: string;
  filter: { field: string; operation: string; value: string };
  permissions: string[];
}
