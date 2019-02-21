import { Component, OnInit } from '@angular/core';
import { ClientHttpService } from '../client-http.service';
import { PathService } from '../path.service';
import { Gate } from '../gate';
import { ParkingArea } from '../parkingArea';
import { Rns } from '../rns';
import { FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
import { Types } from '../types';
import { Materials } from '../materials';

@Component({
  selector: 'app-path',
  templateUrl: './path.component.html',
  styleUrls: ['./path.component.css']
})
export class PathComponent implements OnInit {
  rns: Rns;
  gates: Gate[];
  types: Types;
  materials: Materials;
  parkings: ParkingArea[];
  vehicleId = new FormControl('', [Validators.required]);
  sourceId = new FormControl('', [Validators.required]);
  destinationId = new FormControl('', [Validators.required]);
  typeId = new FormControl('', [Validators.required]);
  selectedMaterials: string[] = [];
  dangerousMaterialsDependencies;

  constructor(private client: ClientHttpService,
              private snackBar: MatSnackBar,
              private pathService: PathService,
              private router: Router) { }

  ngOnInit() {
    const vId = localStorage.getItem('vehicleId');
    const tId = localStorage.getItem('typeId');
    if (vId !== null) {
      this.vehicleId.setValue(vId);
    }
    if (tId !== null) {
      this.typeId.setValue(tId);
    }
    this.getSystem();
    this.getTypes();
    this.getMaterials();
    this.dangerousMaterialsDependencies = {};
  }

  /**
   * Function to get all the system info
   **/
  getSystem() {
    this.gates = [];
    this.parkings = [];
    this.client.getSystem().subscribe(
      data => {
        const self = this;
        data.gate.forEach(function (g) {
          self.gates.push(new Gate(g.id, g.name, g.capacity, g.connectedPlaceId, g.type, g.avgTimeSpent));
        });
        data.parkingArea.forEach(function (p) {
          self.parkings.push(new ParkingArea(p.id, p.name, p.capacity, p.connectedPlaceId, p.avgTimeSpent));
        });
      },
      err => {
        this.openSnackBar(err.error, 'OK');
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
        this.openSnackBar(err.error, 'OK');
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
        this.openSnackBar(err.error, 'OK');
      }
    );
  }

  getErrorMessage(form: FormControl) {
    return form.hasError('required') ? 'You must enter a value' :
        '';
  }

  /**
   * Function to get a path form the server
   * if found, redirect to route component
   **/
  searchPath() {
    if (this.sourceId.hasError('required')
        || this.destinationId.hasError('required')
        || this.vehicleId.hasError('required')) {

    } else {
      this.client.postVehicle(this.sourceId.value,
        this.destinationId.value,
        this.vehicleId.value,
        this.typeId.value,
        this.selectedMaterials).subscribe(
        data => {
          localStorage.setItem('vehicleId', this.vehicleId.value);
          localStorage.setItem('typeId', this.typeId.value);
          this.pathService.path = data;
          this.router.navigate(['/route']);
        },
        err => {
          this.openSnackBar(err.error, 'OK');
        }
      );
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
    this.sourceId.reset();
    this.destinationId.reset();
    this.vehicleId.reset();
    this.selectedMaterials = [];
    localStorage.clear();
  }

  /**
   * Function to delete incompatible dangerous materials
   * For each material selected it search from the list and delete the incompatibles
   * Each incompatible is inserted in a map<string,<count of incompatible>> to add it again
   **/
  checkMaterials(materialId: string) {
      this.client.checkMaterial(materialId).subscribe(
        material => {
          const self = this;
          // material selected
            if (material.incompatibleMaterial !== undefined) {
              if ( this.selectedMaterials.indexOf(materialId) > -1) {
                material.incompatibleMaterial.forEach(function (m1) {
                  const index = self.materials.dangerousMaterial.indexOf(m1);
                  if (index > -1) {
                    self.materials.dangerousMaterial.splice(index, 1);
                  }
                  // increase count of dependencies
                  self.dangerousMaterialsDependencies[m1] = self.dangerousMaterialsDependencies[m1] || 0;
                  self.dangerousMaterialsDependencies[m1]++;
                });
              } else { // material deselected
                material.incompatibleMaterial.forEach(function (m1) {
                  self.dangerousMaterialsDependencies[m1] = self.dangerousMaterialsDependencies[m1] || 0;
                  if (self.dangerousMaterialsDependencies[m1] !== 0) {
                    self.dangerousMaterialsDependencies[m1]--;
                    if (self.dangerousMaterialsDependencies[m1] === 0) {
                      self.materials.dangerousMaterial.push(m1);
                    }
                  }
                });
              }
            }
          },
          err => {
          this.openSnackBar(err.error, 'OK');
        }
      );
  }

  selectedForm(el) {
    console.log(el)
  }
}
