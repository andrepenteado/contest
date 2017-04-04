
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.github.andrepenteado.contest.KGlobal.EntradaSaida;
import com.github.andrepenteado.contest.entity.valueObject.TipoMovimentacao;
import com.github.andrepenteado.contest.entity.valueObject.VistaPrazo;
import com.github.andrepenteado.core.BasicEntity;

@Entity
@Table(name = "movimento_estoque")
public class MovimentoEstoque extends BasicEntity<MovimentoEstoque> implements Serializable {

    public MovimentoEstoque() {
        super(MovimentoEstoque.class);
    }

    public MovimentoEstoque(Long id) {
        super(MovimentoEstoque.class, id);
    }

    private static final long serialVersionUID = -3857026433138038674L;

    @Id
    @SequenceGenerator(name = "movimento_estoque_id_seq", sequenceName = "movimento_estoque_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimento_estoque_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @OneToOne
    @JoinColumn(name = "id_item_venda")
    private ItemVenda itemVenda;

    @OneToOne
    @JoinColumn(name = "id_item_compra")
    private ItemCompra itemCompra;

    @Column(name = "tipo_movimentacao")
    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoMovimentacao;

    @OneToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @Column(name = "vista_prazo")
    @Enumerated(EnumType.STRING)
    private VistaPrazo vistaPrazo;

    @Column(name = "spec_in")
    private Integer specIn;

    @Column(name = "spec_out")
    private Integer specOut;

    @Column(name = "observacao")
    private String observacao;

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
     * @return itemVenda atribuido
     */
    public ItemVenda getItemVenda() {
        return itemVenda;
    }

    /**
     * @param itemVenda itemVenda a ser atribuido
     */
    public void setItemVenda(ItemVenda itemVenda) {
        this.itemVenda = itemVenda;
    }

    /**
     * @return itemCompra atribuido
     */
    public ItemCompra getItemCompra() {
        return itemCompra;
    }

    /**
     * @param itemCompra itemCompra a ser atribuido
     */
    public void setItemCompra(ItemCompra itemCompra) {
        this.itemCompra = itemCompra;
    }

    /**
     * @param tipoMovimentacao tipoMovimentacao a ser atribuido
     */
    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    /**
     * @return tipoMovimentacao atribuido
     */
    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    /**
     * @param vistaPrazo vistaPrazo a ser atribuido
     */
    public void setVistaPrazo(VistaPrazo vistaPrazo) {
        this.vistaPrazo = vistaPrazo;
    }

    /**
     * @return vistaPrazo atribuido
     */
    public VistaPrazo getVistaPrazo() {
        return vistaPrazo;
    }

    /**
     * @return data atribuido
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data data a ser atribuido
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return estoque atribuido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param estoque estoque a ser atribuido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return observacao atribuido
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao observacao a ser atribuido
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getQuantidade() {
        if (getItemVenda() != null)
            return getItemVenda().getQuantidade();
        else if (getItemCompra() != null)
            return getItemCompra().getQuantidade();
        return 0;
    }

    public Produto getProduto() {
        if (getItemVenda() != null)
            return getItemVenda().getProduto();
        else if (getItemCompra() != null)
            return getItemCompra().getProduto();
        return null;
    }

    public Double getValor() {
        if (getItemVenda() != null)
            return getItemVenda().getValorVenda();
        else if (getItemCompra() != null)
            return getItemCompra().getValorCompra();
        return 0d;
    }

    public EntradaSaida getTipoMovimento() {
        if (getItemCompra() != null)
            return EntradaSaida.ENTRADA;
        else if (getItemVenda() != null)
            return EntradaSaida.SAIDA;
        return null;
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
        MovimentoEstoque other = (MovimentoEstoque)obj;
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
        builder.append("MovimentoEstoque [");
        if (itemCompra != null) {
            builder.append("itemCompra=");
            builder.append(itemCompra);
            builder.append(", ");
        }
        if (itemVenda != null) {
            builder.append("itemVenda=");
            builder.append(itemVenda);
        }
        builder.append("]");
        return builder.toString();
    }
}
