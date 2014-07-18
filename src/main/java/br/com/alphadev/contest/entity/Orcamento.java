
package br.com.alphadev.contest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.alphadev.core.BasicEntity;
import br.com.alphadev.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 18/09/2007 - 23:31:39
 */
@Entity
@Table(name = "orcamento")
public class Orcamento extends BasicEntity<Orcamento> implements Serializable {

    public Orcamento() {
        super(Orcamento.class);
    }

    public Orcamento(Long id) {
        super(Orcamento.class, id);
    }

    private static final long serialVersionUID = 8523939534263334568L;

    @Id
    @Column(name = "id_venda")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venda", insertable = false, updatable = false)
    private Venda venda;

    @Column(name = "emissao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date emissao;

    @Column(name = "transportador")
    private String transportador;

    @Column(name = "observacao")
    private String observacao;

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
     * @param emissao emissao a ser atribuido
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
     * @param transportador transportador a ser atribuido
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
     * @param observacao observacao a ser atribuido
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
     * @param venda venda a ser atribuido
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

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((emissao == null) ? 0 : emissao.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        final Orcamento other = (Orcamento)obj;
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
        if (venda == null) {
            if (other.venda != null)
                return false;
        }
        else if (!venda.equals(other.venda))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Orcamento [");
        if (emissao != null) {
            builder.append("emissao=");
            builder.append(emissao);
            builder.append(", ");
        }
        if (id != null) {
            builder.append("id=");
            builder.append(id);
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
