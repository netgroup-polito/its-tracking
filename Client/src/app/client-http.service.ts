import { Injectable } from '@angular/core';
import {  HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rns } from './rns';
import { Path } from './path';
import { Types } from './types';
import { Materials } from './materials';
import { DangerousMaterial } from './dangerousMaterial';

@Injectable({
  providedIn: 'root'
})
export class ClientHttpService {
  private path = 'http://localhost:8080/rns/webapi/';
  state = 'IN_TRANSIT';
  constructor(private http: HttpClient) {}

  getSystem(): Observable<Rns> {
    return this.http.get<Rns>(this.path + 'rns');
  }

  postVehicle(sourceId: string, destinationId: string, vehicleId: string, type: string, dangerousMaterial: string[]) {
    // @ts-ignore
    const json = {
      id: vehicleId,
      destination: destinationId,
      origin: sourceId,
      position: sourceId,
      entryTime: new Date().toJSON(),
      state: {value: this.state},
      type: {value: type},
      material: dangerousMaterial
    };

    return this.http.post<Path>(this.path + 'vehicles', json, {});
  }

  getTypes(): Observable<Types> {
    return this.http.get<Types>(this.path + 'vehicles/types');
  }

  getMaterials(): Observable<Materials> {
    return this.http.get<Materials>(this.path + 'dangerousmaterials');
  }

  checkMaterial(materialId: string) {
    return this.http.get<DangerousMaterial>(this.path + 'dangerousmaterials/' + materialId);
  }
}
