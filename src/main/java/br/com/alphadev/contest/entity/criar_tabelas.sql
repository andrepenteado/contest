------------------------------------------------------------------------------

DROP TABLE IF EXISTS pais CASCADE;

CREATE TABLE pais (
   id              INTEGER      NOT NULL,
   nome            VARCHAR(100) NOT NULL,
   sigla           VARCHAR(5)   NULL
);

ALTER TABLE pais ADD CONSTRAINT pk_pais
                 PRIMARY KEY ( id );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS estado CASCADE;

CREATE TABLE estado (
   id      INTEGER      NOT NULL,
   nome    VARCHAR(100) NOT NULL,
   sigla   CHAR(05)     NOT NULL,
   id_pais INTEGER      NULL,
   codigo_ibge VARCHAR(10)
);

ALTER TABLE estado ADD CONSTRAINT pk_estado
                   PRIMARY KEY ( id );

ALTER TABLE estado ADD CONSTRAINT un_estado_sigla
                   UNIQUE ( sigla );

ALTER TABLE estado ADD CONSTRAINT fk_estado_pais
                   FOREIGN KEY ( id_pais )
                   REFERENCES pais
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS cidade CASCADE;

CREATE TABLE cidade (
   id        INTEGER      NOT NULL,
   nome      VARCHAR(100) NOT NULL,
   id_estado INTEGER      NULL,
   id_pais   INTEGER      NULL,
   codigo_ibge VARCHAR(10)
);

ALTER TABLE cidade ADD CONSTRAINT pk_cidade
                   PRIMARY KEY ( id );

ALTER TABLE cidade ADD CONSTRAINT fk_cidade_estado
                   FOREIGN KEY ( id_estado )
                   REFERENCES estado
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;
                       
ALTER TABLE cidade ADD CONSTRAINT fk_cidade_pais
                   FOREIGN KEY ( id_pais )
                   REFERENCES pais
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS empresa CASCADE;

CREATE TABLE empresa (
   id                  SERIAL       NOT NULL,
   razao_social        VARCHAR(100) NOT NULL,
   nome_fantasia       VARCHAR(100) NULL,
   rua                 VARCHAR(100) NULL,
   numero              VARCHAR(10)  NULL,
   complemento         VARCHAR(100) NULL,
   bairro              VARCHAR(100) NULL,
   cep                 VARCHAR(10)  NULL,
   id_cidade           INTEGER      NULL,
   telefone            VARCHAR(15)  NULL,
   fax                 VARCHAR(15)  NULL,
   cnpj                VARCHAR(20)  NULL,
   inscricao_estadual  VARCHAR(20)  NULL,
   inscricao_municipal VARCHAR(20)  NULL,
   cnae                VARCHAR(20)  NULL,
   email               VARCHAR(50)  NULL,
   pagina_internet     VARCHAR(50)  NULL,
   contato             VARCHAR(50)  NULL,
   observacao          TEXT         NULL
);

ALTER TABLE empresa ADD CONSTRAINT pk_empresa
                    PRIMARY KEY ( id );

ALTER TABLE empresa ADD CONSTRAINT fk_empresa_cidade
                    FOREIGN KEY ( id_cidade )
                    REFERENCES cidade
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

CREATE INDEX idx_empresa_razaosocial ON empresa ( razao_social );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS configuracao CASCADE;

CREATE TABLE configuracao (
   id_empresa         INTEGER      NOT NULL,
   
   -- 0 - Não utilizar
   -- 1 - Ambiente de homologação
   -- 2 - Ambiente de produção
   situacao_nfe       INTEGER      NULL,
   versao_emissor_nfe VARCHAR(20)  NULL,
   versao_leiaute_nfe VARCHAR(20)  NULL
);

ALTER TABLE configuracao ADD CONSTRAINT pk_configuracao
                         PRIMARY KEY ( id_empresa );

ALTER TABLE configuracao ADD CONSTRAINT fk_configuracao_empresa
                         FOREIGN KEY ( id_empresa )
                         REFERENCES empresa
                         ON DELETE RESTRICT
                         ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS aliquota_imposto CASCADE;

CREATE TABLE aliquota_imposto (
   id         SERIAL       NOT NULL,
   id_empresa INTEGER      NOT NULL,
   imposto    VARCHAR(50)  NULL,
   aliquota   NUMERIC(5,2) NULL
);

ALTER TABLE aliquota_imposto ADD CONSTRAINT pk_aliquota_imposto
                             PRIMARY KEY ( Id );

ALTER TABLE aliquota_imposto ADD CONSTRAINT fk_aliquotaimposto_empresa
                             FOREIGN KEY ( id_empresa )
                             REFERENCES empresa
                             ON DELETE RESTRICT
                             ON UPDATE RESTRICT;

ALTER TABLE aliquota_imposto ADD CONSTRAINT chk_aliquotaimposto_imposto
                             CHECK ( imposto IN ('SUBSTITUICAO_TRIBUTARIA_ICMS','ICMS','IPI','PIS','COFINS','ISS') );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS cfop;

CREATE TABLE cfop (
   id        SERIAL,
   codigo    VARCHAR(10) NOT NULL,
   descricao TEXT,
   aplicacao TEXT,
   substituicao_tributaria BOOLEAN
);

