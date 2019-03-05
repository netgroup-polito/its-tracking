import { Component, OnInit } from '@angular/core';
import {Path} from '../path';
import {PathService} from '../path.service';
import {Router} from '@angular/router';
import { ClientHttpService } from '../client-http.service';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-interactive-routes',
  templateUrl: './interactive-routes.component.html',
  styleUrls: ['./interactive-routes.component.css']
})
export class InteractiveRoutesComponent implements OnInit {
  path: Path;
  index: number;
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
    this.index = 0;
    if (this.pathService.path !== undefined) {
      this.path = this.pathService.path;
      this.path.place[this.index].visited = 0;
    } else {
      this.router.navigate(['/home']);
    }
  }

  visitPlace(placeId: string) {
    if (this.index < this.path.place.length - 1) {
      this.pathService.info.entryTime = new Date().toJSON();
      this.client.putVehicle(placeId).subscribe(
        (data) => {
          this.path.place[this.index].visited = 1;
          const nextRightPlace = this.path.place[++this.index].id;
          if (data.place[0].id !== nextRightPlace) { // wrong road selected
            this.openSnackBar('Wrong road: path updated.', 'OK');
            this.path.place.length = this.index;
            this.path.place.push.apply(this.path.place, data.place);
          }
          this.path.place[this.index].visited = 0;
        },
        err => this.openSnackBar(err.error, 'OK')
      );
    } else {
      this.openSnackBar('You have reached your destination', 'OK');
    }
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
      data => {
        this.openSnackBar(data, 'OK');
        this.goHome();
      },
      err => {
        this.openSnackBar(err.message, 'OK');
        this.goHome();
      });
  }

  goHome() {
    this.router.navigate(['/home']);
  }

  color(id: string) {
    if (this.index < this.path.place.length - 1) {
      if (id === this.path.place[this.index + 1].id) {
        return 'primary';
      }
    }
    return 'warn';
  }

}
