import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { MaterialModule } from './material.module';

import { AppComponent } from './app.component';
import { OptionsComponent } from './options/options.component';
import { RoutesComponent } from './routes/routes.component';
import { RegisterComponent } from './register/register.component';

import { RouterModule, Routes } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const appRoutes: Routes = [
  { path: 'home', component: RoutesComponent },
  { path: 'options', component: OptionsComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', component: RoutesComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    OptionsComponent,
    RoutesComponent,
    RegisterComponent
  ],
  imports: [
    BrowserModule,
    MaterialModule,
    RouterModule.forRoot(appRoutes),
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
