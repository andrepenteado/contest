
package com.github.andrepenteado.contest.entity.valueObject;

public enum VistaPrazo {
    VISTA {
        @Override
        public String getDescricao() {
            return "À Vista";
        }
    },
    PRAZO {
        @Override
        public String getDescricao() {
            return "À Prazo";
        }
    };

    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