ALTER TABLE cfop ADD CONSTRAINT pk_cfop
                 PRIMARY KEY ( id );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS funcionario CASCADE;

CREATE TABLE funcionario (
   id                 SERIAL       NOT NULL,
   nome               VARCHAR(100) NOT NULL,
   telefone           VARCHAR(15)  NULL,
   comissao           NUMERIC(5,2) NULL,
   id_empresa         INTEGER      NULL,
   identificacao      VARCHAR(50)  NULL,
   senha              VARCHAR(150) NULL,
   data_ultimo_acesso TIMESTAMP    NULL,
   ip_ultimo_acesso   VARCHAR(30)  NULL,
   observacao         TEXT         NULL
);

ALTER TABLE funcionario ADD CONSTRAINT pk_funcionario
                        PRIMARY KEY ( id );

ALTER TABLE funcionario ADD CONSTRAINT fk_funcionario_empresa
                        FOREIGN KEY ( id_empresa )
                        REFERENCES empresa
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT;

ALTER TABLE funcionario ADD CONSTRAINT un_funcionario_identificacao
                        UNIQUE ( identificacao );

CREATE INDEX idx_funcionario_nome ON funcionario ( nome );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS cliente CASCADE;

CREATE TABLE cliente (
   id                 SERIAL       NOT NULL,
   data_cadastro      TIMESTAMP    NULL,
   id_funcionario     INTEGER      NULL,
   nome               VARCHAR(100) NOT NULL,
   rua                VARCHAR(100) NULL,
   numero             VARCHAR(10)  NULL,
   complemento        VARCHAR(100) NULL,
   bairro             VARCHAR(100) NULL,
   cep                VARCHAR(10)  NULL,
   id_cidade          INTEGER      NULL,
   telefone           VARCHAR(15)  NULL,
   fax                VARCHAR(15)  NULL,
   cnpj               VARCHAR(20)  NULL,
   inscricao_estadual VARCHAR(20)  NULL,
   email              VARCHAR(50)  NULL,
   pagina_internet    VARCHAR(50)  NULL,
   contato            VARCHAR(50)  NULL,
   observacao         TEXT         NULL
);

ALTER TABLE cliente ADD CONSTRAINT pk_cliente
                    PRIMARY KEY ( id );

ALTER TABLE cliente ADD CONSTRAINT fk_cliente_cidade
                    FOREIGN KEY ( id_cidade )
                    REFERENCES cidade
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

ALTER TABLE cliente ADD CONSTRAINT fk_cliente_funcionario
                    FOREIGN KEY ( id_funcionario )
                    REFERENCES funcionario
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

CREATE INDEX idx_cliente_nome ON cliente ( nome );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS fornecedor CASCADE;

CREATE TABLE fornecedor (
   id                 SERIAL       NOT NULL,
   nome               VARCHAR(100) NOT NULL,
   rua                VARCHAR(100) NULL,
   numero             VARCHAR(10)  NULL,
   complemento        VARCHAR(100) NULL,
   bairro             VARCHAR(100) NULL,
   cep                VARCHAR(10)  NULL,
   id_cidade          INTEGER      NULL,
   telefone           VARCHAR(15)  NULL,
   fax                VARCHAR(15)  NULL,
   cnpj               VARCHAR(20)  NULL,
   inscricao_estadual VARCHAR(20)  NULL,
   email              VARCHAR(50)  NULL,
   pagina_internet    VARCHAR(50)  NULL,
   contato            VARCHAR(50)  NULL,
   observacao         TEXT         NULL
);

ALTER TABLE fornecedor ADD CONSTRAINT pk_fornecedor
                       PRIMARY KEY ( id );

ALTER TABLE fornecedor ADD CONSTRAINT fk_fornecedor_cidade
                       FOREIGN KEY ( id_cidade )
                       REFERENCES cidade
                       ON DELETE RESTRICT
                       ON UPDATE RESTRICT;

CREATE INDEX idx_fornecedor_nome ON fornecedor ( nome );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS grupo_produto CASCADE;

CREATE TABLE grupo_produto (
   id         SERIAL      NOT NULL,
   descricao  VARCHAR(30) NOT NULL,
   id_grupo_produto_superior INTEGER NULL,
   observacao TEXT
);

ALTER TABLE grupo_produto ADD CONSTRAINT pk_grupoproduto
                          PRIMARY KEY ( id );

ALTER TABLE grupo_produto ADD CONSTRAINT fk_grupoproduto_grupoprodutosuperior
                        FOREIGN KEY ( id_grupo_produto_superior )
                        REFERENCES grupo_produto
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT;

CREATE INDEX idx_grupoproduto_descricao ON grupo_produto ( descricao );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS tabela_preco CASCADE;

CREATE TABLE tabela_preco (
   id            SERIAL      NOT NULL,
   descricao     VARCHAR(50) NOT NULL,
   observacao    TEXT        NULL
);

ALTER TABLE tabela_preco ADD CONSTRAINT pk_tabela_preco
                         PRIMARY KEY ( Id );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS produto CASCADE;

