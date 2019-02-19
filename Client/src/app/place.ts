export class Place {
  id: string = undefined;
  name: string = undefined;
  capacity: number = undefined;
  connectedPlaceId: string[] = undefined;

  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[]) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.connectedPlaceId = connectedPlaceId;
  }
}
