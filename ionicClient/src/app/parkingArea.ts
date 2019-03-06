import {Place} from './place';

export class ParkingArea extends Place {
  avgTimeSpent: number = undefined;
  connectedPlaceId: string[] = undefined;

  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[], avgTimeSpent: number) {
    super(id, name, capacity, connectedPlaceId, avgTimeSpent);
    this.connectedPlaceId = connectedPlaceId;
  }
}