CREATE TABLE produto (
   id               SERIAL       NOT NULL,
   referencia       VARCHAR(10)  NOT NULL,
   codigo_externo   VARCHAR(20),
   ncm              VARCHAR(20),
   descricao        VARCHAR(100) NOT NULL,
   unidade          VARCHAR(20),
   medida           FLOAT,
   venda_vista      NUMERIC(11,2),
   venda_prazo      NUMERIC(11,2),
   tipo             VARCHAR(10),
   observacao       TEXT,
   foto             BYTEA,
   id_grupo_produto INTEGER,
   id_tabela_preco  INTEGER
);

ALTER TABLE produto ADD CONSTRAINT pk_produto
                    PRIMARY KEY ( id );

ALTER TABLE produto ADD CONSTRAINT chk_produto_tipo
                    CHECK ( tipo IN ('VENDA','LOCACAO','SERVICO') );

ALTER TABLE produto ADD CONSTRAINT chk_produto_unidade
                    CHECK ( unidade IN ('UNIDADE','FARDO','PACOTE','CAIXA','KILO','BOBINA') );

ALTER TABLE produto ADD CONSTRAINT un_produto_referencia
                    UNIQUE ( referencia );

ALTER TABLE produto ADD CONSTRAINT fk_produto_grupoproduto
                    FOREIGN KEY ( id_grupo_produto )
                    REFERENCES grupo_produto
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

ALTER TABLE produto ADD CONSTRAINT fk_produto_tabelapreco
                    FOREIGN KEY ( id_tabela_preco )
                    REFERENCES tabela_preco
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

CREATE UNIQUE INDEX idx_produto_referencia ON produto ( referencia );
CREATE        INDEX idx_produto_descricao  ON produto ( descricao );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS codigo_barras CASCADE;

CREATE TABLE codigo_barras (
   id            SERIAL      NOT NULL,
   codigo_barras VARCHAR(20) NULL,
   id_produto    INTEGER     NULL
);

ALTER TABLE codigo_barras ADD CONSTRAINT pk_codigo_barras
                          PRIMARY KEY ( Id );

ALTER TABLE codigo_barras ADD CONSTRAINT fk_codigobarras_produto
                          FOREIGN KEY ( id_Produto )
                          REFERENCES produto
                          ON DELETE RESTRICT
                          ON UPDATE RESTRICT;

CREATE UNIQUE INDEX idx_codigobarras_codigobarras ON codigo_barras ( codigo_barras );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS estoque CASCADE;

CREATE TABLE estoque (
   id            SERIAL      NOT NULL,
   id_empresa    INTEGER     NOT NULL,
   id_produto    INTEGER     NOT NULL,
   spec_in       INTEGER     NULL,
   spec_out      INTEGER     NULL
);

ALTER TABLE estoque ADD CONSTRAINT pk_estoque
                    PRIMARY KEY ( Id );

ALTER TABLE estoque ADD CONSTRAINT fk_estoque_produto
                    FOREIGN KEY ( id_produto )
                    REFERENCES produto
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

ALTER TABLE estoque ADD CONSTRAINT fk_estoque_empresa
                    FOREIGN KEY ( id_empresa )
                    REFERENCES empresa
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS faixa_preco CASCADE;

CREATE TABLE faixa_preco (
   id              SERIAL       NOT NULL,
   descricao       VARCHAR(50)  NOT NULL,
   markup_vista    NUMERIC(5,2) NOT NULL,
   markup_prazo    NUMERIC(5,2) NOT NULL,
   comissao        NUMERIC(5,2) NOT NULL,
   id_tabela_preco INTEGER
);

ALTER TABLE faixa_preco ADD CONSTRAINT pk_faixa_preco
                        PRIMARY KEY ( Id );

ALTER TABLE faixa_preco ADD CONSTRAINT fk_faixapreco_tabelapreco
                        FOREIGN KEY ( id_tabela_preco )
                        REFERENCES tabela_preco
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS caixa CASCADE;

CREATE TABLE caixa (
   id               SERIAL      NOT NULL,
   data_abertura    DATE        NULL,
   hora_abertura    VARCHAR(5)  NULL,
   valor_abertura   FLOAT       NULL,
   data_fechamento  DATE        NULL,
   hora_fechamento  VARCHAR(5)  NULL,
   valor_fechamento FLOAT       NULL,
   id_funcionario   INTEGER     NULL
);

ALTER TABLE caixa ADD CONSTRAINT pk_caixa
                  PRIMARY KEY ( id );

ALTER TABLE caixa ADD CONSTRAINT fk_caixa_funcionario
                  FOREIGN KEY ( id_funcionario )
                  REFERENCES funcionario
                  ON DELETE RESTRICT
                  ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS prazo_pagamento CASCADE;

CREATE TABLE prazo_pagamento (
   id             SERIAL      NOT NULL,
   descricao      VARCHAR(30) NOT NULL,
   parcelas       VARCHAR(20),
   desconto_juros NUMERIC(5,2),
   observacao     TEXT
);

ALTER TABLE prazo_pagamento ADD CONSTRAINT pk_prazo_pagamento
                            PRIMARY KEY ( id );

CREATE INDEX idx_prazopagamento_descricao ON prazo_pagamento ( descricao );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS venda CASCADE;

CREATE TABLE venda (
   id                 SERIAL    NOT NULL,
   data_lancamento    TIMESTAMP NOT NULL,
   id_empresa         INTEGER   NOT NULL,
   id_cliente         INTEGER,
   id_prazo_pagamento INTEGER,
   id_funcionario     INTEGER,
   forma_pagamento    VARCHAR(20),
   tipo               VARCHAR(50),
   pedido             INTEGER,
   desconto           INTEGER,
   observacao         TEXT
);

