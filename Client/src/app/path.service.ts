import { Injectable } from '@angular/core';
import {Place} from './place';

@Injectable({
  providedIn: 'root'
})
export class PathService {
  path: Place[];
  constructor() { }
}
