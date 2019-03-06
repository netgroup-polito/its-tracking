export class Vehicle {
  id: string = undefined;
  name: string = undefined;
  destination: string = undefined;
  origin: string = undefined;
  position: string = undefined;
  entrytime: string = undefined;
  state: string = undefined;
  type: string = undefined;
  material: string[];

  constructor(id: string,
              name: string,
              destination: string,
              origin: string,
              position: string,
              entrytime: string,
              state: string,
              type: string,
              material: string[]) {
    this.id = id;
    this.name = name;
    this.destination = destination;
    this.origin = origin;
    this.position = position;
    this.entrytime = entrytime;
    this.state = state;
    this.type = type;
    this.material = material;
  }
}
