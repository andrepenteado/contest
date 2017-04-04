
package com.github.andrepenteado.contest.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;

import com.github.andrepenteado.contest.entity.Cliente;
import com.github.andrepenteado.contest.entity.Compra;
import com.github.andrepenteado.contest.entity.ContaCorrente;
import com.github.andrepenteado.contest.entity.Empresa;
import com.github.andrepenteado.contest.entity.Receber;
import com.github.andrepenteado.contest.entity.Recebido;
import com.github.andrepenteado.contest.entity.valueObject.FormaPagamento;
import com.github.andrepenteado.core.BasicService;
import com.github.andrepenteado.exception.ServiceValidationException;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 14/11/2007 - 17:49:11
 */
@Stateless
public class FinanceiroService extends BasicService {

    private Hashtable<Character, String> aggregateFunctions;

    @Override
    public void setParameters(Object[] params) {
        super.setParameters(params);
        aggregateFunctions = new Hashtable<Character, String>();
        aggregateFunctions.put('D', "TO_CHAR(***, '\"periodicidade=D&ano=\"YYYY\"&mes=\"MM\"&dia=\"DD'), TO_CHAR(***, 'YYYY / MM / DD')");
        aggregateFunctions.put('S', "TO_CHAR(***, '\"periodicidade=S&ano=\"YYYY\"&semana=\"WW'), TO_CHAR(***,'YYYY / WW\"o. Semana\"')");
        aggregateFunctions.put('M', "TO_CHAR(***, '\"periodicidade=M&ano=\"YYYY\"&mes=\"MM'), TO_CHAR(***, 'YYYY / MM')");
        aggregateFunctions.put('T', "TO_CHAR(***, '\"periodicidade=T&ano=\"YYYY\"&trimestre=\"Q'), TO_CHAR(***,'YYYY / Q\"o. Trimestre\"')");
        aggregateFunctions.put('A', "TO_CHAR(***, '\"periodicidade=A&ano=\"YYYY'), TO_CHAR(***, 'YYYY')");
    }

