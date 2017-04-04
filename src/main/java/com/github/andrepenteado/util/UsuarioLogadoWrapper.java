
package com.github.andrepenteado.util;

import java.io.Serializable;

import com.github.andrepenteado.contest.entity.Categoria;
import com.github.andrepenteado.contest.entity.Funcionario;

/**
 * @author André Penteado
 * @since 28/08/2007 - 12:04:05
 */
public class UsuarioLogadoWrapper implements Serializable {

    private static final long serialVersionUID = 6612697210357695575L;

    // Objeto usuário (tabela funcionario)
    private Funcionario funcionario;

    // Categoria atualmente selecionada (o usuário pode ter várias categorias)
    private Categoria categoriaAtual;

    // IP do usuário
    private String ip;

    public UsuarioLogadoWrapper() {
    }

    public UsuarioLogadoWrapper(Funcionario funcionario) {
        setFuncionario(funcionario);
    }

    public String getCategoria() {
        if (getCategoriaAtual() != null)
            return getCategoriaAtual().getNome();
        return "";
    }

    public String getIdentificacao() {
        if (getFuncionario() != null)
            return getFuncionario().getIdentificacao();
        return "";
    }

    public String getModulo() {
        return ConfigHelper.get().getString("sistema.nome");
    }

    /**
     * Método utilizado pela fábrica de serviços quando
     * esse objeto é passado como paramêtro para alguma classe
     * de serviço. O pool da fábrica, com esse método, é baseado
     * no nome da categoria, evitando o mesmo método de serviço
     * ser utilizado por outra categoria por estar pegando do
     * pool de serviços da fábrica. 
     */
    @Override
    public String toString() {
        if (getFuncionario() != null)
            return getFuncionario().getIdentificacao();
        return super.toString();
    }

    /**
     * @param funcionario funcionario a ser atribuido
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @return funcionario atribuido
     */
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * @param categoriaAtual categoriaAtual a ser atribuido
     */
    public void setCategoriaAtual(Categoria categoriaAtual) {
        this.categoriaAtual = categoriaAtual;
    }

    /**
     * @return categoriaAtual atribuido
     */
    public Categoria getCategoriaAtual() {
        return categoriaAtual;
    }

    /**
     * @param ip ip a ser atribuido
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return ip atribuido
     */
    public String getIp() {
        return ip;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categoriaAtual == null) ? 0 : categoriaAtual.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((funcionario == null) ? 0 : funcionario.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UsuarioLogadoWrapper other = (UsuarioLogadoWrapper)obj;
        if (categoriaAtual == null) {
            if (other.categoriaAtual != null)
                return false;
        }
        else if (!categoriaAtual.equals(other.categoriaAtual))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        }
        else if (!ip.equals(other.ip))
            return false;
        if (funcionario == null) {
            if (other.funcionario != null)
                return false;
        }
        else if (!funcionario.equals(other.funcionario))
            return false;
        return true;
    }
}
