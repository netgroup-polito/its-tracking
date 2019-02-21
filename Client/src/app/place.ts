export class Place {
  id: string = undefined;
  name: string = undefined;
  capacity: number = undefined;
  connectedPlaceId: string[] = undefined;
  avgTimeSpent: number = undefined;
  // to define layout: -1 not visited, 0 current place, 1 visited 
  visited: number = undefined;
  nextPlace: Place = undefined;

  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[], avgTimeSpent: number) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.connectedPlaceId = connectedPlaceId;
    this.visited = -1;
    this.avgTimeSpent = avgTimeSpent;
  }
}
