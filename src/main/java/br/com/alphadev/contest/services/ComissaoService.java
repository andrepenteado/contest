
package br.com.alphadev.contest.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;

import br.com.alphadev.contest.entity.Comissao;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.entity.valueObject.VistaPrazo;
import br.com.alphadev.core.BasicService;
import br.com.alphadev.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 16/04/2009 - 17:44:36
 */
@Stateless
public class ComissaoService extends BasicService {

    public List<Comissao> pesquisarComissoesPendentes(Funcionario funcionario, VistaPrazo vistaPrazo) {
        log.info("Pesquisar comissões pendentes do funcionário: ".concat(funcionario.toString()));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c FROM br.com.alphadev.contest.entity.Comissao c ");
        hql.append("WHERE c.dataPagamento IS NULL ");
        hql.append("AND c.venda.funcionario.id = :pIdFuncionario ");
        hql.append("AND ( ");
        if (vistaPrazo == null || vistaPrazo.equals(VistaPrazo.PRAZO)) {
            hql.append("   c.venda.id IN ");
            hql.append("   ( SELECT nf.venda.id FROM br.com.alphadev.contest.entity.NotaFiscal nf WHERE cancelada = false ) ");
        }
        if (vistaPrazo == null)
            hql.append("   OR ");
        if (vistaPrazo == null || vistaPrazo.equals(VistaPrazo.VISTA)) {
            hql.append("   c.venda.id IN ");
            hql.append("   ( SELECT o.venda.id FROM br.com.alphadev.contest.entity.Orcamento o ) ");
        }
        hql.append(") ORDER BY c.venda.dataLancamento ");

        Object[][] params = new Object[][] { { "pIdFuncionario", funcionario.getId() } };
        return (List<Comissao>)executeQuery(hql.toString(), params);
    }

    public List<Object[]> pesquisarComissoesPagas(Funcionario funcionario) {
        log.info("Pesquisar comissões pagas ao funcionário: ".concat(funcionario.toString()));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c.dataPagamento, SUM(c.valor) ");
        hql.append("FROM br.com.alphadev.contest.entity.Comissao c ");
        hql.append("WHERE c.venda.funcionario.id = :pIdFuncionario ");
        hql.append("AND c.dataPagamento IS NOT NULL ");
        hql.append("GROUP BY c.dataPagamento ");
        hql.append("ORDER BY c.dataPagamento DESC ");

        Object[][] params = new Object[][] { { "pIdFuncionario", funcionario.getId() } };
        return (List<Object[]>)executeQuery(hql.toString(), params);
    }

    public List<Comissao> pesquisarComissoesPagasPorDataPagamento(Funcionario funcionario, Date dataPagamento) {
        log.info("Pesquisar comissões pagas por data pagamento: ".concat(funcionario.toString()).concat(" - ")
                        .concat(FunctionsHelper.dateFormat(dataPagamento, "dd/MM/yyyy")));

        GregorianCalendar gcInicial = new GregorianCalendar();
        gcInicial.setTime(dataPagamento);
        gcInicial.set(Calendar.HOUR_OF_DAY, 0);

        GregorianCalendar gcFinal = new GregorianCalendar();
        gcFinal.setTime(dataPagamento);
        gcFinal.set(Calendar.HOUR_OF_DAY, 24);

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c FROM br.com.alphadev.contest.entity.Comissao c ");
        hql.append("WHERE c.venda.funcionario.id = :pIdFuncionario ");
        hql.append("AND c.dataPagamento BETWEEN :pDataPagamentoInicio AND :pDataPagamentoFim ");
        hql.append("ORDER BY c.venda.cliente.nome ");

        Object[][] params = new Object[][] { { "pIdFuncionario", funcionario.getId() }, { "pDataPagamentoInicio", gcInicial.getTime() },
                        { "pDataPagamentoFim", gcFinal.getTime() } };
        return (List<Comissao>)executeQuery(hql.toString(), params);
    }
}
