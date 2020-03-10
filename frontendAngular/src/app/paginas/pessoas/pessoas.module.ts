import { NgModule } from '@angular/core';

import { CompartilhadoModule } from '../../compartilhado/compartilhado.module';
import { PessoasRoutingModule } from './pessoas-routing.module';
import { PessoaListaComponent } from './pessoa-lista/pessoa-lista.component';
import { PessoaFormComponent } from './pessoa-form/pessoa-form.component';
import { NgxMaskModule } from 'ngx-mask';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  declarations: [PessoaListaComponent, PessoaFormComponent],
  imports: [
    CompartilhadoModule,
    PessoasRoutingModule,
    NgxPaginationModule,
    NgxMaskModule.forRoot()
  ]
})

export class PessoasModule { }
