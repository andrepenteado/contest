
package com.github.andrepenteado.contest.entity.valueObject;

public enum Frete {
    CIF {
        @Override
        public String getDescricao() {
            return "CIF";
        }

        @Override
        public String getCodigo() {
            return "1";
        }
    },
    FOB {
        @Override
        public String getDescricao() {
            return "FOB";
        }

        @Override
        public String getCodigo() {
            return "2";
        }
    };

    public abstract String getDescricao();

    public abstract String getCodigo();

    @Override
    public String toString() {
        return getDescricao();
    }
}
