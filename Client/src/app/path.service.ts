import { Injectable } from '@angular/core';
import {Path} from './path';

@Injectable({
  providedIn: 'root'
})
export class PathService {
  path: Path;
  constructor() { }
}
