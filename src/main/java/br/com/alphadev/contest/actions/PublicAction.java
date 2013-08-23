
package br.com.alphadev.contest.actions;

import java.util.List;

import org.mentawai.core.BaseAction;
import org.mentawai.core.StreamConsequence;
import org.mentawai.filter.AuthenticationFree;

import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.contest.services.UtilService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;

/**
 * @author Andre Penteado
 * @since 11/08/2007 - 16:04:03
 */
public class PublicAction extends BaseAction implements AuthenticationFree {

    public static final String HOME = "HOME";
    public static final String LOGIN = "LOGIN";

    public boolean bypassAuthentication(String innerAction) {
        return true;
    }

    public String catalogoProdutos() {
        try {
            CadastroService cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
            List<Produto> listaProdutos = cadastroServices.pesquisarProdutosComFoto();
            output.setValue("listaProdutos", listaProdutos);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
        }
        return SUCCESS;
    }

    public String pesquisarEJBQL() throws Exception {
        UtilService util = (UtilService)ServicesFactory.getInstance(UtilService.class, null);
        output.setValue("result", util.pesquisarEJBQL(input.getString("txt_ejbql")));
        return SUCCESS;
    }

    public String visualizarFoto() {
        try {
            Produto produto = new Produto(input.getLong("txt_id"));
            if (produto != null && produto.getFoto() != null && produto.getFoto().length > 0) {
                output.setValue(StreamConsequence.CONTENT_LENGTH, produto.getFoto().length);
                output.setValue(StreamConsequence.STREAM, produto.getFoto());
            }
            else
                output.setValue(StreamConsequence.STREAM, new byte[0]);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
        }
        return SUCCESS;
    }
}