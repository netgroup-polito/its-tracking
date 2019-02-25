import { Injectable } from '@angular/core';
import {  HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rns } from './rns';
import { Path } from './path';
import { Types } from './types';
import { Materials } from './materials';
import { DangerousMaterial } from './dangerousMaterial';
import { PathService } from './path.service';
import { Place } from './place';

@Injectable({
  providedIn: 'root'
})
export class ClientHttpService {
  private path = 'http://localhost:8080/rns/webapi/';

  constructor(private pathService: PathService, private http: HttpClient) {}

  getSystem(): Observable<Rns> {
    return this.http.get<Rns>(this.path + 'rns');
  }

  getPlace(placeId: string): Observable<Place> {
    return this.http.get<Place>(this.path + 'rns/places/' + placeId);
  }

  postVehicle(): Observable<Path> {
    return this.http.post<Path>(this.path + 'vehicles', this.pathService.info, {});
  }

  putVehicle(positionId?: string): Observable<Path> {
    if (positionId !== undefined) {
      this.pathService.info.position = positionId;
    }
    return this.http.put<Path>(this.path + 'vehicles/' + this.pathService.info.id, this.pathService.info, {});
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
    return this.http.delete(this.path + 'vehicles/' + vId, {responseType: 'text'});
  }

  changeState(): any {
    const state = 'PARKED';
    return this.http.put(this.path + 'vehicles/' + this.pathService.info.id + '/state', state, {responseType: 'text'});
  }
}
