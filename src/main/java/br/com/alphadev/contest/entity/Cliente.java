
package br.com.alphadev.contest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.alphadev.core.BasicEntity;

/**
 * Classe de entidade Cliente
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "cliente")
public class Cliente extends BasicEntity<Cliente> implements Serializable {

    public Cliente() {
        super(Cliente.class);
    }

    public Cliente(Long id) {
        super(Cliente.class, id);
    }

    private static final long serialVersionUID = -7067460826235277731L;

    @Id
    @SequenceGenerator(name = "cliente_id_seq", sequenceName = "cliente_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @Column(name = "nome", nullable = false)
    private String nome;

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

    @Column(name = "email")
    private String email;

    @Column(name = "pagina_internet")
    private String paginaInternet;

    @Column(name = "contato")
    private String contato;

    @Column(name = "observacao")
    private String observacao;

    /**
     * Define o id deste Cliente.
     * 
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Cliente para o valor especificado.
     * 
     * @param id o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param dataCadastro dataCadastro a ser atribuido
     */
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
     * @return dataCadastro atribuido
     */
    public Date getDataCadastro() {
        return dataCadastro;
    }

    /**
     * @param funcionario funcionario a ser atribuido
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return funcionario atribuido
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * Define o nome deste Cliente.
     * 
     * @return o nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Define o nome deste Cliente para o valor especificado.
     * 
     * @param nome
     *            o novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * Define o rua deste Cliente.
     * 
     * @return o rua
     */
    public String getRua() {
        return this.rua;
    }

    /**
     * Define o rua deste Cliente para o valor especificado.
     * 
     * @param rua
     *            o novo rua
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
     * Define o bairro deste Cliente.
     * 
     * @return o bairro
     */
    public String getBairro() {
        return this.bairro;
    }

    /**
     * Define o bairro deste Cliente para o valor especificado.
     * 
     * @param bairro
     *            o novo bairro
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * Define o cep deste Cliente.
     * 
     * @return o cep
     */
    public String getCep() {
        return this.cep;
    }

    /**
     * Define o cep deste Cliente para o valor especificado.
     * 
     * @param cep
     *            o novo cep
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * Define o cidade deste Cliente.
     * 
     * @return o cidade
     */
    public Cidade getCidade() {
        return this.cidade;
    }

    /**
     * Define o cidade deste Cliente para o valor especificado.
     * 
     * @param cidade
     *            o novo cidade
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    /**
     * Define o telefone deste Cliente.
     * 
     * @return o telefone
     */
    public String getTelefone() {
        return this.telefone;
    }

    /**
     * Define o telefone deste Cliente para o valor especificado.
     * 
     * @param telefone
     *            o novo telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Define o fax deste Cliente.
     * 
     * @return o fax
     */
    public String getFax() {
        return this.fax;
    }

    /**
     * Define o fax deste Cliente para o valor especificado.
     * 
     * @param fax
     *            o novo fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * Define o cnpj deste Cliente.
     * 
     * @return o cnpj
     */
    public String getCnpj() {
        return this.cnpj;
    }

    /**
     * Define o cnpj deste Cliente para o valor especificado.
     * 
     * @param cnpj
     *            o novo cnpj
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * Define o inscricaoEstadual deste Cliente.
     * 
     * @return o inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return this.inscricaoEstadual;
    }

    /**
     * Define o inscricaoEstadual deste Cliente para o valor especificado.
     * 
     * @param inscricaoEstadual
     *            o novo inscricaoEstadual
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    /**
     * Define o email deste Cliente.
     * 
     * @return o email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Define o email deste Cliente para o valor especificado.
     * 
     * @param email
     *            o novo email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param paginaInternet paginaInternet a ser atribuido
     */
    public void setPaginaInternet(String paginaInternet) {
        this.paginaInternet = paginaInternet;
    }

    /**
     * @return paginaInternet atribuido
     */
    public String getPaginaInternet() {
        return paginaInternet;
    }

    /**
     * Define o contato deste Cliente.
     * 
     * @return o contato
     */
    public String getContato() {
        return this.contato;
    }

    /**
     * Define o contato deste Cliente para o valor especificado.
     * 
     * @param contato
     *            o novo contato
     */
    public void setContato(String contato) {
        this.contato = contato;
    }

    /**
     * Define o observacao deste Cliente.
     * 
     * @return o observacao
     */
    public String getObservacao() {
        return this.observacao;
    }

    /**
     * Define o observacao deste Cliente para o valor especificado.
     * 
     * @param observacao
     *            o novo observacao
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
        final Cliente other = (Cliente)obj;
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
        builder.append("Cliente [");
        if (nome != null) {
            builder.append("nome=");
            builder.append(nome);
        }
        builder.append("]");
        return builder.toString();
    }
}