ALTER TABLE venda ADD CONSTRAINT pk_venda
                  PRIMARY KEY ( id );

ALTER TABLE venda ADD CONSTRAINT chk_venda_formapagamento
                  CHECK ( forma_pagamento IN ('DINHEIRO','BOLETO','CHEQUE_VISTA',
                  'CHEQUE_PRE_DATADO','CARTAO_CREDITO','CARTAO_DEBITO','DEPOSITO_BANCARIO' ) );

ALTER TABLE venda ADD CONSTRAINT chk_venda_tipo
                  CHECK ( tipo IN ('EMISSAO','BONIFICACAO','DEVOLUCAO','ENTREGA_FUTURA','REMESSA_ENTREGA_FUTURA' ) );

ALTER TABLE venda ADD CONSTRAINT fk_venda_cliente
                  FOREIGN KEY ( id_cliente )
                  REFERENCES cliente
                  ON DELETE RESTRICT
                  ON UPDATE RESTRICT;

ALTER TABLE venda ADD CONSTRAINT fk_venda_prazopagamento
                  FOREIGN KEY ( id_prazo_pagamento )
                  REFERENCES prazo_pagamento
                  ON DELETE RESTRICT
                  ON UPDATE RESTRICT;

ALTER TABLE venda ADD CONSTRAINT fk_venda_funcionario
                  FOREIGN KEY ( id_funcionario )
                  REFERENCES funcionario
                  ON DELETE RESTRICT
                  ON UPDATE RESTRICT;

ALTER TABLE venda ADD CONSTRAINT fk_venda_empresa
                  FOREIGN KEY ( id_empresa )
                  REFERENCES empresa
                  ON DELETE RESTRICT
                  ON UPDATE RESTRICT;

CREATE INDEX idx_venda_datalancamento ON venda ( data_lancamento );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS item_venda CASCADE;

CREATE TABLE item_venda (
   id          SERIAL        NOT NULL,
   id_venda    INTEGER       NULL,
   id_produto  INTEGER       NOT NULL,
   quantidade  INTEGER       NOT NULL,
   valor_venda NUMERIC(11,2) NOT NULL,
   comissao    NUMERIC(5,2) NOT NULL,
   id_cfop     INTEGER       NULL
);

ALTER TABLE item_venda ADD CONSTRAINT pk_itemvenda
                       PRIMARY KEY ( id );

ALTER TABLE item_venda ADD CONSTRAINT fk_itemvenda_produto
                       FOREIGN KEY ( id_produto )
                       REFERENCES produto
                       ON DELETE RESTRICT
                       ON UPDATE RESTRICT;

ALTER TABLE item_venda ADD CONSTRAINT fk_itemvenda_venda
                       FOREIGN KEY ( id_venda )
                       REFERENCES venda
                       ON DELETE RESTRICT
                       ON UPDATE RESTRICT;

ALTER TABLE item_venda ADD CONSTRAINT fk_itemvenda_cfop
                       FOREIGN KEY ( id_cfop )
                       REFERENCES cfop
                       ON DELETE RESTRICT
                       ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS nota_fiscal CASCADE;

CREATE TABLE nota_fiscal (
   id_venda      INTEGER      NOT NULL,
   numero        INTEGER      NOT NULL,
   emissao       TIMESTAMP    NULL,
   frete         VARCHAR(20)  NULL,
   transportador VARCHAR(100) NULL,
   cancelada     BOOLEAN      NULL,
   observacao    TEXT         NULL
);

ALTER TABLE nota_fiscal ADD CONSTRAINT pk_notafiscal
                        PRIMARY KEY ( id_venda );

ALTER TABLE nota_fiscal ADD CONSTRAINT chk_notafiscal_frete
                        CHECK ( frete IN ('CIF','FOB') );

ALTER TABLE nota_fiscal ADD CONSTRAINT fk_notafiscal_venda
                        FOREIGN KEY ( id_venda )
                        REFERENCES venda
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT;

CREATE INDEX idx_notafiscal_emissao ON nota_fiscal ( emissao );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS imposto_nota_fiscal CASCADE;

CREATE TABLE imposto_nota_fiscal (
   id              SERIAL       NOT NULL,
   id_item_venda   INTEGER      NULL,
   imposto         VARCHAR(50)  NULL,
   aliquota        NUMERIC(5,2) NULL
);

ALTER TABLE imposto_nota_fiscal ADD CONSTRAINT pk_imposto_nota_fiscal
                                PRIMARY KEY ( Id );

ALTER TABLE imposto_nota_fiscal ADD CONSTRAINT fk_impostonotafiscal_itemvenda
                                FOREIGN KEY ( id_item_venda )
                                REFERENCES item_venda
                                ON DELETE RESTRICT
                                ON UPDATE RESTRICT;

ALTER TABLE imposto_nota_fiscal ADD CONSTRAINT chk_impostonotafiscal_imposto
                                CHECK ( imposto IN ('SUBSTITUICAO_TRIBUTARIA_ICMS','ICMS','IPI','PIS','COFINS','ISS') );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS nfe CASCADE;

