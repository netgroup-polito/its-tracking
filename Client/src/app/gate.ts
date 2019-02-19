import {Place} from './place';

export class Gate extends Place {
  type: string = undefined;

  constructor(id: string, name: string, capacity: number, connectedPlaceId: string[], type: string) {
    super(id, name, capacity, connectedPlaceId);
    this.type = type;
  }
}
