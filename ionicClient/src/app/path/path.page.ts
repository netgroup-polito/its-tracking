import { Component, OnInit } from '@angular/core';
import { Storage } from '@ionic/storage';
import { ClientHttpService } from '../client-http.service';
import { PathService } from '../path.service';
import { Gate } from '../gate';
import { ParkingArea } from '../parkingArea';
import { Rns } from '../rns';
import { Router } from '@angular/router';
import { Types } from '../types';
import { Materials } from '../materials';
import { Place } from '../place';
import { AlertController } from '@ionic/angular';
import { Vehicle } from '../vehicle';

@Component({
  selector: 'app-list',
  templateUrl: 'path.page.html',
  styleUrls: ['path.page.scss']
})
export class PathPage implements OnInit {
  rns: Rns;
  gatesIN: Gate[];
  gatesOUT: Gate[];
  types: Types;
  materials: Materials;
  parkings: ParkingArea[];
  vehicleId: string;
  sourceId: string;
  destinationId: string;
  typeId: string;
  selectedMaterials: string[] = [];
  currentPosition: Place;
  vehicle: Vehicle;

  constructor(private client: ClientHttpService,
              private pathService: PathService,
              public alertController: AlertController,
              private storage: Storage,
              private router: Router) { }

  ngOnInit() {
    this.storage.get('vehicleId').then((val) => {
      this.vehicleId = val;
      this.client.getVehicle(this.vehicleId).subscribe(
        vehicle => {
          if (vehicle !== undefined && vehicle !== null) {
            this.vehicle = vehicle;
            this.typeId = this.vehicle.type;
            this.sourceId = this.vehicle.origin;
            this.destinationId = this.vehicle.destination;
            if (this.vehicle.material !== null) {
              this.selectedMaterials = this.vehicle.material;
            }
            this.client.getPlace(this.vehicle.position).subscribe(
              (data) => this.currentPosition = data
            );
          }
        });
    });
    this.getSystem();
    this.getTypes();
    this.getMaterials();
  }

  /**
   * Function to get all the system info
   **/
  getSystem() {
    this.gatesIN = [];
    this.gatesOUT = [];
    this.parkings = [];
    this.client.getSystem().subscribe(
      data => {
        const self = this;
        data.gate.forEach(function (g) {
          if (g.type === 'IN') {
            self.gatesIN.push(new Gate(g.id, g.name, g.capacity, g.connectedPlaceId, g.type, g.avgTimeSpent));
          } else if (g.type === 'OUT') {
            self.gatesOUT.push(new Gate(g.id, g.name, g.capacity, g.connectedPlaceId, g.type, g.avgTimeSpent));
          } else {
            self.gatesIN.push(new Gate(g.id, g.name, g.capacity, g.connectedPlaceId, g.type, g.avgTimeSpent));
            self.gatesOUT.push(new Gate(g.id, g.name, g.capacity, g.connectedPlaceId, g.type, g.avgTimeSpent));
          }
        });
        data.parkingArea.forEach(function (p) {
          self.parkings.push(new ParkingArea(p.id, p.name, p.capacity, p.connectedPlaceId, p.avgTimeSpent));
        });
      },
      err => {
        this.presentAlert(err.error, 'OK');
      }
    );
  }

  /**
   * Function to get vehicle types
   **/
  private getTypes() {
    this.client.getTypes().subscribe(
      data => {
        this.types = data;
      },
      err => {
        this.presentAlert(err.error, 'OK');
      }
    );
  }

  /**
   * Function to get available dangerous materials
   **/
  private getMaterials() {
    this.client.getMaterials().subscribe(
      data => {
        this.materials = data;
      },
      err => {
        this.presentAlert(err.error, 'OK');
      }
    );
  }

  /**
   * Function to get a path form the server
   * if found, redirect to route component
   **/
  searchPath() {
    if (this.sourceId === undefined
        || this.destinationId === undefined
        || this.vehicleId === undefined
        || this.typeId === undefined) {
          this.presentAlert('Please fill all the information', 'OK');
    } else {
      this.pathService.fillInfo(this.sourceId,
        this.destinationId,
        this.vehicleId,
        this.typeId,
        this.selectedMaterials);
      this.client.putVehicle().subscribe(
        data => {
          this.storage.set('vehicleId', this.vehicleId);
          this.pathService.path = data;
          this.router.navigate(['/routes']);
        },
        err => {
          this.presentAlert(err.error, 'OK');
        }
      );
    }
  }

  async presentAlert(message: string, action: string) {
    const alert = await this.alertController.create({
      message: message,
      buttons: [action]
    });

    await alert.present();
  }

  reset() {
    this.sourceId = undefined;
    this.destinationId = undefined;
    this.vehicleId = undefined;
    this.typeId = undefined;
    this.selectedMaterials = [];
    this.storage.clear();
  }

  exitSystem() {
    this.client.deleteVehicle(this.vehicleId).subscribe(
      data => {
        this.currentPosition = undefined;
        this.presentAlert(data, 'OK');
      },
      err => {
        this.presentAlert(err.message, 'OK');
      });
  }

}
