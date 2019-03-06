import {Gate} from './gate';
import {ParkingArea} from './parkingArea';
import {RoadSegment} from './roadSegment';

export class Rns {
  gate: Gate[];
  parkingArea: ParkingArea[];
  roadSegment: RoadSegment[];

  constructor(gate: Gate[], parkingArea: ParkingArea[], roadSegment: RoadSegment[]) {
    this.gate = gate;
    this.parkingArea = parkingArea;
    this.roadSegment = roadSegment;
  }
}