CREATE TABLE nfe (
   id_venda      INTEGER      NOT NULL
);

ALTER TABLE nfe ADD CONSTRAINT pk_nfe
                PRIMARY KEY ( id_venda );

ALTER TABLE nfe ADD CONSTRAINT fk_nfe_notafiscal
                FOREIGN KEY ( id_venda )
                REFERENCES venda
                ON DELETE RESTRICT
                ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS orcamento CASCADE;

CREATE TABLE orcamento (
   id_venda      INTEGER      NOT NULL,
   emissao       TIMESTAMP    NULL,
   transportador VARCHAR(100) NULL,
   observacao    TEXT         NULL
);

ALTER TABLE orcamento ADD CONSTRAINT pk_orcamento
                      PRIMARY KEY ( id_venda );

ALTER TABLE orcamento ADD CONSTRAINT fk_orcamento_venda
                      FOREIGN KEY ( id_venda )
                      REFERENCES venda
                      ON DELETE RESTRICT
                      ON UPDATE RESTRICT;

CREATE INDEX idx_orcamento_emissao ON orcamento ( emissao );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS comissao CASCADE;

CREATE TABLE comissao (
   id              SERIAL        NOT NULL,
   descricao       VARCHAR(100)  NULL,
   id_venda        INTEGER       NOT NULL,
   id_funcionario  INTEGER       NOT NULL,
   valor           NUMERIC(11,2) NULL,
   data_pagamento  DATE          NULL,
   observacao      TEXT          NULL
);

ALTER TABLE comissao ADD CONSTRAINT pk_comissao
                     PRIMARY KEY ( id );

ALTER TABLE comissao ADD CONSTRAINT fk_comissao_venda
                     FOREIGN KEY ( id_venda )
                     REFERENCES venda
                     ON DELETE RESTRICT
                     ON UPDATE RESTRICT;

ALTER TABLE comissao ADD CONSTRAINT fk_comissao_funcionario
                     FOREIGN KEY ( id_funcionario )
                     REFERENCES funcionario
                     ON DELETE RESTRICT
                     ON UPDATE RESTRICT;

ALTER TABLE comissao ADD CONSTRAINT un_comissao_vendafuncionario
                     UNIQUE ( id_venda, id_funcionario );

CREATE INDEX idx_comissao_datapagamento ON comissao ( data_pagamento );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS tipo_conta CASCADE;

CREATE TABLE tipo_conta (
   id         SERIAL      NOT NULL,
   descricao  VARCHAR(30) NOT NULL,
   observacao TEXT
);

ALTER TABLE tipo_conta ADD CONSTRAINT pk_tipo_conta
                          PRIMARY KEY ( id );

CREATE INDEX idx_tipoconta_descricao ON tipo_conta ( descricao );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS receber CASCADE;

CREATE TABLE receber (
   id               SERIAL        NOT NULL,
   id_venda         INTEGER       NULL,
   id_tipo_conta    INTEGER       NULL,
   descricao        VARCHAR(100)  NULL,
   parcela          INTEGER       NOT NULL,
   vencimento       DATE          NULL,
   valor            NUMERIC(11,2) NOT NULL,
   numero_documento VARCHAR(50)  NULL,
   recebido         BOOLEAN       NULL,
   observacao       TEXT          NULL
);

ALTER TABLE receber ADD CONSTRAINT pk_receber
                    PRIMARY KEY ( id );

ALTER TABLE receber ADD CONSTRAINT fk_receber_venda
                    FOREIGN KEY ( id_venda )
                    REFERENCES venda
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

ALTER TABLE receber ADD CONSTRAINT fk_receber_tipoconta
                    FOREIGN KEY ( id_tipo_conta )
                    REFERENCES tipo_conta
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

CREATE INDEX idx_receber_vencimento ON receber ( vencimento );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS recebido CASCADE;

CREATE TABLE recebido (
   id               SERIAL        NOT NULL,
   id_receber       INTEGER       NOT NULL,
   data_pagamento   DATE          NOT NULL,
   valor_pago       NUMERIC(11,2) NOT NULL,
   forma_pagamento  VARCHAR(20)   NOT NULL,
   id_caixa         INTEGER       NULL,
   observacao       TEXT          NULL
);

ALTER TABLE recebido ADD CONSTRAINT pk_recebido
                     PRIMARY KEY ( id );

ALTER TABLE recebido ADD CONSTRAINT chk_recebido_formapagamento
                     CHECK ( forma_pagamento IN ('DINHEIRO','BOLETO','CHEQUE_VISTA',
                     'CHEQUE_PRE_DATADO','CARTAO_CREDITO','CARTAO_DEBITO','DEPOSITO_BANCARIO' ) );

ALTER TABLE recebido ADD CONSTRAINT fk_recebido_receber
                     FOREIGN KEY ( id_receber )
                     REFERENCES receber
                     ON DELETE RESTRICT
                     ON UPDATE RESTRICT;

ALTER TABLE recebido ADD CONSTRAINT fk_recebido_caixa
                     FOREIGN KEY ( id_caixa )
                     REFERENCES caixa
                     ON DELETE RESTRICT
                     ON UPDATE RESTRICT;

CREATE INDEX idx_recebido_data ON recebido ( data_pagamento );
CREATE INDEX idx_recebido_valorpago ON recebido ( valor_pago );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS compra CASCADE;

