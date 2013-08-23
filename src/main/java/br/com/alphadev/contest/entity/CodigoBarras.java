
package br.com.alphadev.contest.entity;

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

import br.com.alphadev.core.BasicEntity;

@Entity
@Table(name = "codigo_barras")
public class CodigoBarras extends BasicEntity<CodigoBarras> implements Serializable {

    public CodigoBarras() {
        super(CodigoBarras.class);
    }

    public CodigoBarras(Long id) {
        super(CodigoBarras.class, id);
    }

    private static final long serialVersionUID = 5977189461948538487L;

    @Id
    @SequenceGenerator(name = "codigo_barras_id_seq", sequenceName = "codigo_barras_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codigo_barras_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "codigo_barras")
    private String codigoBarras;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

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
     * @param codigoBarras codigoBarras a ser atribuido
     */
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    /**
     * @return codigoBarras atribuido
     */
    public String getCodigoBarras() {
        return codigoBarras;
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
        final CodigoBarras other = (CodigoBarras)obj;
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
        builder.append("CodigoBarras [");
        if (codigoBarras != null) {
            builder.append("codigoBarras=");
            builder.append(codigoBarras);
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
