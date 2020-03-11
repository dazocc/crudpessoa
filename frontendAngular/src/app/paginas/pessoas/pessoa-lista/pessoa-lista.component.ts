import { Page } from './../compartilhado/page.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { PessoaService } from '../compartilhado/pessoa.service';
import { PessoaEntity } from '../compartilhado/pessoa.model';

@Component({
  selector: 'app-pessoa-lista',
  templateUrl: './pessoa-lista.component.html',
  styleUrls: ['./pessoa-lista.component.css']
})
export class PessoaListaComponent implements OnInit {

  pessoaForm: FormGroup;
  page: Page<PessoaEntity>;
  pessoas: PessoaEntity [];
  paginaAtual: number = 1 ;

  constructor(public translate: TranslateService,
              private formBuilder: FormBuilder,
              private pessoaService: PessoaService,
              ) {

    translate.addLangs(['en', 'br']);
    translate.setDefaultLang('br');
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|br/) ?  browserLang : 'br');
    this.translate = translate;
   }

   private buildPessoaForm() {
    this.pessoaForm = this.formBuilder.group({
      nome: [null],
      cpf: [null],
      email: [null],
      dataNascimento: [null],
    });
  }

  ngOnInit(): void {
    this.buildPessoaForm();
    const pessoa: PessoaEntity = Object.assign(new PessoaEntity(), this.pessoaForm.value);
    this.pessoaService.listar(pessoa).subscribe(
      page => {
        this.paginaAtual = page.pageable.pageNumber;
        this.pessoas = page.content;
      },
      error => alert('Erro ao carregar a lista')
    );
  }


  filtrar(){
    const pessoa: PessoaEntity = Object.assign(new PessoaEntity(), this.pessoaForm.value);
    this.pessoaService.listar(pessoa).subscribe(
      page => {
        this.paginaAtual = page.pageable.pageNumber;
        this.pessoas = page.content;
      },
      error => alert('Erro ao carregar a lista')
    );
  }

  deleteResource(pessoa: PessoaEntity) {
    const mustDelete = confirm('Deseja realmente excluir este item?');

    if (mustDelete){
      this.pessoaService.delete(pessoa.id).subscribe(
        () => this.pessoas = this.pessoas.filter(element => element != pessoa),
        () => alert("Erro ao tentar excluir!")
      )
    }
  }

}
