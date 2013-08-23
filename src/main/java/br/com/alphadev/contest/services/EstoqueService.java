
package br.com.alphadev.contest.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import br.com.alphadev.contest.KGlobal.EntradaSaida;
import br.com.alphadev.contest.entity.Estoque;
import br.com.alphadev.contest.entity.ItemCompra;
import br.com.alphadev.contest.entity.ItemVenda;
import br.com.alphadev.contest.entity.MovimentoEstoque;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.entity.valueObject.TipoMovimentacao;
import br.com.alphadev.contest.entity.valueObject.VistaPrazo;
import br.com.alphadev.contest.filters.ProdutoFilter;
import br.com.alphadev.core.BasicService;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.FunctionsHelper;

@Stateless
public class EstoqueService extends BasicService {

    public List<ItemCompra> pesquisarItensPorFornecedor(long idFornecedor) {
        if (idFornecedor <= 0)
            return null;

        log.info("Pesquisar itens movimentados do Estoque por fornecedor ID: ".concat(FunctionsHelper.toStringNotNull(idFornecedor)));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT item ");
        hql.append("FROM ");
        hql.append("WHERE item.compra.fornecedor.id = :pIdFornecedor ");
        hql.append("ORDER BY ");
        hql.append("GROUP BY ");

        Object[][] params = new Object[][] { { "pIdFornecedor", idFornecedor } };
        return (List<ItemCompra>)executeQuery(hql.toString(), params);
    }

    @SuppressWarnings("rawtypes")
    public void atualizarMovimentacaoEstoque(Iterator itens, VistaPrazo vistaPrazo, TipoMovimentacao operacao, String observacao) {
        while (itens.hasNext()) {
            Object item = itens.next();
            EntradaSaida tipoMovimento = EntradaSaida.SAIDA;
            Integer[] qtdEstoque = new Integer[] { 0, 0 };
            MovimentoEstoque movimentoEstoque = new MovimentoEstoque();

            if (item instanceof ItemCompra) {
                if (TipoMovimentacao.EMISSAO.equals(operacao))
                    tipoMovimento = EntradaSaida.ENTRADA;
                Produto produto = ((ItemCompra)item).getProduto();
                qtdEstoque = atualizarEstoqueProduto(tipoMovimento, vistaPrazo, ((ItemCompra)item).getQuantidade(), produto);
            }
            else if (item instanceof ItemVenda) {
                if (TipoMovimentacao.DEVOLUCAO.equals(operacao) || TipoMovimentacao.ESTORNO.equals(operacao))
                    tipoMovimento = EntradaSaida.ENTRADA;
                Produto produto = ((ItemVenda)item).getProduto();
                qtdEstoque = atualizarEstoqueProduto(tipoMovimento, vistaPrazo, ((ItemVenda)item).getQuantidade(), produto);
            }

            movimentoEstoque.setData(new Date());
            movimentoEstoque.setTipoMovimentacao(operacao);
            movimentoEstoque.setEmpresa(this.userLogin.getFuncionario().getEmpresa());
            movimentoEstoque.setVistaPrazo(vistaPrazo);
            movimentoEstoque.setSpecIn(qtdEstoque[0]);
            movimentoEstoque.setSpecOut(qtdEstoque[1]);
            movimentoEstoque.setObservacao(observacao);

            if (item instanceof ItemCompra) {
                ItemCompra itemAtualizado = (ItemCompra)save(item);
                movimentoEstoque.setItemCompra(itemAtualizado);
            }
            else if (item instanceof ItemVenda) {
                ItemVenda itemAtualizado = (ItemVenda)save(item);
                movimentoEstoque.setItemVenda(itemAtualizado);
            }

            save(movimentoEstoque);
            log.info("Atualizado Movimentação de Estoque: ".concat(item.toString()));
        }
    }

