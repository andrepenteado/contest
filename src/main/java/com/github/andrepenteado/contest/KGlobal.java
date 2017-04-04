
package com.github.andrepenteado.contest;

import com.github.andrepenteado.contest.entity.valueObject.VistaPrazo;
import com.github.andrepenteado.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 13/11/2007 - 16:15:24
 */
public final class KGlobal {

    public static final String CATEGORIA_SUPERUSUARIO = "superusuario";
    public static final String CATEGORIA_ADMINISTRATIVO = "administrativo";
    public static final String CATEGORIA_VENDAS = "vendas";
    public static final String CATEGORIA_COMPRAS = "compras";
    public static final String CATEGORIA_FINANCEIRO = "financeiro";
    public static final String CATEGORIA_PDV = "pdv";
    public static final String CATEGORIA_VENDEDOR = "vendedor";
    public static final String CATEGORIA_ESTOQUE = "estoque";
    public static final String CATEGORIA_RECEPCAO = "recepcao";

    public static final double JUROS_DIA = 0.08d;

    public static enum TipoPesquisaCliente {
        CODIGO_NOME, TELEFONE_EMAIL, RUA_CEP, CIDADE, VENDEDOR
    };

    public static enum TipoPesquisaFornecedor {
        CODIGO_NOME, TELEFONE_EMAIL, RUA_CEP, CIDADE, CNPJ
    };

    public static enum TipoPesquisaProduto {
        CODIGO_REFERENCIA_DESCRICAO, GRUPO_PRODUTO, FORNECEDOR
    };

    public static enum TipoPesquisaVenda {
        NUMERO, CLIENTE, EMISSAO, VENCIMENTO
    };

    public static enum TipoPesquisaCompra {
        NUMERO, FORNECEDOR, EMISSAO, VENCIMENTO
    };

    public static enum TipoPesquisaPagar {
        NUMERO, FORNECEDOR, EMISSAO, VENCIMENTO, TIPO_CONTA
    };

    public static enum TipoPesquisaReceber {
        NUMERO, CLIENTE, EMISSAO, VENCIMENTO, TIPO_CONTA
    };

    public static enum EntradaSaida {
        ENTRADA {
            @Override
            public String getDescricao() {
                return "Entrada de Produto";
            }
        },
        SAIDA {
            @Override
            public String getDescricao() {
                return "Saída de Produto";
            }
        };

        public abstract String getDescricao();

        @Override
        public String toString() {
            return getDescricao();
        }
    };

    public static class ComboPrecoComissao {
        double preco;
        double comissao;
        private VistaPrazo vistaPrazo;

        public double getPreco() {
            return preco;
        }

        public void setPreco(double preco) {
            this.preco = preco;
        }

        public double getComissao() {
            return comissao;
        }

        public void setComissao(double comissao) {
            this.comissao = comissao;
        }

        public void setVistaPrazo(VistaPrazo vistaPrazo) {
            this.vistaPrazo = vistaPrazo;
        }

        public VistaPrazo getVistaPrazo() {
            return vistaPrazo;
        }

        public String getChavePrecoComissao() {
            if (getPreco() > 0d && getComissao() > 0d)
                return getPreco() + "|" + getComissao();
            return "";
        }

        public String getStringPreco() {
            if (getPreco() > 0d)
                return FunctionsHelper.numberFormat(getPreco(), "#0.00");
            return "0,00";
        }
    }
}
