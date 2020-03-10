import { ArquivoEntity } from './arquivo.model';

export class PessoaEntity{
  constructor(
    public id?: number,
    public nome?: string,
    public cpf?: string,
    public dataNascimento?: string,
    public email?: string,
    public avatar?: ArquivoEntity
  ){}
}
