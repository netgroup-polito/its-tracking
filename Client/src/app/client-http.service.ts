import { Injectable } from '@angular/core';
import {  HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Rns} from './rns';

@Injectable({
  providedIn: 'root'
})
export class ClientHttpService {
  private path = 'http://localhost:8080/rns/webapi/';

  constructor(private http: HttpClient) {}

  getSystem(): Observable<Rns> {
    return this.http.get<Rns>(this.path + 'rns');
  }
}
