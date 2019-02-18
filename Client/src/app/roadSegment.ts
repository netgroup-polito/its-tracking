export class RoadSegment {
  id: string = undefined;
  name: string = undefined;
  capacity: number = undefined;
  avgTimeSpent: number = undefined;
  connectedPlaceId: string[] = undefined;
  containerPlaceId: string = undefined;

  constructor(id: string, name: string, capacity: number, avgTimeSpent: number, connectedPlaceId: string[], containerPlaceId: string) {
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.avgTimeSpent = avgTimeSpent;
    this.connectedPlaceId = connectedPlaceId;
    this.containerPlaceId = containerPlaceId;
  }
}
