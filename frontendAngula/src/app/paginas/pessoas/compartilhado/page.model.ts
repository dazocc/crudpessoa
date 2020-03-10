import { Pageable } from './pageable.model';

export class Page<T>{

  constructor(
    public content?: T[],
    public totalPages?: number,
    public totalElements?: number,
    public first?: boolean,
    public last?: boolean,
    public empty?: boolean,
    public size?: number,
    public pageable?: Pageable
  ){}

}
