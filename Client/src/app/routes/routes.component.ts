import { Component, OnInit } from '@angular/core';
import {Path} from '../path';
import {PathService} from '../path.service';
import {Router} from '@angular/router';
import { Place } from '../place';
import { ClientHttpService } from '../client-http.service';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-routes',
  templateUrl: './routes.component.html',
  styleUrls: ['./routes.component.css']
})
export class RoutesComponent implements OnInit {
  path: Path;
  intervals;
  parked: boolean;
  tooltipOptions = {
    'placement': 'bottom',
    'show-delay': 50,
    'hide-delay': 0
  };

  constructor(private client: ClientHttpService,
              private snackBar: MatSnackBar,
              public pathService: PathService,
              private router: Router) { }

  ngOnInit() {
    this.parked = false;
    this.intervals = [];
    if (this.pathService.path !== undefined) {
      this.path = this.pathService.path;
      this.drive();
    } else {
      this.router.navigate(['/home']);
    }
  }

  drive(): any {
    const randomTime = this.getRandomInt(0, 5);
    this.path.place[0].visited = 0;
    this.visitPlace(0, randomTime);
  }

  visitPlace(index: number, time: number) {
    const self = this;
    let timer = 0;
    this.intervals[index] = setInterval( function() {
      if (self.parked) {
        timer--;
      }
      if (timer >= time) {
        clearInterval(self.intervals[index]);
        self.path.place[index].visited = 1;
        const currentIndex = index++;
        if (index < self.path.place.length) {
          const randomTime = self.getRandomInt(0, 5);
          const randomRoad = self.getRandomInt(0, 100);
          if (randomRoad <= 90 || self.path.place[currentIndex].connectedPlaceId.length === 1) { // right road
            self.path.place[index].visited = 0;
            self.client.putVehicle(self.path.place[index].id).subscribe(
              () => self.visitPlace(index, randomTime),
              err => self.openSnackBar(err.error, 'OK')
            );
          } else { // wrong road
            // remove next place in the right path from connected places and then choose another
            const nextRightPlace = self.path.place[index].id;
            const indexNextRightPlace = self.path.place[currentIndex].connectedPlaceId.indexOf(nextRightPlace);
            self.path.place[currentIndex].connectedPlaceId.splice(indexNextRightPlace, 1);
            const randomChoice = self.getRandomInt(0, self.path.place[currentIndex].connectedPlaceId.length - 1);
            self.client.putVehicle(self.path.place[currentIndex].connectedPlaceId[randomChoice]).subscribe(
              (data) => {
                self.openSnackBar('Wrong road: path updated.', 'OK');
                self.path.place.length = index;
                self.path.place.push.apply(self.path.place, data.place);
                self.path.place[index].visited = 0;
                const randomTimeNew = self.getRandomInt(0, 5);
                self.visitPlace(index, randomTimeNew);
              },
              err => self.openSnackBar(err.error, 'OK')
            );
          }
        }
      } else {
        timer++;
      }
    }, 1000);
  }

  getRandomInt(min: number, max: number) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return 5;
    // return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 4000,
    });
  }

  park() {
    this.parked = true;
    this.client.changeState().subscribe(
      data => this.openSnackBar(data, 'OK'),
      err => this.openSnackBar(err.message, 'OK')
    );
  }

  exitSystem() {
    const vId = localStorage.getItem('vehicleId');
    this.client.deleteVehicle(vId).subscribe(
      () => this.goHome(),
      () => this.goHome()
    );
  }

  goHome() {
    this.router.navigate(['/home']);
  }

}
