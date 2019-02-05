import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Node } from './node';

@Injectable({
  providedIn: 'root'
})
export class ClientHttpService {
  private path = 'http://localhost:8080/rns/webapi/';

  constructor(private http: HttpClient) { }

  /**
   * Funzione per prendere tutte le posizioni caricate dallo user loggato
   */
  getGates(): Observable<Node[]> {
    return this.http.get<Node[]>(this.path + 'gate');
  }

}
