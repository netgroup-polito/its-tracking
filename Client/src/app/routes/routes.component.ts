import { Component, OnInit } from '@angular/core';
import {Path} from '../path';
import {PathService} from '../path.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-routes',
  templateUrl: './routes.component.html',
  styleUrls: ['./routes.component.css']
})
export class RoutesComponent implements OnInit {
  path: Path;

  constructor(public pathService: PathService, private router: Router) { }

  ngOnInit() {
    if (this.pathService.path !== undefined) {
      this.path = this.pathService.path;
    } else {
      this.router.navigate(['/home']);
    }
  }

}
