import {Place} from './place';

export class Path {
  place: Place[];

  constructor(path: Place[]) {
    this.place = path;
  }
}
