import { Injectable } from '@angular/core';
import { Path } from './path';

@Injectable({
  providedIn: 'root'
})
export class PathService {
  path: Path;
  state = 'IN_TRANSIT';
  info;

  constructor() {}

  fillInfo(
    sourceId: string,
    destinationId: string,
    vehicleId: string,
    type: string,
    dangerousMaterial: string[]): any {
    this.info = {
      id: vehicleId,
      name: vehicleId,
      destination: destinationId,
      origin: sourceId,
      position: sourceId,
      entryTime: new Date().toJSON(),
      state: {value: this.state},
      type: {value: type},
      material: dangerousMaterial
    };
  }
}
