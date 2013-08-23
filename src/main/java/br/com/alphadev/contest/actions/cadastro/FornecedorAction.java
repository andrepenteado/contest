
package br.com.alphadev.contest.actions.cadastro;

import java.util.Collection;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.CNPJRule;
import org.mentawai.rule.EmailRule;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.TipoPesquisaCliente;
import br.com.alphadev.contest.KGlobal.TipoPesquisaFornecedor;
import br.com.alphadev.contest.entity.Cidade;
import br.com.alphadev.contest.entity.Compra;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.ItemCompra;
import br.com.alphadev.contest.filters.CompraFilter;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.contest.services.CompraService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/fornecedor")
public class FornecedorAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(FornecedorAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(FornecedorAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(FornecedorAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if ("iniciar".equals(innerAction) || "pesquisar".equals(innerAction))
            return true;

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_COMPRAS) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_nome", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Nome"));
            validator.add("txt_email", new EmailRule(), ConfigHelper.getProperty("error.invalid", "E-mail"));
            validator.add("txt_cnpj", new CNPJRule(), ConfigHelper.getProperty("error.invalid", "CNPJ"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/fornecedor/pesquisar.jsp"))
    public String iniciar() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/fornecedor/pesquisar.jsp"))
    public String pesquisar() {
        try {
            String codigoNome = input.getString("txt_codigo_nome");
            String telefoneEmail = input.getString("txt_telefone_email");
            String ruaCep = input.getString("txt_rua_cep");

            Cidade cidade = new Cidade(input.getLong("cbo_cidade"));
            if (cidade.getId() == null)
                cidade = null;

            TipoPesquisaFornecedor tipoPesquisaFornecedor = null;
            if (input.getString(TipoPesquisaCliente.CODIGO_NOME.name()) != null
                            && input.getString(TipoPesquisaFornecedor.CODIGO_NOME.name()).length() > 0)
                tipoPesquisaFornecedor = TipoPesquisaFornecedor.CODIGO_NOME;
            else if (input.getString(TipoPesquisaFornecedor.TELEFONE_EMAIL.name()) != null
                            && input.getString(TipoPesquisaCliente.TELEFONE_EMAIL.name()).length() > 0)
                tipoPesquisaFornecedor = TipoPesquisaFornecedor.TELEFONE_EMAIL;
            else if (input.getString(TipoPesquisaFornecedor.RUA_CEP.name()) != null
                            && input.getString(TipoPesquisaFornecedor.RUA_CEP.name()).length() > 0)
                tipoPesquisaFornecedor = TipoPesquisaFornecedor.RUA_CEP;
            else if (input.getString(TipoPesquisaFornecedor.CIDADE.name()) != null
                            && input.getString(TipoPesquisaFornecedor.CIDADE.name()).length() > 0)
                tipoPesquisaFornecedor = TipoPesquisaFornecedor.CIDADE;

            Collection<Fornecedor> listaFornecedores = cadastroServices.pesquisarFornecedor(codigoNome, telefoneEmail, ruaCep, cidade, null,
                            tipoPesquisaFornecedor);
            output.setValue("tabName", tipoPesquisaFornecedor);
            output.setValue("listaFornecedores", listaFornecedores);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/fornecedor/pesquisar.jsp"))
    public String buscar() {
        try {
            long idFornecedor = input.getLong("txt_id_fornecedor");
            Fornecedor fornecedor = new Fornecedor(idFornecedor);
            if (fornecedor.getId() == null) {
                addError("txt_nome_fornecedor", ConfigHelper.getProperty("error.notFound", "Fornecedor"));
                output.setValue("txt_nome_fornecedor", "");
                return SUCCESS;
            }
            output.setValue("txt_nome_fornecedor", fornecedor.getNome());
            output.setValue("fornecedor", fornecedor);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/fornecedor/cadastro.jsp"))
    public String cadastro() {
        try {
            Fornecedor fornecedor = new Fornecedor(input.getLong("txt_id"));
            output.setValue("fornecedor", fornecedor);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/fornecedor/cadastro.jsp") })
    public String gravar() {
        try {
            Fornecedor fornecedor = new Fornecedor(input.getLong("hid_id"));
            if (fornecedor.getId() == null)
                fornecedor = new Fornecedor();
            fornecedor.setId(input.getLong("hid_id"));
            fornecedor.setNome(input.getString("txt_nome"));
            fornecedor.setRua(input.getString("txt_rua"));
            fornecedor.setNumero(input.getString("txt_numero"));
            fornecedor.setComplemento(input.getString("txt_complemento"));
            fornecedor.setBairro(input.getString("txt_bairro"));
            fornecedor.setCep(input.getString("txt_cep"));
            fornecedor.setCidade(new Cidade(input.getLong("cbo_cidade")));
            fornecedor.setEmail(input.getString("txt_email"));
            fornecedor.setPaginaInternet(input.getString("txt_pagina_internet"));
            fornecedor.setContato(input.getString("txt_contato"));
            fornecedor.setTelefone(input.getString("txt_telefone"));
            fornecedor.setFax(input.getString("txt_fax"));
            fornecedor.setCnpj(input.getString("txt_cnpj"));
            fornecedor.setInscricaoEstadual(input.getString("txt_ie"));
            fornecedor.setObservacao(input.getString("txt_observacao"));
            fornecedor.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        output.setValue("titulo", "Cadastro de Fornecedores");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Fornecedor"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/fornecedor.iniciar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/fornecedor/pesquisar.jsp"))
    public String excluir() {
        try {
            Fornecedor fornecedor = new Fornecedor(input.getLong("txt_id"));
            if (fornecedor != null && fornecedor.getId() != null)
                fornecedor.excluir();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/fornecedor/compras.jsp"))
    public String compras() {
        try {
            long idFornecedor = input.getLong("txt_id");
            CompraFilter<Compra> filter = ServicesFactory.getInstance(CompraFilter.class, Compra.class);
            filter.setIdFornecedor(idFornecedor);
            Collection<Compra> listaCompras = filter.executeFilter();
            output.setValue("listaCompras", listaCompras);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/fornecedor/itens.jsp"))
    public String itens() {
        try {
            CompraService compraServices = (CompraService)ServicesFactory.getInstance(CompraService.class,
                            new Object[] { (UsuarioLogadoWrapper)getUserSession() });
            int idFornecedor = input.getInt("txt_id");
            List<ItemCompra> listaItens = compraServices.pesquisarItensCompradosPorFornecedor(idFornecedor);
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