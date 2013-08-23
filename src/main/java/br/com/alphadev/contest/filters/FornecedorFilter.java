
package br.com.alphadev.contest.filters;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import br.com.alphadev.contest.entity.Cidade;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.core.BasicFilter;
import br.com.alphadev.util.Log4jWrapper;

@Stateless
public class FornecedorFilter extends BasicFilter {

    private Log4jWrapper log = new Log4jWrapper(FornecedorFilter.class, null);

    private String nome;

    private String telefone;

    private String email;

    private String rua;

    private String cep;

    private Cidade cidade;

    private String cnpj;

    @Override
    public void createFilter() {
        filterDescription = new StringBuilder();
        parameters = new ArrayList<Object[]>();
        filterDescription.append("SELECT f FROM br.com.alphadev.contest.entity.Fornecedor f ");
        filterDescription.append("WHERE 1 = 1 ");
        adicionarNome();
        adicionarTelefone();
        adicionarEmail();
        adicionarRua();
        adicionarCep();
        adicionarCidade();
        adicionarCnpj();
        filterDescription.append("ORDER BY f.nome");
    }

    @Override
    public Collection<Fornecedor> executeFilter() {
        createFilter();
        log.info("Executar filtro " + filterDescription.toString());
        return (Collection<Fornecedor>)executeQuery(filterDescription.toString(), (Object[][])parameters.toArray(new Object[parameters.size()][2]));
    }

    private void adicionarNome() {
        if (nome != null) {
            filterDescription.append("AND UPPER(f.nome) LIKE UPPER(:pNome) ");
            parameters.add(new Object[] { "pNome", "%".concat(nome).concat("%") });
        }
    }

    private void adicionarTelefone() {
        if (telefone != null) {
            filterDescription.append("AND f.telefone LIKE :pTelefone ");
            parameters.add(new Object[] { "pTelefone", "%".concat(telefone).concat("%") });
        }
    }

    private void adicionarEmail() {
        if (email != null) {
            filterDescription.append("AND LOWER(f.email) LIKE LOWER(:pEmail) ");
            parameters.add(new Object[] { "pEmail", "%".concat(email).concat("%") });
        }
    }

    private void adicionarRua() {
        if (rua != null) {
            filterDescription.append("AND UPPER(f.rua) LIKE UPPER(:pRua) ");
            parameters.add(new Object[] { "pRua", "%".concat(rua).concat("%") });
        }
    }

    private void adicionarCep() {
        if (cep != null) {
            filterDescription.append("AND f.cep LIKE :pCep ");
            parameters.add(new Object[] { "pCep", "%".concat(cep).concat("%") });
        }
    }

    private void adicionarCidade() {
        if (cidade != null) {
            filterDescription.append("AND f.cidade = :pCidade ");
            parameters.add(new Object[] { "pCidade", cidade });
        }
    }

    private void adicionarCnpj() {
        if (cnpj != null) {
            filterDescription.append("AND f.cnpj = :pCnpj ");
            parameters.add(new Object[] { "pCnpj", cnpj });
        }
    }
    
    
    /**
     * @param nome nome a ser atribuido
     */
    public void setNome(String nome) {
        this.nome = nome.replace("%", "");
    }

    /**
     * @param telefone telefone a ser atribuido
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone.replace("%", "");
    }

    /**
     * @param email email a ser atribuido
     */
    public void setEmail(String email) {
        this.email = email.replace("%", "");
    }

    /**
     * @param rua rua a ser atribuido
     */
    public void setRua(String rua) {
        this.rua = rua.replace("%", "");
    }

    /**
     * @param cep cep a ser atribuido
     */
    public void setCep(String cep) {
        this.cep = cep.replace("%", "");
    }

    /**
     * @param cidade cidade a ser atribuido
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    /**
     * @param cnpj cnpj a ser atribuido
     */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
}