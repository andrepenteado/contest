
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.github.andrepenteado.core.BasicEntity;

/**
 * Classe de entidade TabelaPreco
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "tabela_preco")
public class TabelaPreco extends BasicEntity<TabelaPreco> implements Serializable {

    public TabelaPreco() {
        super(TabelaPreco.class);
    }

    public TabelaPreco(Long id) {
        super(TabelaPreco.class, id);
    }

    private static final long serialVersionUID = 161128367087692L;

    @Id
    @SequenceGenerator(name = "tabela_preco_id_seq", sequenceName = "tabela_preco_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tabela_preco_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "observacao")
    private String observacao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_tabela_preco")
    private Collection<FaixaPreco> faixas;

    /**
     * Define o id deste GrupoProduto.
     * 
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste GrupoProduto para o valor especificado.
     * 
     * @param id
     *            o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o descricao deste GrupoProduto.
     * 
     * @return o descricao
     */
    public String getDescricao() {
        return this.descricao;
    }

    /**
     * Define o descricao deste GrupoProduto para o valor especificado.
     * 
     * @param descricao
     *            o novo descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Define o observacao deste GrupoProduto.
     * 
     * @return o observacao
     */
    public String getObservacao() {
        return this.observacao;
    }

    /**
     * Define o observacao deste GrupoProduto para o valor especificado.
     * 
     * @param observacao
     *            o novo observacao
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @param faixas faixas a ser atribuido
     */
    public void setFaixas(Collection<FaixaPreco> faixas) {
        this.faixas = faixas;
    }

    /**
     * @return faixas atribuido
     */
    public Collection<FaixaPreco> getFaixas() {
        return faixas;
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
        final TabelaPreco other = (TabelaPreco)obj;
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
        builder.append("TabelaPreco [descricao=");
        builder.append(descricao);
        builder.append("]");
        return builder.toString();
    }
}
