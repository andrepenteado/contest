
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.github.andrepenteado.core.BasicEntity;

/**
 * @author Andre Penteado
 * @since 18/09/2007 - 23:31:39
 */
@Entity
@Table(name = "nfe")
public class NFe extends BasicEntity<NFe> implements Serializable {

    public NFe() {
        super(NFe.class);
    }

    public NFe(Long id) {
        super(NFe.class, id);
    }

    private static final long serialVersionUID = -4070642834050663561L;

    @Id
    @Column(name = "id_venda")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venda", insertable = false, updatable = false)
    private NotaFiscal notaFiscal;

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
        NFe other = (NFe)obj;
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
        builder.append("NFe [");
        if (notaFiscal != null) {
            builder.append("notaFiscal=");
            builder.append(notaFiscal);
        }
        builder.append("]");
        return builder.toString();
    }
}