CREATE TABLE compra (
   id                 SERIAL       NOT NULL,
   nota_fiscal        INTEGER      NULL,
   pedido             INTEGER      NULL,
   emissao            TIMESTAMP    NULL,
   data_pedido        TIMESTAMP    NULL,
   previsao_entrega   TIMESTAMP    NULL,
   transportador      VARCHAR(100) NULL,
   frete              VARCHAR(20)  NULL,
   tipo               VARCHAR(50)  NULL,
   observacao         TEXT         NULL,
   id_fornecedor      INTEGER      NOT NULL,
   id_prazo_pagamento INTEGER      NOT NULL,
   id_empresa         INTEGER      NULL
);

ALTER TABLE compra ADD CONSTRAINT pk_compra
                   PRIMARY KEY ( id );

ALTER TABLE compra ADD CONSTRAINT fk_compra_fornecedor
                   FOREIGN KEY ( id_fornecedor )
                   REFERENCES fornecedor
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;

ALTER TABLE compra ADD CONSTRAINT fk_compra_prazopagamento
                   FOREIGN KEY ( id_prazo_pagamento )
                   REFERENCES prazo_pagamento
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;

ALTER TABLE compra ADD CONSTRAINT chk_compra_frete
                   CHECK ( frete IN ('CIF','FOB') );

ALTER TABLE compra ADD CONSTRAINT chk_compra_tipo
                   CHECK ( tipo IN ('EMISSAO','BONIFICACAO','DEVOLUCAO','ENTREGA_FUTURA','REMESSA_ENTREGA_FUTURA' ) );

ALTER TABLE compra ADD CONSTRAINT fk_compra_empresa
                   FOREIGN KEY ( id_empresa )
                   REFERENCES empresa
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;

CREATE INDEX idx_compra_emissao ON compra ( emissao );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS item_compra CASCADE;

CREATE TABLE item_compra (
   id           SERIAL        NOT NULL,
   id_compra    INTEGER       NULL,
   id_produto   INTEGER       NOT NULL,
   quantidade   INTEGER       NOT NULL,
   valor_compra NUMERIC(11,2) NOT NULL,
   id_cfop      INTEGER       NULL
);

ALTER TABLE item_compra ADD CONSTRAINT pk_itemcompra
                        PRIMARY KEY ( id );

ALTER TABLE item_compra ADD CONSTRAINT fk_itemcompra_produto
                        FOREIGN KEY ( id_produto )
                        REFERENCES produto
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT;

ALTER TABLE item_compra ADD CONSTRAINT fk_itemcompra_compra
                        FOREIGN KEY ( id_compra )
                        REFERENCES compra
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT;

ALTER TABLE item_compra ADD CONSTRAINT fk_itemcompra_cfop
                        FOREIGN KEY ( id_cfop )
                        REFERENCES cfop
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS imposto_compra CASCADE;

CREATE TABLE imposto_compra (
   id              SERIAL       NOT NULL,
   id_item_compra  INTEGER      NULL,
   imposto         VARCHAR(50)  NULL,
   aliquota        NUMERIC(5,2) NULL
);

ALTER TABLE imposto_compra ADD CONSTRAINT pk_imposto_compra
                           PRIMARY KEY ( Id );

ALTER TABLE imposto_compra ADD CONSTRAINT fk_impostocompra_itemcompra
                           FOREIGN KEY ( id_item_compra )
                           REFERENCES item_compra
                           ON DELETE RESTRICT
                           ON UPDATE RESTRICT;

ALTER TABLE imposto_compra ADD CONSTRAINT chk_impostocompra_imposto
                           CHECK ( imposto IN ('SUBSTITUICAO_TRIBUTARIA_ICMS','ICMS','IPI','PIS','COFINS','ISS') );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS pagar CASCADE;

CREATE TABLE pagar (
   id              SERIAL        NOT NULL,
   id_compra       INTEGER       NULL,
   id_tipo_conta   INTEGER       NULL,
   descricao       VARCHAR(100)  NULL,
   parcela         INTEGER       NOT NULL,
   vencimento      DATE          NULL,
   valor           NUMERIC(11,2) NOT NULL,
   pago            BOOLEAN       NULL,
   observacao      TEXT          NULL
);

ALTER TABLE pagar ADD CONSTRAINT pk_pagar
                  PRIMARY KEY ( id );

ALTER TABLE pagar ADD CONSTRAINT fk_pagar_compra
                  FOREIGN KEY ( id_compra )
                  REFERENCES compra
                  ON DELETE RESTRICT
                  ON UPDATE RESTRICT;

ALTER TABLE pagar ADD CONSTRAINT fk_pagar_tipoconta
                  FOREIGN KEY ( id_tipo_conta )
                  REFERENCES tipo_conta
                  ON DELETE RESTRICT
                  ON UPDATE RESTRICT;

CREATE INDEX idx_pagar_vencimento ON pagar ( vencimento );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS pago CASCADE;

CREATE TABLE pago (
   id               SERIAL        NOT NULL,
   id_pagar         INTEGER       NOT NULL,
   data_pagamento   DATE          NOT NULL,
   valor_pago       NUMERIC(11,2) NOT NULL,
   forma_pagamento  VARCHAR(20)   NOT NULL,
   observacao       TEXT          NULL
);

