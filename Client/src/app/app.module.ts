import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { MaterialModule } from './material.module';

import { AppComponent } from './app.component';
import { RoutesComponent } from './routes/routes.component';
import { PathComponent } from './path/path.component';

import { RouterModule, Routes } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

const appRoutes: Routes = [
  { path: 'home', component: PathComponent },
  { path: 'route', component: RoutesComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', component: PathComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    RoutesComponent,
    PathComponent
  ],
  imports: [
    BrowserModule,
    MaterialModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
