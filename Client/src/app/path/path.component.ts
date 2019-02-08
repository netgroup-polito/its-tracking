import { Component, OnInit } from '@angular/core';
import { ClientHttpService } from '../client-http.service';
import { Node } from '../node';

@Component({
  selector: 'app-path',
  templateUrl: './path.component.html',
  styleUrls: ['./path.component.css']
})
export class PathComponent implements OnInit {
  gates: Node[];

  constructor(private client: ClientHttpService) { }

  ngOnInit() {
    this.getNodes();
  }

  getNodes() {
    this.client.getGates().subscribe(
      data => {
        console.log(data);
        // this.gates = data
      },
      err => {
          console.log('ERROR:   ' + err.message);
        }
    );
  }
}
