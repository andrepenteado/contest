
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

/**
 * Classe de entidade FaixaPreco
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "faixa_preco")
public class FaixaPreco extends BasicEntity<FaixaPreco> implements Serializable {

    public FaixaPreco() {
        super(FaixaPreco.class);
    }

    public FaixaPreco(Long id) {
        super(FaixaPreco.class, id);
    }

    private static final long serialVersionUID = 161128367087692L;

    @Id
    @SequenceGenerator(name = "faixa_preco_id_seq", sequenceName = "faixa_preco_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faixa_preco_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "markup_vista")
    private Double markupVista;

    @Column(name = "markup_prazo")
    private Double markupPrazo;

    @Column(name = "comissao")
    private Double comissao;

    @ManyToOne
    @JoinColumn(name = "id_tabela_preco", referencedColumnName = "id")
    private TabelaPreco tabelaPreco;

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
     * @param markupVista markup a ser atribuido
     */
    public void setMarkupVista(Double markupVista) {
        this.markupVista = markupVista;
    }

    /**
     * @return markup atribuido
     */
    public Double getMarkupVista() {
        return markupVista;
    }

    /**
     * @param markupPrazo markupPrazo a ser atribuido
     */
    public void setMarkupPrazo(Double markupPrazo) {
        this.markupPrazo = markupPrazo;
    }

    /**
     * @return markupPrazo atribuido
     */
    public Double getMarkupPrazo() {
        return markupPrazo;
    }

    /**
     * @param comissao comissao a ser atribuido
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

    /**
     * @param tabelaPreco tabelaPreco a ser atribuido
     */
    public void setTabelaPreco(TabelaPreco tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
    }

    /**
     * @return tabelaPreco atribuido
     */
    public TabelaPreco getTabelaPreco() {
        return tabelaPreco;
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
        final FaixaPreco other = (FaixaPreco)obj;
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
        builder.append("FaixaPreco [descricao=");
        builder.append(descricao);
        builder.append("]");
        return builder.toString();
    }
}
