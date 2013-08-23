
package br.com.alphadev.contest;

import java.io.IOException;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;

/**
 * Wrapper para implementar regras de seleção de skin e decoração de leiaute
 * 
 * @author Andre Penteado
 * @since 22/05/2012 - 18:50:30
 */
public class ContestSkinSelector extends ConfigurableSiteMeshFilter {
    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {

        /**
         * Classe interna com a regra de seleção de skin
         * 
         * @author Andre Penteado
         * @since 22/05/2012 - 18:50:10
         */
        class SkinDecoratorSelector implements DecoratorSelector<WebAppContext> {

            private final DecoratorSelector<WebAppContext> fallbackSelector;

            public SkinDecoratorSelector(DecoratorSelector<WebAppContext> fallbackSelector) {
                this.fallbackSelector = fallbackSelector;
            }

            @Override
            public String[] selectDecoratorPaths(Content content, WebAppContext context) throws IOException {
                // Fetch <meta name=decorator> value.
                // The default HTML processor already extracts these into 'meta.NAME' properties.
                String decorator = content.getExtractedProperties().getChild("meta").getChild("decorator").getValue();

                if (decorator != null) {
                    if ("popup".equals(decorator))
                        return new String[] { "/template/popup.jsp" };
                }
                /*else {
                    // Verifica se está logado e pega skin da sessão do usuário 
                    UsuarioLogadoWrapper usuario = ((UsuarioLogadoWrapper)context.getRequest().getSession().getAttribute("user"));
                    if (usuario != null && usuario.getFuncionario() != null && usuario.getFuncionario().getEmpresa() != null)
                        decorator = usuario.getFuncionario().getEmpresa().getSkin();

                    // Verifica se não encontrou skin e busca pela URL do sistema
                    if (decorator == null || "".equals(decorator)) {
                        CadastroService cs = (CadastroService)ServicesFactory.getInstance(CadastroService.class, null);
                        Empresa empresa = cs.buscarEmpresaPorPaginaInternet(context.getRequest().getRequestURL().toString()
                                        .replace(context.getRequest().getRequestURI(), ""));
                        if (empresa != null)
                            decorator = empresa.getSkin();
                    }
                }*/

                if (decorator != null)
                    return new String[] { "/skins/".concat(decorator).concat("/index.jsp") };

                return fallbackSelector.selectDecoratorPaths(content, context);
            }
        }

        builder.setCustomDecoratorSelector(new SkinDecoratorSelector(builder.getDecoratorSelector()));
    }
}