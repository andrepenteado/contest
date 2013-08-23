
package br.com.alphadev.contest.services;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import br.com.alphadev.contest.KGlobal.TipoPesquisaFornecedor;
import br.com.alphadev.contest.entity.CFOP;
import br.com.alphadev.contest.entity.Cliente;
import br.com.alphadev.contest.entity.Compra;
import br.com.alphadev.contest.entity.Configuracao;
import br.com.alphadev.contest.entity.Empresa;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.ImpostoCompra;
import br.com.alphadev.contest.entity.ItemCompra;
import br.com.alphadev.contest.entity.ItemVenda;
import br.com.alphadev.contest.entity.NotaFiscal;
import br.com.alphadev.contest.entity.Pagar;
import br.com.alphadev.contest.entity.PrazoPagamento;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.entity.Receber;
import br.com.alphadev.contest.entity.Venda;
import br.com.alphadev.contest.entity.valueObject.Frete;
import br.com.alphadev.contest.entity.valueObject.Imposto;
import br.com.alphadev.contest.entity.valueObject.TipoCompraVenda;
import br.com.alphadev.contest.entity.valueObject.TipoMovimentacao;
import br.com.alphadev.contest.entity.valueObject.TipoProduto;
import br.com.alphadev.contest.entity.valueObject.VistaPrazo;
import br.com.alphadev.core.BasicService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.exception.ServiceValidationException;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.FunctionsHelper;

/**
 * @author Rafael Abe
 * @since 24/03/2011 - 00:00:00
 */
@Stateless
public class NfeService extends BasicService {

    private EstoqueService estoqueService;
    private CadastroService cadastroService;
    private CompraService compraService;

    @Override
    public void setParameters(Object[] params) {
        super.setParameters(params);
        this.estoqueService = (EstoqueService)ServicesFactory.getInstance(EstoqueService.class, params);
        this.cadastroService = (CadastroService)ServicesFactory.getInstance(CadastroService.class, params);
        this.compraService = (CompraService)ServicesFactory.getInstance(CompraService.class, params);
    }

