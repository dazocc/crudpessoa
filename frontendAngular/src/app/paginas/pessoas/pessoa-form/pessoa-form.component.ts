import { Component, OnInit, AfterContentChecked } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { switchMap } from 'rxjs/operators';

import { PessoaService } from '../compartilhado/pessoa.service';
import { PessoaEntity } from '../compartilhado/pessoa.model';
import { ArquivoEntity } from '../compartilhado/arquivo.model';

@Component({
  selector: 'app-pessoa-form',
  templateUrl: './pessoa-form.component.html',
  styleUrls: ['./pessoa-form.component.css']
})
export class PessoaFormComponent implements OnInit, AfterContentChecked {

  edicao: boolean;
  pessoaForm: FormGroup;
  pageTitle: string;
  serverErrorMessages: string[] = null;
  submittingForm: boolean = false;
  pessoa: PessoaEntity = new PessoaEntity();
  formData: FormData;
  url: any = null;
  file: File;

 constructor(
    public translate: TranslateService,
    private toastr: ToastrService,
    private pessoaService: PessoaService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder) {

    translate.addLangs(['en', 'br']);
    translate.setDefaultLang('br');
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|br/) ?  browserLang : 'br');
    this.translate = translate;
  }

  ngOnInit(): void {
    this.setCurrentAction();
    this.buildPessoaForm();
    this.loadPessoa();
  }

  private buildPessoaForm() {
    this.pessoaForm = this.formBuilder.group({
      id:[null],
      nome: [null, [Validators.required, Validators.maxLength(150)]],
      email: [null, [Validators.required, Validators.maxLength(400), Validators.email]],
      cpf: [null, [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
      dataNascimento: [null]
    });
  }

  private setCurrentAction() {
    if(this.route.snapshot.url[0].path == "new"){
      this.edicao = false;
    }else{
      this.edicao = true;
    }
  }

  private loadPessoa() {
    if(this.edicao){
      this.route.paramMap.pipe(
          switchMap(params => this.pessoaService.buscar(+params.get("id")))
      )
      .subscribe(
        (pessoa) => {
        this.pessoa = pessoa;
        this.pessoaForm.patchValue(pessoa);

        if(pessoa.avatar !== null){
          const arquivo: ArquivoEntity = pessoa.avatar;
          const imageBlob = this.dataURItoBlob(arquivo);
          this.file = new File([imageBlob], arquivo.nome, { type: arquivo.tipo });
          this.preview();
        }

      },
        (error)  => this.actionsForError(error)
      );
    }
  }

  dataURItoBlob(dataURI: ArquivoEntity) {
    const byteString = window.atob(dataURI.arquivo);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteString.length; i++) {
      int8Array[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([int8Array], { type: dataURI.tipo });
    return blob;
 }

  ngAfterContentChecked(): void {
    if(this.edicao){
      const pessoaNome = this.pessoa.nome + '(' + this.pessoa.id + ')' || "";
      this.pageTitle = this.translate.instant('EDIT_PERSON.TITLE1') + pessoaNome;
    }else{
      this.pageTitle = this.translate.instant('EDIT_PERSON.TITLE2') + '';
    }
  }

  submitForm(){
    this.submittingForm = true;

    if(this.edicao){
      this.updateResource();
    } else{
      this.createResource();
    }
  }

  protected createResource(){
    const pessoa: PessoaEntity = Object.assign(new PessoaEntity(), this.pessoaForm.value);

    this.pessoaService.create(pessoa)
      .subscribe(
        resource => {
          this.actionsForSuccess(resource)
        },
        error => this.actionsForError(error)
      );
  }

  protected updateResource(){
    const pessoa: PessoaEntity = Object.assign(new PessoaEntity(), this.pessoaForm.value);

    this.pessoaService.update(pessoa)
      .subscribe(
        pessoa => this.actionsForSuccess(pessoa),
        error => this.actionsForError(error)
      )
  }

  protected actionsForSuccess(pessoa: PessoaEntity){

    const arquivo = this.formData;

    if(arquivo && arquivo.has('file') && pessoa.id != null){
      this.pessoaService.updateArquivo(arquivo, pessoa.id)    .subscribe(
        (response) => {
          this.toastr.success(this.translate.instant('MSG.SUCCESS'));
          this.router.navigateByUrl('pessoas', {skipLocationChange: true}).then(
            () => this.router.navigate(['pessoas', pessoa.id, "edit"])
          )
        },
        error => this.actionsForError(error)
      );
    }else{
      this.toastr.success(this.translate.instant('MSG.SUCCESS'));
      this.router.navigateByUrl('pessoas', {skipLocationChange: true}).then(
        () => this.router.navigate(['pessoas', pessoa.id, "edit"])
      )
    }

  }

  protected actionsForError(error){
    this.toastr.error(this.translate.instant('MSG.ERROR'));

    this.submittingForm = false;

    if(error.error && error.error.titulos)
      this.serverErrorMessages = error.error.titulos;
    else
      this.serverErrorMessages = [this.translate.instant('MSG.ERROR_SERVER')];
  }

  protected preview() {
    // Show preview
    var mimeType = this.file.type;
    if (mimeType.match(/image\/*/) == null) {
      return;
    }

    var reader = new FileReader();
    reader.readAsDataURL(this.file);
    reader.onload = (_event) => {
      this.url = reader.result;
    }
  }

  fileChange(event) {
    let fileList: FileList = event.target.files;
    if(fileList.length > 0) {
        this.file = fileList[0];
        this.formData = new FormData();
        this.formData.append('file', this.file, this.file.name);
        this.preview();
    }

  }
}
