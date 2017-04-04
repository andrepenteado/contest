
package com.github.andrepenteado.contest.actions.cadastro;

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
import com.github.andrepenteado.contest.entity.GrupoProduto;
import com.github.andrepenteado.contest.services.CadastroService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/grupoProduto")
public class GrupoProdutoAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(GrupoProdutoAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(GrupoProdutoAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(GrupoProdutoAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ADMINISTRATIVO) != -1
                        || groups.indexOf(KGlobal.CATEGORIA_COMPRAS) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_descricao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Descrição"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/grupo_produto/pesquisar.jsp"))
    public String pesquisar() {
        try {
            List<GrupoProduto> listaGruposProdutos = cadastroServices.listarGruposProdutos();
            output.setValue("listaGruposProdutos", listaGruposProdutos);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/grupo_produto/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("txt_id");
            GrupoProduto grupoProduto = new GrupoProduto(id);
            output.setValue("grupoProduto", grupoProduto);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/grupo_produto/cadastro.jsp") })
    public String gravar() {
        try {
            GrupoProduto grupoProduto = new GrupoProduto(input.getLong("hid_id"));
            GrupoProduto grupoProdutoSuperior = new GrupoProduto(input.getLong("cbo_grupo_superior"));
            grupoProduto.setId(input.getLong("hid_id"));
            grupoProduto.setDescricao(input.getString("txt_descricao"));
            grupoProduto.setObservacao(input.getString("txt_observacao"));
            if (grupoProdutoSuperior.getId() != null && grupoProdutoSuperior.getId() > 0)
                grupoProduto.setGrupoProdutoSuperior(grupoProdutoSuperior);
            else
                grupoProduto.setGrupoProdutoSuperior(null);
            grupoProduto.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        // Refaz lista
        SimpleListData listaGruposProdutos = new SimpleListData("listaGruposProdutos");
        for (GrupoProduto gp : cadastroServices.listarGruposProdutos())
            listaGruposProdutos.add(gp.getId().intValue(), gp.getDescricao());
        ListManager.addList(listaGruposProdutos);

        output.setValue("titulo", "Cadastro de Grupo de Produto");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Grupo de Produto"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/grupoProduto.pesquisar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/grupo_produto/pesquisar.jsp"))
    public String excluir() {
        try {
            GrupoProduto grupoProduto = new GrupoProduto(input.getLong("txt_id"));
            if (grupoProduto != null && grupoProduto.getId() != null) {
                grupoProduto.excluir();

                // Refaz lista
                SimpleListData listaGruposProdutos = new SimpleListData("listaGruposProdutos");
                for (GrupoProduto gp : cadastroServices.listarGruposProdutos())
                    listaGruposProdutos.add(gp.getId().intValue(), gp.getDescricao());
                ListManager.addList(listaGruposProdutos);
            }
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return SUCCESS;
    }
}
