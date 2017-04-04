
package com.github.andrepenteado.contest.entity.valueObject;

/**
 *
 * @author André Penteado
 */
public enum Imposto {

    ICMS {
        @Override
        public String getDescricao() {
            return "ICMS";
        }
    },
    SUBSTITUICAO_TRIBUTARIA_ICMS {
        @Override
        public String getDescricao() {
            return "S.T. ICMS";
        }
    },
    IPI {
        @Override
        public String getDescricao() {
            return "IPI";
        }
    },
    PIS {
        @Override
        public String getDescricao() {
            return "PIS";
        }
    },
    COFINS {
        @Override
        public String getDescricao() {
            return "COFINS";
        }
    },
    ISS {
        @Override
        public String getDescricao() {
            return "ISS";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
