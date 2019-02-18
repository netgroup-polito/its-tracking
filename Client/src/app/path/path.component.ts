import { Component, OnInit } from '@angular/core';
import { ClientHttpService } from '../client-http.service';
import { Gate } from '../gate';
import {xml2js, xml2json} from 'xml-js';
import {forEach} from '@angular/router/src/utils/collection';

@Component({
  selector: 'app-path',
  templateUrl: './path.component.html',
  styleUrls: ['./path.component.css']
})
export class PathComponent implements OnInit {
  gates: Gate[];
  options = {compact: true, ignoreComment: true, ignoreDeclaration: true, alwaysChildren: true};

  constructor(private client: ClientHttpService) { }

  ngOnInit() {
    this.getNodes();
  }

  getNodes() {
    this.gates = [];
    this.client.getGates().subscribe(
      data => {
        const self = this;
        const gatesObject = xml2js(data, this.options);
        // @ts-ignore
        gatesObject.gates.gate.forEach(function (e) {
          self.gates.push(new Gate(e._attributes.id, e.name._text, e.type._text, e.capacity._text));
        });
      },
      err => {
          console.log('ERROR:   ' + err.message);
        }
    );
  }
}
