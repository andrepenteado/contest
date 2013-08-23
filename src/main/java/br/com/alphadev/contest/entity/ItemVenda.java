
package br.com.alphadev.contest.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.alphadev.contest.entity.valueObject.Imposto;
import br.com.alphadev.core.BasicEntity;

/**
 * Classe de entidade ItemVenda
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "item_venda")
public class ItemVenda extends BasicEntity<ItemVenda> implements Serializable {

    public ItemVenda() {
        super(ItemVenda.class);
    }

    public ItemVenda(Long id) {
        super(ItemVenda.class, id);
    }

    private static final long serialVersionUID = 2127765053813253000L;

    @Id
    @SequenceGenerator(name = "item_venda_id_seq", sequenceName = "item_venda_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_venda_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Column(name = "valor_venda", nullable = false)
    private Double valorVenda;

    @Column(name = "comissao")
    private Double comissao;

    @ManyToOne
    @JoinColumn(name = "id_venda", nullable = true, insertable = false, updatable = false)
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_cfop")
    private CFOP cfop;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_item_venda", nullable = true)
    private Collection<ImpostoNotaFiscal> impostos;

    /**
     * @param id
     *            id a ser atribuido
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return id atribuido
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Define o quantidade deste ItemVenda.
     * 
     * @return o quantidade
     */
    public int getQuantidade() {
        return this.quantidade;
    }

    /**
     * Define o quantidade deste ItemVenda para o valor especificado.
     * 
     * @param quantidade
     *            o novo quantidade
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Define o valorVenda deste ItemVenda.
     * 
     * @return o valorVenda
     */
    public Double getValorVenda() {
        return this.valorVenda;
    }

    /**
     * Define o valorVenda deste ItemVenda para o valor especificado.
     * 
     * @param valorVenda
     *            o novo valorVenda
     */
    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    /**
     * @param comissao
     *            comissao a ser atribuido
     */
    public void setComissao(Double comissao) {
        this.comissao = comissao;
    }

    /**
     * @return comissao atribuido
     */
    public Double getComissao() {
        return comissao;
    }

    public Double getValorDesconto() {
        double result = 0d;
        if (getVenda().getDesconto() != null && getVenda().getDesconto() > 0)
            result += getSubTotalSemDesconto() * (getVenda().getDesconto() / 100d);
        if (getVenda().getPrazoPagamento() != null && getVenda().getPrazoPagamento().getDescontoJuros() != null
                        && getVenda().getPrazoPagamento().getDescontoJuros() < 0)
            result += getSubTotalSemDesconto() * (Math.abs(getVenda().getPrazoPagamento().getDescontoJuros()) / 100d);
        return result;
    }

    /**
     * Define o produto deste ItemVenda.
     * 
     * @return o produto
     */
    public Produto getProduto() {
        return this.produto;
    }

    /**
     * Define o produto deste ItemVenda para o valor especificado.
     * 
     * @param produto
     *            o novo produto
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * Define o venda deste ItemVenda.
     * 
     * @return o venda
     */
    public Venda getVenda() {
        return this.venda;
    }

    /**
     * Define o venda deste ItemVenda para o valor especificado.
     * 
     * @param venda
     *            o novo venda
     */
    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    /**
     * @param impostos
     *            the impostos to set
     */
    public void setImpostos(Collection<ImpostoNotaFiscal> impostos) {
        this.impostos = impostos;
    }

    /**
     * @return the impostos
     */
    public Collection<ImpostoNotaFiscal> getImpostos() {
        return impostos;
    }

    public double getSubTotalSemDesconto() {
        if (getValorVenda() != null)
            return getQuantidade() * getValorVenda().doubleValue();
        return 0d;
    }

    public double getSubTotal() {
        if (getValorVenda() != null)
            return getSubTotalSemDesconto() - getValorDesconto();
        return 0d;
    }

    /**
     * @param cfop
     *            the cfop to set
     */
    public void setCfop(CFOP cfop) {
        this.cfop = cfop;
    }

    /**
     * @return the cfop
     */
    public CFOP getCfop() {
        return cfop;
    }

    public boolean isSubstituicaoTributariaIcms() {
        for (ImpostoNotaFiscal imposto : getImpostos()) {
            if (imposto.getImposto().name().equals(Imposto.SUBSTITUICAO_TRIBUTARIA_ICMS.name()))
                return true;
        }
        return false;
    }

    /**
     * @return Valor total do imposto selecionado
     */
    public double getTotalImposto(Imposto imp) {
        if (getSubTotal() > 0)
            for (ImpostoNotaFiscal imposto : getImpostos())
                if (imposto.getImposto().name().equals(imp.name()))
                    return getSubTotal() * (imposto.getAliquota().doubleValue() / 100d);
        return 0;
    }

    /**
     * @return Valor total do ICMS
     */
    public double getTotalIss() {
        return getTotalImposto(Imposto.ISS);
    }

    /**
     * @return Valor total do ICMS
     */
    public double getTotalIcms() {
        return getTotalImposto(Imposto.ICMS);
    }

    /**
     * @return Valor total do IPI
     */
    public double getTotalIpi() {
        return getTotalImposto(Imposto.IPI);
    }

    /**
     * @return Valor total do PIS
     */
    public double getTotalPis() {
        return getTotalImposto(Imposto.PIS);
    }

    /**
     * @return Valor total do COFINS
     */
    public double getTotalCofins() {
        return getTotalImposto(Imposto.COFINS);
    }

    /**
     * @return Valor total do imposto selecionado
     */
    public double getAliquotaImposto(Imposto imp) {
        if (getImpostos() != null & !getImpostos().isEmpty())
            for (ImpostoNotaFiscal imposto : getImpostos())
                if (imposto.getImposto().name().equals(imp.toString()))
                    return imposto.getAliquota().doubleValue();
        return 0;
    }

    /**
     * @return Valor total do ICMS
     */
    public double getAliquotaIss() {
        return getAliquotaImposto(Imposto.ISS);
    }

    /**
     * @return Valor total do ICMS
     */
    public double getAliquotaIcms() {
        return getAliquotaImposto(Imposto.ICMS);
    }

    /**
     * @return Valor total do IPI
     */
    public double getAliquotaIpi() {
        return getAliquotaImposto(Imposto.IPI);
    }

    /**
     * @return Valor total do PIS
     */
    public double getAliquotaPis() {
        return getAliquotaImposto(Imposto.PIS);
    }

    /**
     * @return Valor total do COFINS
     */
    public double getAliquotaCofins() {
        return getAliquotaImposto(Imposto.COFINS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ItemVenda [");
        if (produto != null) {
            builder.append("produto=");
            builder.append(produto);
            builder.append(", ");
        }
        if (venda != null) {
            builder.append("venda=");
            builder.append(venda);
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ItemVenda other = (ItemVenda)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }
}