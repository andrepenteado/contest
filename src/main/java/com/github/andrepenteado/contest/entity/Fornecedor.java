
package com.github.andrepenteado.contest.entity;

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

import com.github.andrepenteado.core.BasicEntity;

/**
 * Classe de entidade Fornecedor
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "fornecedor")
public class Fornecedor extends BasicEntity<Fornecedor> implements Serializable {

    public Fornecedor() {
        super(Fornecedor.class);
    }

    public Fornecedor(Long id) {
        super(Fornecedor.class, id);
    }

    private static final long serialVersionUID = 3656654519324600694L;

    @Id
    @SequenceGenerator(name = "fornecedor_id_seq", sequenceName = "fornecedor_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedor_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

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
     * Define o id deste Fornecedor.
     * 
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Fornecedor para o valor especificado.
     * 
     * @param id o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o nome deste Fornecedor.
     * 
     * @return o nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Define o nome deste Fornecedor para o valor especificado.
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
     * Define o endereco deste Fornecedor.
     * 
     * @return o endereco
     */
    public String getRua() {
        return this.rua;
    }

    /**
     * Define o endereco deste Fornecedor para o valor especificado.
     * 
     * @param rua
     *            o novo endereco
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
     * Define o bairro deste Fornecedor.
     * 
     * @return o bairro
     */
    public String getBairro() {
        return this.bairro;
    }

    /**
     * Define o bairro deste Fornecedor para o valor especificado.
     * 
     * @param bairro
     *            o novo bairro
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * Define o cep deste Fornecedor.
     * 
     * @return o cep
     */
    public String getCep() {
        return this.cep;
    }

    /**
     * Define o cep deste Fornecedor para o valor especificado.
     * 
     * @param cep
     *            o novo cep
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * Define o cidade deste Fornecedor.
     * 
     * @return o cidade
     */
    public Cidade getCidade() {
        return this.cidade;
    }

    /**
     * Define o cidade deste Fornecedor para o valor especificado.
     * 
     * @param cidade
     *            o novo cidade
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    /**
     * Define o telefone deste Fornecedor.
     * 
     * @return o telefone
     */
    public String getTelefone() {
        return this.telefone;
    }

    /**
     * Define o telefone deste Fornecedor para o valor especificado.
     * 
     * @param telefone
     *            o novo telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Define o fax deste Fornecedor.
     * 
     * @return o fax
     */
    public String getFax() {
        return this.fax;
    }

    /**
     * Define o fax deste Fornecedor para o valor especificado.
     * 
     * @param fax
     *            o novo fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * Define o cnpj deste Fornecedor.
     * 
     * @return o cnpj
     */
    public String getCnpj() {
        return this.cnpj;
    }

    /**
     * Define o cnpj deste Fornecedor para o valor especificado.
     * 
     * @param cnpj
     *            o novo cnpj
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * Define o inscricaoEstadual deste Fornecedor.
     * 
     * @return o inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return this.inscricaoEstadual;
    }

    /**
     * Define o inscricaoEstadual deste Fornecedor para o valor especificado.
     * 
     * @param inscricaoEstadual
     *            o novo inscricaoEstadual
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    /**
     * Define o email deste Fornecedor.
     * 
     * @return o email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Define o email deste Fornecedor para o valor especificado.
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
     * Define o contato deste Fornecedor.
     * 
     * @return o contato
     */
    public String getContato() {
        return this.contato;
    }

    /**
     * Define o contato deste Fornecedor para o valor especificado.
     * 
     * @param contato
     *            o novo contato
     */
    public void setContato(String contato) {
        this.contato = contato;
    }

    /**
     * Define o observacao deste Fornecedor.
     * 
     * @return o observacao
     */
    public String getObservacao() {
        return this.observacao;
    }

    /**
     * Define o observacao deste Fornecedor para o valor especificado.
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
        final Fornecedor other = (Fornecedor)obj;
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
        builder.append("Fornecedor [");
        if (nome != null) {
            builder.append("nome=");
            builder.append(nome);
        }
        builder.append("]");
        return builder.toString();
    }
}
