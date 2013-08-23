
package br.com.alphadev.contest.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.alphadev.contest.entity.valueObject.Frete;
import br.com.alphadev.contest.entity.valueObject.Imposto;
import br.com.alphadev.contest.entity.valueObject.TipoCompraVenda;
import br.com.alphadev.core.BasicEntity;
import br.com.alphadev.util.FunctionsHelper;

/**
 * Classe de entidade Compra
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "compra")
public class Compra extends BasicEntity<Compra> implements Serializable {

    public Compra() {
        super(Compra.class);
    }

    public Compra(Long id) {
        super(Compra.class, id);
        if (getItens() != null)
            getItens().size();
        if (getPagamentos() != null)
            getPagamentos().size();
    }

    private static final long serialVersionUID = 8985583152510711676L;

    @Id
    @SequenceGenerator(name = "compra_id_seq", sequenceName = "compra_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compra_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nota_fiscal")
    private Long notaFiscal;

    @Column(name = "pedido")
    private Long pedido;

    @Column(name = "emissao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emissao;

    @Column(name = "data_pedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPedido;

    @Column(name = "previsao_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date previsaoEntrega;

    @Column(name = "transportador")
    private String transportador;

    @Column(name = "frete")
    @Enumerated(EnumType.STRING)
    private Frete frete;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoCompraVenda tipo;

    @Column(name = "observacao")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "id_prazo_pagamento")
    private PrazoPagamento prazoPagamento;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_compra", nullable = true)
    private Collection<ItemCompra> itens;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_compra")
    private Collection<Pagar> pagamentos;

    /**
     * Define o id deste Venda.
     * 
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Venda para o valor especificado.
     * 
     * @param id
     *            o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return notaFiscal atribuido
     */
    public Long getNotaFiscal() {
        return notaFiscal;
    }

    /**
     * @param notaFiscal
     *            notaFiscal a ser atribuido
     */
    public void setNotaFiscal(Long notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    /**
     * @return pedido atribuido
     */
    public Long getPedido() {
        return pedido;
    }

    /**
     * @param pedido
     *            pedido a ser atribuido
     */
    public void setPedido(Long pedido) {
        this.pedido = pedido;
    }

    /**
     * Define o emissao deste Venda.
     * 
     * @return o emissao
     */
    public Date getEmissao() {
        return this.emissao;
    }

    /**
     * Define o emissao deste Venda para o valor especificado.
     * 
     * @param emissao
     *            o novo emissao
     */
    public void setEmissao(Date lancamento) {
        this.emissao = lancamento;
    }

    public String getStringEmissao() {
        return FunctionsHelper.dateFormat(getEmissao(), "dd/MM/yyyy");
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public String getStringDataPedido() {
        return FunctionsHelper.dateFormat(getDataPedido(), "dd/MM/yyyy");
    }

    /**
     * @param previsaoEntrega previsaoEntrega a ser atribuido
     */
    public void setPrevisaoEntrega(Date previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    /**
     * @return previsaoEntrega atribuido
     */
    public Date getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public String getStringPrevisaoEntrega() {
        return FunctionsHelper.dateFormat(getPrevisaoEntrega(), "dd/MM/yyyy");
    }

    /**
     * @param transportador
     *            transportador a ser atribuido
     */
    public void setTransportador(String transportador) {
        this.transportador = transportador;
    }

    /**
     * @return transportador atribuido
     */
    public String getTransportador() {
        return transportador;
    }

    /**
     * @param frete
     *            frete a ser atribuido
     */
    public void setFrete(Frete frete) {
        this.frete = frete;
    }

    /**
     * @return frete atribuido
     */
    public Frete getFrete() {
        return frete;
    }

    /**
     * @param tipo tipo a ser atribuido
     */
    public void setTipo(TipoCompraVenda tipo) {
        this.tipo = tipo;
    }

    /**
     * @return tipo atribuido
     */
    public TipoCompraVenda getTipo() {
        return tipo;
    }

    /**
     * @param observacao
     *            observacao a ser atribuido
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return observacao atribuido
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param fornecedor
     *            fornecedor a ser atribuido
     */
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @return fornecedor atribuido
     */
    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    /**
     * @param formaPagamento
     *            formaPagamento a ser atribuido
     */
    public void setPrazoPagamento(PrazoPagamento prazoPagamento) {
        this.prazoPagamento = prazoPagamento;
    }

    /**
     * @return formaPagamento atribuido
     */
    public PrazoPagamento getPrazoPagamento() {
        return prazoPagamento;
    }

    /**
     * @param pagamentos
     *            pagamentos a ser atribuido
     */
    public void setPagamentos(Collection<Pagar> pagamentos) {
        this.pagamentos = pagamentos;
    }

    /**
     * Calcular o total da compra
     * 
     * @return = Total da compra
     */
    public double getTotalProduto() {
        if (getItens() == null)
            return 0d;
        double result = 0d;
        Iterator<ItemCompra> it = getItens().iterator();
        while (it.hasNext()) {
            ItemCompra item = it.next();
            result += item.getQuantidade() * item.getValorCompra().doubleValue();
        }
        return result;
    }

    public double getValorTotal() {
        double result = getTotalProduto();

        for (ItemCompra item : getItens()) {
            if (item.getImpostos() != null) {
                for (ImpostoCompra imposto : item.getImpostos()) {
                    if (imposto.getImposto().name().equals(Imposto.IPI.name())) {
                        double aliq = (imposto.getAliquota() / 100d);
                        double aux = (item.getSubTotal() * aliq);
                        result += aux;
                    }
                }
            }
        }

        return result;
    }

    /**
     * @return pagamentos atribuido
     */
    public Collection<Pagar> getPagamentos() {
        return pagamentos;
    }

    /**
     * @param empresa empresa a ser atribuido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return empresa atribuido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param itens
     *            itens a ser atribuido
     */
    public void setItens(Collection<ItemCompra> itens) {
        this.itens = itens;
    }

    /**
     * @return itens atribuido
     */
    public Collection<ItemCompra> getItens() {
        return itens;
    }

    public boolean isEmitido() {
        if (this.id != null && this.id != -1 && this.emissao != null)
            return true;
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Compra other = (Compra)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Compra [");
        if (emissao != null) {
            builder.append("emissao=");
            builder.append(emissao);
            builder.append(", ");
        }
        if (dataPedido != null) {
            builder.append("dataPedido=");
            builder.append(dataPedido);
            builder.append(", ");
        }
        if (fornecedor != null) {
            builder.append("fornecedor=");
            builder.append(fornecedor);
            builder.append(", ");
        }
        if (notaFiscal != null || pedido != null) {
            builder.append("numero=");
            builder.append(notaFiscal != null ? notaFiscal : pedido);
        }
        builder.append("]");
        return builder.toString();
    }
}