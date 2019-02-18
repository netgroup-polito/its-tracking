export class Gate {
  id: string = undefined;
  name: string = undefined;
  type: string = undefined;
  capacity: number = undefined;
  connectedPlaceId: string[] = undefined;

  constructor(id: string, name: string, type: string, capacity: number, connectedPlaceId: string[]) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.capacity = capacity;
    this.connectedPlaceId = connectedPlaceId;
  }
}
