
package com.github.andrepenteado.contest.actions.financeiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.entity.Compra;
import com.github.andrepenteado.contest.services.FinanceiroService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.exception.ServiceValidationException;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

@ActionClass(prefix = "/financeiro/retorno")
public class RetornoBancarioAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(RetornoBancarioAction.class, null);
    private FinanceiroService financeiroService = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            financeiroService = (FinanceiroService)ServicesFactory.getInstance(FinanceiroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(RetornoBancarioAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(RetornoBancarioAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_FINANCEIRO) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {

    }

    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/retorno.jsp"))
    public String iniciar() {
        output.setValue("contadorCampo", 2);
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/retorno.jsp"))
    public String processar() {
        List<Compra> listaCompra = null;
        Map<String, List<String>> mapErros = null;

        List<FileItem> arquivosRetorno = new ArrayList<FileItem>();

        int contador = 1;
        FileItem fileItem = null;
        do {
            fileItem = (FileItem)input.getValue("txt_arquivo_" + contador);
            if (fileItem != null) {
                arquivosRetorno.add(fileItem);
            }
            contador++;
        }
        while (fileItem != null);

        if (arquivosRetorno == null || arquivosRetorno.isEmpty()) {
            addError("Selecione um arquivo");
            return SUCCESS;
        }

        for (FileItem retorno : arquivosRetorno) {
            Collection<Compra> comprasProcessadas = null;
            try {
                comprasProcessadas = financeiroService.processarRetornoBancario(retorno.getString());
            }
            catch (ServiceValidationException ex) {
                if (mapErros == null)
                    mapErros = new HashMap<String, List<String>>();

                //quebra a mensagem de erro por vírgula e seta na lista de erros para exibir na tela
                String mensagem = ex.getMessage();
                mensagem = mensagem.substring(1, mensagem.length() - 1);
                String[] erros = mensagem.split("[,]");
                List<String> listaErros = Arrays.asList(erros);
                mapErros.put(retorno.getName(), listaErros);
            }
            catch (Exception ex) {
                addError(ConfigHelper.get().getString("error.general"));
                log.error(ConfigHelper.get().getString("error.general"), ex);
            }

            if (comprasProcessadas != null) {
                if (listaCompra == null)
                    listaCompra = new ArrayList<Compra>();
                listaCompra.addAll(comprasProcessadas);
            }

        }

        //RETORNAR RESULTADOS
        output.setValue("listaCompra", listaCompra); //Lista de Compras importadas
        output.setValue("mapErrosKeys", mapErros != null ? mapErros.keySet() : null); //Contém o nome dos arquivos
        output.setValue("mapErros", mapErros); //Mapa com nomes dos arquivos e respectivos erros

        return SUCCESS;
    }
}