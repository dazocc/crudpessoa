import { Component, OnInit, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-server-error-messages',
  templateUrl: './server-error-messages.component.html',
  styleUrls: ['./server-error-messages.component.css']
})
export class ServerErrorMessagesComponent implements OnInit {

  @Input('server-error-messages') serverErrorMessages: string[] = null;

  constructor(public translate: TranslateService) {

    translate.addLangs(['en', 'br']);
    translate.setDefaultLang('br');
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|br/) ?  browserLang : 'br');
    this.translate = translate;
  }

  ngOnInit() {
  }

}
