
package com.github.andrepenteado.contest.entity.valueObject;

public enum TipoCompraVenda {
    EMISSAO {
        @Override
        public String getDescricao() {
            return "Emiss�o";
        }

        @Override
        public String getNaturezaOperacao() {
            return "VENDA";
        }
    },
    DEVOLUCAO {
        @Override
        public String getDescricao() {
            return "Devolu��o";
        }

        @Override
        public String getNaturezaOperacao() {
            return "DEVOLU��O";
        }
    },
    BONIFICACAO {
        @Override
        public String getDescricao() {
            return "Bonifica��o";
        }

        @Override
        public String getNaturezaOperacao() {
            return "BONIFICA��O";
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
