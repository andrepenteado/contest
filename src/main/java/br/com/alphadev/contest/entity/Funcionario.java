
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.alphadev.core.BasicEntity;

/**
 * Classe de entidade Funcionario
 * 
 * @author Andre Penteado
 */
@Entity
@Table(name = "funcionario")
public class Funcionario extends BasicEntity<Funcionario> implements Serializable {

    public Funcionario() {
        super(Funcionario.class);
    }

    public Funcionario(Long id) {
        super(Funcionario.class, id);
    }

    private static final long serialVersionUID = -6574981533920397368L;

    @Id
    @SequenceGenerator(name = "funcionario_id_seq", sequenceName = "funcionario_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionario_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "comissao")
    private Double comissao;

    @Column(name = "identificacao")
    private String identificacao;

    @Column(name = "senha")
    private String senha;

    @Column(name = "data_ultimo_acesso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimoAcesso;

    @Column(name = "ip_ultimo_acesso")
    private String ipUltimoAcesso;

    @Column(name = "observacao")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @ManyToMany
    @JoinTable(name = "funcionario_categoria", joinColumns = { @JoinColumn(name = "id_funcionario") }, inverseJoinColumns = { @JoinColumn(name = "id_categoria") })
    private Collection<Categoria> categorias;

    /**
     * Define o id deste Funcionario.
     * 
     * @return o id
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Define o id deste Funcionario para o valor especificado.
     * 
     * @param id o novo id
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o nome deste Funcionario.
     * 
     * @return o nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Define o nome deste Funcionario para o valor especificado.
     * 
     * @param nome
     *            o novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Define o telefone deste Funcionario.
     * 
     * @return o telefone
     */
    public String getTelefone() {
        return this.telefone;
    }

    /**
     * Define o telefone deste Funcionario para o valor especificado.
     * 
     * @param telefone
     *            o novo telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Define o comissao deste Funcionario.
     * 
     * @return o comissao
     */
    public Double getComissao() {
        return this.comissao;
    }

    /**
     * Define o comissao deste Funcionario para o valor especificado.
     * 
     * @param comissao
     *            o novo comissao
     */
    public void setComissao(Double comissao) {
        this.comissao = comissao;
    }

    /**
     * @param empresa empresa a ser atribuido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return empresa atribuido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * Define o observacao deste Funcionario.
     * 
     * @return o observacao
     */
    public String getObservacao() {
        return this.observacao;
    }

    /**
     * Define o observacao deste Funcionario para o valor especificado.
     * 
     * @param observacao
     *            o novo observacao
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return the identificacao
     */
    public String getIdentificacao() {
        return identificacao;
    }

    /**
     * @param identificacao the identificacao to set
     */
    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the dataUltimoAcesso
     */
    public Date getDataUltimoAcesso() {
        return dataUltimoAcesso;
    }

    /**
     * @param dataUltimoAcesso the dataUltimoAcesso to set
     */
    public void setDataUltimoAcesso(Date dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }

    /**
     * @return the ipUltimoAcesso
     */
    public String getIpUltimoAcesso() {
        return ipUltimoAcesso;
    }

    /**
     * @param ipUltimoAcesso the ipUltimoAcesso to set
     */
    public void setIpUltimoAcesso(String ipUltimoAcesso) {
        this.ipUltimoAcesso = ipUltimoAcesso;
    }

    /**
     * @return the categorias
     */
    public Collection<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * @param categorias the categorias to set
     */
    public void setCategorias(Collection<Categoria> categorias) {
        this.categorias = categorias;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Funcionario other = (Funcionario)obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Funcionario [");
        if (nome != null) {
            builder.append("nome=");
            builder.append(nome);
        }
        builder.append("]");
        return builder.toString();
    }
}