    /**
     * Relatório analítico financeiro com dados 
     * de tipos de contas agrupados em um período
     * 
     * @param dataInicial = Data inicial
     * @param dataFinal = Data final
     * @param idsTiposConta = Tipos de contas a serem pesquisadas
     * @param periodicidade = Diário, Semanal, Mensal, Trimestral ou Anual
     * @param situacao = A receber, A pagar, Recebido ou Pago
     * 
     * @return Lista com array de objetos. Na posição do objeto:
     *          0 - Periodo conforme a periodicidade selecionada
     *          1 - Id do tipo de conta
     *          2 - Descrição do tipo de conta
     *          3 - Somatório do valor do tipo de conta daquele período
     */
    public List<Object[]> pesquisarContasPorTipoPorPeriodo(Date dataInicial, Date dataFinal, int[] idsTiposConta, char periodicidade, char situacao) {
        log.info("Pesquisar contas no período");

        StringBuilder sb = new StringBuilder();

        // Acertar datas
        GregorianCalendar gcInicial = new GregorianCalendar();
        gcInicial.setTime(dataInicial);
        gcInicial.set(Calendar.HOUR_OF_DAY, 0);
        GregorianCalendar gcFinal = new GregorianCalendar();
        gcFinal.setTime(dataFinal);
        gcFinal.set(Calendar.HOUR_OF_DAY, 24);

        // Acertar IDs
        boolean movimentoProduto = false;
        ArrayList<Integer> idsTC = new ArrayList<Integer>();
        for (int i = 0; i < idsTiposConta.length; i++) {
            int id = idsTiposConta[i];
            if (id == -10) {
                movimentoProduto = true;
                continue;
            }
            idsTC.add(id);
        }

        // Construir HQL
        if (situacao == 'R') { // Receber
            String aggregate = aggregateFunctions.get(periodicidade).replace("***", "r.vencimento").concat(", r.tipoConta.id");
            sb.append("SELECT ").append(aggregate).append(", ");
            sb.append("(SELECT descricao FROM com.github.andrepenteado.contest.entity.TipoConta WHERE id = r.tipoConta.id) ");
            sb.append(", SUM(r.valor) FROM com.github.andrepenteado.contest.entity.Receber r ");
            sb.append("WHERE r.vencimento BETWEEN :pDataInicial AND :pDataFinal ");
            sb.append("AND ( ");
            if (idsTC.size() > 0)
                sb.append("r.tipoConta IN ( ").append(StringUtils.join(idsTC, ",")).append(") ");
            if (movimentoProduto && idsTC.size() > 0)
                sb.append("OR ");
            if (movimentoProduto)
                sb.append("r.tipoConta IS NULL ");
            sb.append(") ");
            sb.append("GROUP BY ").append(aggregate).append(" ");
            sb.append("ORDER BY ").append(aggregate);
        }
        else if (situacao == 'P') { // Pagar
            String aggregate = aggregateFunctions.get(periodicidade).replace("***", "p.vencimento").concat(", p.tipoConta.id");
            sb.append("SELECT ").append(aggregate).append(", ");
            sb.append("(SELECT descricao FROM com.github.andrepenteado.contest.entity.TipoConta WHERE id = p.tipoConta.id) ");
            sb.append(", SUM(p.valor) FROM com.github.andrepenteado.contest.entity.Pagar p ");
            sb.append("WHERE p.vencimento BETWEEN :pDataInicial AND :pDataFinal ");
            sb.append("AND ( ");
            if (idsTC.size() > 0)
                sb.append("p.tipoConta IN ( ").append(StringUtils.join(idsTC, ",")).append(") ");
            if (movimentoProduto && idsTC.size() > 0)
                sb.append("OR ");
            if (movimentoProduto)
                sb.append("p.tipoConta IS NULL ");
            sb.append(") ");
            sb.append("GROUP BY ").append(aggregate).append(" ");
            sb.append("ORDER BY ").append(aggregate);
        }
        else if (situacao == 'B') { // Recebido
            String aggregate = aggregateFunctions.get(periodicidade).replace("***", "r.receber.vencimento").concat(", r.receber.tipoConta.id");
            sb.append("SELECT ").append(aggregate).append(", ");
            sb.append("(SELECT descricao FROM com.github.andrepenteado.contest.entity.TipoConta WHERE id = r.receber.tipoConta.id) ");
            sb.append(", SUM(r.valorPago) FROM com.github.andrepenteado.contest.entity.Recebido r ");
            sb.append("WHERE r.receber.vencimento BETWEEN :pDataInicial AND :pDataFinal ");
            sb.append("AND ( ");
            if (idsTC.size() > 0)
                sb.append("r.receber.tipoConta IN ( ").append(StringUtils.join(idsTC, ",")).append(") ");
            if (movimentoProduto && idsTC.size() > 0)
                sb.append("OR ");
            if (movimentoProduto)
                sb.append("r.receber.tipoConta IS NULL ");
            sb.append(") ");
            sb.append("GROUP BY ").append(aggregate).append(" ");
            sb.append("ORDER BY ").append(aggregate);
        }
        else if (situacao == 'G') { // Pago
            String aggregate = aggregateFunctions.get(periodicidade).replace("***", "p.pagar.vencimento").concat(", p.pagar.tipoConta.id");
            sb.append("SELECT ").append(aggregate).append(", ");
            sb.append("(SELECT descricao FROM com.github.andrepenteado.contest.entity.TipoConta WHERE id = p.pagar.tipoConta.id) ");
            sb.append(", SUM(p.valorPago) FROM com.github.andrepenteado.contest.entity.Pago p ");
            sb.append("WHERE p.pagar.vencimento BETWEEN :pDataInicial AND :pDataFinal ");
            sb.append("AND ( ");
            if (idsTC.size() > 0)
                sb.append("p.pagar.tipoConta IN ( ").append(StringUtils.join(idsTC, ",")).append(") ");
            if (movimentoProduto && idsTC.size() > 0)
                sb.append("OR ");
            if (movimentoProduto)
                sb.append("p.pagar.tipoConta IS NULL ");
            sb.append(") ");
            sb.append("GROUP BY ").append(aggregate).append(" ");
            sb.append("ORDER BY ").append(aggregate);
        }

        Object[][] params = new Object[][] { { "pDataInicial", dataInicial }, { "pDataFinal", dataFinal } };
        return (List<Object[]>)executeQuery(sb.toString(), params);
    }

