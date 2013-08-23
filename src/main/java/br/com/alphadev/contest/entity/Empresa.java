
package br.com.alphadev.contest.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.alphadev.contest.entity.valueObject.Imposto;
import br.com.alphadev.core.BasicEntity;

/**
 * @author Andre Penteado
 * @since 08/10/2008 - 18:26:50
 */
@Entity
@Table(name = "empresa")
public class Empresa extends BasicEntity<Empresa> implements Serializable {

    public Empresa() {
        super(Empresa.class);
    }

    public Empresa(Long id) {
        super(Empresa.class, id);
        if (getContasCorrentes() != null)
            getContasCorrentes().size();
    }

    private static final long serialVersionUID = -8863270113332761365L;

    @Id
    @SequenceGenerator(name = "empresa_id_seq", sequenceName = "empresa_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresa_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Column(name = "rua")
    private String rua;

    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cep")
    private String cep;

    @ManyToOne
    @JoinColumn(name = "id_cidade")
    private Cidade cidade;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "inscricao_estadual")
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal")
    private String inscricaoMunicipal;

    @Column(name = "cnae")
    private String cnae;

    @Column(name = "email")
    private String email;

    @Column(name = "pagina_internet")
    private String paginaInternet;

    @Column(name = "contato")
    private String contato;

    @Column(name = "observacao")
    private String observacao;

    @OneToOne(mappedBy = "empresa")
    private Configuracao configuracao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_empresa", nullable = true)
    private Collection<AliquotaImposto> impostos;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_empresa", nullable = true)
    private Collection<ContaCorrente> contasCorrentes;

    /**
     * @return id atribuido
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id id a ser atribuido
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return razaoSocial atribuido
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * @param razaoSocial razaoSocial a ser atribuido
     */
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    /**
     * @return nomeFantasia atribuido
     */
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    /**
     * @param nomeFantasia nomeFantasia a ser atribuido
     */
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEndereco() {
        StringBuilder endereco = new StringBuilder();
        if (getRua() != null)
            endereco.append(getRua());
        if (getNumero() != null)
            endereco.append(", ").append(getNumero());
        if (getComplemento() != null)
            endereco.append(" - ").append(getComplemento());
        return endereco.toString();
    }

    /**
     * @return rua atribuido
     */
    public String getRua() {
        return rua;
    }

    /**
     * @param rua rua a ser atribuido
     */
    public void setRua(String rua) {
        this.rua = rua;
    }

    /**
     * @return numero atribuido
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero numero a ser atribuido
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return complemento atribuido
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * @param complemento complemento a ser atribuido
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return bairro atribuido
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @param bairro bairro a ser atribuido
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return cep atribuido
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep cep a ser atribuido
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return cidade atribuido
     */
    public Cidade getCidade() {
        return cidade;
    }

    /**
     * @param cidade cidade a ser atribuido
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    /**
     * @return telefone atribuido
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone telefone a ser atribuido
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return fax atribuido
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax fax a ser atribuido
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return cnpj atribuido
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj cnpj a ser atribuido
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return inscricaoEstadual atribuido
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * @param inscricaoEstadual inscricaoEstadual a ser atribuido
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    /**
     * @param inscricaoMunicipal inscricaoMunicipal a ser atribuido
     */
    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    /**
     * @return inscricaoMunicipal atribuido
     */
    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    /**
     * @param cnae cnae a ser atribuido
     */
    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    /**
     * @return cnae atribuido
     */
    public String getCnae() {
        return cnae;
    }

    /**
     * @return email atribuido
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email email a ser atribuido
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return paginaInternet atribuido
     */
    public String getPaginaInternet() {
        return paginaInternet;
    }

    /**
     * @param paginaInternet paginaInternet a ser atribuido
     */
    public void setPaginaInternet(String paginaInternet) {
        this.paginaInternet = paginaInternet;
    }

    /**
     * @return contato atribuido
     */
    public String getContato() {
        return contato;
    }

    /**
     * @param contato contato a ser atribuido
     */
    public void setContato(String contato) {
        this.contato = contato;
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

    /**
     * @param configuracao configuracao a ser atribuido
     */
    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    /**
     * @return configuracao atribuido
     */
    public Configuracao getConfiguracao() {
        return configuracao;
    }

    /**
     * @param impostos impostos a ser atribuido
     */
    public void setImpostos(Collection<AliquotaImposto> impostos) {
        this.impostos = impostos;
    }

    /**
     * @return impostos atribuido
     */
    public Collection<AliquotaImposto> getImpostos() {
        return impostos;
    }

    /**
     * @param contasCorrentes contasCorrentes a ser atribuido
     */
    public void setContasCorrentes(Collection<ContaCorrente> contasCorrentes) {
        this.contasCorrentes = contasCorrentes;
    }

    /**
     * @return contasCorrentes atribuido
     */
    public Collection<ContaCorrente> getContasCorrentes() {
        return contasCorrentes;
    }

    public ContaCorrente getContaCorrenteCobranca() {
        if (getContasCorrentes() != null && !getContasCorrentes().isEmpty()) {
            for (ContaCorrente cc : getContasCorrentes())
                if (cc.getSequenciaArquivoCobranca() != null)
                    return cc;
        }
        return null;
    }

    public AliquotaImposto getImpostoServico() {
        if (getImpostos() != null && !getImpostos().isEmpty()) {
            for (AliquotaImposto servico : getImpostos())
                if (Imposto.ISS.name().equals(servico.getImposto().name()))
                    return servico;

        }
        return null;
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
        Empresa other = (Empresa)obj;
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
        builder.append("Empresa [");
        if (nomeFantasia != null) {
            builder.append("nomeFantasia=");
            builder.append(nomeFantasia);
        }
        builder.append("]");
        return builder.toString();
    }
}