ALTER TABLE pago ADD CONSTRAINT pk_pago
                 PRIMARY KEY ( id );

ALTER TABLE pago ADD CONSTRAINT chk_pago_formapagamento
                 CHECK ( forma_pagamento IN ('DINHEIRO','BOLETO','CHEQUE_VISTA',
                 'CHEQUE_PRE_DATADO','CARTAO_CREDITO','CARTAO_DEBITO','DEPOSITO_BANCARIO' ) );

ALTER TABLE pago ADD CONSTRAINT fk_pago_pagar
                 FOREIGN KEY ( id_pagar )
                 REFERENCES pagar
                 ON DELETE RESTRICT
                 ON UPDATE RESTRICT;

CREATE INDEX idx_pago_data ON pago ( data_pagamento );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS categoria CASCADE;

CREATE TABLE categoria (
   id           SERIAL       NOT NULL,
   nome         VARCHAR(20)  NOT NULL,
   descricao    VARCHAR(100) NULL
);

ALTER TABLE categoria ADD CONSTRAINT pk_categoria
                      PRIMARY KEY ( id );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS funcionario_categoria CASCADE;

CREATE TABLE funcionario_categoria (
   id_funcionario INTEGER NOT NULL,
   id_categoria   INTEGER NOT NULL
);

ALTER TABLE funcionario_categoria ADD CONSTRAINT pk_funcionario_categoria
                                  PRIMARY KEY ( id_funcionario, id_categoria );

ALTER TABLE funcionario_categoria ADD CONSTRAINT fk_funcionarioCategoria_categoria
                                  FOREIGN KEY ( id_categoria )
                                  REFERENCES categoria
                                  ON DELETE RESTRICT
                                  ON UPDATE RESTRICT;

ALTER TABLE funcionario_categoria ADD CONSTRAINT fk_funcionarioCategoria_funcionario
                                  FOREIGN KEY ( id_funcionario )
                                  REFERENCES funcionario
                                  ON DELETE RESTRICT
                                  ON UPDATE RESTRICT;

------------------------------------------------------------------------------

DROP TABLE IF EXISTS movimento_estoque CASCADE;

CREATE TABLE movimento_estoque (
   id                 SERIAL      NOT NULL,
   id_empresa         INTEGER     NOT NULL,
   tipo_movimentacao  VARCHAR(50) NOT NULL,
   id_item_venda      INTEGER     NULL,
   id_item_compra     INTEGER     NULL,
   data               TIMESTAMP   NULL,
   vista_prazo        VARCHAR(50) NULL,
   spec_in            INTEGER     NULL,
   spec_out           INTEGER     NULL,
   observacao         TEXT        NULL
);

ALTER TABLE movimento_estoque ADD CONSTRAINT pk_movimento_estoque
                              PRIMARY KEY ( Id );

ALTER TABLE movimento_estoque ADD CONSTRAINT fk_movimentoestoque_empresa
                              FOREIGN KEY ( id_empresa )
                              REFERENCES empresa
                              ON DELETE RESTRICT
                              ON UPDATE RESTRICT;

ALTER TABLE movimento_estoque ADD CONSTRAINT fk_movimentoestoque_itemvenda
                              FOREIGN KEY ( id_item_venda )
                              REFERENCES item_venda
                              ON DELETE RESTRICT
                              ON UPDATE RESTRICT;

ALTER TABLE movimento_estoque ADD CONSTRAINT fk_movimentoestoque_item_compra
                              FOREIGN KEY ( id_item_compra )
                              REFERENCES item_compra
                              ON DELETE RESTRICT
                              ON UPDATE RESTRICT;

ALTER TABLE movimento_estoque ADD CONSTRAINT chk_movimentoestoque_tipomovimentacao
                              CHECK ( tipo_movimentacao IN ('EMISSAO','ESTORNO','DEVOLUCAO','CORRECAO_ESTOQUE',
                              'TROCA','FABRICACAO','EXTRAVIO','BONIFICACAO','CONSUMO','ABERTURA_VOLUME',
                              'ESTORNO_DEVOLUCAO') );

ALTER TABLE movimento_estoque ADD CONSTRAINT chk_movimentoestoque_vistaprazo
                              CHECK ( vista_prazo IN ('VISTA', 'PRAZO' ) );

CREATE INDEX idx_movimentoestoque_data ON movimento_estoque ( data );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS cheque CASCADE;

CREATE TABLE cheque (
   id              SERIAL       NOT NULL,
   numero          VARCHAR(20)  NOT NULL,
   id_cliente      INTEGER          NULL,     
   id_fornecedor   INTEGER          NULL, 
   nome            VARCHAR(20)  NOT NULL,
   id_tipo_conta   INTEGER          NULL,
   valor           NUMERIC(11,2)    NULL,
   data_emissao    TIMESTAMP        NULL,
   data_vencimento TIMESTAMP        NULL,
   observacao      VARCHAR(100)     NULL
);

ALTER TABLE cheque ADD CONSTRAINT pk_cheque
                   PRIMARY KEY ( id );

ALTER TABLE cheque ADD CONSTRAINT fk_cheque_tipoconta
                   FOREIGN KEY ( id_tipo_conta )
                   REFERENCES tipo_conta
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;
                   