    /**
     * Gerar relatório com dados do relatório tipos de conta por período.
     * Cria gráfico de linhas no tempo, sendo cada linha um tipo de conta.
     * 
     * @param relatorio = Dados do relatório
     * 
     * @return Imagem binária em JPEG do gráfico
     */
    public byte[] gerarGraficoContasPorTipoPorPeriodo(List<Object[]> relatorio, int[] idsTiposConta, char periodicidade) throws IOException {
        log.info("Gerar gráfico de contas no período");

        // Separa em linhas (TimeSeries) do gráfico os tipos de conta, e coloca o indice o ID do tipo conta
        Hashtable<Integer, TimeSeries> series = new Hashtable<Integer, TimeSeries>();

        // Percorre o relatório
        for (Object[] obj : relatorio) {

            // Verifica o tipo de conta e armazena no seu respectiva linha (TimeSerie)
            for (int id : idsTiposConta) {
                // Caso seja Movimento Produto || Tipo de Conta cadastrado
                if ((obj[2] == null && id == -10) || (obj[2] != null && (Integer)obj[2] == id)) {
                    if (series.get(id) == null) {
                        if (obj[2] == null)
                            series.put(id, new TimeSeries("Movimento de Produto"));
                        else
                            series.put(id, new TimeSeries((String)obj[3]));
                    }
                    TimeSeries aux = series.get(id);
                    aux.add(instanciarRegularTimePeriodDeFuncaoAgregada(periodicidade, (String)obj[1]), (Double)obj[4]);
                    series.put(id, aux);
                }
            }
        }

        // Adiciona linhas ao gráfico
        TimeSeriesCollection ds = new TimeSeriesCollection();
        for (int id : idsTiposConta) {
            TimeSeries s = series.get(id);
            if (s != null)
                ds.addSeries(s);
        }

        JFreeChart grafico = ChartFactory.createTimeSeriesChart("Tipo de Conta no Período",
                        ConfigHelper.get().getString("periodicidadeEstatistica." + periodicidade), "Valores em R$", ds, true, true, true);
        XYPlot plot = grafico.getXYPlot();
        DateAxis dateAxis = (DateAxis)plot.getDomainAxis();
        if (periodicidade == 'D')
            dateAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
        else if (periodicidade == 'S')
            dateAxis.setDateFormatOverride(new SimpleDateFormat("ww/yyyy"));
        else if (periodicidade == 'M' || periodicidade == 'T' || periodicidade == 'A')
            dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM/yyyy"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsJPEG(out, grafico, 600, 400);
        return out.toByteArray();
    }

    /**
     * Instanciar RegularTimePeriod através da decomposição da string
     * com a função agregada que exibe o período
     * 
     * @param periodicidade = Diário, Semanal, Mensal, Trimestral ou Anual
     * @param funcaoAgregada = Valor vindo do BD com a data conforme período
     * 
     * @return RegularTimePeriod (Day, Month, Week, Quarter, Year) do jfreechart
     */
    private RegularTimePeriod instanciarRegularTimePeriodDeFuncaoAgregada(char periodicidade, String funcaoAgregada) {
        if (periodicidade == 'D') {
            String dia = funcaoAgregada.substring(12, 14);
            String mes = funcaoAgregada.substring(7, 9);
            String ano = funcaoAgregada.substring(0, 4);
            return new Day(Integer.parseInt(dia), Integer.parseInt(mes), Integer.parseInt(ano));
        }
        else if (periodicidade == 'S') {
            String semana = funcaoAgregada.substring(7, 9);
            String ano = funcaoAgregada.substring(0, 4);
            return new Week(Integer.parseInt(semana), Integer.parseInt(ano));
        }
        else if (periodicidade == 'M') {
            String mes = funcaoAgregada.substring(7, 9);
            String ano = funcaoAgregada.substring(0, 4);
            return new Month(Integer.parseInt(mes), Integer.parseInt(ano));
        }
        else if (periodicidade == 'T') {
            String trimestre = funcaoAgregada.substring(7, 8);
            String ano = funcaoAgregada.substring(0, 4);
            return new Quarter(Integer.parseInt(trimestre), Integer.parseInt(ano));
        }
        else if (periodicidade == 'A') {
            String ano = funcaoAgregada.substring(0, 4);
            return new Year(Integer.parseInt(ano));
        }
        return null;
    }

