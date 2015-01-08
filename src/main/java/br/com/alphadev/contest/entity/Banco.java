
package br.com.alphadev.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.alphadev.core.BasicEntity;

/**
 * @author André Penteado
 * @since 14/10/2010 - 16:08:02
 */
@Entity
@Table(name = "banco")
public class Banco extends BasicEntity<Banco> implements Serializable {

    private static final long serialVersionUID = 3455562940983241751L;

    @Id
    @SequenceGenerator(name = "banco_id_seq", sequenceName = "banco_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banco_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    public Banco() {
        super(Banco.class);
    }

    public Banco(Long id) {
        super(Banco.class, id);
    }

    /**
     * @return id atribuido
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id id a ser atribuido
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return nome atribuido
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome nome a ser atribuido
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return codigo atribuido
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo codigo a ser atribuido
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        final Banco other = (Banco)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

}
