import { Component, OnInit } from '@angular/core';
import {Path} from '../path';
import {PathService} from '../path.service';
import {Router} from '@angular/router';
import { Place } from '../place';

@Component({
  selector: 'app-routes',
  templateUrl: './routes.component.html',
  styleUrls: ['./routes.component.css']
})
export class RoutesComponent implements OnInit {
  path: Path;
  intervals;

  constructor(public pathService: PathService, private router: Router) { }

  ngOnInit() {
    const p1 = new Place('p1', 'place1', 2, ['p2'], 3);
    const p2 = new Place('p2', 'place2', 25, ['p1', 'p3'], 4);
    const p3 = new Place('p3', 'place3', 12, ['p2'], 5);
    this.intervals = [];
    this.pathService.path = new Path([p1, p2, p3]);
    if (this.pathService.path !== undefined) {
      this.path = this.pathService.path;
      this.drive();
    } else {
      this.router.navigate(['/home']);
    }
  }

  drive(): any {
    const randomTime = this.getRandomInt(this.path.place[0].avgTimeSpent - 2,
                              this.path.place[0].avgTimeSpent + 2);
    this.visitPlace(0, 2);
  }

  visitPlace(index: number, time: number) {
    const self = this;
    let timer = 0;
    this.intervals[index] = setInterval( function() {
      if (timer === time) {
        clearInterval(self.intervals[index]);
        self.path.place[index].visited = 1;
        index++;
        if (index < self.path.place.length) {
          const randomTime = self.getRandomInt(self.path.place[index].avgTimeSpent - 2,
          self.path.place[index].avgTimeSpent + 2);
          self.visitPlace(index, 2);
        }
      } else {
        timer++;
      }
    }, 1000);
  }

  getRandomInt(min: number, max: number) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

}
