
package br.com.alphadev.contest.actions.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.list.ListManager;
import org.mentawai.list.SimpleListData;
import org.mentawai.rule.EqualRule;
import org.mentawai.rule.NumberRule;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.rule.StringRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.entity.Categoria;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.SHA512CryptLinuxShadow;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/funcionario")
public class FuncionarioAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(FuncionarioAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(FuncionarioAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(FuncionarioAction.class.getName()), ex);
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

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ADMINISTRATIVO) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_nome", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Nome"));
            validator.add("txt_comissao", new NumberRule(), ConfigHelper.getProperty("error.integer", "Comissão"));
            validator.add("txt_senha", new StringRule(6), ConfigHelper.getProperty("error.lessThan", new String[] { "Senha", "6 caracteres" }));
            validator.add("txt_senha", new EqualRule("txt_senha", "txt_confirme_senha"),
                            ConfigHelper.getProperty("error.notEquals", new String[] { "Senha", "Confirmação da Senha" }));
            validator.add("txt_confirme_senha", new EqualRule("txt_senha", "txt_confirme_senha"),
                            ConfigHelper.getProperty("error.notEquals", new String[] { "Senha", "Confirmação da Senha" }));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/funcionario/pesquisar.jsp"))
    public String iniciar() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/funcionario/pesquisar.jsp"))
    public String pesquisar() {
        try {
            String nome = input.getString("txt_funcionario");
            List<Funcionario> listaFuncionarios = cadastroServices.pesquisarFuncionarioPorNome(nome);
            output.setValue("listaFuncionarios", listaFuncionarios);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/funcionario/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("txt_id");
            Funcionario funcionario = new Funcionario(id);

            // Marca os cehckboxes com categoria atribuída
            if (funcionario.getCategorias() != null && !funcionario.getCategorias().isEmpty()) {
                int[] categoriasAtribuidas = new int[funcionario.getCategorias().size()];
                for (int i = 0; i < funcionario.getCategorias().size(); i++) {
                    Categoria categoria = (Categoria)funcionario.getCategorias().toArray()[i];
                    categoriasAtribuidas[i] = categoria.getId().intValue();
                }
                output.setValue("txt_categorias", categoriasAtribuidas);
            }
            output.setValue("funcionario", funcionario);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/funcionario/cadastro.jsp") })
    public String gravar() {
        try {
            Funcionario funcionario = new Funcionario(input.getLong("hid_id"));
            funcionario.setId(input.getLong("hid_id"));
            funcionario.setNome(input.getString("txt_nome"));
            funcionario.setTelefone(input.getString("txt_telefone"));
            funcionario.setComissao(input.getDouble("txt_comissao"));
            funcionario.setIdentificacao(input.getString("txt_identificacao"));
            funcionario.setObservacao(input.getString("txt_observacao"));
            funcionario.setEmpresa(((UsuarioLogadoWrapper)getUserSession()).getFuncionario().getEmpresa());
            if (input.getString("txt_senha") != null && input.getString("txt_senha").length() > 0)
                funcionario.setSenha(SHA512CryptLinuxShadow.crypt(input.getString("txt_senha")));

            // Gravar as categorias
            funcionario.setCategorias(new ArrayList<Categoria>());
            int[] categorias = input.getInts("txt_categorias");
            if (categorias != null && categorias.length > 0) {
                for (long id : categorias)
                    funcionario.getCategorias().add(new Categoria(new Long(id)));
            }
            funcionario.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        // Refaz a lista
        SimpleListData listaVendedores = new SimpleListData("listaVendedores");
        for (Funcionario vendedor : cadastroServices.pesquisarFuncionarioPorNome(""))
            listaVendedores.add(vendedor.getId().intValue(), vendedor.getNome());
        ListManager.addList(listaVendedores);

        output.setValue("titulo", "Cadastro de Funcionários");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Funcionário"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/funcionario.iniciar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/funcionario/pesquisar.jsp"))
    public String excluir() {
        try {
            Funcionario funcionario = new Funcionario(input.getLong("txt_id"));
            if (funcionario != null && funcionario.getId() != null) {
                funcionario.excluir();

                // Refaz a lista
                SimpleListData listaVendedores = new SimpleListData("listaVendedores");
                for (Funcionario vendedor : cadastroServices.pesquisarFuncionarioPorNome(""))
                    listaVendedores.add(vendedor.getId().intValue(), vendedor.getNome());
                ListManager.addList(listaVendedores);
            }
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }
}
