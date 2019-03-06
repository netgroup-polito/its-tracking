export class DangerousMaterial {
  id: string;
  incompatibleMaterial: string[];

  constructor(id: string, incompatibleMaterial: string[]) {
    this.id = id;
    this.incompatibleMaterial = incompatibleMaterial;
  }
}
