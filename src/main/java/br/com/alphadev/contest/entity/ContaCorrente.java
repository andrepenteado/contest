/*
 * Copyright (c) 2006 by UNESP - Universidade Estadual Paulista "JÚLIO DE MESQUITA FILHO"
 *               Faculdade de Ciências, Bauru, São Paulo, Brasil
 *               http://www.fc.unesp.br, http://www.unesp.br
 *
 *
 * Este arquivo é parte do programa Core.
 *
 * Core é um software livre; você pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como 
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da 
 * Licença, ou (na sua opnião) qualquer versão.
 *
 * Core é distribuido na esperança que possa ser util, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer
 * MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a
 * Licença Pública Geral GNU para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU
 * junto com Core, se não, escreva para a Fundação do Software
 * Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 * This file is part of Core.
 *
 * Core is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Core; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA *
 *
 *
 * Date:    26/02/2008 10:54:51
 *
 * Author: br.com.alphadev.beansdo
 */

package br.com.alphadev.contest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.alphadev.core.BasicEntity;

/**
 * @author Filipe Oliveira
 * @since 14/10/2010 - 16:20:45
 */
@Entity
@Table(name = "conta_corrente")
public class ContaCorrente extends BasicEntity<ContaCorrente> implements Serializable {

    private static final long serialVersionUID = 3455562940983241751L;

    @Id
    @SequenceGenerator(name = "conta_corrente_id_seq", sequenceName = "conta_corrente_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_corrente_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "agencia", nullable = false)
    private String agencia;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "sequencia_arquivo_cobranca")
    private Integer sequenciaArquivoCobranca;

    @ManyToOne
    @JoinColumn(name = "id_banco")
    private Banco banco;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    public ContaCorrente() {
        super(ContaCorrente.class);
    }

    public ContaCorrente(Long id) {
        super(ContaCorrente.class, id);
    }

    /**
     * @return id atribuido
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id id a ser atribuido
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return numero atribuido
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero numero a ser atribuido
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return agencia atribuido
     */
    public String getAgencia() {
        return agencia;
    }

    /**
     * @param agencia agencia a ser atribuido
     */
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    /**
     * @param sequenciaArquivoCobranca sequenciaArquivoCobranca a ser atribuido
     */
    public void setSequenciaArquivoCobranca(Integer sequenciaArquivoCobranca) {
        this.sequenciaArquivoCobranca = sequenciaArquivoCobranca;
    }

    /**
     * @return sequenciaArquivoCobranca atribuido
     */
    public Integer getSequenciaArquivoCobranca() {
        return sequenciaArquivoCobranca;
    }

    /**
     * @return banco atribuido
     */
    public Banco getBanco() {
        return banco;
    }

    /**
     * @param banco banco a ser atribuido
     */
    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    /**
     * @return empresa atribuido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa empresa a ser atribuido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getDigito() {
        return getNumero().substring(getNumero().length());
    }

    public String getNumeroSemDigito() {
        return getNumero().substring(0, getNumero().length()).replace("-", "");
    }

    public String getDigitoAgencia() {
        return getAgencia().substring(getAgencia().length());
    }

    public String getAgenciaSemDigito() {
        return getAgencia().substring(0, getAgencia().length()).replace("-", "");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        final ContaCorrente other = (ContaCorrente)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }
}