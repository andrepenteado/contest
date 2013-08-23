
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

import br.com.alphadev.core.BasicEntity;
import br.com.alphadev.util.FunctionsHelper;

/**
 * Classe de entidade Pagar
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "pagar")
public class Pagar extends BasicEntity<Pagar> implements Serializable {

    public Pagar() {
        super(Pagar.class);
    }

    public Pagar(Long id) {
        super(Pagar.class, id);
        if (getPagamentos() != null)
            getPagamentos().size();
    }

    private static final long serialVersionUID = -8860279845475251506L;

    @Id
    @SequenceGenerator(name = "pagar_id_seq", sequenceName = "pagar_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagar_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_compra", insertable = false, updatable = false)
    private Compra compra;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "parcela", nullable = false)
    private int parcela;

    @Column(name = "vencimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date vencimento;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "pago")
    private Boolean pago;

    @ManyToOne
    @JoinColumn(name = "id_tipo_conta")
    private TipoConta tipoConta;

    @OneToMany
    @JoinColumn(name = "id_pagar")
    private Collection<Pago> pagamentos;

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
     * Define o vencimento deste Pagar.
     * @return o vencimento
     */
    public Date getVencimento() {
        return this.vencimento;
    }

    public String getStringVencimento() {
        return FunctionsHelper.dateFormat(getVencimento(), "dd/MM/yyyy");
    }

    /**
     * Define o vencimento deste Pagar para o valor especificado.
     * @param vencimento o novo vencimento
     */
    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    /**
     * Define o valor deste Pagar.
     * @return o valor
     */
    public Double getValor() {
        return this.valor;
    }

    /**
     * Define o valor deste Pagar para o valor especificado.
     * @param valor o novo valor
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * Define o compra deste Pagar.
     * @return o compra
     */
    public Compra getCompra() {
        return this.compra;
    }

    /**
     * Define o compra deste Pagar para o valor especificado.
     * @param compra o novo compra
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
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
        builder.append("Pagar [");
        if (compra != null) {
            builder.append("compra=");
            builder.append(compra);
            builder.append(", ");
        }
        if (descricao != null) {
            builder.append("descricao=");
            builder.append(descricao);
            builder.append(", ");
        }
        if (vencimento != null) {
            builder.append("vencimento=");
            builder.append(vencimento);
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
        final Pagar other = (Pagar)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    /**
     * @return the pagamentos
     */
    public Collection<Pago> getPagamentos() {
        return pagamentos;
    }

    /**
     * @param pagamentos the pagamentos to set
     */
    public void setPagamentos(Collection<Pago> pagamentos) {
        this.pagamentos = pagamentos;
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

    public double getValorPago() {
        if (getPagamentos() == null || getPagamentos().size() <= 0)
            return 0;

        double totalPago = 0;
        for (Pago pago : getPagamentos())
            totalPago += pago.getValorPago().doubleValue();

        return totalPago;
    }

    public double getValorDevido() {
        if (getValor() == null || getValor() - getValorPago() < 0)
            return 0;
        return getValor() - getValorPago();
    }

    /**
     * @return pago atribuido
     */
    public Boolean getPago() {
        return pago;
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
}