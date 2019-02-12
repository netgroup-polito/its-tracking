export class Gate {
    id: string = undefined;
    name: string = undefined;
    type: string = undefined;
    capacity: number = undefined;

    constructor(id: string, name: string, type: string, capacity: number) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
    }
}
