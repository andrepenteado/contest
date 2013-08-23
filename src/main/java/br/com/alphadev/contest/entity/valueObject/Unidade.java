
package br.com.alphadev.contest.entity.valueObject;

public enum Unidade {
    UNIDADE {
        @Override
        public String getDescricao() {
            return getCodigo().concat("-Unidade");
        }

        @Override
        public String getCodigo() {
            return "UN";
        }
    },
    FARDO {
        @Override
        public String getDescricao() {
            return getCodigo().concat("-Fardo");
        }

        @Override
        public String getCodigo() {
            return "FD";
        }
    },
    PACOTE {
        @Override
        public String getDescricao() {
            return getCodigo().concat("-Pacote");
        }

        @Override
        public String getCodigo() {
            return "PT";
        }
    },
    CAIXA {
        @Override
        public String getDescricao() {
            return getCodigo().concat("-Caixa");
        }

        @Override
        public String getCodigo() {
            return "CX";
        }
    },
    BOBINA {
        @Override
        public String getDescricao() {
            return getCodigo().concat("-Bobina");
        }

        @Override
        public String getCodigo() {
            return "BB";
        }
    },
    KILO {
        @Override
        public String getDescricao() {
            return getCodigo().concat("-Kilo");
        }

        @Override
        public String getCodigo() {
            return "KG";
        }
    };

    public abstract String getDescricao();

    public abstract String getCodigo();

    @Override
    public String toString() {
        return getDescricao();
    }
}
