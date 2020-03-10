import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest, HttpParams } from  '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { map, catchError, flatMap } from 'rxjs/operators';
import { PessoaEntity } from './pessoa.model';
import { Page } from './page.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PessoaService {

  private apiPath: string = environment.apiUrl + '/pessoas';

  constructor(private http:HttpClient) { }

  listar(pessoa: PessoaEntity): Observable<Page<PessoaEntity>> {

    let params = new HttpParams();

    if(pessoa != null){
      if(pessoa.nome != null){
        params =  params.append('nome', pessoa.nome);
      }
      if(pessoa.email != null){
        params =  params.append('email', pessoa.email);
      }
      if(pessoa.cpf != null){
        params =  params.append('cpf', pessoa.cpf);
      }
      if(pessoa.dataNascimento != null){
        params =  params.append('dataNascimento', pessoa.dataNascimento);
      }
    }

    return this.http.get(this.apiPath, { params } ).pipe(
      map(this.jsonDataToPage.bind(this)),
      catchError(this.handleError)
    )
  }

  buscar(id: number): Observable<PessoaEntity> {
    const url = `${this.apiPath}/${id}`;

    return this.http.get(url).pipe(
      map(this.jsonDataToResource.bind(this)),
      catchError(this.handleError)
    )
  }

  create(pessoa: PessoaEntity): Observable<PessoaEntity> {
    return this.http.post(this.apiPath, pessoa).pipe(
      map(this.jsonDataToResource.bind(this)),
      catchError(this.handleError)
    )
  }

  update(pessoa: PessoaEntity): Observable<PessoaEntity> {
    const url = `${this.apiPath}/${pessoa.id}`;
    return this.http.put(url, pessoa).pipe(
      map(() => pessoa),
      catchError(this.handleError)
    )
  }

  updateArquivo(formData: FormData, id: number) {
    const url = `${this.apiPath}/${id}/arquivo`;

    return this.http.put(url, formData).pipe(
      map(() => null),
      catchError(this.handleError)
    );

  }

  delete(id: number): Observable<any> {
    const url = `${this.apiPath}/${id}`;

    return this.http.delete(url).pipe(
      map(() => null),
      catchError(this.handleError)
    )
  }

  protected jsonDataToPage(jsonData: any): Page<PessoaEntity> {
    return jsonData as Page<PessoaEntity>;
  }

  protected jsonDataToResources(jsonData: any[]): PessoaEntity[] {
    const resources: PessoaEntity[] = [];
    jsonData.forEach(
      element => resources.push( element as PessoaEntity)
    );
    return resources;
  }

  protected jsonDataToResource(jsonData: any): PessoaEntity {
    return jsonData as PessoaEntity;
  }

  protected handleError(error: any): Observable<any>{
    console.log("ERRO NA REQUISIÇÃO => ", error);
    return throwError(error);
  }
}
