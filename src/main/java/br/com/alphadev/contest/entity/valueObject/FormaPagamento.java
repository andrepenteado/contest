
package br.com.alphadev.contest.entity.valueObject;

/**
 *
 * @author André Penteado
 */
public enum FormaPagamento {

    DINHEIRO {
        @Override
        public String getDescricao() {
            return "Dinheiro";
        }
    },
    BOLETO {
        @Override
        public String getDescricao() {
            return "Boleto Bancário";
        }
    },
    CHEQUE_VISTA {
        @Override
        public String getDescricao() {
            return "Cheque à Vista";
        }
    },
    CHEQUE_PRE_DATADO {
        @Override
        public String getDescricao() {
            return "Cheque Pré-Datado";
        }
    },
    DEPOSITO_BANCARIO {
        @Override
        public String getDescricao() {
            return "Depósito Bancário";
        }
    },
    CARTAO_CREDITO {
        @Override
        public String getDescricao() {
            return "Cartão de Crédito";
        }
    },
    CARTAO_DEBITO {
        @Override
        public String getDescricao() {
            return "Cartão de Débito";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
