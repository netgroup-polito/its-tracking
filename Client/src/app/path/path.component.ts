import { Component, OnInit } from '@angular/core';
import { ClientHttpService } from '../client-http.service';
import { Gate } from '../gate';
import { ParkingArea } from '../parkingArea';
import {Rns} from '../rns';
import {Vehicle} from '../vehicle';

@Component({
  selector: 'app-path',
  templateUrl: './path.component.html',
  styleUrls: ['./path.component.css']
})
export class PathComponent implements OnInit {
  rns: Rns;
  gates: Gate[];
  parkings: ParkingArea[];
  vehicles: Vehicle[];
  vehicleId: string;

  constructor(private client: ClientHttpService) { }

  ngOnInit() {
    this.getSystem();
  }

  getSystem() {
    this.gates = [];
    this.parkings = [];
    this.vehicles = [];
    this.client.getSystem().subscribe(
      data => {
        const self = this;
        data.gate.forEach(function (g) {
          self.gates.push(new Gate(g.id, g.name, g.type, g.capacity, g.connectedPlaceId));
        });
        data.parkingArea.forEach(function (p) {
          self.parkings.push(new ParkingArea(p.id, p.name, p.capacity, p.avgTimeSpent, p.connectedPlaceId));
        });
        data.vehicle.forEach(function (v) {
          self.vehicles.push(
            new Vehicle(v.id, v.name, v.destination, v.origin, v.position, v.entrytime, v.state, v.type)
          );
        });
      },
      err => {
        console.log('ERROR:   ' + err.message);
      }
    );
  }

  setValueVehicleId(id: string) {
    this.vehicleId = id;
  }

  searchPath() {
    return null;
  }
}
