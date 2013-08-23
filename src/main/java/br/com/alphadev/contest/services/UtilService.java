
package br.com.alphadev.contest.services;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.com.alphadev.core.BasicService;
import br.com.alphadev.util.FunctionsHelper;

/**
 * @author André Penteado
 * @since 24/08/2007 - 10:45:12
 */
@Stateless
public class UtilService extends BasicService {

    /**
     * Realiza uma consulta EJB-QL sem paramêtros com o
     * propósito de testes.
     *
     * @param ejbql = Consulta EJB-QL
     * @return Lista de Strings com cada linha formatada com os resultados
     */
    public List pesquisarEJBQL(String ejbql) {
        ArrayList resultString = new ArrayList();

        if (ejbql != null && !"".equals(ejbql)) {
            try {
                List query = new ArrayList(executeQuery(ejbql, null));

                if (query != null) {
                    for (int i = 0; i < query.size(); i++) {
                        StringBuilder sb = new StringBuilder(" | ");
                        Object[] objs = null;

                        try {
                            objs = (Object[])query.get(i);
                        }
                        catch (ClassCastException ccex) {
                            objs = new Object[] { query.get(i) };
                        }

                        for (int x = 0; x < objs.length; x++) {
                            Object obj = objs[x];

                            if (obj == null) {
                                sb.append("null | ");
                                continue;
                            }

                            Class klass = obj.getClass();

                            // Tipo primitivo (ex: result de um count())
                            if (klass.getName().startsWith("java.lang")) {
                                Method method = klass.getMethod("toString");
                                sb.append(method.invoke(obj)).append(" | ");
                            }
                            else {
                                // Classe BEAN anotada como entity bean
                                Method[] listMethods = klass.getDeclaredMethods();

                                for (int j = 0; j < listMethods.length; j++) {
                                    Method method = listMethods[j];

                                    if (method.getName().startsWith("get") || method.getName().startsWith("is")) { // && method.getReturnType().getName().startsWith("java.lang"))
                                        try {
                                            sb.append(method.invoke(obj)).append(" | ");
                                        }
                                        catch (Exception ex) {
                                        }
                                    }
                                }
                            }
                        }

                        resultString.add(sb.toString());
                    }
                }
                else
                    resultString.add("CONSULTA VAZIA");
            }
            catch (Exception ex) {
                resultString.add(FunctionsHelper.exceptionToString(ex));
            }
        }

        return resultString;
    }
}
