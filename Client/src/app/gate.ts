import {Place} from './place';

export class Gate extends Place {
  type: string = undefined;

  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[], type: string, avgTimeSpent: string) {
    super(id, name, capacity, connectedPlaceId, avgTimeSpent);
    this.type = type;
  }
}
