import { Component, OnInit } from '@angular/core';
import { Gate } from '../gate';
import { ParkingArea } from '../parkingArea';
import { Vehicle } from '../vehicle';
import { Rns } from '../rns';
import { ClientHttpService } from '../client-http.service';
import { MatSnackBar } from '@angular/material';
import { RoadSegment } from '../roadSegment';
import { Place } from '../place';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  rns: Rns;
  gates: Gate[];
  parkings: ParkingArea[];
  roads: RoadSegment[];
  vehicles: Vehicle[];
  tooltipOptions = {
    'placement': 'bottom',
    'show-delay': 50,
    'hide-delay': 50
  };

  constructor(private client: ClientHttpService,
      private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.getSystem();
  }

  /**
   * Function to get all the system info
   **/
  getSystem() {
    this.gates = [];
    this.parkings = [];
    this.roads = [];
    this.vehicles = [];
    this.client.getSystemAdmin().subscribe(
      data => {
        const self = this;
        data.gate.forEach(function (g) {
          self.gates.push(new Gate(g.id, g.name, g.capacity, g.connectedPlaceId, g.type, g.avgTimeSpent));
        });
        data.parkingArea.forEach(function (p) {
          self.parkings.push(new ParkingArea(p.id, p.name, p.capacity, p.connectedPlaceId, p.avgTimeSpent, p.service));
        });
        data.roadSegment.forEach(function (r) {
          self.roads.push(new RoadSegment(r.id, r.name, r.capacity, r.connectedPlaceId, r.avgTimeSpent, r.containerPlaceId));
        });
        if (data.vehicle !== undefined) {
          data.vehicle.forEach(function (v) {
            self.vehicles.push(new Vehicle(v.id, v.name, v.destination, v.origin, v.position, v.entryTime, v.state, v.type, v.material));
          });
        }
      },
      err => {
        this.openSnackBar(err.error, 'OK');
      }
    );
  }

  getPlace(id: string) {
    this.client.getPlaceAdmin(id).subscribe(
      data => this.openSnackBar('Number of reservation for place ' + id + ': ' + data.numberOfReservation, 'OK'),
      err =>  this.openSnackBar(err.error, 'OK')
    );
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 4000,
    });
  }
}
