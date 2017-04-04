
package com.github.andrepenteado.contest.entity;

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

import com.github.andrepenteado.contest.entity.valueObject.Imposto;
import com.github.andrepenteado.core.BasicEntity;

/**
 * Classe de entidade ItemCompra
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "item_compra")
public class ItemCompra extends BasicEntity<ItemCompra> implements Serializable {

    public ItemCompra() {
        super(ItemCompra.class);
    }

    public ItemCompra(Long id) {
        super(ItemCompra.class, id);
    }

    private static final long serialVersionUID = 3141980235223689073L;

    @Id
    @SequenceGenerator(name = "item_compra_id_seq", sequenceName = "item_compra_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_compra_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Column(name = "valor_compra", nullable = false)
    private Double valorCompra;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = true, insertable = false, updatable = false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_cfop")
    private CFOP cfop;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_item_compra", nullable = true)
    private Collection<ImpostoCompra> impostos;

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
     * Define o quantidade deste ItemCompra.
     * 
     * @return o quantidade
     */
    public int getQuantidade() {
        return this.quantidade;
    }

    /**
     * Define o quantidade deste ItemCompra para o valor especificado.
     * 
     * @param quantidade
     *            o novo quantidade
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Define o valorCompra deste ItemCompra.
     * 
     * @return o valorCompra
     */
    public Double getValorCompra() {
        return this.valorCompra;
    }

    /**
     * Define o valorCompra deste ItemCompra para o valor especificado.
     * 
     * @param valorCompra
     *            o novo valorCompra
     */
    public void setValorCompra(Double valorCompra) {
        this.valorCompra = valorCompra;
    }

    /**
     * Define o produto deste ItemCompra.
     * 
     * @return o produto
     */
    public Produto getProduto() {
        return this.produto;
    }

    /**
     * Define o produto deste ItemCompra para o valor especificado.
     * 
     * @param produto
     *            o novo produto
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * Define o compra deste ItemCompa.
     * 
     * @return o compra
     */
    public Compra getCompra() {
        return this.compra;
    }

    /**
     * Define o compra deste ItemCompra para o valor especificado.
     * 
     * @param compra
     *            o novo compra
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    /**
     * @param impostos
     *            the impostos to set
     */
    public void setImpostos(Collection<ImpostoCompra> impostos) {
        this.impostos = impostos;
    }

    /**
     * @return the impostos
     */
    public Collection<ImpostoCompra> getImpostos() {
        return impostos;
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

    public double getSubTotal() {
        return getQuantidade() * getValorCompra().doubleValue();
    }

    public boolean isSubstituicaoTributariaIcms() {
        for (ImpostoCompra imposto : getImpostos()) {
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
            for (ImpostoCompra imposto : getImpostos())
                if (imposto.getImposto().name().equals(imp.toString()))
                    return getSubTotal() * (imposto.getAliquota().doubleValue() / 100d);
        return 0;
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
        if (getImpostos() != null && !getImpostos().isEmpty())
            for (ImpostoCompra imposto : getImpostos())
                if (imposto.getImposto().name().equals(imp.toString()))
                    return imposto.getAliquota().doubleValue();
        return 0;
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
        final ItemCompra other = (ItemCompra)obj;
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
        builder.append("ItemCompra [");
        if (compra != null) {
            builder.append("compra=");
            builder.append(compra);
            builder.append(", ");
        }
        if (produto != null) {
            builder.append("produto=");
            builder.append(produto);
        }
        builder.append("]");
        return builder.toString();
    }
}