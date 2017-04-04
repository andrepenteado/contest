
package com.github.andrepenteado.contest.entity.valueObject;

/**
 *
 * @author Andr� Penteado
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
            return "Loca��o";
        }
    },
    SERVICO {
        @Override
        public String getDescricao() {
            return "Servi�o";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
