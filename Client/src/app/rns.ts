import {Gate} from './gate';
import {ParkingArea} from './parkingArea';
import {RoadSegment} from './roadSegment';
import {Vehicle} from './vehicle';

export class Rns {
  gate: Gate[];
  parkingArea: ParkingArea[];
  roadSegment: RoadSegment[];
  vehicle: Vehicle[];

  constructor(gate: Gate[], parkingArea: ParkingArea[], roadSegment: RoadSegment[], vehicle: Vehicle[]) {
    this.gate = gate;
    this.parkingArea = parkingArea;
    this.roadSegment = roadSegment;
    this.vehicle = vehicle;
  }
}
