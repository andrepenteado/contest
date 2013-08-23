
package br.com.alphadev.contest.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.core.BasicEntity;
import br.com.alphadev.util.FunctionsHelper;

/**
 * Classe de entidade Receber
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "receber")
public class Receber extends BasicEntity<Receber> implements Serializable {

    public Receber() {
        super(Receber.class);
    }

    public Receber(Long id) {
        super(Receber.class, id);
        if (getRecebimentos() != null)
            getRecebimentos().size();
    }

    private static final long serialVersionUID = 4809819365110774747L;

    @Id
    @SequenceGenerator(name = "receber_id_seq", sequenceName = "receber_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receber_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_venda", insertable = false, updatable = false)
    private Venda venda;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "parcela", nullable = false)
    private int parcela;

    @Column(name = "vencimento")
    @Temporal(TemporalType.DATE)
    private Date vencimento;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "recebido")
    private Boolean recebido;

    @ManyToOne
    @JoinColumn(name = "id_tipo_conta")
    private TipoConta tipoConta;

    @OneToMany
    @JoinColumn(name = "id_receber")
    private Collection<Recebido> recebimentos;

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
     * Define o vencimento deste Receber.
     * @return o vencimento
     */
    public Date getVencimento() {
        return this.vencimento;
    }

    public String getStringVencimento() {
        return FunctionsHelper.dateFormat(getVencimento(), "dd/MM/yyyy");
    }

    /**
     * Define o vencimento deste Receber para o valor especificado.
     * @param vencimento o novo vencimento
     */
    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    /**
     * Define o valor deste Receber.
     * @return o valor
     */
    public Double getValor() {
        return this.valor;
    }

    /**
     * Define o valor deste Receber para o valor especificado.
     * @param valor o novo valor
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * Define o venda deste Receber.
     * @return o venda
     */
    public Venda getVenda() {
        return this.venda;
    }

    /**
     * Define o venda deste Receber para o valor especificado.
     * @param venda o novo venda
     */
    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    /**
     * @param parcela parcela a ser atribuido
     */
    public void setParcela(int parcela) {
        this.parcela = parcela;
    }

    /**
     * @return parcela atribuido
     */
    public int getParcela() {
        return parcela;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Receber [");
        if (descricao != null) {
            builder.append("descricao=");
            builder.append(descricao);
            builder.append(", ");
        }
        if (vencimento != null) {
            builder.append("vencimento=");
            builder.append(vencimento);
            builder.append(", ");
        }
        if (venda != null) {
            builder.append("venda=");
            builder.append(venda);
        }
        builder.append("]");
        return builder.toString();
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
        final Receber other = (Receber)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    /**
     * @return the recebimentos
     */
    public Collection<Recebido> getRecebimentos() {
        return recebimentos;
    }

    /**
     * @param recebimentos the recebimentos to set
     */
    public void setRecebimentos(Collection<Recebido> recebimentos) {
        this.recebimentos = recebimentos;
    }

    /**
     * @return descricao atribuido
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao descricao a ser atribuido
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public double getValorRecebido() {
        if (getRecebimentos() == null || getRecebimentos().size() <= 0)
            return 0;

        double totalRecebido = 0;
        for (Recebido recebido : getRecebimentos())
            totalRecebido += recebido.getValorPago().doubleValue();

        return totalRecebido;
    }

    public double getValorDevido() {
        if (getValor() == null || getValor() - getValorRecebido() < 0)
            return 0;
        return getValor() - getValorRecebido();
    }

    public double getValorJurosDia() {
        return (getValor() * (KGlobal.JUROS_DIA / 100d));
    }

    /**
     * @return recebido atribuido
     */
    public Boolean getRecebido() {
        return recebido;
    }

    /**
     * @param tipoConta tipoConta a ser atribuido
     */
    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    /**
     * @return tipoConta atribuido
     */
    public TipoConta getTipoConta() {
        return tipoConta;
    }

    /**
     * @param numeroDocumento numeroDocumento a ser atribuido
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return numeroDocumento atribuido
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
}