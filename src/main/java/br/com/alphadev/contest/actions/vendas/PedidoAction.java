
package br.com.alphadev.contest.actions.vendas;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.IntegerRule;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.ConsequenceType;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.entity.Cliente;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.entity.ItemVenda;
import br.com.alphadev.contest.entity.PrazoPagamento;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.entity.Receber;
import br.com.alphadev.contest.entity.Venda;
import br.com.alphadev.contest.entity.valueObject.FormaPagamento;
import br.com.alphadev.contest.entity.valueObject.TipoCompraVenda;
import br.com.alphadev.contest.services.VendaService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.exception.ServiceValidationException;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 29/08/2007 - 20:59:52
 */
@ActionClass(prefix = "/vendas/pedido")
public class PedidoAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(PedidoAction.class, null);
    private VendaService vendaService = null;
    private UsuarioLogadoWrapper userLogin = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            vendaService = (VendaService)ServicesFactory.getInstance(VendaService.class, new Object[] { userLogin });
            log = new Log4jWrapper(PedidoAction.class, userLogin);
            this.userLogin = userLogin;
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(PedidoAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_VENDAS) != -1)
            return true;

        if (!"excluir".equals(innerAction) && groups.indexOf(KGlobal.CATEGORIA_VENDEDOR) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_nome_cliente", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Cliente"));
            validator.add("txt_id_cliente", new IntegerRule(), ConfigHelper.getProperty("error.integer", "Código do Cliente"));
            if (!userLogin.getCategoria().equals(KGlobal.CATEGORIA_VENDEDOR)) {
                validator.add("txt_nome_funcionario", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Vendedor"));
                validator.add("txt_id_funcionario", new IntegerRule(), ConfigHelper.getProperty("error.integer", "Código do Vendedor"));
                validator.add("rad_tipo_venda", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Tipo de Venda"));
            }
        }
        else if (innerAction.equals("incluirItem")) {
            validator.add("txt_descricao_produto", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Produto"));
            validator.add("txt_quantidade", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Quantidade"));
            if (input.getString("cbo_lista_preco_produto") == null)
                validator.add("txt_preco_produto", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Preço"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/pedido/pesquisar.jsp"))
    public String pesquisar() {
        try {
            List<Venda> listaVendasNaoEmitidas = vendaService.pesquisarVendasNaoEmitidas();
            output.setValue("listaPedidos", listaVendasNaoEmitidas);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(result = SUCCESS, page = "org.mentawai.ajax.renderer.JsonRenderer", type = ConsequenceType.AJAXCONSEQUENCE))
    public String verAvisos() {
        try {
            Venda venda = new Venda(input.getLong("hid_id"));
            output.setValue("avisos", vendaService.verificarAvisosVenda(venda));
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/pedido/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("hid_id");
            Venda venda = null;
            if (id > 0)
                venda = new Venda(id);
            if (venda == null) {
                venda = new Venda();
                venda.setDataLancamento(new Date());
                venda.setItens(new ArrayList<ItemVenda>());
                venda.setRecebimentos(new ArrayList<Receber>());
            }

            session.setAttribute("venda", venda);
            output.setValue("foco", "txt_id_cliente");
            session.setAttribute("produtoVenda", true);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/pedido/cadastro.jsp"))
    public String incluirItem() {
        try {
            Produto produto = new Produto(input.getLong("txt_id_produto"));
            if (produto.getId() == null)
                return ERROR;

            Venda venda = (Venda)session.getAttribute("venda");
            if (venda.getCliente() == null)
                venda.setCliente(new Cliente(input.getLong("txt_id_cliente")));

            // Verifica se item já foi incluído
            Iterator<ItemVenda> it = venda.getItens().iterator();
            while (it.hasNext()) {
                ItemVenda itemAtual = it.next();
                if (itemAtual.getProduto().getId().intValue() == produto.getId().intValue()) {
                    addError("txt_descricao_produto", ConfigHelper.getProperty("error.found", "Produto"));
                    return SUCCESS;
                }
            }

            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(input.getInt("txt_quantidade"));

            // Combo
            if (input.getString("cbo_lista_preco_produto") != null) {
                if (!input.getString("cbo_lista_preco_produto").equals("")) {
                    StringTokenizer stk = new StringTokenizer(input.getString("cbo_lista_preco_produto"), "|");
                    item.setValorVenda(new Double(stk.nextToken()));
                    item.setComissao(new Double(stk.nextToken()));
                }
                else {
                    item.setValorVenda(0d);
                    item.setComissao(0d);
                }
            }
            else {
                item.setValorVenda(new Double(input.getString("txt_preco_produto").replace(".", "").replace(",", ".")));
                // Vendedor sem opção do combo
                if (userLogin.getCategoria().equals(KGlobal.CATEGORIA_VENDEDOR) && userLogin.getFuncionario().getComissao().doubleValue() > 0d)
                    item.setComissao(userLogin.getFuncionario().getComissao());
                else if (input.getString("txt_comissao_produto") != null && !input.getString("txt_comissao_produto").isEmpty())
                    item.setComissao(new Double(input.getString("txt_comissao_produto").replace(".", "").replace(",", ".")));
            }
            item.setVenda(venda);

            venda.getItens().add(item);

            session.setAttribute("venda", venda);
            output.setValue("txt_referencia_produto", "");
            output.setValue("txt_descricao_produto", "");
            output.setValue("txt_quantidade", "");
            output.setValue("txt_preco_produto", "");
            output.setValue("foco", "txt_referencia_produto");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/pedido/cadastro.jsp"))
    public String excluirItem() {
        try {
            int indice = input.getInt("txt_indice");
            Venda venda = (Venda)session.getAttribute("venda");
            if (indice > 0 && indice <= venda.getItens().size())
                ((List<ItemVenda>)venda.getItens()).remove(indice - 1);
            session.setAttribute("venda", venda);
            output.setValue("foco", "txt_referencia_produto");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/vendas/pedido/cadastro.jsp") })
    public String gravar() {
        try {
            Venda venda = (Venda)session.getAttribute("venda");
            Cliente cliente = new Cliente(input.getLong("txt_id_cliente"));
            PrazoPagamento prazoPagamento = new PrazoPagamento(input.getLong("cbo_prazo_pagamento"));

            venda.setId(input.getLong("hid_id"));
            venda.setDesconto(input.getInt("txt_desconto") > 0 ? input.getInt("txt_desconto") : null);
            venda.setCliente(cliente);
            venda.setPrazoPagamento(prazoPagamento.getId() != null ? prazoPagamento : null);
            venda.setFormaPagamento(!input.getString("cbo_forma_pagamento").equals("") ? FormaPagamento.valueOf(input
                            .getString("cbo_forma_pagamento")) : null);
            venda.setPedido(input.getLong("txt_pedido") > 0l ? input.getLong("txt_pedido") : null);
            venda.setObservacao(input.getString("txt_observacao"));

            if (userLogin.getCategoria().equals(KGlobal.CATEGORIA_VENDEDOR)) {
                // Perfil vendedor
                venda.setFuncionario(userLogin.getFuncionario());
                venda.setTipo(TipoCompraVenda.EMISSAO);
            }
            else {
                // Outros perfis
                venda.setFuncionario(new Funcionario(input.getLong("txt_id_funcionario")));
                venda.setTipo(TipoCompraVenda.valueOf(input.getString("rad_tipo_venda")));

                // Atualizar, se for o caso, os preços e comissão dos produtos, digitados no grid
                for (ItemVenda item : venda.getItens()) {
                    String valorVendaAtualizado = input.getString("txt_preco_produto_".concat(item.getProduto().getId().toString()));
                    if (valorVendaAtualizado != null && !valorVendaAtualizado.isEmpty())
                        item.setValorVenda(new Double(valorVendaAtualizado.replace(".", "").replace(",", ".")));
                    String comissaoAtualizado = input.getString("txt_comissao_produto_".concat(item.getProduto().getId().toString()));
                    if (comissaoAtualizado != null && !comissaoAtualizado.isEmpty())
                        item.setComissao(new Double(comissaoAtualizado.replace(".", "").replace(",", ".")));
                }
            }

            vendaService.gravarPedidoVenda(venda);

            output.setValue("titulo", "Lançamento de Vendas");
            output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Venda"));
            output.setValue("url", input.getProperty("contextPath") + "/comum.home.action");
        }
        catch (ServiceValidationException vex) {
            addError(vex.getMessage());
            return ERROR;
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum.home.action", type = ConsequenceType.REDIRECT),
                    @ConsequenceOutput(result = ERROR, page = "/vendas/pedido/pesquisar.jsp") })
    public String excluir() {
        try {
            Venda venda = new Venda(input.getLong("hid_id"));
            vendaService.excluirPedidoVenda(venda);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/comum/mensagem.jsp"))
    public String mensagemOK() {
        output.setValue("titulo", "Cadastro de Pedidos");
        output.setValue("mensagem", "Clique no botão abaixo para voltar");
        output.setValue("url", input.getProperty("contextPath") + "/comum.home.action");
        return SUCCESS;
    }

    //    @Consequences(outputs = @ConsequenceOutput(result = SUCCESS, page = "application/pdf", type = ConsequenceType.STREAMCONSEQUENCE))
    //    public String gerarBoleto() {
    //        JBoletoBean jBoletoBean = new JBoletoBean();
    //
    //        jBoletoBean.setDataDocumento("14/04/2011");
    //        jBoletoBean.setDataProcessamento("14/04/2011");
    //        jBoletoBean.setNoDocumento("123456");
    //        jBoletoBean.setEspecieDocumento("DM");
    //        jBoletoBean.setAceite("N");
    //
    //        jBoletoBean.setCedente("C.P. Bauru Distribuidora de Embalagens Ltda");
    //        jBoletoBean.setCodCliente("176230300000");
    //        jBoletoBean.setNomeSacado("André Luiz Penteado de Almeida");
    //        jBoletoBean.setEnderecoSacado("Rua João Esteves de Souza, 2-28");
    //        jBoletoBean.setBairroSacado("Jardim Carolina");
    //        jBoletoBean.setCidadeSacado("Bauru");
    //        jBoletoBean.setUfSacado("SP");
    //        jBoletoBean.setCepSacado("17032-580");
    //        jBoletoBean.setCpfSacado("291.897.748-92");
    //
    //        jBoletoBean.setLocalPagamento("ATE O VENCIMENTO, PREFERENCIALMENTE NO SANTANDER");
    //        jBoletoBean.setLocalPagamento2("APOS O VENCIMENTO, SOMENTE NO SANTANDER");
    //
    //        Vector<String> descricoes = new Vector<String>();
    //        descricoes.add("Hospedagem I - teste descricao1 - R$ 39,90");
    //        descricoes.add("Manutencao - teste ricao2 - R$ 32,90");
    //        descricoes.add("Sistema - teste ssssde descricao3 - R$ 45,90");
    //        descricoes.add("Extra - teste de descricao4 - R$ 78,90");
    //        jBoletoBean.setDescricoes(descricoes);
    //
    //        jBoletoBean.setDataVencimento("14/05/2011");
    //        jBoletoBean.setInstrucao1("Cobrar Mora Diária de R$ 0,10");
    //        jBoletoBean.setInstrucao2("Protestar após 5 dias úteis");
    //        jBoletoBean.setInstrucao3("");
    //        jBoletoBean.setInstrucao4("");
    //
    //        jBoletoBean.setCarteira("101");
    //        jBoletoBean.setIOS("0");
    //        jBoletoBean.setAgencia("0004");
    //        jBoletoBean.setDvAgencia("3");
    //
    //        jBoletoBean.setContaCorrente("13006877");
    //        jBoletoBean.setDvContaCorrente("9");
    //
    //        jBoletoBean.setNossoNumero("123456", 12);
    //        jBoletoBean.setValorBoleto("5.00");
    //
    //        Generator generator = new PDFGenerator(jBoletoBean, JBoleto.SANTANDER);
    //
    //        JBoleto jBoleto = new JBoleto(generator, jBoletoBean, JBoleto.SANTANDER);
    //
    //        jBoleto.addBoleto();
    //
    //        byte[] buffer = new byte[4096];
    //        ByteArrayOutputStream ous = new ByteArrayOutputStream();
    //        try {
    //            File file = File.createTempFile("boleto", "pdf");
    //            jBoleto.closeBoleto(file.getAbsolutePath());
    //            InputStream ios = new FileInputStream(file);
    //            int read = 0;
    //            while ((read = ios.read(buffer)) != -1)
    //                ous.write(buffer, 0, read);
    //            ios.close();
    //        }
    //        catch (Exception ex) {
    //            ex.printStackTrace();
    //        }
    //        byte[] saida = ous.toByteArray();
    //
    //        output.setValue(StreamConsequence.CONTENT_DISPOSITION, "attachment; filename=\"boleto.pdf" + "\"");
    //        output.setValue(StreamConsequence.STREAM, saida);
    //        output.setValue(StreamConsequence.CONTENT_LENGTH, saida.length);
    //
    //        return SUCCESS;
    //    }
}