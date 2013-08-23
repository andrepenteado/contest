
package br.com.alphadev.contest.actions.financeiro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.entity.Receber;
import br.com.alphadev.contest.entity.TipoConta;
import br.com.alphadev.contest.filters.VendaFilter;
import br.com.alphadev.contest.services.FinanceiroService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.FunctionsHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

@ActionClass(prefix = "/financeiro/relatorio")
public class RelatorioFinanceiroAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(RelatorioFinanceiroAction.class, null);
    private FinanceiroService financeiroService = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            financeiroService = (FinanceiroService)ServicesFactory.getInstance(FinanceiroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(RelatorioFinanceiroAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(RelatorioFinanceiroAction.class.getName()), ex);
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

    @Override
    public void prepareValidator(Validator validator, String innerAction) {
        if ("tipoContaPorPeriodo".equals(innerAction) && input.getString("btn_pesquisar") != null) {
            validator.add("txt_data_inicial", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Data Inicial"));
            validator.add("txt_data_final", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Data Final"));
            validator.add("chk_tipo_conta", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Tipo de Conta"));
            validator.add("cbo_periodicidade", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Periodicidade"));
            validator.add("chk_situacao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Situação"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/relatorio/balanco_no_periodo.jsp"))
    public String balancoNoPeriodo() {
        try {
            if (input.getString("btn_pesquisar") != null) {
                Date dataInicial = input.getDate("txt_data_inicial", "dd/MM/yyyy");
                Date dataFinal = input.getDate("txt_data_final", "dd/MM/yyyy");
                int[] idsTipoConta = input.getInts("chk_tipo_conta");
                char periodicidade = input.getString("cbo_periodicidade").toCharArray()[0];
                char situacao = input.getString("chk_situacao").toCharArray()[0];
                boolean gerarGrafico = input.getBoolean("chk_grafico");

                // Gerar relatório
                List<Object[]> lista = financeiroService.pesquisarContasPorTipoPorPeriodo(dataInicial, dataFinal, idsTipoConta, periodicidade,
                                situacao);
                output.setValue("lista", lista);

                // Gerar gráfico
                if (gerarGrafico)
                    output.setValue("jpegBase64", Base64.encodeBase64String(financeiroService.gerarGraficoContasPorTipoPorPeriodo(lista, idsTipoConta, periodicidade)));
            }
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/relatorio/detalhar_tipo_conta.jsp"))
    public String detalharTipoConta() {
        try {
            char periodicidade = input.getString("periodicidade").toCharArray()[0];
            TipoConta tipoConta = null;
            Long idTipoConta = input.getLong("id_tipo_conta");

            if (idTipoConta > 0)
                tipoConta = new TipoConta(idTipoConta);

            if (periodicidade == 'D') // Diário
                ;
            else if (periodicidade == 'S') // Semanal
                ;
            else if (periodicidade == 'M') // Mensal
                ;
            else if (periodicidade == 'T') // Trimestral
                ;
            else if (periodicidade == 'A') // Anual
                ;

            output.setValue("lista", new ArrayList<Object[]>());
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/relatorio/recebimentos_atraso.jsp"))
    public String recebimentosAtraso() {
        try {
            int ano = input.getInt("lst_ano");
            VendaFilter<Receber> filter = ServicesFactory.getInstance(VendaFilter.class, Receber.class);
            if (ano > 0) {
                filter.setDataEmissaoInicial(FunctionsHelper.stringToDate("01/01/".concat(Integer.toString(ano))));
                filter.setDataEmissaoFinal(FunctionsHelper.stringToDate("31/12/".concat(Integer.toString(ano))));
            }

            // Diminui 1 dia
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(Calendar.DAY_OF_MONTH, -2);

            filter.setDataVencimentoFinal(gc.getTime());
            output.setValue("listaReceber", filter.executeFilter());
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }
}