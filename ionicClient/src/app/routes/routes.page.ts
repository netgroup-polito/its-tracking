import { Component, OnInit } from '@angular/core';
import {Path} from '../path';
import {PathService} from '../path.service';
import {Router} from '@angular/router';
import { ClientHttpService } from '../client-http.service';
import { AlertController } from '@ionic/angular';
import { Storage } from '@ionic/storage';

@Component({
  selector: 'app-routes',
  templateUrl: './routes.page.html',
  styleUrls: ['./routes.page.scss'],
})
export class RoutesPage implements OnInit {
  path: Path;
  index: number;
  parked: boolean;
  tooltipOptions = {
    'placement': 'bottom',
    'show-delay': 50,
    'hide-delay': 0
  };

  constructor(private client: ClientHttpService,
    public alertController: AlertController,
              public pathService: PathService,
              private storage: Storage,
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
            this.presentAlert('Wrong road: path updated.', 'OK');
            this.path.place.length = this.index;
            this.path.place.push.apply(this.path.place, data.place);
          }
          this.path.place[this.index].visited = 0;
        },
        err => this.presentAlert(err.error, 'OK')
      );
    } else {
      this.presentAlert('You have reached your destination', 'OK');
    }
  }

  async presentAlert(message: string, action: string) {
    const alert = await this.alertController.create({
      message: message,
      buttons: [action]
    });

    await alert.present();
  }

  park() {
    this.parked = true;
    this.client.changeState().subscribe(
      data => this.presentAlert(data, 'OK'),
      err => this.presentAlert(err.message, 'OK')
    );
  }

  exitSystem() {
    this.storage.get('vehicleId').then((vehicleId) => {
      this.client.deleteVehicle(vehicleId).subscribe(
      data => {
        this.presentAlert(data, 'OK');
        this.goHome();
      },
      err => {
        this.presentAlert(err.message, 'OK');
        this.goHome();
      });
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
    return 'danger';
  }

}