ALTER TABLE cheque ADD CONSTRAINT fk_cheque_cliente
                   FOREIGN KEY ( id_cliente )
                   REFERENCES cliente
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;
             
ALTER TABLE cheque ADD CONSTRAINT fk_cheque_fornecedor
                   FOREIGN KEY ( id_fornecedor )
                   REFERENCES fornecedor
                   ON DELETE RESTRICT
                   ON UPDATE RESTRICT;
                   
------------------------------------------------------------------------------

DROP TABLE IF EXISTS log CASCADE;

CREATE TABLE log (
   id        SERIAL        NOT NULL,
   usuario   VARCHAR(50)   NULL,
   ip        VARCHAR(20)   NULL,
   categoria VARCHAR(50)   NULL,
   sistema   VARCHAR(50)   NULL,
   data_hora TIMESTAMP     NULL,
   mensagem  TEXT          NULL,
   nivel     VARCHAR(15)   NULL,
   classe    VARCHAR(100)  NULL
);

ALTER TABLE log ADD CONSTRAINT pk_log
                PRIMARY KEY ( Id );

-----------------------------------------------------------------------------

DROP TABLE IF EXISTS Banco CASCADE;

CREATE TABLE Banco (
   Id           SERIAL      NOT NULL,
   Nome         VARCHAR(50) NOT NULL,
   Codigo       VARCHAR(03) NOT NULL
);

ALTER TABLE Banco ADD CONSTRAINT PK_Banco
                  PRIMARY KEY ( Id );

------------------------------------------------------------------------------

DROP TABLE IF EXISTS Conta_Corrente CASCADE;

CREATE TABLE Conta_Corrente (
   Id               SERIAL      NOT NULL,
   Id_Empresa       INTEGER     NOT NULL,
   Id_Banco         INTEGER     NOT NULL,
   Agencia          VARCHAR(20) NOT NULL,
   Numero           VARCHAR(20) NOT NULL,
   Sequencia_Arquivo_Cobranca INTEGER NULL
);

ALTER TABLE Conta_Corrente ADD CONSTRAINT PK_Conta_Corrente
                           PRIMARY KEY ( Id );

ALTER TABLE Conta_Corrente ADD CONSTRAINT FK_ContaCorrente_Empresa
                           FOREIGN KEY ( Id_Empresa )
                           REFERENCES Empresa
                           ON DELETE RESTRICT
                           ON UPDATE RESTRICT;

ALTER TABLE Conta_Corrente ADD CONSTRAINT FK_ContaCorrente_Banco
                           FOREIGN KEY ( Id_Banco )
                           REFERENCES Banco
                           ON DELETE RESTRICT
                           ON UPDATE RESTRICT;

------------------------------------------------------------------------------

-- Baixar automaticamente pagamentos (flag pago em pagar)
CREATE OR REPLACE FUNCTION baixar_ou_estornar_pagamento()
RETURNS TRIGGER AS $$
DECLARE
   pk_pagar INTEGER;
   total_pago NUMERIC(11,2);
   total_pagar NUMERIC(11,2); 
BEGIN
   IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
      pk_pagar := NEW.id_pagar;
   ELSIF (TG_OP = 'DELETE') THEN
      pk_pagar := OLD.id_pagar;
   END IF;

   SELECT SUM(pg.valor_pago) INTO total_pago FROM pago pg WHERE pg.id_pagar = pk_pagar GROUP BY pg.id_pagar;
   SELECT p.valor INTO total_pagar FROM pagar p WHERE p.id = pk_pagar;

   IF (total_pago >= total_pagar) THEN
      UPDATE pagar SET pago = TRUE WHERE id = pk_pagar;
   ELSE
      UPDATE pagar SET pago = FALSE WHERE id = pk_pagar;
   END IF;
   
   RETURN NULL;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER modificar_pago AFTER INSERT OR UPDATE OR DELETE
ON pago FOR EACH ROW EXECUTE PROCEDURE baixar_ou_estornar_pagamento();

-----------------------------------------------------------------------------

-- Baixar automaticamente recebimentos (flag recebido em receber)
CREATE OR REPLACE FUNCTION baixar_ou_estornar_recebimento()
RETURNS TRIGGER AS $$
DECLARE
   pk_receber INTEGER;
   total_recebido NUMERIC(11,2);
   total_receber NUMERIC(11,2); 
BEGIN
   IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
      pk_receber := NEW.id_receber;
   ELSIF (TG_OP = 'DELETE') THEN
      pk_receber := OLD.id_receber;
   END IF;

   SELECT SUM(rb.valor_pago) INTO total_recebido FROM recebido rb WHERE rb.id_receber = pk_receber GROUP BY rb.id_receber;
   SELECT r.valor INTO total_receber FROM receber r WHERE r.id = pk_receber;

   IF (total_recebido >= total_receber) THEN
      UPDATE receber SET recebido = TRUE WHERE id = pk_receber;
   ELSE
      UPDATE receber SET recebido = FALSE WHERE id = pk_receber;
   END IF;
   
   RETURN NULL;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER modificar_recebido AFTER INSERT OR UPDATE OR DELETE
ON recebido FOR EACH ROW EXECUTE PROCEDURE baixar_ou_estornar_recebimento();

-----------------------------------------------------------------------------