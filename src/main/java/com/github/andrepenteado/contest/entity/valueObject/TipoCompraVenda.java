
package com.github.andrepenteado.contest.entity.valueObject;

public enum TipoCompraVenda {
    EMISSAO {
        @Override
        public String getDescricao() {
            return "Emissão";
        }

        @Override
        public String getNaturezaOperacao() {
            return "VENDA";
        }
    },
    DEVOLUCAO {
        @Override
        public String getDescricao() {
            return "Devolução";
        }

        @Override
        public String getNaturezaOperacao() {
            return "DEVOLUÇÃO";
        }
    },
    BONIFICACAO {
        @Override
        public String getDescricao() {
            return "Bonificação";
        }

        @Override
        public String getNaturezaOperacao() {
            return "BONIFICAÇÃO";
        }
    },
    ENTREGA_FUTURA {
        @Override
        public String getDescricao() {
            return "Entrega Futura";
        }

        @Override
        public String getNaturezaOperacao() {
            return "ENTREGA FUTURA";
        }
    },
    REMESSA_ENTREGA_FUTURA {
        @Override
        public String getDescricao() {
            return "Remessa de Entrega Futura";
        }

        @Override
        public String getNaturezaOperacao() {
            return "REMESSA ENTREGA FUTURA";
        }
    };

    public abstract String getDescricao();
    
    public abstract String getNaturezaOperacao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