    /**
     * Lê o arquivo xml e retorn um bean compra preenchido
     * 
     * @param File file - arquivo xml
     * @param Namespace namespace - namespace do arquivo xml
     * Namespace namespace = Namespace.getNamespace("http://www.portalfiscal.inf.br/nfe");
     * @return Compra - bean Compra preenchido com os dados lidos do arquivo xml
     */
    public Compra converterXml(byte[] xml, Namespace namespace) throws ServiceValidationException, Exception {
        List<String> erros = new ArrayList<String>();

        // Criar XML
        File file = File.createTempFile("nfe", "xml");
        FileOutputStream out = new FileOutputStream(file);
        out.write(xml);
        out.flush();
        out.close();

        SAXBuilder sb = new SAXBuilder();
        Document d = null;
        try {
            d = sb.build(file);
        }
        catch (Exception e) {
            log.error(ConfigHelper.get().getString("error.leituraArquivo"), e);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.leituraArquivo"));
        }

        //ELEMENTOS BASE
        Element raizXml = d.getRootElement();
        Element elementInfNFe = raizXml.getChild("NFe", namespace).getChild("infNFe", namespace);
        Element elementIde = elementInfNFe.getChild("ide", namespace);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //Cria Compra	    	   
        Compra compra = new Compra();
        compra.setNotaFiscal(Long.parseLong(elementIde.getChild("nNF", namespace).getValue()));

        Date dataEmissao = null;

        try {
            dataEmissao = sdf.parse(elementIde.getChild("dEmi", namespace).getValue());
        }
        catch (Exception e) {
            log.error(ConfigHelper.getProperty("error.date", "Data de Emissão"), e);
            erros.add(ConfigHelper.getProperty("error.date", "Data de Emissão"));
        }
        compra.setEmissao(dataEmissao);
        if (elementInfNFe.getChild("transp", namespace).getChild("transporta", namespace) != null)
            compra.setTransportador(elementInfNFe.getChild("transp", namespace).getChild("transporta", namespace).getChild("xNome", namespace)
                            .getValue());

        //Frete
        String modFrete = elementInfNFe.getChild("transp", namespace).getChild("modFrete", namespace).getValue();
        for (Frete f : Frete.values())
            if (f.getCodigo().equals(modFrete))
                compra.setFrete(f);

        //Fornecedor
        Element elementEmit = elementInfNFe.getChild("emit", namespace);
        Fornecedor fornecedor = null;

        String cnpj = elementEmit.getChild("CNPJ", namespace).getValue();
        cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-"
                        + cnpj.substring(12);

        //Busca o fornecedor pelo CNPJ
        List<Fornecedor> fornecedores = (ArrayList<Fornecedor>)cadastroService.pesquisarFornecedor(null, null, null, null, cnpj,
                        TipoPesquisaFornecedor.CNPJ);
        if (fornecedores != null && fornecedores.size() > 0)
            fornecedor = fornecedores.get(0);
        else
            erros.add(ConfigHelper.getProperty("error.notFound", "Fornecedor de CNPJ " + cnpj));

        compra.setFornecedor(fornecedor);

        // Verifica se NF já importada
        if (compraService.buscarNotaFiscalCompraPorFornecedor(compra.getNotaFiscal(), fornecedor) != null)
            erros.add(ConfigHelper.get().getString("error.notaFiscalImportada"));

        //Empresa
        Empresa empresa = userLogin.getFuncionario().getEmpresa();
        compra.setEmpresa(empresa);

        //Itens
        List<ItemCompra> itensComprados = new ArrayList<ItemCompra>();

        List<Element> valoresDet = elementInfNFe.getChildren("det", namespace);
        ItemCompra item = null;
        for (Element itemDet : valoresDet) {
            Element prod = itemDet.getChild("prod", namespace);

            item = new ItemCompra();
            Produto produto = null;
            //Busca o produto pelo código externo
            String codigoExterno = prod.getChild("cProd", namespace).getValue();
            produto = cadastroService.buscarProdutoPorCodigoExterno(codigoExterno);

            if (produto == null) {
                erros.add(ConfigHelper.getProperty("error.notFound", "Produto de código externo " + codigoExterno));
                continue;
            }

            item.setProduto(produto);
            item.setQuantidade(Integer.parseInt(prod.getChild("qCom", namespace).getValue().split("[.]")[0]));
            item.setValorCompra(Double.parseDouble(prod.getChild("vUnCom", namespace).getValue().replace("[.]", ",")));

            //CFOP
            String stringCFOP = prod.getChild("CFOP", namespace).getValue();
            if (stringCFOP != null && !"".equals(stringCFOP)) {
                CFOP cfop = cadastroService.buscarCFOPPorCodigo(stringCFOP);

                if (cfop != null)
                    item.setCfop(cfop);
                else if (!erros.contains(ConfigHelper.getProperty("error.notFound", "CFOP " + stringCFOP)))
                    erros.add(ConfigHelper.getProperty("error.notFound", "CFOP " + stringCFOP));

            }

            //Busca os impostos
            List<ImpostoCompra> impostos = new ArrayList<ImpostoCompra>();
            ImpostoCompra imposto = new ImpostoCompra();
            Element itemImposto = itemDet.getChild("imposto", namespace);

            //ICMS
            Element icmsElement = (Element)itemImposto.getChild("ICMS", namespace).getChildren().get(0);
            if (icmsElement != null) {
                Element icmsElement2 = icmsElement.getChild("pICMS", namespace);
                if (icmsElement2 != null) {
                    String aliquotaICMS = icmsElement2.getValue();
                    imposto = new ImpostoCompra();
                    imposto.setAliquota(Double.parseDouble(aliquotaICMS.replace("[.]", ",")));
                    imposto.setImposto(Imposto.ICMS);
                    impostos.add(imposto);
                }
            }

            // ICMS ST
            Element icmsStElement = icmsElement.getChild("pMVAST", namespace);
            if (icmsStElement != null) {
                String aliquotaICMSST = icmsStElement.getValue();
                imposto = new ImpostoCompra();
                imposto.setAliquota(Double.parseDouble(aliquotaICMSST.replace("[.]", ",")));
                imposto.setImposto(Imposto.SUBSTITUICAO_TRIBUTARIA_ICMS);
                impostos.add(imposto);
            }

            //IPI
            Element ipiElement = (Element)itemImposto.getChild("IPI", namespace);
            if (ipiElement != null) {
                Element ipiElement2 = ipiElement.getChild("IPITrib", namespace);
                if (ipiElement2 != null) {
                    Element ipiElement3 = ipiElement2.getChild("pIPI", namespace);
                    if (ipiElement3 != null) {
                        String aliquotaIPI = ipiElement3.getValue();
                        imposto = new ImpostoCompra();
                        imposto.setAliquota(Double.parseDouble(aliquotaIPI.replace("[.]", ",")));
                        imposto.setImposto(Imposto.IPI);
                        impostos.add(imposto);
                    }
                }
            }

            //PIS
            Element itemPISAliq = itemImposto.getChild("PIS", namespace);
            if (itemPISAliq != null) {
                Element pisElement = itemPISAliq.getChild("PISAliq", namespace);
                if (pisElement != null) {
                    Element pisElement2 = pisElement.getChild("pPIS", namespace);
                    if (pisElement2 != null) {
                        String aliquotaPIS = pisElement2.getValue();
                        imposto = new ImpostoCompra();
                        imposto.setAliquota(Double.parseDouble(aliquotaPIS.replace("[.]", ",")));
                        imposto.setImposto(Imposto.PIS);
                        impostos.add(imposto);
                    }
                }
            }

            //COFINS
            Element itemCOFINSAliq = itemImposto.getChild("COFINS", namespace);
            if (itemCOFINSAliq != null) {
                Element cofinsElement = itemCOFINSAliq.getChild("COFINSAliq", namespace);
                if (cofinsElement != null) {
                    Element cofinsElement2 = cofinsElement.getChild("pCOFINS", namespace);
                    if (cofinsElement2 != null) {
                        String aliquotaCOFINS = cofinsElement2.getValue();
                        imposto = new ImpostoCompra();
                        imposto.setAliquota(Double.parseDouble(aliquotaCOFINS.replace("[.]", ",")));
                        imposto.setImposto(Imposto.COFINS);
                        impostos.add(imposto);
                    }
                }
            }

            //Adiciona os impostos ao item
            item.setImpostos(impostos);

            itensComprados.add(item);
        }
        compra.setItens(itensComprados);

        //Pagamentos
        List<Pagar> pagamentos = new ArrayList<Pagar>();

        List<Element> valoresDup = elementInfNFe.getChild("cobr", namespace).getChildren("dup", namespace);
        Pagar pagamento = null;

        for (Element itemDup : valoresDup) {
            pagamento = new Pagar();
            pagamento.setDescricao(itemDup.getChild("nDup", namespace).getValue());
            try {
                pagamento.setVencimento(sdf.parse(itemDup.getChild("dVenc", namespace).getValue()));
            }
            catch (Exception e) {
                log.error(ConfigHelper.getProperty("error.date", "Data de Vencimento"), e);
                erros.add(ConfigHelper.getProperty("error.date", "Data de Vencimento"));
            }
            pagamento.setValor(Double.parseDouble(itemDup.getChild("vDup", namespace).getValue()));

            pagamentos.add(pagamento);
        }

        //Ordena os pagamentos por ordem crescente de vencimento para setar a parcela
        Comparator<Pagar> comparator = new Comparator<Pagar>() {
            @Override
            public int compare(Pagar o1, Pagar o2) {
                return o1.getVencimento().compareTo(o2.getVencimento());
            }
        };
        Collections.sort(pagamentos, comparator);

        String parcelasPagamento = "";
        int parcela = 1;
        for (Pagar pagar : pagamentos) {
            double dias = ((double)pagar.getVencimento().getTime() - (double)dataEmissao.getTime()) / (1000d * 60d * 60d * 24d);
            parcelasPagamento += new BigDecimal(dias).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + ",";
            pagar.setParcela(parcela++);
        }

        compra.setPagamentos(pagamentos);

        //Prazo Pagamento 
        parcelasPagamento = parcelasPagamento.substring(0, parcelasPagamento.length() - 1);
        PrazoPagamento prazoPagamento = cadastroService.buscarPrazoPagamentoPorParcelas(parcelasPagamento);
        if (prazoPagamento == null)
            erros.add(ConfigHelper.getProperty("error.notFound", "Prazo com parcelas " + parcelasPagamento));

        compra.setPrazoPagamento(prazoPagamento);

        compra.setTipo(TipoCompraVenda.EMISSAO);

        //Se tiver erros lança exceção
        if (erros.size() > 0)
            throw new ServiceValidationException(erros.toString());

        Compra compraAtualizada = null;
        try {
            beginTransaction();
            compraAtualizada = (Compra)save(compra);

            //Atualiza o estoque
            VistaPrazo vistaPrazo = compra.getNotaFiscal() == null ? VistaPrazo.VISTA : VistaPrazo.PRAZO;
            estoqueService.atualizarMovimentacaoEstoque(compraAtualizada.getItens().iterator(), vistaPrazo, TipoMovimentacao.EMISSAO, null);

            commit();
        }
        catch (Exception ex) {
            rollback();
            throw new Exception(ConfigHelper.get().getString("error.general"));
        }

        return compraAtualizada;
    }

