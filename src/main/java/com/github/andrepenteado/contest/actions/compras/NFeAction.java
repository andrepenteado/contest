
package com.github.andrepenteado.contest.actions.compras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Namespace;
import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.core.FileUpload;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.entity.Compra;
import com.github.andrepenteado.contest.services.NfeService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.exception.ServiceValidationException;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

@ActionClass(prefix = "/compras/nfe")
public class NFeAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(NFeAction.class, null);
    private NfeService nfeService = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            nfeService = (NfeService)ServicesFactory.getInstance(NfeService.class, new Object[] { userLogin });
            log = new Log4jWrapper(NFeAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(NFeAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_COMPRAS) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {

    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/importar.jsp"))
    public String iniciar() {
        output.setValue("contadorCampo", 2);
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/importar.jsp"))
    public String importar() {
        List<Compra> listaCompra = null;
        Map<String, List<String>> mapErros = null;

        String contentType = "text/xml";

        List<FileUpload> arquivosXmls = new ArrayList<FileUpload>();

        int contador = 1;
        FileUpload fileUpload = null;
        do {
            fileUpload = (FileUpload)input.getValue("txt_arquivo_" + contador);
            if (fileUpload != null) {
                if (!contentType.equals(fileUpload.getContentType())) {
                    addError("Tipo de arquivo inválido. Apenas arquivos com extensão .xml podem ser importados.");
                    return SUCCESS;
                }
                arquivosXmls.add(fileUpload);
            }
            contador++;
        }
        while (fileUpload != null);

        if (arquivosXmls == null || arquivosXmls.isEmpty()) {
            addError("Selecione um arquivo");
            return SUCCESS;
        }

        for (FileUpload xml : arquivosXmls) {
            Namespace namespace = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");

            Compra compra = null;

            try {
                compra = nfeService.converterXml(xml.toByteArray(), namespace);
            }
            catch (ServiceValidationException ex) {
                if (mapErros == null)
                    mapErros = new HashMap<String, List<String>>();

                //quebra a mensagem de erro por vírgula e seta na lista de erros para exibir na tela
                String mensagem = ex.getMessage();
                mensagem = mensagem.substring(1, mensagem.length() - 1);
                String[] erros = mensagem.split("[,]");
                List<String> listaErros = Arrays.asList(erros);
                mapErros.put(xml.getOriginalFilename(), listaErros);
            }
            catch (Exception ex) {
                addError(ConfigHelper.get().getString("error.general"));
                log.error(ConfigHelper.get().getString("error.general"), ex);
            }

            if (compra != null) {
                if (listaCompra == null)
                    listaCompra = new ArrayList<Compra>();
                listaCompra.add(compra);
            }

        }

        //RETORNAR RESULTADOS
        output.setValue("listaCompra", listaCompra); //Lista de Compras importadas
        output.setValue("mapErrosKeys", mapErros != null ? mapErros.keySet() : null); //Contém o nome dos arquivos
        output.setValue("mapErros", mapErros); //Mapa com nomes dos arquivos e respectivos erros

        return SUCCESS;
    }
}