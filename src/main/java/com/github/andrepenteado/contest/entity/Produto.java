
package com.github.andrepenteado.contest.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.github.andrepenteado.contest.KGlobal.ComboPrecoComissao;
import com.github.andrepenteado.contest.entity.valueObject.TipoProduto;
import com.github.andrepenteado.contest.entity.valueObject.Unidade;
import com.github.andrepenteado.contest.entity.valueObject.VistaPrazo;
import com.github.andrepenteado.contest.services.CompraService;
import com.github.andrepenteado.contest.services.VendaService;
import com.github.andrepenteado.core.BasicEntity;
import com.github.andrepenteado.core.ServicesFactory;

/**
 * Classe de entidade Produto
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "produto")
public class Produto extends BasicEntity<Produto> implements Serializable {

    public Produto() {
        super(Produto.class);
    }

    public Produto(Long id) {
        super(Produto.class, id);
        if (getEstoques() != null)
            getEstoques().size();
    }

    @Transient
    private CompraService compraService = (CompraService)ServicesFactory.getInstance(CompraService.class, null);

    @Transient
    private VendaService vendaService = (VendaService)ServicesFactory.getInstance(VendaService.class, null);

    private static final long serialVersionUID = 7198494397837545956L;

    @Id
    @SequenceGenerator(name = "produto_id_seq", sequenceName = "produto_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "referencia", nullable = false)
    private String referencia;

    @Column(name = "ncm")
    private String ncm;

    @Column(name = "codigo_externo")
    private String codigoExterno;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "unidade")
    @Enumerated(EnumType.STRING)
    private Unidade unidade;

    @Column(name = "medida")
    private Double medida;

    @Column(name = "venda_vista")
    private Double vendaVista;

    @Column(name = "venda_prazo")
    private Double vendaPrazo;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoProduto tipo;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "foto")
    private byte[] foto;

    @ManyToOne
    @JoinColumn(name = "id_grupo_produto")
    private GrupoProduto grupoProduto;

    @ManyToOne
    @JoinColumn(name = "id_tabela_preco")
    private TabelaPreco tabelaPreco;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produto", orphanRemoval = true)
    private Collection<CodigoBarras> codigosBarras;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_produto", nullable = false)
    private Collection<Estoque> estoques;

    /**
     * Define o id deste Produto.
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Produto para o valor especificado.
     * @param id o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o referencia deste Produto.
     * @return o referencia
     */
    public String getReferencia() {
        return this.referencia;
    }

    /**
     * Define o referencia deste Produto para o valor especificado.
     * @param referencia o novo referencia
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * @param ncm ncm a ser atribuido
     */
    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    /**
     * @return ncm atribuido
     */
    public String getNcm() {
        return ncm;
    }

    /**
     * @param codigoExterno codigoExterno a ser atribuido
     */
    public void setCodigoExterno(String codigoExterno) {
        this.codigoExterno = codigoExterno;
    }

    /**
     * @return codigoExterno atribuido
     */
    public String getCodigoExterno() {
        return codigoExterno;
    }

    /**
     * Define o descricao deste Produto.
     * @return o descricao
     */
    public String getDescricao() {
        return this.descricao;
    }

    /**
     * Define o descricao deste Produto para o valor especificado.
     * @param descricao o novo descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Define o unidade deste Produto.
     * @return o unidade
     */
    public Unidade getUnidade() {
        return this.unidade;
    }

    /**
     * Define o unidade deste Produto para o valor especificado.
     * @param unidade o novo unidade
     */
    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    /**
     * @param medida medida a ser atribuido
     */
    public void setMedida(Double medida) {
        this.medida = medida;
    }

    /**
     * @return medida atribuido
     */
    public Double getMedida() {
        return medida;
    }

    /**
     * Define o custoVista deste Produto.
     * @return o custoVista
     */
    public Double getCustoVista() {
        Double custoVista = 0d;
        ItemCompra item = compraService.buscarUltimoItemCompraVista(this);
        if (item != null) {
            custoVista = item.getValorCompra();
            if (custoVista > 0d && item.getAliquotaIpi() > 0d)
                custoVista = custoVista * ((100 + item.getAliquotaIpi()) / 100);
        }
        return custoVista;
    }

    /**
     * @return custoPrazo atribuido
     */
    public Double getCustoPrazo() {
        Double custoPrazo = 0d;
        ItemCompra item = compraService.buscarUltimoItemCompraPrazo(this);
        if (item != null) {
            custoPrazo = item.getValorCompra();
            if (custoPrazo > 0d && item.getAliquotaIpi() > 0d)
                custoPrazo = custoPrazo * ((100 + item.getAliquotaIpi()) / 100);
        }
        return custoPrazo;
    }

    public Double getPrecoPadraoVista() {
        Double result = vendaVista != null ? vendaVista : 0d;
        Collection<ComboPrecoComissao> comboPrecos = vendaService.calcularPrecosVenda(this);
        if (comboPrecos != null) {
            for (ComboPrecoComissao cpc : comboPrecos) {
                if (cpc.getPreco() > result.doubleValue() && cpc.getVistaPrazo().equals(VistaPrazo.VISTA))
                    result = cpc.getPreco();
            }
        }
        return result;
    }

    public Double getPrecoPadraoPrazo() {
        Double result = vendaVista != null ? vendaVista : 0d;
        Collection<ComboPrecoComissao> comboPrecos = vendaService.calcularPrecosVenda(this);
        if (comboPrecos != null) {
            for (ComboPrecoComissao cpc : comboPrecos) {
                if (cpc.getPreco() > result.doubleValue() && cpc.getVistaPrazo().equals(VistaPrazo.PRAZO))
                    result = cpc.getPreco();
            }
        }
        return result;
    }

    /**
     * Define o vendaVista deste Produto.
     * @return o vendaVista
     */
    public Double getVendaVista() {
        return vendaVista;
    }

    /**
     * Define o vendaVista deste Produto para o valor especificado.
     * @param vendaVista o novo vendaVista
     */
    public void setVendaVista(Double vendaVista) {
        this.vendaVista = vendaVista;
    }

    /**
     * @param vendaPrazo vendaPrazo a ser atribuido
     */
    public void setVendaPrazo(Double vendaPrazo) {
        this.vendaPrazo = vendaPrazo;
    }

    /**
     * @return vendaPrazo atribuido
     */
    public Double getVendaPrazo() {
        return vendaPrazo;
    }

    /**
     * Define o observacao deste Produto.
     * @return o observacao
     */
    public String getObservacao() {
        return this.observacao;
    }

    /**
     * Define o observacao deste Produto para o valor especificado.
     * @param observacao o novo observacao
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @param tipo tipoProduto a ser atribuido
     */
    public void setTipo(TipoProduto tipo) {
        this.tipo = tipo;
    }

    /**
     * @return tipoProduto atribuido
     */
    public TipoProduto getTipo() {
        return tipo;
    }

    /**
     * @param foto foto a ser atribuido
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    /**
     * @return foto atribuido
     */
    public byte[] getFoto() {
        return foto;
    }

    /**
     * Define o grupoProduto deste Produto.
     * @return o grupoProduto
     */
    public GrupoProduto getGrupoProduto() {
        return this.grupoProduto;
    }

    /**
     * Define o grupoProduto deste Produto para o valor especificado.
     * @param grupoProduto o novo grupoProduto
     */
    public void setGrupoProduto(GrupoProduto grupoProduto) {
        this.grupoProduto = grupoProduto;
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

    /**
     * @param codigosBarras codigosBarras a ser atribuido
     */
    public void setCodigosBarras(Collection<CodigoBarras> codigosBarras) {
        this.codigosBarras = codigosBarras;
    }

    /**
     * @return codigosBarras atribuido
     */
    public Collection<CodigoBarras> getCodigosBarras() {
        return codigosBarras;
    }

    /**
     * @param estoques estoques a ser atribuido
     */
    public void setEstoques(Collection<Estoque> estoques) {
        this.estoques = estoques;
    }

    /**
     * @return estoques atribuido
     */
    public Collection<Estoque> getEstoques() {
        return estoques;
    }

    public int getQuantidadeEstoque() {
        int result = 0;
        for (Estoque estoque : getEstoques())
            result += estoque.getSpecIn() + estoque.getSpecOut();
        return result;
    }

    public int getTotalSpecIn() {
        int result = 0;
        for (Estoque estoque : getEstoques())
            result += estoque.getSpecIn();
        return result;
    }

    public int getTotalSpecOut() {
        int result = 0;
        for (Estoque estoque : getEstoques())
            result += estoque.getSpecOut();
        return result;
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
        final Produto other = (Produto)obj;
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
        builder.append("Produto [");
        if (descricao != null) {
            builder.append("descricao=");
            builder.append(descricao);
        }
        builder.append("]");
        return builder.toString();
    }
}
