
package br.com.alphadev.contest.actions;

import java.util.Collection;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.core.SessionContext;
import org.mentawai.list.ListManager;

import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.ComboPrecoComissao;
import br.com.alphadev.contest.entity.CFOP;
import br.com.alphadev.contest.entity.Cliente;
import br.com.alphadev.contest.entity.Estado;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.contest.services.VendaService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

public class BuscaPadraoAction extends BaseAction implements Authorizable {

    public static enum FORWARD {
        PESQUISA_CLIENTE {
            @Override
            public String getTarget() {
                return "/cadastros/cliente/pesquisar.jsp";
            }
        },
        CADASTRO_CLIENTE {
            @Override
            public String getTarget() {
                return "/cadastros/cliente/cadastro.jsp";
            }
        },
        PESQUISA_FORNECEDOR {
            @Override
            public String getTarget() {
                return "/cadastros/fornecedor/pesquisar.jsp";
            }
        },
        CADASTRO_FORNECEDOR {
            @Override
            public String getTarget() {
                return "/cadastros/fornecedor/cadastro.jsp";
            }
        },
        PESQUISA_PRODUTO {
            @Override
            public String getTarget() {
                return "/cadastros/produto/pesquisar.jsp";
            }
        },
        PESQUISA_NOTA_FISCAL {
            @Override
            public String getTarget() {
                return "/vendas/nota_fiscal/pesquisar.jsp";
            }
        },
        PESQUISA_ORCAMENTO {
            @Override
            public String getTarget() {
                return "/vendas/orcamento/pesquisar.jsp";
            }
        },
        CADASTRO_PEDIDO {
            @Override
            public String getTarget() {
                return "/vendas/pedido/cadastro.jsp";
            }
        },
        CADASTRO_ESTOQUE {
            @Override
            public String getTarget() {
                return "/estoque/movimento/cadastro.jsp";
            }
        },
        PESQUISA_ESTOQUE {
            @Override
            public String getTarget() {
                return "/estoque/movimento/pesquisar.jsp";
            }
        },
        ESTOQUE_SINTETICO {
            @Override
            public String getTarget() {
                return "/estoque/relatorio/sintetico.jsp";
            }
        },
        PESQUISA_RECEBER {
            @Override
            public String getTarget() {
                return "/financeiro/receber/pesquisar.jsp";
            }
        },
        PESQUISA_RECEBIDO {
            @Override
            public String getTarget() {
                return "/financeiro/recebido/pesquisar.jsp";
            }
        },
        PESQUISAR_COMPRA {
            @Override
            public String getTarget() {
                return "/compras/pesquisar.jsp";
            }
        },
        LANCAMENTO_COMPRA {
            @Override
            public String getTarget() {
                return "/compras/cadastro.jsp";
            }
        },
        PESQUISA_PAGAR {
            @Override
            public String getTarget() {
                return "/financeiro/pagar/pesquisar.jsp";
            }
        },
        COMISSAO_PENDENTE {
            @Override
            public String getTarget() {
                return "/vendas/comissao/pendente.jsp";
            }
        },
        COMISSAO_PAGA {
            @Override
            public String getTarget() {
                return "/vendas/comissao/paga.jsp";
            }
        },
        PESQUISA_PAGO {
            @Override
            public String getTarget() {
                return "/financeiro/pago/pesquisar.jsp";
            }
        };

        public abstract String getTarget();
    };

