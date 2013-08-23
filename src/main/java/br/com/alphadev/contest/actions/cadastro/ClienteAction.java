
package br.com.alphadev.contest.actions.cadastro;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.EmailRule;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.TipoPesquisaCliente;
import br.com.alphadev.contest.entity.Cidade;
import br.com.alphadev.contest.entity.Cliente;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.entity.ItemVenda;
import br.com.alphadev.contest.entity.Venda;
import br.com.alphadev.contest.filters.VendaFilter;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.contest.services.VendaService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/cliente")
public class ClienteAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(ClienteAction.class, null);
    private CadastroService cadastroServices = null;
    private VendaService vendaServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            vendaServices = (VendaService)ServicesFactory.getInstance(VendaService.class, null);
            log = new Log4jWrapper(ClienteAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(ClienteAction.class.getName()), ex);
        }
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if ("iniciar".equals(innerAction) || "pesquisar".equals(innerAction))
            return true;

        if (!"buscar".equals(innerAction) && !"excluir".equals(innerAction) && groups.indexOf(KGlobal.CATEGORIA_VENDEDOR) != -1)
            return true;

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_VENDAS) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_nome", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Nome"));
            validator.add("cbo_cidade", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Cidade"));
            validator.add("txt_email", new EmailRule(), ConfigHelper.getProperty("error.invalid", "E-mail"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cliente/pesquisar.jsp"))
    public String iniciar() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cliente/pesquisar.jsp"))
    public String pesquisar() {
        try {
            String codigoNome = input.getString("txt_codigo_nome");
            String telefoneEmail = input.getString("txt_telefone_email");
            String ruaCep = input.getString("txt_rua_cep");

            Cidade cidade = new Cidade(input.getLong("cbo_cidade"));
            if (cidade.getId() == null)
                cidade = null;

            Funcionario vendedor = new Funcionario(input.getLong("txt_id_funcionario"));
            if (vendedor.getId() == null)
                vendedor = null;

            TipoPesquisaCliente tipoPesquisaCliente = null;
            if (input.getString(TipoPesquisaCliente.CODIGO_NOME.name()) != null
                            && input.getString(TipoPesquisaCliente.CODIGO_NOME.name()).length() > 0)
                tipoPesquisaCliente = TipoPesquisaCliente.CODIGO_NOME;
            else if (input.getString(TipoPesquisaCliente.TELEFONE_EMAIL.name()) != null
                            && input.getString(TipoPesquisaCliente.TELEFONE_EMAIL.name()).length() > 0)
                tipoPesquisaCliente = TipoPesquisaCliente.TELEFONE_EMAIL;
            else if (input.getString(TipoPesquisaCliente.RUA_CEP.name()) != null && input.getString(TipoPesquisaCliente.RUA_CEP.name()).length() > 0)
                tipoPesquisaCliente = TipoPesquisaCliente.RUA_CEP;
            else if (input.getString(TipoPesquisaCliente.CIDADE.name()) != null && input.getString(TipoPesquisaCliente.CIDADE.name()).length() > 0)
                tipoPesquisaCliente = TipoPesquisaCliente.CIDADE;
            else if (input.getString(TipoPesquisaCliente.VENDEDOR.name()) != null
                            && input.getString(TipoPesquisaCliente.VENDEDOR.name()).length() > 0)
                tipoPesquisaCliente = TipoPesquisaCliente.VENDEDOR;

            Collection<Cliente> listaClientes = cadastroServices.pesquisarCliente(codigoNome, telefoneEmail, ruaCep, cidade, vendedor,
                            tipoPesquisaCliente);
            output.setValue("tabName", tipoPesquisaCliente);
            output.setValue("listaClientes", listaClientes);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cliente/pesquisar.jsp"))
    public String buscar() {
        try {
            UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();
            Cliente cliente = new Cliente(input.getLong("txt_id_cliente"));
            // Cliente não encontrado ou não pertence ao vendedor
            if (cliente.getId() == null
                            || (userLogin.getCategoriaAtual().getNome().equals(KGlobal.CATEGORIA_VENDEDOR) && cliente.getFuncionario().getId()
                                            .intValue() != userLogin.getFuncionario().getId().intValue())) {
                addError("txt_nome_cliente", ConfigHelper.getProperty("error.notFound", "Cliente"));
                output.setValue("txt_nome_cliente", "");
                return SUCCESS;
            }

            output.setValue("txt_nome_cliente", cliente.getNome());
            output.setValue("cliente", cliente);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cliente/cadastro.jsp"))
    public String cadastro() {
        try {
            Cliente cliente = new Cliente(input.getLong("txt_id"));
            output.setValue("cliente", cliente);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/cliente/cadastro.jsp") })
    public String gravar() {
        try {
            Cliente cliente = new Cliente(input.getLong("hid_id"));
            // novo cadastro [inclusão]
            if (cliente.getId() == null || cliente.getId() < 0) {
                cliente.setDataCadastro(new Date());
                UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();
                if (userLogin.getCategoriaAtual().getNome().equals(KGlobal.CATEGORIA_VENDEDOR))
                    cliente.setFuncionario(userLogin.getFuncionario());
            }
            cliente.setId(input.getLong("hid_id"));
            cliente.setNome(input.getString("txt_nome"));
            cliente.setRua(input.getString("txt_rua"));
            cliente.setNumero(input.getString("txt_numero"));
            cliente.setComplemento(input.getString("txt_complemento"));
            cliente.setBairro(input.getString("txt_bairro"));
            cliente.setCep(input.getString("txt_cep"));
            cliente.setCidade(new Cidade(input.getLong("cbo_cidade")));
            cliente.setEmail(input.getString("txt_email"));
            cliente.setPaginaInternet(input.getString("txt_pagina_internet"));
            cliente.setContato(input.getString("txt_contato"));
            cliente.setTelefone(input.getString("txt_telefone"));
            cliente.setFax(input.getString("txt_fax"));
            cliente.setCnpj(input.getString("txt_cnpj"));
            cliente.setInscricaoEstadual(input.getString("txt_ie"));
            long idFuncionario = input.getLong("cbo_vendedor");
            if (idFuncionario > 0l)
                cliente.setFuncionario(new Funcionario(idFuncionario));
            else
                cliente.setFuncionario(null);
            cliente.setObservacao(input.getString("txt_observacao"));
            cliente.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        output.setValue("titulo", "Cadastro de Clientes");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Cliente"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/cliente.iniciar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cliente/pesquisar.jsp"))
    public String excluir() {
        try {
            Cliente cliente = new Cliente(input.getLong("txt_id"));
            cliente.excluir();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cliente/vendas.jsp"))
    public String vendas() {
        try {
            long idCliente = input.getLong("txt_id");
            VendaFilter<Venda> filter = ServicesFactory.getInstance(VendaFilter.class, Venda.class);
            filter.setIdCliente(idCliente);
            Collection<Venda> listaVendas = filter.executeFilter();
            output.setValue("listaVendas", listaVendas);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cliente/itens.jsp"))
    public String itens() {
        try {
            int idCliente = input.getInt("txt_id");
            List<ItemVenda> listaItens = vendaServices.pesquisarItensVendidosPorCliente(idCliente);
            output.setValue("listaItens", listaItens);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }
}