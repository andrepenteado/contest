
package br.com.alphadev.contest.actions.estoque;

import java.util.Date;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.TipoPesquisaProduto;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.GrupoProduto;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.contest.services.EstoqueService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author André Penteado
 * @since 14/02/2010 - 16:09:17
 */
@ActionClass(prefix = "/estoque/relatorio")
public class RelatorioEstoqueAction extends BaseAction implements Authorizable {

    private Log4jWrapper log = new Log4jWrapper(RelatorioEstoqueAction.class, null);
    private EstoqueService estoqueService = null;
    private CadastroService cadastroService = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            estoqueService = (EstoqueService)ServicesFactory.getInstance(EstoqueService.class, new Object[] { userLogin });
            cadastroService = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(RelatorioEstoqueAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(RelatorioEstoqueAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ESTOQUE) != -1)
            return true;

        return false;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/estoque/relatorio/sintetico.jsp"))
    public String sintetico() {
        try {
            Date data = input.getDate("txt_data", "dd/MM/yyyy");
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

            output.setValue("listaEstoque", estoqueService.relatorioSintetico(
                            cadastroService.pesquisarProduto(codigoReferenciaDescricao, grupoProduto, fornecedor, tipoPesquisaProduto, true), data));
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/estoque/relatorio/analitico.jsp"))
    public String analitico() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/estoque/relatorio/previsao.jsp"))
    public String previsao() {
        try {
            if (input.getString("btn_pesquisar") != null) {
                Date dataInicial = input.getDate("txt_data_inicial", "dd/MM/yyyy");
                Date dataFinal = input.getDate("txt_data_final", "dd/MM/yyyy");
                String descProduto = input.getString("txt_descricao_produto");
                int diasSeguranca = input.getInt("txt_dias_seguranca");
                char periodicidade = input.getString("cbo_periodicidade").toCharArray()[0];
                String tempoEntrega = input.getString("rad_entrega");
                int diasEntrega = input.getInt("txt_dias_demora");

                List<Object> lista = estoqueService.calcularPrevisaoDeEstoque(dataInicial, dataFinal, descProduto, diasSeguranca, periodicidade,
                                tempoEntrega, diasEntrega);
                output.setValue("lista", lista);
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
