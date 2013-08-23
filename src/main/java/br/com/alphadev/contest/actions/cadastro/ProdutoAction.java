
package br.com.alphadev.contest.actions.cadastro;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.TipoPesquisaProduto;
import br.com.alphadev.contest.entity.Empresa;
import br.com.alphadev.contest.entity.Estoque;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.GrupoProduto;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.entity.TabelaPreco;
import br.com.alphadev.contest.entity.valueObject.TipoProduto;
import br.com.alphadev.contest.entity.valueObject.Unidade;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/produto")
public class ProdutoAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(ProdutoAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(ProdutoAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(ProdutoAction.class.getName()), ex);
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

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ADMINISTRATIVO) != -1
                        || groups.indexOf(KGlobal.CATEGORIA_COMPRAS) != -1)
            return true;

        if (("pesquisar".equals(innerAction) || "iniciar".equals(innerAction))
                        && (groups.indexOf(KGlobal.CATEGORIA_VENDAS) != -1 || groups.indexOf(KGlobal.CATEGORIA_ESTOQUE) != -1))
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_referencia", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Referência"));
            validator.add("txt_descricao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Descrição"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/produto/pesquisar.jsp"))
    public String iniciar() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/produto/pesquisar.jsp"))
    public String pesquisar() {
        try {
            // Mostra os produtos inativos somente no cadastro de produtos
            boolean ativo = true;
            if ("0".equals(input.getString("hid_ativo")))
                ativo = false;
            String codigoReferenciaDescricao = input.getString("txt_codigo_referencia_descricao");

            GrupoProduto grupoProduto = new GrupoProduto(input.getLong("cbo_grupo_produto"));
            if (grupoProduto.getId() == null)
                grupoProduto = null;

            Fornecedor fornecedor = new Fornecedor(input.getLong("txt_id_fornecedor"));
            if (fornecedor.getId() == null)
                fornecedor = null;

            TipoPesquisaProduto tipoPesquisaProduto = null;
            if (input.getString(TipoPesquisaProduto.CODIGO_REFERENCIA_DESCRICAO.name()) != null
                            && input.getString(TipoPesquisaProduto.CODIGO_REFERENCIA_DESCRICAO.name()).length() > 0)
                tipoPesquisaProduto = TipoPesquisaProduto.CODIGO_REFERENCIA_DESCRICAO;
            else if (input.getString(TipoPesquisaProduto.GRUPO_PRODUTO.name()) != null
                            && input.getString(TipoPesquisaProduto.GRUPO_PRODUTO.name()).length() > 0)
                tipoPesquisaProduto = TipoPesquisaProduto.GRUPO_PRODUTO;
            else if (input.getString(TipoPesquisaProduto.FORNECEDOR.name()) != null
                            && input.getString(TipoPesquisaProduto.FORNECEDOR.name()).length() > 0)
                tipoPesquisaProduto = TipoPesquisaProduto.FORNECEDOR;

            output.setValue("tabName", tipoPesquisaProduto);
            output.setValue("listaProdutos",
                            cadastroServices.pesquisarProduto(codigoReferenciaDescricao, grupoProduto, fornecedor, tipoPesquisaProduto, ativo));

            UsuarioLogadoWrapper user = (UsuarioLogadoWrapper)getUserSession();

            if (KGlobal.CATEGORIA_COMPRAS.equals(user.getCategoriaAtual().getNome()))
                session.setAttribute("produtoVenda", false);
            if (KGlobal.CATEGORIA_VENDAS.equals(user.getCategoriaAtual().getNome()))
                session.setAttribute("produtoVenda", true);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/produto/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("txt_id");
            Produto produto = new Produto(id);

            if (produto.getEstoques() == null)
                produto.setEstoques(new ArrayList<Estoque>());

            // Cria as entradas de estoque para todas as empresas
            List<Empresa> empresas = cadastroServices.listarEmpresas();
            for (Empresa emp : empresas) {
                boolean existeEmpresa = false;
                for (Estoque estoque : produto.getEstoques()) {
                    if (estoque.getEmpresa().getId().intValue() == emp.getId().intValue())
                        existeEmpresa = true;
                }
                if (!existeEmpresa) {
                    Estoque estoque = new Estoque();
                    estoque.setEmpresa(emp);
                    estoque.setProduto(produto);
                    estoque.setSpecIn(0);
                    estoque.setSpecOut(0);
                    produto.getEstoques().add(estoque);
                }
            }

            output.setValue("produto", produto);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/produto/cadastro.jsp") })
    public String gravar() {
        try {
            Produto produto = new Produto(input.getLong("hid_id"));
            GrupoProduto grupoProduto = new GrupoProduto(input.getLong("cbo_grupo"));
            TabelaPreco tabelaPreco = new TabelaPreco(input.getLong("cbo_tabela_preco"));

            if (produto.getEstoques() == null)
                produto.setEstoques(new ArrayList<Estoque>());

            // Cria as entradas de estoque para todas as empresas
            List<Empresa> empresas = cadastroServices.listarEmpresas();
            for (Empresa emp : empresas) {
                boolean existeEmpresa = false;
                for (Estoque estoque : produto.getEstoques()) {
                    if (estoque.getEmpresa().getId().intValue() == emp.getId().intValue())
                        existeEmpresa = true;
                }
                if (!existeEmpresa) {
                    Estoque estoque = new Estoque();
                    estoque.setEmpresa(emp);
                    estoque.setProduto(produto);
                    estoque.setSpecIn(0);
                    estoque.setSpecOut(0);
                    produto.getEstoques().add(estoque);
                }
            }

            produto.setReferencia(input.getString("txt_referencia"));
            produto.setNcm(input.getString("txt_ncm"));
            produto.setCodigoExterno(input.getString("txt_codigo_externo"));
            produto.setDescricao(input.getString("txt_descricao"));
            produto.setUnidade(input.getString("lst_unidade") != null ? Unidade.valueOf(input.getString("lst_unidade")) : null);
            produto.setTipo(input.getString("lst_tipo") != null ? TipoProduto.valueOf(input.getString("lst_tipo")) : null);
            produto.setGrupoProduto(grupoProduto.getId() != null ? grupoProduto : null);
            produto.setTabelaPreco(tabelaPreco.getId() != null ? tabelaPreco : null);
            produto.setObservacao(input.getString("txt_observacao"));

            // Venda a Vista
            try {
                if (!"".equals(input.getString("txt_medida")))
                    produto.setMedida(new Double(input.getString("txt_medida").replace(",", ".")));
            }
            catch (Exception ex) {
                addError("txt_medida", ConfigHelper.getProperty("error.invalid", "Medida"));
                return ERROR;
            }

            // Venda a Vista
            try {
                if (!"".equals(input.getString("txt_venda_vista")))
                    produto.setVendaVista(new Double(input.getString("txt_venda_vista").replace(".", "").replace(",", ".")));
            }
            catch (Exception ex) {
                addError("txt_venda_vista", ConfigHelper.getProperty("error.invalid", "Venda à Vista"));
                return ERROR;
            }

            // Venda a Prazo
            try {
                if (!"".equals(input.getString("txt_venda_prazo")))
                    produto.setVendaPrazo(new Double(input.getString("txt_venda_prazo").replace(".", "").replace(",", ".")));
            }
            catch (Exception ex) {
                addError("txt_venda_prazo", ConfigHelper.getProperty("error.invalid", "Venda à Prazo"));
                return ERROR;
            }

            UsuarioLogadoWrapper user = (UsuarioLogadoWrapper)getUserSession();

            if (KGlobal.CATEGORIA_ADMINISTRATIVO.equals(user.getCategoriaAtual().getNome())
                            || KGlobal.CATEGORIA_SUPERUSUARIO.equals(user.getCategoriaAtual().getNome())) {
                for (Empresa emp : empresas) {
                    for (Estoque estoque : produto.getEstoques()) {
                        if (estoque.getEmpresa().getId().intValue() == emp.getId().intValue()) {
                            estoque.setSpecIn(input.getInt("txt_spec_in_" + emp.getId()));
                            estoque.setSpecOut(input.getInt("txt_spec_out_" + emp.getId()));
                        }
                    }
                }
            }

            FileItem file = (FileItem)input.getValue("txt_foto");
            if (file != null && file.getSize() > 0) {
                InputStream in = file.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = in.read(buffer)) != -1)
                    out.write(buffer, 0, length);
                out.flush();
                out.close();
                in.close();
                produto.setFoto(out.toByteArray());
            }

            produto.gravar();
            session.removeAttribute("listaGruposProdutos");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        output.setValue("titulo", "Cadastro de Produtos");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Produto"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/produto.iniciar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/produto/pesquisar.jsp"))
    public String excluir() {
        try {
            Produto produto = new Produto(input.getLong("txt_id"));
            if (produto != null && produto.getId() != null)
                produto.excluir();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return SUCCESS;
    }
}
