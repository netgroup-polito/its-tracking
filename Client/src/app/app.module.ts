import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { MaterialModule } from './material.module';

import { AppComponent } from './app.component';
import { RoutesComponent } from './routes/routes.component';
import { PathComponent } from './path/path.component';

import { RouterModule, Routes } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TooltipModule } from 'ng2-tooltip-directive';
import { AdminComponent } from './admin/admin.component';

const appRoutes: Routes = [
  { path: 'home', component: PathComponent },
  { path: 'route', component: RoutesComponent },
  { path: 'admin', component: AdminComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', component: PathComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    RoutesComponent,
    PathComponent,
    AdminComponent
  ],
  imports: [
    BrowserModule,
    MaterialModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    TooltipModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
