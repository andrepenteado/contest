
package com.github.andrepenteado.contest.entity.valueObject;

/**
 *
 * @author André Penteado
 */
public enum TipoProduto {

    VENDA {
        @Override
        public String getDescricao() {
            return "Venda";
        }
    },
    LOCACAO {
        @Override
        public String getDescricao() {
            return "Locação";
        }
    },
    SERVICO {
        @Override
        public String getDescricao() {
            return "Serviço";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
