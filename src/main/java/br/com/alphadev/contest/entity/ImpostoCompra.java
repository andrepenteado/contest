package br.com.alphadev.contest.entity;

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

import br.com.alphadev.contest.entity.valueObject.Imposto;
import br.com.alphadev.core.BasicEntity;

/**
 * Classe de entidade ItemVenda
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "imposto_compra")
public class ImpostoCompra extends BasicEntity<ImpostoCompra> implements
		Serializable {

	public ImpostoCompra() {
		super(ImpostoCompra.class);
	}

	public ImpostoCompra(Long id) {
		super(ImpostoCompra.class, id);
	}

	private static final long serialVersionUID = 2127765053813253000L;

	@Id
	@SequenceGenerator(name = "imposto_compra_id_seq", sequenceName = "imposto_compra_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imposto_compra_id_seq")
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_item_compra", nullable = true, insertable = false, updatable = false)
	private ItemCompra itemCompra;

	@Column(name = "imposto")
	@Enumerated(EnumType.STRING)
	private Imposto imposto;

	@Column(name = "aliquota")
	private Double aliquota;

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
	 * @param itemCompra the itemCompra to set
	 */
	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	/**
	 * @return the itemCompra
	 */
	public ItemCompra getItemCompra() {
		return itemCompra;
	}

	/**
	 * @return imposto atribuido
	 */
	public Imposto getImposto() {
		return imposto;
	}

	/**
	 * @param imposto
	 *            imposto a ser atribuido
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
	 * @param aliquota
	 *            aliquota a ser atribuido
	 */
	public void setAliquota(Double aliquota) {
		this.aliquota = aliquota;
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
		ImpostoCompra other = (ImpostoCompra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		builder.append("AliquotaImposto [imposto=");
		builder.append(imposto);
		builder.append("]");
		return builder.toString();
	}
}