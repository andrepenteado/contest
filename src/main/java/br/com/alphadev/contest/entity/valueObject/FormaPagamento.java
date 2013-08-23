
package br.com.alphadev.contest.entity.valueObject;

/**
 *
 * @author Andr� Penteado
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
            return "Boleto Banc�rio";
        }
    },
    CHEQUE_VISTA {
        @Override
        public String getDescricao() {
            return "Cheque � Vista";
        }
    },
    CHEQUE_PRE_DATADO {
        @Override
        public String getDescricao() {
            return "Cheque Pr�-Datado";
        }
    },
    DEPOSITO_BANCARIO {
        @Override
        public String getDescricao() {
            return "Dep�sito Banc�rio";
        }
    },
    CARTAO_CREDITO {
        @Override
        public String getDescricao() {
            return "Cart�o de Cr�dito";
        }
    },
    CARTAO_DEBITO {
        @Override
        public String getDescricao() {
            return "Cart�o de D�bito";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
