
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.alphadev.contest.entity.valueObject.FormaPagamento;
import br.com.alphadev.contest.entity.valueObject.TipoCompraVenda;
import br.com.alphadev.contest.entity.valueObject.TipoProduto;
import br.com.alphadev.contest.services.VendaService;
import br.com.alphadev.core.BasicEntity;
import br.com.alphadev.util.FunctionsHelper;

/**
 * Classe de entidade Venda
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "venda")
public class Venda extends BasicEntity<Venda> implements Serializable {

    public Venda() {
        super(Venda.class);
    }

    public Venda(Long id) {
        super(Venda.class, id);
        if (getItens() != null)
            getItens().size();
        if (getRecebimentos() != null)
            getRecebimentos().size();
    }

    private static final long serialVersionUID = -194693735833077801L;

    @Id
    @SequenceGenerator(name = "venda_id_seq", sequenceName = "venda_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venda_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "data_lancamento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLancamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoCompraVenda tipo;

    @Column(name = "pedido")
    private Long pedido;

    @Column(name = "desconto")
    private Integer desconto;

    @Column(name = "observacao")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_prazo_pagamento")
    private PrazoPagamento prazoPagamento;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @OneToOne(mappedBy = "venda")
    private NotaFiscal notaFiscal;

    @OneToOne(mappedBy = "venda")
    private Orcamento orcamento;

    @OneToOne(mappedBy = "venda")
    private Comissao comissao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_venda", nullable = true)
    private Collection<ItemVenda> itens;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_venda")
    private Collection<Receber> recebimentos;

    /**
     * Define o id deste Venda.
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Venda para o valor especificado.
     * @param id o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o lancamento deste Venda.
     * @return o lancamento
     */
    public Date getDataLancamento() {
        return this.dataLancamento;
    }

    /**
     * Define o lancamento deste Venda para o valor especificado.
     * @param lancamento o novo lancamento
     */
    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getStringDataLancamento() {
        return FunctionsHelper.dateFormat(getDataLancamento(), "dd/MM/yyyy");
    }

    public Date getEmissao() {
        if (getNotaFiscal() != null)
            return getNotaFiscal().getEmissao();
        else if (getOrcamento() != null)
            return getOrcamento().getEmissao();
        return null;
    }

    /**
     * @param formaPagamento formaPagamento a ser atribuido
     */
    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    /**
     * @return formaPagamento atribuido
     */
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
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
     * Define o pedido deste Venda.
     * @return o pedido
     */
    public Long getPedido() {
        return this.pedido;
    }

    /**
     * Define o pedido deste Venda para o valor especificado.
     * @param pedido o novo pedido
     */
    public void setPedido(Long pedido) {
        this.pedido = pedido;
    }

    /**
     * @param desconto desconto a ser atribuido
     */
    public void setDesconto(Integer desconto) {
        this.desconto = desconto;
    }

    /**
     * @return desconto atribuido
     */
    public Integer getDesconto() {
        return desconto;
    }

    public Double getValorDesconto() {
        return getValorTotalProduto() - getValorTotal();
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getObservacao() {
        return observacao;
    }

    /**
     * @param comissao comissao a ser atribuido
     */
    public void setComissao(Comissao comissao) {
        this.comissao = comissao;
    }

    /**
     * @return comissao atribuido
     */
    public Comissao getComissao() {
        return comissao;
    }

    /**
     * @param cliente cliente a ser atribuido
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return cliente atribuido
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param formaPagamento formaPagamento a ser atribuido
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
     * @param funcionario funcionario a ser atribuido
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return funcionario atribuido
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param recebimentos recebimentos a ser atribuido
     */
    public void setRecebimentos(Collection<Receber> recebimentos) {
        this.recebimentos = recebimentos;
    }

    /**
     * Calcular o total da venda
     * 
     * @return = Total da venda
     */
    public double getValorTotal() {
        return VendaService.aplicarJuroDesconto(this, getValorTotalProduto() + getValorTotalServicos());
    }

    /**
     * Calcular o total de produtos da venda
     * 
     * @return = Total da venda
     */
    public double getValorTotalProduto() {
        if (getItens() == null)
            return 0d;
        double total = 0d;
        Iterator<ItemVenda> it = getItens().iterator();
        while (it.hasNext()) {
            ItemVenda item = it.next();
            if (item.getValorVenda() != null && !TipoProduto.SERVICO.name().equals(item.getProduto().getTipo().name()))
                total += item.getQuantidade() * item.getValorVenda().doubleValue();
        }
        return total;
    }

    /**
     * Calcular o total de produtos da venda
     * 
     * @return = Total da venda
     */
    public double getValorTotalServicos() {
        if (getItens() == null)
            return 0d;
        double total = 0d;
        Iterator<ItemVenda> it = getItens().iterator();
        while (it.hasNext()) {
            ItemVenda item = it.next();
            if (item.getValorVenda() != null && TipoProduto.SERVICO.name().equals(item.getProduto().getTipo().name()))
                total += item.getQuantidade() * item.getValorVenda().doubleValue();
        }
        return total;
    }

    /**
     * Calcular a quantidade total da produtos
     * @return = Quantidade Total de produtos
     */
    public int getQuantidadeTotal() {
        if (getItens() == null)
            return 0;
        int result = 0;
        Iterator<ItemVenda> it = getItens().iterator();
        while (it.hasNext()) {
            ItemVenda item = it.next();
            result += item.getQuantidade();
        }
        return result;
    }

    /**
     * Verifica se venda foi totalmente ou parcialmente
     * consolidada [paga]
     * 
     * @return Sim ou Não
     */
    public boolean isConsolidada() {
        boolean result = false;
        if (getRecebimentos() != null && !getRecebimentos().isEmpty()) {
            for (Receber receber : getRecebimentos()) {
                if (receber.getRecebido() != null && receber.getRecebido()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @return Objeto com os dados da 1a. parcela, caso houver,
     * para ser usado no relatório a ser gerado pelo iReport
     */
    public double getPrimeiraParcela() {
        if (getRecebimentos() != null && getRecebimentos().size() >= 1)
            return ((Receber)getRecebimentos().toArray()[0]).getValor().doubleValue();
        return 0;
    }

    /**
     * @return Objeto com os dados da 2a. parcela, caso houver,
     * para ser usado no relatório a ser gerado pelo iReport
     */
    public double getSegundaParcela() {
        if (getRecebimentos() != null && getRecebimentos().size() >= 2)
            return ((Receber)getRecebimentos().toArray()[1]).getValor().doubleValue();
        return 0;
    }

    /**
     * @return Objeto com os dados da 3a. parcela, caso houver,
     * para ser usado no relatório a ser gerado pelo iReport
     */
    public double getTerceiraParcela() {
        if (getRecebimentos() != null && getRecebimentos().size() >= 3)
            return ((Receber)getRecebimentos().toArray()[2]).getValor().doubleValue();
        return 0;
    }

    /**
     * @return Objeto com os dados da 1a. vencimento, caso houver,
     * para ser usado no relatório a ser gerado pelo iReport
     */
    public Date getPrimeiroVencimento() {
        if (getRecebimentos() != null && getRecebimentos().size() >= 1)
            return ((Receber)getRecebimentos().toArray()[0]).getVencimento();
        return null;
    }

    /**
     * @return Objeto com os dados da 2a. vencimento, caso houver,
     * para ser usado no relatório a ser gerado pelo iReport
     */
    public Date getSegundoVencimento() {
        if (getRecebimentos() != null && getRecebimentos().size() >= 2)
            return ((Receber)getRecebimentos().toArray()[1]).getVencimento();
        return null;
    }

    /**
     * @return Objeto com os dados da 3a. vencimento, caso houver,
     * para ser usado no relatório a ser gerado pelo iReport
     */
    public Date getTerceiroVencimento() {
        if (getRecebimentos() != null && getRecebimentos().size() >= 3)
            return ((Receber)getRecebimentos().toArray()[2]).getVencimento();
        return null;
    }

    /**
     * @return recebimentos atribuido
     */
    public Collection<Receber> getRecebimentos() {
        return recebimentos;
    }

    /**
     * @param itens itens a ser atribuido
     */
    public void setItens(Collection<ItemVenda> itens) {
        this.itens = itens;
    }

    /**
     * @return itens atribuido
     */
    public Collection<ItemVenda> getItens() {
        return itens;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        final Venda other = (Venda)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return notaFiscal atribuido
     */
    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }

    /**
     * @param notaFiscal notaFiscal a ser atribuido
     */
    public void setNotaFiscal(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    /**
     * @return orcamento atribuido
     */
    public Orcamento getOrcamento() {
        return orcamento;
    }

    /**
     * @param orcamento orcamento a ser atribuido
     */
    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Venda [");
        if (cliente != null) {
            builder.append("cliente=");
            builder.append(cliente);
            builder.append(", ");
        }
        else {
            builder.append("cliente=CONSUMIDOR, ");
        }
        if (dataLancamento != null) {
            builder.append("dataLancamento=");
            builder.append(dataLancamento);
        }
        builder.append("]");
        return builder.toString();
    }
}
