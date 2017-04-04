
package com.github.andrepenteado.contest.entity.valueObject;

public enum VistaPrazo {
    VISTA {
        @Override
        public String getDescricao() {
            return "� Vista";
        }
    },
    PRAZO {
        @Override
        public String getDescricao() {
            return "� Prazo";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
