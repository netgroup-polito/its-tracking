import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'path',
    pathMatch: 'full'
  },
  { path: 'path', loadChildren: './path/path.module#PathPageModule' },
  { path: 'routes', loadChildren: './routes/routes.module#RoutesPageModule' },
  {
    path: '**',
    redirectTo: 'path'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
