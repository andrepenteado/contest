
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.github.andrepenteado.core.BasicEntity;

@Entity
@Table(name = "estoque")
public class Estoque extends BasicEntity<Estoque> implements Serializable {

    public Estoque() {
        super(Estoque.class);
    }

    public Estoque(Long id) {
        super(Estoque.class, id);
    }

    private static final long serialVersionUID = -3857026433138038674L;

    @Id
    @SequenceGenerator(name = "estoque_id_seq", sequenceName = "estoque_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estoque_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "spec_in")
    private Integer specIn;

    @Column(name = "spec_out")
    private Integer specOut;

    @ManyToOne
    @JoinColumn(name = "id_produto", insertable = false, updatable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    /**
     * @param produto produto a ser atribuido.
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * @return Retornar produto.
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * @param id id a ser atribuido.
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Retornar id.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @return specIn atribuido
     */
    public Integer getSpecIn() {
        return specIn;
    }

    /**
     * @param specIn specIn a ser atribuido
     */
    public void setSpecIn(Integer specIn) {
        this.specIn = specIn;
    }

    /**
     * @return specOut atribuido
     */
    public Integer getSpecOut() {
        return specOut;
    }

    /**
     * @param specOut specOut a ser atribuido
     */
    public void setSpecOut(Integer specOut) {
        this.specOut = specOut;
    }

    /**
     * @return empresa atribuido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa empresa a ser atribuido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
        final Estoque other = (Estoque)obj;
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
        builder.append("Estoque [");
        if (produto != null) {
            builder.append("produto=");
            builder.append(produto);
            builder.append(", ");
        }
        if (specIn != null) {
            builder.append("specIn=");
            builder.append(specIn);
            builder.append(", ");
        }
        if (specOut != null) {
            builder.append("specOut=");
            builder.append(specOut);
        }
        builder.append("]");
        return builder.toString();
    }
}
