import { Component, OnInit } from '@angular/core';
import {Place} from '../place';
import {PathService} from '../path.service';

@Component({
  selector: 'app-routes',
  templateUrl: './routes.component.html',
  styleUrls: ['./routes.component.css']
})
export class RoutesComponent implements OnInit {
  path: Place[];

  constructor(public pathService: PathService) { }

  ngOnInit() {
    this.path = this.pathService.path;
    console.log(this.path);
  }

}