    public String gerarArquivoRemessaBoletoCNAB(Receber[] recebimentos) throws ServiceValidationException {
        StringBuilder result = new StringBuilder();

        Empresa empresa = userLogin.getFuncionario().getEmpresa();
        ContaCorrente cc = empresa.getContaCorrenteCobranca();

        if (cc == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Conta Corrente de Cobrança"));

        // Header Arquivo
        result.append(cc.getBanco().getCodigo());
        result.append("00000        2");
        result.append("0").append(empresa.getCnpj().replace(".", "").replace("/", "").replace("-", ""));
        result.append("               "); // Código de transmissão (cedido pelo banco)
        result.append("                         ");
        result.append(FunctionsHelper.fillSpacesRight(empresa.getRazaoSocial(), 30));
        result.append(FunctionsHelper.fillSpacesRight(cc.getBanco().getNome(), 30));
        result.append("          ");
        result.append("1"); // 1=Remessa
        result.append(FunctionsHelper.dateFormat(new Date(), "ddMMyyyy"));
        result.append("      ");
        result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(cc.getSequenciaArquivoCobranca()), 6)); // No. sequencial do arquivo
        result.append("040");
        result.append("                                                                          \n");

        // Header lote
        result.append(cc.getBanco().getCodigo());
        result.append("00011R01  030 2");
        result.append("0").append(empresa.getCnpj().replace(".", "").replace("/", "").replace("-", ""));
        result.append("                              ");
        result.append("    "); // Código de transmissão (cedido pelo banco)
        result.append("     ");
        result.append(FunctionsHelper.fillSpacesRight(empresa.getRazaoSocial(), 30));
        result.append("                                        ");
        result.append("                                        ");
        result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(cc.getSequenciaArquivoCobranca()), 8)); // No. sequencial do arquivo para retorno
        result.append(FunctionsHelper.dateFormat(new Date(), "ddMMyyyy"));
        result.append("                                         \n");

        int loteRemessa = 2;
        int loteSequencial = 1;

        beginTransaction();

        cc.setSequenciaArquivoCobranca(cc.getSequenciaArquivoCobranca() + 1);
        save(cc);

        for (Receber receber : recebimentos) {
            if (!receber.getVenda().getFormaPagamento().name().equals(FormaPagamento.BOLETO.name()))
                throw new ServiceValidationException(ConfigHelper.getProperty("error.vendaNaoBoleto", Long.toString(receber.getVenda().getId())));

            Cliente cliente = receber.getVenda().getCliente();
            loteRemessa++;

            // Detalhe - Segmento P
            result.append(cc.getBanco().getCodigo());
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa), 4).replace(" ", "0"));
            result.append("3");
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteSequencial++), 5).replace(" ", "0"));
            result.append("P ");
            result.append("01"); // Entrada de título
            result.append(FunctionsHelper.fillSpacesLeft(cc.getAgencia(), 5));
            result.append(FunctionsHelper.fillSpacesLeft(cc.getNumero(), 10));
            result.append(FunctionsHelper.fillSpacesLeft(cc.getNumero(), 10));
            result.append("  ");
            result.append(FunctionsHelper.fillSpacesLeft(receber.getVenda().getNotaFiscal().getNumero() + "" + receber.getParcela(), 13).replace(" ",
                            "0"));
            result.append("512  ");
            result.append(FunctionsHelper.fillSpacesRight(receber.getVenda().getNotaFiscal().getNumero() + "/" + receber.getParcela(), 15));
            result.append(FunctionsHelper.dateFormat(receber.getVencimento(), "ddMMyyyy"));
            result.append(FunctionsHelper.numberFormat(receber.getValor(), "0000000000000.00").replace(".", "").replace(",", ""));
            result.append("      02N");
            result.append(FunctionsHelper.dateFormat(receber.getVenda().getNotaFiscal().getEmissao(), "ddMMyyyy"));
            result.append("1");
            result.append(FunctionsHelper.dateFormat(receber.getVencimento(), "ddMMyyyy"));
            result.append(FunctionsHelper.numberFormat(receber.getValorJurosDia(), "0000000000000.00").replace(".", "").replace(",", ""));
            result.append("000000000000000000000000000000000000000000000000000000");
            result.append(FunctionsHelper.fillSpacesRight(receber.getVenda().getNotaFiscal().getNumero() + "/" + receber.getParcela(), 25));
            result.append("205200000           ");
            result.append("\n");

            // Detalhe - Segmento Q
            result.append(cc.getBanco().getCodigo());
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa), 4).replace(" ", "0"));
            result.append("3");
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteSequencial++), 5).replace(" ", "0"));
            result.append("Q 012");
            result.append(FunctionsHelper.fillSpacesRight(cliente.getCnpj().replace(".", "").replace("-", "").replace("/", ""), 15));
            result.append(FunctionsHelper.fillSpacesRight(cliente.getNome(), 40));
            result.append(FunctionsHelper.fillSpacesRight(cliente.getEndereco() + ", " + cliente.getNumero() + " - " + cliente.getComplemento(), 40));
            result.append(FunctionsHelper.fillSpacesRight(cliente.getBairro(), 15));
            result.append(cliente.getCep().replace(".", "").replace("-", ""));
            result.append(FunctionsHelper.fillSpacesRight(cliente.getCidade().getNome(), 15));
            result.append(cliente.getCidade().getEstado().getSigla().trim());
            result.append("0000000000000000                                        000000000000                   ");
            result.append("\n");

            // Detalhe - Segmento R
            result.append(cc.getBanco().getCodigo());
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa), 4).replace(" ", "0"));
            result.append("3");
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteSequencial++), 5).replace(" ", "0"));
            result.append("R 01000000000000000000000000                        200000000000000000000000                                                                                                                                                       ");
            result.append("\n");

            // Detalhe - Segmento S
            /*result.append(cc.getBanco().getCodigo());
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa), 4).replace(" ", "0"));
            result.append("3");
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteSequencial++), 5).replace(" ", "0"));
            result.append("S 011");
            result.append("\n");*/

            // Trailler de Lote
            result.append(cc.getBanco().getCodigo());
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa), 4).replace(" ", "0"));
            result.append("5         ");
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa + 2), 6).replace(" ", "0"));
            result.append("                                                                                                                                                                                                                         ");
            result.append("\n");

            // Dar baixa
            Recebido recebido = new Recebido();
            recebido.setReceber(receber);
            recebido.setFormaPagamento(FormaPagamento.BOLETO);
            recebido.setDataPagamento(receber.getVencimento());
            recebido.setValorPago(receber.getValor());
            save(recebido);
        }

        // Trailler do Arquivo
        result.append(cc.getBanco().getCodigo());
        result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa), 4).replace(" ", "0"));
        result.append("9         000001");
        result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(loteRemessa + 4), 6).replace(" ", "0"));
        result.append("                                                                                                                                                                                                                   ");

        commit();

        return result.toString();
    }

    public Collection<Compra> processarRetornoBancario(String retorno) throws ServiceValidationException {
        return null;
    }
}