    /*protected byte[] gerarArquivoEmissorNFe_v1(NotaFiscal nf) throws ServiceValidationException {
        StringBuilder file = new StringBuilder();
        Empresa emitente = nf.getVenda().getEmpresa();
        Cliente cliente = nf.getVenda().getCliente();
        Configuracao config = emitente.getConfiguracao();
        Venda venda = nf.getVenda();

        // Cabeçalho
        file.append("NOTAFISCAL|1\n");

        // A - Atributos NF-e
        file.append("A|");
        file.append(config.getVersaoLeiauteNfe()).append("|");
        file.append("NFe").append("\n");

        // B - Identificadores da NF-e
        file.append("B|");
        file.append(emitente.getCidade().getEstado().getCodigoIbge()).append("|");
        file.append("|");
        if (nf.getCfop() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CFOP"));
        file.append(FunctionsHelper.noAccent(nf.getCfop().getDescricao())).append("|");
        if (venda.getPrazoPagamento() == null || venda.getPrazoPagamento().getParcelas() == null
                        || venda.getPrazoPagamento().getParcelas().equals(""))
            file.append("2|");
        else if (venda.getPrazoPagamento().getParcelas().equals("0"))
            file.append("0|");
        else
            file.append("1|");
        file.append("55|");
        file.append("1|");
        if (nf.getNumero() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Número da NF"));
        file.append(nf.getNumero()).append("|");
        if (nf.getEmissao() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Data de Emissão"));
        file.append(FunctionsHelper.dateFormat(nf.getEmissao(), "yyyy-MM-dd")).append("|");
        file.append(FunctionsHelper.dateFormat(nf.getEmissao(), "yyyy-MM-dd")).append("|");
        file.append("1|");
        file.append(emitente.getCidade().getCodigoIbge()).append("|");
        file.append("1|");
        file.append("1|");
        file.append("|");
        if (config.getSituacaoNfe().intValue() == 1) // Homologação
            file.append("2|");
        if (config.getSituacaoNfe().intValue() == 2) // Produção
            file.append("1|");
        file.append("1|");
        file.append("3|");
        file.append(config.getVersaoEmissorNfe());
        file.append("\n");

        // C - Emitente
        file.append("C|");
        if (emitente.getRazaoSocial() == null || emitente.getRazaoSocial().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Razão Social do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getRazaoSocial())).append("|");
        if (emitente.getRazaoSocial() == null || emitente.getRazaoSocial().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Nome Fantasia do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getNomeFantasia())).append("|");
        if (emitente.getInscricaoEstadual() == null || emitente.getInscricaoEstadual().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Inscrição Estadual do Emitente"));
        file.append(emitente.getInscricaoEstadual().replace("-", "").replace(".", "").replace(" ", "")).append("|");
        file.append("|");
        if (emitente.getInscricaoMunicipal() == null || emitente.getInscricaoMunicipal().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Inscrição Municipal do Emitente"));
        file.append(emitente.getInscricaoMunicipal()).append("|");
        file.append(emitente.getCnae()).append("|");
        file.append("\n");
        if (emitente.getCnpj() == null || emitente.getCnpj().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CNPJ do Emitente"));
        file.append("C02|").append(emitente.getCnpj().replace("-", "").replace(".", "").replace("/", "").replace(" ", ""));
        file.append("\n");

        //C05 - Endereço
        file.append("C05|");
        if (emitente.getRua() == null || emitente.getRua().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Rua do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getRua())).append("|");
        if (emitente.getNumero() == null || emitente.getNumero().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Número da Rua do Emitente"));
        file.append(emitente.getNumero().replace("-", "")).append("|");
        file.append(FunctionsHelper.noAccent(emitente.getComplemento())).append("|");
        if (emitente.getBairro() == null || emitente.getBairro().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Bairro do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getBairro())).append("|");
        if (emitente.getCidade() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Cidade do Emitente"));
        file.append(emitente.getCidade().getCodigoIbge()).append("|");
        file.append(FunctionsHelper.noAccent(emitente.getCidade().getNome())).append("|");
        file.append(emitente.getCidade().getEstado().getSigla().trim()).append("|");
        if (emitente.getCep() == null || emitente.getCep().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CEP do Emitente"));
        file.append(emitente.getCep().replace("-", "").replace(".", "").replace(" ", "")).append("|");
        file.append("1058|");
        file.append("BRASIL|");
        if (emitente.getTelefone() == null || emitente.getTelefone().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Telefone do Emitente"));
        file.append(emitente.getTelefone().replace("-", "").replace(".", "").replace("(", "").replace(")", "").replace(" ", ""));
        file.append("\n");

        // E - Destinatário
        file.append("E|");
        if (cliente.getNome() == null || cliente.getNome().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Nome do Cliente"));
        file.append(FunctionsHelper.noAccent(cliente.getNome())).append("|");
        String cnpjOuCpf = cliente.getCnpj().replace("-", "").replace(".", "").replace("/", "").replace(" ", "");
        boolean pessoaFisica = cnpjOuCpf.length() == 11;
        if (!pessoaFisica) {
            if (cliente.getInscricaoEstadual() == null || cliente.getInscricaoEstadual().equals(""))
                throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Inscrição Estadual do Cliente"));
            file.append(cliente.getInscricaoEstadual().replace("-", "").replace(".", "").replace(" ", ""));
        }
        file.append("|");
        file.append("\n");
        if (cliente.getCnpj() == null || cliente.getCnpj().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CNPJ do Cliente"));
        if (pessoaFisica)
            file.append("E03|").append(cnpjOuCpf);
        else
            file.append("E02|").append(cnpjOuCpf);
        file.append("\n");

        //E05 - Endereço
        file.append("E05|");
        if (cliente.getRua() == null || cliente.getRua().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Rua do Cliente"));
        file.append(FunctionsHelper.noAccent(cliente.getRua())).append("|");
        if (cliente.getNumero() == null || cliente.getNumero().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Número da Rua do Cliente"));
        file.append(cliente.getNumero().replace("-", "")).append("|");
        file.append(FunctionsHelper.noAccent(cliente.getComplemento())).append("|");
        if (cliente.getBairro() == null || cliente.getBairro().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Bairro do Cliente"));
        file.append(FunctionsHelper.noAccent(cliente.getBairro())).append("|");
        if (cliente.getCidade() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Cidade do Cliente"));
        file.append(cliente.getCidade().getCodigoIbge()).append("|");
        file.append(FunctionsHelper.noAccent(cliente.getCidade().getNome())).append("|");
        file.append(cliente.getCidade().getEstado().getSigla().trim()).append("|");
        file.append(cliente.getCep().replace("-", "").replace(".", "").replace(" ", "")).append("|");
        file.append("1058|");
        file.append("BRASIL|");
        if (cliente.getTelefone() != null)
            file.append(cliente.getTelefone().replace("-", "").replace(".", "").replace("(", "").replace(")", "").replace(" ", ""));
        file.append("\n");

        int count = 1;
        int quantidadeVolumes = 0;
        for (ItemVenda item : venda.getItens()) {
            quantidadeVolumes += item.getQuantidade();

            file.append("H|").append(count).append("\n");
            file.append("I|");
            file.append(item.getProduto().getId()).append("|");
            file.append("|");
            file.append(FunctionsHelper.noAccent(item.getProduto().getDescricao())).append("|");
            if (item.getProduto().getNcm() != null)
                file.append(item.getProduto().getNcm());
            file.append("|");
            file.append("|");
            file.append("|");
            file.append(nf.getCfop().getCodigo()).append("|");
            if (item.getProduto().getUnidade() == null)
                throw new ServiceValidationException(ConfigHelper.getProperty("error.required",
                                "Unidade do produto ".concat(item.getProduto().getReferencia())));
            file.append(item.getProduto().getUnidade().getCodigo()).append("|");
            file.append(FunctionsHelper.numberFormat(item.getQuantidade(), "#0.0000").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getValorVenda(), "#0.0000").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
            file.append("|");
            file.append(item.getProduto().getUnidade().getCodigo()).append("|");
            file.append(FunctionsHelper.numberFormat(item.getQuantidade(), "#0.0000").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getValorVenda(), "#0.0000").replace(",", ".")).append("|");
            file.append("|");
            file.append("|");
            if (item.getValorDesconto().doubleValue() > 0d)
                file.append(FunctionsHelper.numberFormat(item.getValorDesconto(), "#0.00").replace(",", ".")).append("|");
            else
                file.append("|");
            file.append("\n");
            count++;

            // M - Grupo de Tributos sobre Produto/Serviço
            file.append("M\n");

            // N - ICMS
            file.append("N\n");

            if (nf.getCfop().name().equals(CFOP.VENDA_ENTREGA_FUTURA.name())
                            || nf.getCfop().name().equals(CFOP.REMESSA_ENCOMENDA_ENTREGA_FUTURA.name())) {
                file.append("N06|");
                file.append("0|");
                file.append("41|"); // Não Tributada
                file.append("||||||||||\n");
            }
            else if (nf.getCfop().name().equals(CFOP.IMPOSTO_RECOLHIDO_SUBSTITUICAO.name())) {
                file.append("N08|");
                file.append("0|");
                file.append("60|");
                file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getSubTotal() * (nf.getAliquotaIcms() / 100), "#0.00").replace(",", "."));
                file.append("|\n");
            }
            else {
                file.append("N02|");
                file.append("0|");
                file.append("00|");
                file.append("3|");
                file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(nf.getAliquotaIcms(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getSubTotal() * (nf.getAliquotaIcms() / 100), "#0.00").replace(",", "."));
                file.append("\n");
            }

            // Q - PIS
            file.append("Q\n");
            file.append("Q02|");
            file.append("01|");
            file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(nf.getAliquotaPis(), "#0.00").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getSubTotal() * (nf.getAliquotaPis() / 100), "#0.00").replace(",", "."));
            file.append("\n");

            // S - COFINS
            file.append("S\n");
            file.append("S02|");
            file.append("01|");
            file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(nf.getAliquotaCofins(), "#0.00").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getSubTotal() * (nf.getAliquotaCofins() / 100), "#0.00").replace(",", "."));
            file.append("\n");
        }

        // W - Totais
        file.append("W\n");
        file.append("W02|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotal(), "#0.00").replace(",", ".")).append("|");
        if (nf.getCfop().name().equals(CFOP.IMPOSTO_RECOLHIDO_SUBSTITUICAO.name())
                        || nf.getCfop().name().equals(CFOP.REMESSA_ENCOMENDA_ENTREGA_FUTURA.name()))
            file.append("0.00|");
        else
            file.append(FunctionsHelper.numberFormat(nf.getTotalIcms(), "#0.00").replace(",", ".")).append("|");
        file.append("0.00|");
        file.append("0.00|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotalProduto(), "#0.00").replace(",", ".")).append("|");
        file.append("0.00|");
        file.append("0.00|");
        if (venda.getValorDesconto().doubleValue() > 0d)
            file.append(FunctionsHelper.numberFormat(venda.getValorDesconto(), "#0.00").replace(",", ".")).append("|");
        else
            file.append("|");
        file.append("0.00|");
        file.append("0.00|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotal() * (nf.getAliquotaPis() / 100), "#0.00").replace(",", ".")).append("|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotal() * (nf.getAliquotaCofins() / 100), "#0.00").replace(",", ".")).append("|");
        file.append("0.00|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotal(), "#0.00").replace(",", "."));
        file.append("\n");

        // X - Transporte
        file.append("X|").append(Integer.parseInt(nf.getFrete().getCodigo()) - 1).append("\n");

        // X26 - Volumes
        file.append("X26|").append(quantidadeVolumes).append("|Volumes||||\n");

        // Y - Vencimentos
        StringBuilder vencimentos = new StringBuilder();

        file.append("Y\n");
        file.append("Y02|");
        file.append(nf.getNumero()).append("|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotalProduto(), "#0.00").replace(",", ".")).append("|");
        if (venda.getValorDesconto().doubleValue() > 0d)
            file.append(FunctionsHelper.numberFormat(venda.getValorDesconto(), "#0.00").replace(",", ".")).append("|");
        else
            file.append("|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotal(), "#0.00").replace(",", ".")).append("\n");
        for (Receber receber : venda.getRecebimentos()) {
            file.append("Y07|");
            file.append(nf.getNumero()).append("/").append(receber.getParcela()).append("|");
            file.append(FunctionsHelper.dateFormat(receber.getVencimento(), "yyyy-MM-dd")).append("|");
            file.append(FunctionsHelper.numberFormat(receber.getValor(), "#0.00").replace(",", ".")).append("\n");

            // Vencimentos formatados para exibição em observação
            vencimentos.append(FunctionsHelper.dateFormat(receber.getVencimento(), "dd/MM/yyyy")).append(" [");
            vencimentos.append(FunctionsHelper.numberFormat(receber.getValor(), "#0.00")).append("] ");
        }

        // Z - Observações
        file.append("Z||");
        if (venda.getComissao() != null)
            file.append("Vendedor: ").append(FunctionsHelper.noAccent(venda.getFuncionario().getNome())).append(" ** ");
        if (venda.getPrazoPagamento() != null)
            file.append("Prazos: ").append(FunctionsHelper.noAccent(venda.getPrazoPagamento().getDescricao())).append(" ** ");
        file.append("Vencimentos: ").append(vencimentos).append(" ** ");
        file.append("Forma Pagto: ").append(FunctionsHelper.noAccent(venda.getFormaPagamento().getDescricao())).append(" ** ");
        file.append(FunctionsHelper.noAccent(nf.getObservacao().replace("\n", " ")));

        return file.toString().getBytes();
    }*/

