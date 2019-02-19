import { Component, OnInit } from '@angular/core';
import { ClientHttpService } from '../client-http.service';
import { Gate } from '../gate';
import { ParkingArea } from '../parkingArea';
import { Rns } from '../rns';
import { FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-path',
  templateUrl: './path.component.html',
  styleUrls: ['./path.component.css']
})
export class PathComponent implements OnInit {
  rns: Rns;
  gates: Gate[];
  parkings: ParkingArea[];
  vehicleId = new FormControl('', [Validators.required]);
  sourceId = new FormControl('', [Validators.required]);
  destinationId = new FormControl('', [Validators.required]);

  constructor(private client: ClientHttpService, public snackBar: MatSnackBar) { }

  ngOnInit() {
    this.getSystem();
  }

  getSystem() {
    this.gates = [];
    this.parkings = [];
    this.client.getSystem().subscribe(
      data => {
        const self = this;
        data.gate.forEach(function (g) {
          self.gates.push(new Gate(g.id, g.name, g.type, g.capacity, g.connectedPlaceId));
        });
        data.parkingArea.forEach(function (p) {
          self.parkings.push(new ParkingArea(p.id, p.name, p.capacity, p.avgTimeSpent, p.connectedPlaceId));
        });
      },
      err => {
        console.log('ERROR:   ' + err.message);
      }
    );
  }

  getErrorMessage() {
    return this.vehicleId.hasError('required') ? 'You must enter a value' :
        '';
  }

  searchPath() {
    if (this.sourceId.hasError('required')
        || this.destinationId.hasError('required')
        || this.vehicleId.hasError('required')) {
      this.openSnackBar('You have to fill all fields', 'OK');
    } else {
      // this.client.getSystem().subscribe();
      this.openSnackBar('Ok', 'OK');
    }
  }

  fillSrcId(id: string) {
    this.sourceId.setValue(id);
  }

  fillDstId(id: string) {
    this.destinationId.setValue(id);

  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }

  reset() {
    this.sourceId.setValue('');
    this.destinationId.setValue('');
    this.vehicleId.setValue('');
  }
}
