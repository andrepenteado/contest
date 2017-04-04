
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.github.andrepenteado.contest.entity.valueObject.Imposto;
import com.github.andrepenteado.core.BasicEntity;

/**
 * Classe de entidade ItemVenda
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "imposto_nota_fiscal")
public class ImpostoNotaFiscal extends BasicEntity<ImpostoNotaFiscal> implements Serializable {

    public ImpostoNotaFiscal() {
        super(ImpostoNotaFiscal.class);
    }

    public ImpostoNotaFiscal(Long id) {
        super(ImpostoNotaFiscal.class, id);
    }

    private static final long serialVersionUID = 2127765053813253000L;

    @Id
    @SequenceGenerator(name = "imposto_nota_fiscal_id_seq", sequenceName = "imposto_nota_fiscal_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imposto_nota_fiscal_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_item_venda", nullable = true, insertable = false, updatable = false)
    private ItemVenda itemVenda;

    @Column(name = "imposto")
    @Enumerated(EnumType.STRING)
    private Imposto imposto;

    @Column(name = "aliquota")
    private Double aliquota;

    /**
     * @param id id a ser atribuido
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
	 * @param itemVenda the itemVenda to set
	 */
	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}

	/**
	 * @return the itemVenda
	 */
	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	/**
     * @return imposto atribuido
     */
    public Imposto getImposto() {
        return imposto;
    }

    /**
     * @param imposto imposto a ser atribuido
     */
    public void setImposto(Imposto imposto) {
        this.imposto = imposto;
    }

    /**
     * @return aliquota atribuido
     */
    public Double getAliquota() {
        return aliquota;
    }

    /**
     * @param aliquota aliquota a ser atribuido
     */
    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
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
        ImpostoNotaFiscal other = (ImpostoNotaFiscal)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AliquotaImposto [imposto=");
        builder.append(imposto);
        builder.append("]");
        return builder.toString();
    }
}