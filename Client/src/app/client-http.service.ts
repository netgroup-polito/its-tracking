import { Injectable } from '@angular/core';
import {  HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rns } from './rns';
import { Place } from './place';

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
      type: {value: 'CAR'},
      material: ''
    };

    return this.http.post<Place[]>(this.path + 'vehicles', json, {});
  }
}