    private Log4jWrapper log = new Log4jWrapper(BuscaPadraoAction.class, null);
    private CadastroService cadastroService = null;
    private VendaService vendaService = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroService = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            vendaService = (VendaService)ServicesFactory.getInstance(VendaService.class, new Object[] { userLogin });
            log = new Log4jWrapper(BuscaPadraoAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(BuscaPadraoAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        return true;
    }

    public String cliente() {
        UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();

        try {
            Cliente cliente = new Cliente(input.getLong("txt_id_cliente"));
            if (cliente == null
                            || cliente.getId() == null
                            || (userLogin.getCategoriaAtual().getNome().equals(KGlobal.CATEGORIA_VENDEDOR) && cliente.getFuncionario().getId()
                                            .intValue() != userLogin.getFuncionario().getId().intValue()))
                output.setValue("erroBuscarCliente", ConfigHelper.getProperty("error.notFound", "Cliente"));
            else
                output.setValue("nomeCliente", cliente.getNome());
        }
        catch (Exception ex) {
            output.setValue("erroBuscarCliente", ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }

        return SUCCESS;
    }

    public String fornecedor() {
        try {
            long idFornecedor = input.getLong("txt_id_fornecedor");
            Fornecedor fornecedor = new Fornecedor(idFornecedor);
            if (fornecedor == null || fornecedor.getId() == null)
                output.setValue("erroBuscarFornecedor", ConfigHelper.getProperty("error.notFound", "Fornecedor"));
            else
                output.setValue("nomeFornecedor", fornecedor.getNome());
        }
        catch (Exception ex) {
            output.setValue("erroBuscarFornecedor", ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return SUCCESS;
    }

    public String produto() {
        String target = input.getString("target").replace(((SessionContext)session).getRequest().getContextPath() + "/WEB-INF/jsp", "");

        try {
            String chave = input.getString("txt_referencia_produto");

            // Procurar por referência
            Produto produto = cadastroService.buscarProdutoPorReferencia(chave);
            if (produto == null) {
                try {
                    // Caso não encontrado por referência, procurar por ID
                    produto = new Produto(Long.parseLong(chave));
                }
                catch (NumberFormatException nbfex) {
                }
                if (produto == null || produto.getId() == null) {
                    addError("txt_descricao_produto", ConfigHelper.getProperty("error.notFound", "Produto"));
                    output.setValue("txt_descricao_produto", "");
                    output.setValue("txt_preco_produto", "");
                    output.setValue("txt_id_produto", "");
                    output.setValue("foco", "txt_referencia_produto");
                    return target;
                }
            }

            output.setValue("txt_descricao_produto", produto.getDescricao());
            output.setValue("txt_id_produto", produto.getId());
            output.setValue("foco", "txt_quantidade");

            if (target.startsWith("/compras/"))
                output.setValue("txt_preco_produto", produto.getCustoPrazo());
            else if (target.startsWith("/vendas/pedido/")) {
                Collection<ComboPrecoComissao> comboPrecos = vendaService.calcularPrecosVenda(produto);
                if (produto.getTabelaPreco() != null && comboPrecos != null && !comboPrecos.isEmpty()) {
                    output.setValue("comboPrecos", ListManager.convert(comboPrecos, "chavePrecoComissao", "stringPreco"));
                    //output.setValue("txt_preco_produto", produto.getVendaPrazo());  // Provisoriamente !!!!
                }
                else {
                    output.setValue("txt_preco_produto", produto.getVendaPrazo());
                }
            }
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }

        return target;
    }

    public String funcionario() {
        try {
            long idVendedor = input.getLong("txt_id_funcionario");
            Funcionario funcionario = new Funcionario(idVendedor);
            if (funcionario == null || funcionario.getId() == null)
                output.setValue("erroBuscarFuncionario", ConfigHelper.getProperty("error.notFound", "Vendedor"));
            else
                output.setValue("nomeFuncionario", funcionario.getNome());
        }
        catch (Exception ex) {
            output.setValue("erroBuscarFuncionario", ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return SUCCESS;
    }

    public String cidades() {
        Estado estado = new Estado(input.getLong("id_estado"));
        output.setValue("cidades", cadastroService.pesquisarCidadesPorEstado(estado));
        return SUCCESS;
    }

    public String cfop() {
        String target = input.getString("target").replace(((SessionContext)session).getRequest().getContextPath() + "/WEB-INF/jsp", "");

        try {
            String chave = input.getString("txt_codigo_cfop");

            // Procurar por referência
            CFOP cfop = cadastroService.buscarCFOPPorCodigo(chave);
            if (cfop == null) {
                try {
                    // Caso não encontrado por referência, procurar por ID
                    cfop = new CFOP(Long.parseLong(chave));
                }
                catch (NumberFormatException nbfex) {
                }
                if (cfop == null || cfop.getId() == null) {
                    addError("txt_descricao_cfop", ConfigHelper.getProperty("error.notFound", "CFOP"));
                    output.setValue("txt_descricao_cfop", "");
                    output.setValue("txt_id_cfop", "");
                    output.setValue("foco", "txt_codigo_cfop");
                    return target;
                }
            }

            output.setValue("txt_descricao_cfop", cfop.getDescricao());
            output.setValue("txt_id_cfop", cfop.getId());
            output.setValue("foco", "txt_id_cfop");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }

        return target;
    }

}
