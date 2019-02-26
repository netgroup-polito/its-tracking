import {Place} from './place';

export class ParkingArea extends Place {
  connectedPlaceId: string[] = undefined;
  service: string[] = undefined;

  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[], avgTimeSpent: number, service?: string[]) {
    super(id, name, capacity, connectedPlaceId, avgTimeSpent);
    this.connectedPlaceId = connectedPlaceId;
    this.service = service;
  }
}
