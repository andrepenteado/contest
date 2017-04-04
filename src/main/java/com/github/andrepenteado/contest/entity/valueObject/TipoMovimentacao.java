
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
            return "Fabricação";
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
            return "Emissão";
        }
    },
    BONIFICACAO {
        @Override
        public String getDescricao() {
            return "Bonificação";
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
            return "Correção de Estoque";
        }
    },
    DEVOLUCAO {
        @Override
        public String getDescricao() {
            return "Devolução";
        }
    },
    ESTORNO_DEVOLUCAO {
        @Override
        public String getDescricao() {
            return "Estorno de Devolução";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}