    protected byte[] gerarArquivoEmissorNFe_v2(NotaFiscal nf) throws ServiceValidationException {
        StringBuilder file = new StringBuilder();
        Empresa emitente = nf.getVenda().getEmpresa();
        Cliente cliente = nf.getVenda().getCliente();
        Configuracao config = emitente.getConfiguracao();
        Venda venda = nf.getVenda();

        // Cabeçalho
        file.append("NOTAFISCAL|1\n");

        // A - Atributos NF-e
        file.append("A|");
        file.append(config.getVersaoLeiauteNfe()).append("|");
        file.append("NFe").append("\n");

        // B - Identificadores da NF-e
        file.append("B|");
        file.append(emitente.getCidade().getEstado().getCodigoIbge()).append("|");
        file.append("|");
        if (nf.getVenda().getTipo() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Operação"));
        file.append(FunctionsHelper.noAccent(nf.getVenda().getTipo().getNaturezaOperacao())).append("|");
        if (venda.getPrazoPagamento() == null || venda.getPrazoPagamento().getParcelas() == null
                        || venda.getPrazoPagamento().getParcelas().equals(""))
            file.append("2|");
        else if (venda.getPrazoPagamento().getParcelas().equals("0"))
            file.append("0|");
        else
            file.append("1|");
        file.append("55|");
        file.append("1|");
        if (nf.getNumero() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Número da NF"));
        file.append(nf.getNumero()).append("|");
        if (nf.getEmissao() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Data de Emissão"));
        file.append(FunctionsHelper.dateFormat(nf.getEmissao(), "yyyy-MM-dd")).append("|");
        file.append(FunctionsHelper.dateFormat(nf.getEmissao(), "yyyy-MM-dd")).append("|");
        file.append(FunctionsHelper.dateFormat(nf.getEmissao(), "HH:mm:ss")).append("|");
        file.append("1|");
        file.append(emitente.getCidade().getCodigoIbge()).append("|");
        file.append("1|");
        file.append("1|");
        file.append("|");
        if (config.getSituacaoNfe().intValue() == 1) // Homologação
            file.append("2|");
        if (config.getSituacaoNfe().intValue() == 2) // Produção
            file.append("1|");
        file.append("1|");
        file.append("3|");
        file.append(config.getVersaoEmissorNfe());
        file.append("|");
        file.append("|");
        file.append("|");
        file.append("\n");

        // C - Emitente
        file.append("C|");
        if (emitente.getRazaoSocial() == null || emitente.getRazaoSocial().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Razão Social do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getRazaoSocial())).append("|");
        if (emitente.getRazaoSocial() == null || emitente.getRazaoSocial().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Nome Fantasia do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getNomeFantasia())).append("|");
        if (emitente.getInscricaoEstadual() == null || emitente.getInscricaoEstadual().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Inscrição Estadual do Emitente"));
        file.append(emitente.getInscricaoEstadual().replace("-", "").replace(".", "").replace(" ", "")).append("|");
        file.append("|");
        if (emitente.getInscricaoMunicipal() == null || emitente.getInscricaoMunicipal().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Inscrição Municipal do Emitente"));
        file.append(emitente.getInscricaoMunicipal()).append("|");
        file.append(emitente.getCnae()).append("|");
        file.append("3|"); // Regime tributário: 1-Simples 2-Simples (excesso receita) 3-Normal
        file.append("\n");
        if (emitente.getCnpj() == null || emitente.getCnpj().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CNPJ do Emitente"));
        file.append("C02|").append(emitente.getCnpj().replace("-", "").replace(".", "").replace("/", "").replace(" ", ""));
        file.append("\n");

        //C05 - Endereço
        file.append("C05|");
        if (emitente.getRua() == null || emitente.getRua().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Rua do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getRua())).append("|");
        if (emitente.getNumero() == null || emitente.getNumero().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Número da Rua do Emitente"));
        file.append(emitente.getNumero().replace("-", "")).append("|");
        file.append(FunctionsHelper.noAccent(emitente.getComplemento())).append("|");
        if (emitente.getBairro() == null || emitente.getBairro().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Bairro do Emitente"));
        file.append(FunctionsHelper.noAccent(emitente.getBairro())).append("|");
        if (emitente.getCidade() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Cidade do Emitente"));
        file.append(emitente.getCidade().getCodigoIbge()).append("|");
        file.append(FunctionsHelper.noAccent(emitente.getCidade().getNome())).append("|");
        file.append(emitente.getCidade().getEstado().getSigla().trim()).append("|");
        if (emitente.getCep() == null || emitente.getCep().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CEP do Emitente"));
        file.append(emitente.getCep().replace("-", "").replace(".", "").replace(" ", "")).append("|");
        file.append("1058|");
        file.append("BRASIL|");
        if (emitente.getTelefone() == null || emitente.getTelefone().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Telefone do Emitente"));
        file.append(emitente.getTelefone().replace("-", "").replace(".", "").replace("(", "").replace(")", "").replace(" ", ""));
        file.append("|");
        file.append("\n");

        // E - Destinatário
        file.append("E|");
        if (cliente.getNome() == null || cliente.getNome().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Nome do Cliente"));
        file.append(FunctionsHelper.noAccent(cliente.getNome())).append("|");
        String cnpjOuCpf = cliente.getCnpj().replace("-", "").replace(".", "").replace("/", "").replace(" ", "");
        boolean pessoaFisica = cnpjOuCpf.length() == 11;
        if (!pessoaFisica) {
            if (cliente.getInscricaoEstadual() == null || cliente.getInscricaoEstadual().equals(""))
                throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Inscrição Estadual do Cliente"));
            file.append(cliente.getInscricaoEstadual().replace("-", "").replace(".", "").replace(" ", ""));
        }
        file.append("|||");
        file.append("\n");
        if (cliente.getCnpj() == null || cliente.getCnpj().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CNPJ do Cliente"));
        if (pessoaFisica)
            file.append("E03|").append(cnpjOuCpf);
        else
            file.append("E02|").append(cnpjOuCpf);
        file.append("|");
        file.append("\n");

        //E05 - Endereço
        file.append("E05|");
        if (cliente.getRua() == null || cliente.getRua().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Rua do Cliente"));
        file.append(FunctionsHelper.noAccent(cliente.getRua())).append("|");
        if (cliente.getNumero() == null || cliente.getNumero().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Número da Rua do Cliente"));
        file.append(cliente.getNumero().replace("-", "")).append("|");
        file.append(FunctionsHelper.noAccent(cliente.getComplemento())).append("|");
        if (cliente.getBairro() == null || cliente.getBairro().equals(""))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Bairro do Cliente"));
        file.append(FunctionsHelper.noAccent(cliente.getBairro())).append("|");
        if (cliente.getCidade() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Cidade do Cliente"));
        file.append(cliente.getCidade().getCodigoIbge()).append("|");
        file.append(FunctionsHelper.noAccent(cliente.getCidade().getNome())).append("|");
        file.append(cliente.getCidade().getEstado().getSigla().trim()).append("|");
        file.append(cliente.getCep().replace("-", "").replace(".", "").replace(" ", "")).append("|");
        file.append("1058|");
        file.append("BRASIL|");
        if (cliente.getTelefone() != null)
            file.append(cliente.getTelefone().replace("-", "").replace(".", "").replace("(", "").replace(")", "").replace(" ", ""));
        file.append("\n");

        int count = 1;
        int quantidadeVolumes = 0;
        for (ItemVenda item : venda.getItens()) {
            quantidadeVolumes += item.getQuantidade();

            file.append("H|").append(count).append("||").append("\n");
            file.append("I|");
            file.append(item.getProduto().getId()).append("|");
            file.append("|");
            file.append(FunctionsHelper.noAccent(item.getProduto().getDescricao())).append("|");
            if (item.getProduto().getNcm() == null)
                throw new ServiceValidationException(ConfigHelper.getProperty("error.required",
                                "NCM do produto ".concat(item.getProduto().getReferencia())));
            file.append(item.getProduto().getNcm()).append("|");
            file.append("|");
            file.append(item.getCfop().getCodigo()).append("|");
            if (item.getProduto().getUnidade() == null)
                throw new ServiceValidationException(ConfigHelper.getProperty("error.required",
                                "Unidade do produto ".concat(item.getProduto().getReferencia())));
            file.append(item.getProduto().getUnidade().getCodigo()).append("|");
            file.append(FunctionsHelper.numberFormat(item.getQuantidade(), "#0.0000").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getValorVenda(), "#0.0000000000").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getSubTotalSemDesconto(), "#0.00").replace(",", ".")).append("|");
            file.append("|");
            file.append(item.getProduto().getUnidade().getCodigo()).append("|");
            file.append(FunctionsHelper.numberFormat(item.getQuantidade(), "#0.0000").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(item.getValorVenda(), "#0.0000000000").replace(",", ".")).append("|");
            file.append("|");
            file.append("|");
            if (item.getValorDesconto().doubleValue() > 0d)
                file.append(FunctionsHelper.numberFormat(item.getValorDesconto(), "#0.00").replace(",", ".")).append("|");
            else
                file.append("|");
            file.append("|");
            file.append("1|");
            file.append("|");
            file.append("|");
            file.append("\n");
            count++;

            // M - Grupo de Tributos sobre Produto/Serviço
            file.append("M\n");

            if (TipoProduto.SERVICO.name().equals(item.getProduto().getTipo().name())) {
                // U - ISS
                file.append("U|");
                file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getAliquotaIss(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getTotalIss(), "#0.00").replace(",", ".")).append("|");
                file.append(emitente.getCidade().getCodigoIbge()).append("|");
                file.append("1010|N|\n");

                // Q - PIS [Isento]
                file.append("Q|\n");
                file.append("Q04|06|\n");

                // S - COFINS [Isento]
                file.append("S|\n");
                file.append("S04|06|\n");
            }
            else {
                // N - ICMS
                file.append("N\n");

                // Venda ou Remessa Futura
                if (item.getVenda().getTipo().equals(TipoCompraVenda.ENTREGA_FUTURA)
                                || item.getVenda().getTipo().equals(TipoCompraVenda.REMESSA_ENTREGA_FUTURA)) {
                    file.append("N06|");
                    file.append("0|");
                    file.append("41|"); // Não Tributada
                    file.append("||||||||||\n");
                }
                // Substituição Tributária
                else if (item.isSubstituicaoTributariaIcms()) {
                    file.append("N08|");
                    file.append("0|");
                    file.append("60|");
                    file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
                    file.append(FunctionsHelper.numberFormat(item.getTotalIcms(), "#0.00").replace(",", "."));
                    file.append("||\n");
                }
                else {
                    file.append("N02|");
                    file.append("0|");
                    file.append("00|");
                    file.append("3|");
                    file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
                    file.append(FunctionsHelper.numberFormat(item.getAliquotaIcms(), "#0.00").replace(",", ".")).append("|");
                    file.append(FunctionsHelper.numberFormat(item.getTotalIcms(), "#0.00").replace(",", "."));
                    file.append("|");
                    file.append("\n");
                }

                // Q - PIS
                file.append("Q\n");
                file.append("Q02|");
                file.append("01|");
                file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getAliquotaPis(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getTotalPis(), "#0.00").replace(",", "."));
                file.append("|");
                file.append("\n");

                // S - COFINS
                file.append("S\n");
                file.append("S02|");
                file.append("01|");
                file.append(FunctionsHelper.numberFormat(item.getSubTotal(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getAliquotaCofins(), "#0.00").replace(",", ".")).append("|");
                file.append(FunctionsHelper.numberFormat(item.getTotalCofins(), "#0.00").replace(",", "."));
                file.append("|");
                file.append("\n");
            }
        }

        // W - Totais
        file.append("W\n");
        file.append("W02|");
        file.append(FunctionsHelper.numberFormat(nf.getBaseCalculoIcms(), "#0.00").replace(",", ".")).append("|");
        file.append(FunctionsHelper.numberFormat(nf.getTotalIcms(), "#0.00").replace(",", ".")).append("|");
        file.append("0.00|");
        file.append("0.00|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotalProduto(), "#0.00").replace(",", ".")).append("|");
        file.append("0.00|");
        file.append("0.00|");
        if (venda.getValorDesconto().doubleValue() > 0d)
            file.append(FunctionsHelper.numberFormat(venda.getValorDesconto(), "#0.00").replace(",", ".")).append("|");
        else
            file.append("0.00|");
        file.append("0.00|");
        file.append("0.00|");
        file.append(FunctionsHelper.numberFormat(nf.getTotalPis(), "#0.00").replace(",", ".")).append("|");
        file.append(FunctionsHelper.numberFormat(nf.getTotalCofins(), "#0.00").replace(",", ".")).append("|");
        file.append("0.00|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotalProduto(), "#0.00").replace(",", "."));
        file.append("|");
        file.append("\n");

        // W17 - ISS
        if (venda.getValorTotalServicos() > 0) {
            file.append("W17||");
            file.append(FunctionsHelper.numberFormat(venda.getValorTotalServicos(), "#0.00").replace(",", ".")).append("|");
            file.append(FunctionsHelper.numberFormat(venda.getValorTotalServicos(), "#0.00").replace(",", ".")).append("\n");
        }

        // X - Transporte
        file.append("X|").append(Integer.parseInt(nf.getFrete().getCodigo()) - 1).append("\n");

        // X26 - Volumes
        file.append("X26|").append(quantidadeVolumes).append("|Volumes|||||\n");

        // Y - Vencimentos
        StringBuilder vencimentos = new StringBuilder();

        file.append("Y\n");
        file.append("Y02|");
        file.append(nf.getNumero()).append("|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotal(), "#0.00").replace(",", ".")).append("|");
        if (venda.getValorDesconto().doubleValue() > 0d)
            file.append(FunctionsHelper.numberFormat(venda.getValorDesconto(), "#0.00").replace(",", ".")).append("|");
        else
            file.append("|");
        file.append(FunctionsHelper.numberFormat(venda.getValorTotal(), "#0.00").replace(",", "."));
        file.append("|").append("\n");
        for (Receber receber : venda.getRecebimentos()) {
            file.append("Y07|");
            file.append(nf.getNumero()).append("/").append(receber.getParcela()).append("|");
            file.append(FunctionsHelper.dateFormat(receber.getVencimento(), "yyyy-MM-dd")).append("|");
            file.append(FunctionsHelper.numberFormat(receber.getValor(), "#0.00").replace(",", ".")).append("|").append("\n");

            // Vencimentos formatados para exibição em observação
            vencimentos.append(FunctionsHelper.dateFormat(receber.getVencimento(), "dd/MM/yyyy")).append(" [");
            vencimentos.append(FunctionsHelper.numberFormat(receber.getValor(), "#0.00")).append("] ");
        }

        // Z - Observações
        file.append("Z||");
        if (venda.getComissao() != null)
            file.append("Vendedor: ").append(FunctionsHelper.noAccent(venda.getFuncionario().getNome())).append(" ** ");
        if (venda.getPrazoPagamento() != null)
            file.append("Prazos: ").append(FunctionsHelper.noAccent(venda.getPrazoPagamento().getDescricao())).append(" ** ");
        /*file.append("Vencimentos: ").append(vencimentos).append(" ** ");*/
        if (venda.getFormaPagamento() != null)
            file.append("Forma Pagto: ").append(FunctionsHelper.noAccent(venda.getFormaPagamento().getDescricao())).append(" ** ");
        file.append(FunctionsHelper.noAccent(nf.getObservacao().replace("\n", " ")));

        return file.toString().getBytes();
    }
}