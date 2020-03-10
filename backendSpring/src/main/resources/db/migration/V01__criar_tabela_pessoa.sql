create table public.tb_arquivo(
  id bigserial NOT NULL,
  arquivo oid NOT NULL,
  nome varchar(150) NOT NULL,
  tipo varchar(100) NOT NULL,
  CONSTRAINT tb_arquivo_pkey PRIMARY KEY (id)
);

CREATE TABLE public.tb_pessoa (
  id bigserial NOT NULL,
  id_avatar INTEGER REFERENCES tb_arquivo(id) null,
  cpf varchar(255) NOT NULL,
  email varchar(400) NOT NULL,
  nome varchar(150) NOT NULL,
  data_nascimento date NULL,
  ativo bool NOT NULL,
  CONSTRAINT tb_pessoa_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX pesssoa_cnpj_uk ON tb_pessoa (cpf) WHERE (ativo = true);


