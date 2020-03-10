import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { PessoasModule } from  './paginas/pessoas/pessoas.module';

const routes: Routes = [
  { path: 'pessoas', loadChildren: () => import('./paginas/pessoas/pessoas.module').then(m => m.PessoasModule) }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
