
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.github.andrepenteado.contest.entity.valueObject.Frete;
import com.github.andrepenteado.core.BasicEntity;
import com.github.andrepenteado.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 18/09/2007 - 23:31:39
 */
@Entity
@Table(name = "nota_fiscal")
public class NotaFiscal extends BasicEntity<NotaFiscal> implements Serializable {

    public NotaFiscal() {
        super(NotaFiscal.class);
    }

    public NotaFiscal(Long id) {
        super(NotaFiscal.class, id);
    }

    private static final long serialVersionUID = -4070642834050663561L;

    @Id
    @Column(name = "id_venda")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venda", insertable = false, updatable = false)
    private Venda venda;

    @Column(name = "numero")
    private Long numero;

    @Column(name = "emissao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emissao;

    @Column(name = "transportador")
    private String transportador;

    @Column(name = "frete")
    @Enumerated(EnumType.STRING)
    private Frete frete;

    @Column(name = "cancelada")
    private Boolean cancelada;

    @Column(name = "observacao")
    private String observacao;

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
     * @param numero
     *            numero a ser atribuido
     */
    public void setNumero(Long numero) {
        this.numero = numero;
    }

    /**
     * @return numero atribuido
     */
    public Long getNumero() {
        return numero;
    }

    /**
     * @param emissao
     *            emissao a ser atribuido
     */
    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    /**
     * @return emissao atribuido
     */
    public Date getEmissao() {
        return emissao;
    }

    public String getStringEmissao() {
        return FunctionsHelper.dateFormat(getEmissao(), "dd/MM/yyyy");
    }

    /**
     * @param transportador
     *            transportador a ser atribuido
     */
    public void setTransportador(String transportador) {
        this.transportador = transportador;
    }

    /**
     * @return transportador atribuido
     */
    public String getTransportador() {
        return transportador;
    }

    /**
     * @param frete
     *            frete a ser atribuido
     */
    public void setFrete(Frete frete) {
        this.frete = frete;
    }

    /**
     * @return frete atribuido
     */
    public Frete getFrete() {
        return frete;
    }

    /**
     * @param cancelada
     *            cancelada a ser atribuido
     */
    public void setCancelada(Boolean cancelada) {
        this.cancelada = cancelada;
    }

    /**
     * @return cancelada atribuido
     */
    public Boolean getCancelada() {
        return cancelada;
    }

    /**
     * @param observacao
     *            observacao a ser atribuido
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return observacao atribuido
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param venda
     *            venda a ser atribuido
     */
    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    /**
     * @return venda atribuido
     */
    public Venda getVenda() {
        return venda;
    }

    public double getTotalIcms() {
        double result = 0d;
        for (ItemVenda item : getVenda().getItens())
            if (item.getTotalIcms() > 0d)
                result += item.getTotalIcms();
        return result;
    }

    public double getBaseCalculoIcms() {
        double result = 0d;
        for (ItemVenda item : getVenda().getItens())
            if (item.getTotalIcms() > 0d)
                result += item.getSubTotal();
        return result;
    }

    public double getTotalPis() {
        double result = 0d;
        for (ItemVenda item : getVenda().getItens())
            if (item.getTotalPis() > 0d)
                result += item.getTotalPis();
        return result;
    }

    public double getTotalCofins() {
        double result = 0d;
        for (ItemVenda item : getVenda().getItens())
            if (item.getTotalCofins() > 0d)
                result += item.getTotalCofins();
        return result;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((emissao == null) ? 0 : emissao.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        result = prime * result + ((venda == null) ? 0 : venda.hashCode());
        return result;
    }

    /*
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
        final NotaFiscal other = (NotaFiscal)obj;
        if (emissao == null) {
            if (other.emissao != null)
                return false;
        }
        else if (!emissao.equals(other.emissao))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (numero == null) {
            if (other.numero != null)
                return false;
        }
        else if (!numero.equals(other.numero))
            return false;
        if (venda == null) {
            if (other.venda != null)
                return false;
        }
        else if (!venda.equals(other.venda))
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
        builder.append("NotaFiscal [");
        if (emissao != null) {
            builder.append("emissao=");
            builder.append(emissao);
            builder.append(", ");
        }
        if (numero != null) {
            builder.append("numero=");
            builder.append(numero);
            builder.append(", ");
        }
        if (venda != null) {
            builder.append("venda=");
            builder.append(venda);
        }
        builder.append("]");
        return builder.toString();
    }
}
