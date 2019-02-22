import { Injectable } from '@angular/core';
import {  HttpClient, HttpResponse } from '@angular/common/http';
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
  json;

  constructor(private http: HttpClient) {}

  getSystem(): Observable<Rns> {
    return this.http.get<Rns>(this.path + 'rns');
  }

  postVehicle(
    sourceId: string,
    destinationId: string,
    vehicleId: string,
    type: string,
    dangerousMaterial: string[]): Observable<Path> {
    // @ts-ignore
    this.json = {
      id: vehicleId,
      destination: destinationId,
      origin: sourceId,
      position: sourceId,
      entryTime: new Date().toJSON(),
      state: {value: this.state},
      type: {value: type},
      material: dangerousMaterial
    };

    return this.http.post<Path>(this.path + 'vehicles', this.json, {});
  }

  putVehicle(positionId: string): Observable<Path> {
    this.json.position = positionId;
    return this.http.put<Path>(this.path + 'vehicles/' + this.json.id, this.json, {});
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

  deleteVehicle(vId: string): any {
    return this.http.delete(this.path + 'vehicles/' + vId);
  }

  changeState(): Observable<HttpResponse<string>> {
    const state = 'PARKED';
    return this.http.put<HttpResponse<string>>(this.path + 'vehicles/' + this.json.id + '/state', state, {});
  }
}
