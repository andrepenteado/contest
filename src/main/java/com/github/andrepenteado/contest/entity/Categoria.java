
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.andrepenteado.core.BasicEntity;

/**
 * Classe de entidade Categoria
 * @author André Penteado
 * @since 30 de Maio de 2007, 14:50
 */
@Entity
@Table(name = "categoria")
public class Categoria extends BasicEntity<Categoria> implements Serializable {

    public Categoria() {
        super(Categoria.class);
    }

    public Categoria(Long id) {
        super(Categoria.class, id);
    }

    private static final long serialVersionUID = -4060757426121327282L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    /**
     * Define o id deste Categoria.
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Categoria para o valor especificado.
     * @param id o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o nome deste Categoria.
     * @return o nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Define o nome deste Categoria para o valor especificado.
     * @param nome o novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define o descricao deste Categoria.
     * @return o descricao
     */
    public String getDescricao() {
        return this.descricao;
    }

    /**
     * Define o descricao deste Categoria para o valor especificado.
     * @param descricao o novo descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        final Categoria other = (Categoria)obj;
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
        builder.append("Categoria [");
        if (descricao != null) {
            builder.append("descricao=");
            builder.append(descricao);
        }
        builder.append("]");
        return builder.toString();
    }
}
