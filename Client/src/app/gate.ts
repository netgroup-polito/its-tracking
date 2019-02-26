import {Place} from './place';

export class Gate extends Place {
  type: string = undefined;

  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[], type: string, avgTimeSpent: number) {
    super(id, name, capacity, connectedPlaceId, avgTimeSpent);
    this.type = type;
  }
}