    private Integer[] atualizarEstoqueProduto(EntradaSaida tipoMovimento, VistaPrazo vistaPrazo, int quantidade, Produto produto) {
        Estoque estoqueAtual = null;

        for (Estoque estoque : produto.getEstoques())
            if (estoque.getEmpresa().getId().intValue() == userLogin.getFuncionario().getEmpresa().getId().intValue())
                estoqueAtual = estoque;

        if (estoqueAtual == null) { // Novo produto no estoque
            estoqueAtual = new Estoque();
            estoqueAtual.setId(-1l);
            estoqueAtual.setEmpresa(userLogin.getFuncionario().getEmpresa());
            estoqueAtual.setProduto(produto);
            estoqueAtual.setSpecIn(0);
            estoqueAtual.setSpecOut(0);
            produto.getEstoques().add(estoqueAtual);
        }

        if (tipoMovimento.equals(EntradaSaida.ENTRADA) && vistaPrazo.equals(VistaPrazo.PRAZO))
            estoqueAtual.setSpecIn(estoqueAtual.getSpecIn().intValue() + quantidade);
        else if (tipoMovimento.equals(EntradaSaida.SAIDA) && vistaPrazo.equals(VistaPrazo.PRAZO))
            estoqueAtual.setSpecIn(estoqueAtual.getSpecIn().intValue() - quantidade);
        else if (tipoMovimento.equals(EntradaSaida.ENTRADA) && vistaPrazo.equals(VistaPrazo.VISTA))
            estoqueAtual.setSpecOut(estoqueAtual.getSpecOut().intValue() + quantidade);
        else if (tipoMovimento.equals(EntradaSaida.SAIDA) && vistaPrazo.equals(VistaPrazo.VISTA))
            estoqueAtual.setSpecOut(estoqueAtual.getSpecOut().intValue() - quantidade);

        save(produto);
        log.info("Atualizado quantidade de estoque do produto ".concat(produto.toString()));

        return new Integer[] { estoqueAtual.getSpecIn(), estoqueAtual.getSpecOut() };
    }

    public void gravarMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
        beginTransaction();
        EntradaSaida tipoMovimento = null;
        movimentoEstoque.setEmpresa(this.userLogin.getFuncionario().getEmpresa());

        if (movimentoEstoque.getItemCompra() != null) {
            tipoMovimento = EntradaSaida.ENTRADA;
            movimentoEstoque.setItemCompra((ItemCompra)save(movimentoEstoque.getItemCompra()));
        }
        else if (movimentoEstoque.getItemVenda() != null) {
            tipoMovimento = EntradaSaida.SAIDA;
            movimentoEstoque.setItemVenda((ItemVenda)save(movimentoEstoque.getItemVenda()));
        }

        Integer[] qtdEstoque = atualizarEstoqueProduto(tipoMovimento, movimentoEstoque.getVistaPrazo(), movimentoEstoque.getQuantidade(),
                        movimentoEstoque.getProduto());
        movimentoEstoque.setSpecIn(qtdEstoque[0]);
        movimentoEstoque.setSpecOut(qtdEstoque[1]);

