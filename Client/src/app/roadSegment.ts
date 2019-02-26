import {Place} from './place';

export class RoadSegment extends Place {
  containerPlaceId: string = undefined;


  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[], avgTimeSpent: number, containerPlaceId: string) {
    super(id, name, capacity, connectedPlaceId, avgTimeSpent);
    this.containerPlaceId = containerPlaceId;
  }
}
