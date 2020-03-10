import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PessoaListaComponent } from './pessoa-lista.component';

describe('PessoaListaComponent', () => {
  let component: PessoaListaComponent;
  let fixture: ComponentFixture<PessoaListaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PessoaListaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PessoaListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
