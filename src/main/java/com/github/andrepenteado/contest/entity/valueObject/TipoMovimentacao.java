
package com.github.andrepenteado.contest.entity.valueObject;

/**
 * @author Andre Penteado
 * @since 24 de Novembro de 2009, 14:37
 */
public enum TipoMovimentacao {
    TROCA {
        @Override
        public String getDescricao() {
            return "Troca";
        }
    },
    FABRICACAO {
        @Override
        public String getDescricao() {
            return "Fabrica��o";
        }
    },
    EXTRAVIO {
        @Override
        public String getDescricao() {
            return "Extravio";
        }
    },
    EMISSAO {
        @Override
        public String getDescricao() {
            return "Emiss�o";
        }
    },
    BONIFICACAO {
        @Override
        public String getDescricao() {
            return "Bonifica��o";
        }
    },
    ABERTURA_VOLUME {
        @Override
        public String getDescricao() {
            return "Abertura Volume";
        }
    },
    CONSUMO {
        @Override
        public String getDescricao() {
            return "Consumo";
        }
    },
    ESTORNO {
        @Override
        public String getDescricao() {
            return "Estorno";
        }
    },
    CORRECAO_ESTOQUE {
        @Override
        public String getDescricao() {
            return "Corre��o de Estoque";
        }
    },
    DEVOLUCAO {
        @Override
        public String getDescricao() {
            return "Devolu��o";
        }
    },
    ESTORNO_DEVOLUCAO {
        @Override
        public String getDescricao() {
            return "Estorno de Devolu��o";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}