        save(movimentoEstoque);
        commit();
    }

    public List<Object> calcularPrevisaoDeEstoque(java.util.Date dataInicial, java.util.Date dataFinal, String descProduto, int diasSeguranca,
                    char periodicidade, String tempoEntrega, int diasEntrega) {
        List<Object> lista = new ArrayList<Object>();
        ;
        try {
            Long qtdeProduto = pesquisarQuantidadeProdutoPorDescricao(descProduto);
            Integer qtdeDias = diasEntreDatas(dataInicial, dataFinal);

            double media = (double)qtdeProduto / (double)qtdeDias;
            Double minimo = 0d;
            Double sugestao = 0d;
            Double tempoCobertura = 0d;
            int diasPeriodicidade = 0, diasParaEntrega = 0;

            List<Produto> maisVendidos = pesquisarProdutosMaisVendidosNoPeriodo(dataInicial, dataFinal);

            List<Produto> prodA = pesquisarProdutosCurvaA(maisVendidos);
            List<Produto> prodB = pesquisarProdutosCurvaB(maisVendidos);
            List<Produto> prodC = pesquisarProdutosCurvaC(maisVendidos);

            for (Produto produto : prodA) {
                if (produto.getDescricao().equals(descProduto))
                    minimo = (media * diasEntrega) + (media * diasSeguranca);
            }

            for (Produto produto : prodB) {
                if (produto.getDescricao().equals(descProduto))
                    minimo = media * diasEntrega;
            }

            for (Produto produto : prodC) {
                if (produto.getDescricao().equals(descProduto))
                    minimo = media * diasEntrega;
            }

            String period = Character.toString(periodicidade);

            if (period.equals("D"))
                diasPeriodicidade = 1;
            if (period.equals("S"))
                diasPeriodicidade = 7;
            if (period.equals("M"))
                diasPeriodicidade = 30;
            if (period.equals("T"))
                diasPeriodicidade = 90;
            if (period.equals("A"))
                diasPeriodicidade = 365;

            if (tempoEntrega.equals("V"))
                diasParaEntrega = 0;
            else
                diasParaEntrega = diasEntrega;

            ProdutoFilter filter = null;
            filter = new ProdutoFilter();
            filter.setDescricao(descProduto);
            Collection<Produto> produto = filter.executeFilter();

            for (Produto prod : produto) {
                Estoque produtoNoEstoque = new Estoque(prod.getId());

                sugestao = (minimo + ((diasPeriodicidade - diasParaEntrega) * media)) - (prod.getEstoques().size());
                tempoCobertura = prod.getEstoques().size() / media;
                int fisico = prod.getEstoques().size();
                int in = produtoNoEstoque.getSpecIn();
                int out = produtoNoEstoque.getSpecOut();

                lista.add(minimo);
                lista.add(sugestao);
                lista.add(tempoCobertura);
                lista.add(fisico);
                //lista.add(in, out);
                lista.add(produtoNoEstoque);
            }

        }
        catch (Exception ex) {
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return lista;
    }

    public List<Produto> pesquisarProdutosMaisVendidosNoPeriodo(Date dataInicial, Date dataFinal) {
        if (dataInicial == null || dataFinal == null)
            return null;

        log.info("Pesquisar produtos mais vendidos no período: ");
        log.debug(">>> Data início: ".concat(FunctionsHelper.dateFormat(dataInicial, "dd/MM/yyyy")));
        log.debug(">>> Data fim: ".concat(FunctionsHelper.dateFormat(dataFinal, "dd/MM/yyyy")));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT me.itemVenda.produto.id ");
        hql.append("FROM br.com.alphadev.contest.entity.MovimentoEstoque me ");
        hql.append("WHERE me.data BETWEEN :pDataInicial AND :pDataFinal ");
        hql.append("GROUP BY me.itemVenda.produto.id ");
        hql.append("ORDER BY SUM(me.itemVenda.quantidade) DESC ");
        /*Presciso nesta select ter o todas as vendas e quantos produtos foram vendidos para podermos tirar 
         * as médias e rankiar os produtos em A,B ou C agora não sei se dá para fazer tudo isso pela Select
         * ou teremos que tratar o dado na classe.
         * 
         * */

        // Seta a hora da data inicial para 0
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataInicial);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        dataInicial = gc.getTime();

        // Seta a hora da data final para 24 e soma 1 dia
        gc.setTime(dataFinal);
        gc.set(Calendar.HOUR_OF_DAY, 24);
        gc.add(Calendar.DAY_OF_MONTH, 1);
        dataFinal = gc.getTime();

        Object[][] params = new Object[][] { { "pDataInicial", dataInicial }, { "pDataFinal", dataFinal } };
        List<Long> listaIdsProdutos = (List<Long>)executeQuery(hql.toString(), params);
        List<Produto> result = null;
        if (listaIdsProdutos != null && !listaIdsProdutos.isEmpty()) {
            result = new ArrayList<Produto>();
            for (Long idProduto : listaIdsProdutos)
                result.add(new Produto(idProduto));

        }
        return result;
    }

    private List<Produto> pesquisarProdutosCurvaA(List<Produto> listaProdutosMaisVendidos) {
        List<Produto> result = null;
        if (listaProdutosMaisVendidos != null && !listaProdutosMaisVendidos.isEmpty()) {
            result = new ArrayList<Produto>();
            int totalProduto = listaProdutosMaisVendidos.size();
            int quantidadeCurvaA = (int)Math.round(totalProduto * 0.3);
            for (int i = 0; i < quantidadeCurvaA; i++) {
                Produto produto = listaProdutosMaisVendidos.get(i);
                result.add(produto);
            }
        }
        return result;
    }

    private List<Produto> pesquisarProdutosCurvaB(List<Produto> listaProdutosMaisVendidos) {
        List<Produto> result = null;
        if (listaProdutosMaisVendidos != null && !listaProdutosMaisVendidos.isEmpty()) {
            result = new ArrayList<Produto>();
            int totalProduto = listaProdutosMaisVendidos.size();
            int quantidadeCurvaB = (int)Math.round(totalProduto * 0.4);
            for (int i = (int)Math.round(totalProduto * 0.3); i < quantidadeCurvaB; i++) {
                Produto produto = listaProdutosMaisVendidos.get(i);
                result.add(produto);
            }
        }
        return result;
    }

    private List<Produto> pesquisarProdutosCurvaC(List<Produto> listaProdutosMaisVendidos) {
        List<Produto> result = null;
        if (listaProdutosMaisVendidos != null && !listaProdutosMaisVendidos.isEmpty()) {
            result = new ArrayList<Produto>();
            int totalProduto = listaProdutosMaisVendidos.size();
            int quantidadeCurvaC = (int)Math.round(totalProduto * 0.4);
            for (int i = (int)Math.round(totalProduto * 0.7); i < totalProduto; i++) {
                Produto produto = listaProdutosMaisVendidos.get(i);
                result.add(produto);
            }
        }
        return result;
    }

    public Long pesquisarQuantidadeProdutoPorDescricao(String descProduto) {
        if (descProduto == null)
            return null;
        log.info("Pesquisar movimento de estoque do produto: ".concat(FunctionsHelper.toStringNotNull(descProduto)));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT SUM (p.itemVenda.quantidade) ");
        hql.append("FROM br.com.alphadev.contest.entity.MovimentoEstoque p ");
        hql.append("WHERE p.itemVenda.produto.descricao = :pDescProduto ");

        Object[][] params = new Object[][] { { "pDescProduto", descProduto } };
        List<Long> result = (List<Long>)executeQuery(hql.toString(), params);
        return result.get(0);
    }

    public List<Object[]> pesquisarQuantEmEstoque(String descProduto) {

        log.info("Pesquisar quantidade em estoque do produto: ".concat(FunctionsHelper.toStringNotNull(descProduto)));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT p ");
        hql.append("FROM br.com.alphadev.contest.entity.Estoque p ");
        hql.append("WHERE p.produto.descricao = :pDescProduto ");

        Object[][] params = new Object[][] { { "pDescProduto", descProduto } };

        return (List<Object[]>)executeQuery(hql.toString(), params);
    }

    private int diasEntreDatas(Date inicio, Date fim) {
        if (fim.before(inicio))
            throw new IllegalArgumentException("Data inicial maior que a final");
        Calendar dataInicio = new GregorianCalendar().getInstance();
        dataInicio.setTime(inicio);
        dataInicio.set(Calendar.HOUR_OF_DAY, 0);
        dataInicio.set(Calendar.MINUTE, 0);
        dataInicio.set(Calendar.SECOND, 0);
        long inicioMilisegundos = dataInicio.getTimeInMillis();

        Calendar dataFim = new GregorianCalendar().getInstance();
        dataFim.setTime(fim);
        dataFim.set(Calendar.HOUR_OF_DAY, 0);
        dataFim.set(Calendar.MINUTE, 0);
        dataFim.set(Calendar.SECOND, 0);
        long fimMilisegundos = dataFim.getTimeInMillis();

        return Math.round((fimMilisegundos - inicioMilisegundos) / (1000 * 60 * 60 * 24));
    }

    public Collection<MovimentoEstoque> relatorioSintetico(Collection<Produto> produtos, Date data) {
        ArrayList<MovimentoEstoque> result = new ArrayList<MovimentoEstoque>();

        for (Produto produto : produtos) {
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT me FROM br.com.alphadev.contest.entity.MovimentoEstoque me ");
            hql.append("LEFT JOIN me.itemVenda iv ");
            hql.append("LEFT JOIN me.itemCompra ic ");
            hql.append("WHERE me.data <= :pData ");
            hql.append("AND (");
            hql.append("   iv.produto.id = :pIdProduto ");
            hql.append("   OR ");
            hql.append("   ic.produto.id = :pIdProduto ");
            hql.append(") ");
            hql.append("ORDER BY me.data DESC ");
            Object[][] params = new Object[][] { { "pData", data }, { "pIdProduto", produto.getId() } };
            MovimentoEstoque me = (MovimentoEstoque)executeFind(hql.toString(), params);
            if (me != null)
                result.add(me);
        }

        return result;
    }
}
