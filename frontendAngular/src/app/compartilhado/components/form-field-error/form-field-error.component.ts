import { Component, OnInit, Input } from '@angular/core';
import { FormControl } from "@angular/forms";
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-form-field-error',
  template: `
    <p class="text-danger">
      {{errorMessage}}
    </p>
  `,
  styleUrls: ['./form-field-error.component.css']
})
export class FormFieldErrorComponent implements OnInit {

  @Input('form-control') formControl: FormControl;

  constructor(public translate: TranslateService) {

    translate.addLangs(['en', 'br']);
    translate.setDefaultLang('br');
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|br/) ?  browserLang : 'br');
  }

  ngOnInit() {
  }

  public get errorMessage(): string | null {
    if( this.mustShowErrorMessage() )
      return this.getErrorMessage();
    else
      return null;
  }


  private mustShowErrorMessage(): boolean {
    return this.formControl.invalid && this.formControl.touched
  }

  private getErrorMessage(): string | null {
    if( this.formControl.errors.required )
      return this.translate.instant('COMPONENT_MSG_ERROR.DATA_IS_MANDATORY');

    else if( this.formControl.errors.email)
      return this.translate.instant('COMPONENT_MSG_ERROR.EMAIL_INVALID');

    else if( this.formControl.errors.minlength){
      const requiredLength = this.formControl.errors.minlength.requiredLength;
      return this.translate.instant('COMPONENT_MSG_ERROR.MINIMUM_CHARACTERS', { requiredLength });
    }

    else if( this.formControl.errors.maxlength){
      const requiredLength = this.formControl.errors.maxlength.requiredLength;
      return this.translate.instant('COMPONENT_MSG_ERROR.MAXIMUM_CHARACTERS', { requiredLength });
    }
  }

}
