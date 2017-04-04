
package com.github.andrepenteado.contest.actions.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.list.ListManager;
import org.mentawai.list.SimpleListData;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.entity.FaixaPreco;
import com.github.andrepenteado.contest.entity.TabelaPreco;
import com.github.andrepenteado.contest.services.CadastroService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/tabelaPreco")
public class TabelaPrecoAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(TabelaPrecoAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(TabelaPrecoAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(TabelaPrecoAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ADMINISTRATIVO) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_descricao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Descrição"));
        }
        else if (innerAction.equals("incluirFaixa")) {
            validator.add("txt_descricao_faixa", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Descrição"));
            validator.add("txt_markup_vista", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Markup à Vista"));
            validator.add("txt_markup_prazo", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Markup à Prazo"));
            validator.add("txt_comissao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Comissão"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/tabela_preco/pesquisar.jsp"))
    public String pesquisar() {
        try {
            List<TabelaPreco> listaTabelaPrecos = cadastroServices.listarTabelaPrecos();
            output.setValue("listaTabelaPrecos", listaTabelaPrecos);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/tabela_preco/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("txt_id");
            TabelaPreco tabelaPreco = new TabelaPreco(id);
            if (tabelaPreco.getFaixas() == null)
                tabelaPreco.setFaixas(new ArrayList<FaixaPreco>());
            session.setAttribute("tabelaPreco", tabelaPreco);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/tabela_preco/cadastro.jsp"))
    public String incluirFaixa() {
        try {
            TabelaPreco tabelaPreco = (TabelaPreco)session.getAttribute("tabelaPreco");

            FaixaPreco faixa = new FaixaPreco();
            faixa.setDescricao(input.getString("txt_descricao_faixa"));
            faixa.setMarkupVista(new Double(input.getString("txt_markup_vista").replace(".", "").replace(",", ".")));
            faixa.setMarkupPrazo(new Double(input.getString("txt_markup_prazo").replace(".", "").replace(",", ".")));
            faixa.setComissao(new Double(input.getString("txt_comissao").replace(".", "").replace(",", ".")));
            faixa.setTabelaPreco(tabelaPreco);

            tabelaPreco.getFaixas().add(faixa);

            session.setAttribute("tabelaPreco", tabelaPreco);
            output.setValue("txt_descricao_faixa", "");
            output.setValue("txt_markup_vista", "");
            output.setValue("txt_markup_prazo", "");
            output.setValue("txt_comissao", "");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/tabela_preco/cadastro.jsp"))
    public String excluirFaixa() {
        try {
            int indice = input.getInt("txt_indice");
            TabelaPreco tabelaPreco = (TabelaPreco)session.getAttribute("tabelaPreco");
            if (indice >= 0 && indice <= tabelaPreco.getFaixas().size())
                tabelaPreco.getFaixas().remove(tabelaPreco.getFaixas().toArray()[indice - 1]);
            session.setAttribute("tabelaPreco", tabelaPreco);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/tabela_preco/cadastro.jsp") })
    public String gravar() {
        try {
            TabelaPreco tabelaPreco = (TabelaPreco)session.getAttribute("tabelaPreco");
            tabelaPreco.setId(input.getLong("hid_id"));
            tabelaPreco.setDescricao(input.getString("txt_descricao"));
            tabelaPreco.setObservacao(input.getString("txt_observacao"));
            tabelaPreco.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        // Refaz lista
        SimpleListData listaTabelaPrecos = new SimpleListData("listaTabelasPrecos");
        for (TabelaPreco tp : cadastroServices.listarTabelaPrecos())
            listaTabelaPrecos.add(tp.getId().intValue(), tp.getDescricao());
        ListManager.addList(listaTabelaPrecos);

        output.setValue("titulo", "Cadastro de Tabela de Preços");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Tabela de Preços"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/tabelaPreco.pesquisar.action");
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/tabela_preco/pesquisar.jsp") })
    public String excluir() {
        try {
            TabelaPreco tabelaPreco = new TabelaPreco(input.getLong("txt_id"));
            if (tabelaPreco != null && tabelaPreco.getId() != null) {
                tabelaPreco.excluir();
                
                // Refaz lista
                SimpleListData listaTabelaPrecos = new SimpleListData("listaTabelasPrecos");
                for (TabelaPreco tp : cadastroServices.listarTabelaPrecos())
                    listaTabelaPrecos.add(tp.getId().intValue(), tp.getDescricao());
                ListManager.addList(listaTabelaPrecos);
            }
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        output.setValue("titulo", "Excluir de Tabela de Preços");
        output.setValue("mensagem", ConfigHelper.getProperty("info.deleteOk", "Tabela de Preços"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/tabelaPreco.pesquisar.action");
        return SUCCESS;
    }
}
