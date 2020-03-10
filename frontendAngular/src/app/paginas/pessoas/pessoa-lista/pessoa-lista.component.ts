import { Page } from './../compartilhado/page.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

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
  paginaAtual : number = 1 ;

  constructor(private pessoaService: PessoaService,
              private formBuilder: FormBuilder) {
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
