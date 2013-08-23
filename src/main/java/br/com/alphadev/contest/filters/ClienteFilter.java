
package br.com.alphadev.contest.filters;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import br.com.alphadev.contest.entity.Cidade;
import br.com.alphadev.contest.entity.Cliente;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.core.BasicFilter;
import br.com.alphadev.util.Log4jWrapper;

@Stateless
public class ClienteFilter extends BasicFilter {

    private Log4jWrapper log = new Log4jWrapper(ClienteFilter.class, null);

    private String nome;

    private String telefone;

    private String email;

    private String rua;

    private String cep;

    private Funcionario vendedor;

    private Cidade cidade;

    @Override
    public void createFilter() {
        filterDescription = new StringBuilder();
        parameters = new ArrayList<Object[]>();
        filterDescription.append("SELECT c FROM br.com.alphadev.contest.entity.Cliente c ");
        filterDescription.append("WHERE 1 = 1 ");
        adicionarNome();
        adicionarTelefone();
        adicionarEmail();
        adicionarRua();
        adicionarCep();
        adicionarCidade();
        adicionarVendedor();
        filterDescription.append("ORDER BY c.nome");
    }

    @Override
    public Collection<Cliente> executeFilter() {
        createFilter();
        log.info("Executar filtro " + filterDescription.toString());
        return (Collection<Cliente>)executeQuery(filterDescription.toString(), (Object[][])parameters.toArray(new Object[parameters.size()][2]));
    }

    private void adicionarNome() {
        if (nome != null) {
            filterDescription.append("AND UPPER(c.nome) LIKE UPPER(:pNome) ");
            parameters.add(new Object[] { "pNome", "%".concat(nome).concat("%") });
        }
    }

    private void adicionarTelefone() {
        if (telefone != null) {
            filterDescription.append("AND c.telefone LIKE :pTelefone ");
            parameters.add(new Object[] { "pTelefone", "%".concat(telefone).concat("%") });
        }
    }

    private void adicionarEmail() {
        if (email != null) {
            filterDescription.append("AND LOWER(c.email) LIKE LOWER(:pEmail) ");
            parameters.add(new Object[] { "pEmail", "%".concat(email).concat("%") });
        }
    }

    private void adicionarRua() {
        if (rua != null) {
            filterDescription.append("AND UPPER(c.rua) LIKE UPPER(:pRua) ");
            parameters.add(new Object[] { "pRua", "%".concat(rua).concat("%") });
        }
    }

    private void adicionarCep() {
        if (cep != null) {
            filterDescription.append("AND c.cep LIKE :pCep ");
            parameters.add(new Object[] { "pCep", "%".concat(cep).concat("%") });
        }
    }

    private void adicionarCidade() {
        if (cidade != null) {
            filterDescription.append("AND c.cidade = :pCidade ");
            parameters.add(new Object[] { "pCidade", cidade });
        }
    }

    private void adicionarVendedor() {
        if (vendedor != null) {
            /*filterDescription.append("AND c IN (");
            filterDescription.append("   SELECT v.cliente FROM br.com.alphadev.contest.entity.Venda v ");
            filterDescription.append("   WHERE v.funcionario = :pVendedor ");
            filterDescription.append(") ");*/
            filterDescription.append("AND c.funcionario = :pVendedor ");
            parameters.add(new Object[] { "pVendedor", vendedor });
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
     * @param vendedor vendedor a ser atribuido
     */
    public void setVendedor(Funcionario vendedor) {
        this.vendedor = vendedor;
    }

    /**
     * @param cidade cidade a ser atribuido
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}