/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GerarArquivoReceita.java
 *
 * Created on 26/09/2009, 11:18:40
 */
package com.github.andrepenteado.contest.desktop;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.andrepenteado.contest.entity.ItemVenda;
import com.github.andrepenteado.contest.entity.NotaFiscal;
import com.github.andrepenteado.contest.filters.VendaFilter;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.util.FunctionsHelper;

/**
 *
 * @author Andr� Penteado
 */
public class NFp extends javax.swing.JFrame {

    private static final long serialVersionUID = 6883960036328772347L;

    /** Creates new form GerarArquivoReceita */
    public NFp() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        btnGerarArquivo = new javax.swing.JButton();
        lblDataInicio = new javax.swing.JLabel();
        txtDataInicio = new javax.swing.JFormattedTextField();
        txtDataFinal = new javax.swing.JFormattedTextField();
        lblDataFinal = new javax.swing.JLabel();
        pbProgresso = new javax.swing.JProgressBar();
        lblNotaFiscalAtual = new javax.swing.JLabel();
        lblItemAtual = new javax.swing.JLabel();
        lblArquivoSaida = new javax.swing.JLabel();
        txtArquivoSaida = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gerar Arquivo da Receita");
        setResizable(false);

        btnGerarArquivo.setText("Gerar Arquivo");
        btnGerarArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarArquivoActionPerformed(evt);
            }
        });

        lblDataInicio.setText("Data In�cio");

        txtDataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        txtDataFinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        lblDataFinal.setText("Data Final");

        pbProgresso.setStringPainted(true);

        lblNotaFiscalAtual.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblNotaFiscalAtual.setForeground(java.awt.Color.red);
        lblNotaFiscalAtual.setText("Nota Fiscal =>");

        lblItemAtual.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblItemAtual.setForeground(java.awt.Color.red);
        lblItemAtual.setText("Item =>");

        lblArquivoSaida.setText("Arquivo de Sa�da");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGerarArquivo)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pbProgresso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(lblNotaFiscalAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblItemAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblDataInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblArquivoSaida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lblDataFinal)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtArquivoSaida)))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataInicio)
                    .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDataFinal)
                    .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblArquivoSaida)
                    .addComponent(txtArquivoSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNotaFiscalAtual)
                    .addComponent(lblItemAtual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(btnGerarArquivo)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerarArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarArquivoActionPerformed
        new Thread() {

            @Override
            public void run() {
                try {
                    Date dataInicio = FunctionsHelper.stringToDate(txtDataInicio.getText());
                    Date dataFinal = FunctionsHelper.stringToDate(txtDataFinal.getText());

                    if (dataInicio.after(new Date())) {
                        throw new Exception("Data inicial maior que data atual");
                    }
                    if (dataFinal.after(new Date())) {
                        throw new Exception("Data final maior que data atual");
                    }

                    VendaFilter<NotaFiscal> filter = ServicesFactory.getInstance(VendaFilter.class, NotaFiscal.class);
                    filter.setDataEmissaoInicial(dataInicio);
                    filter.setDataEmissaoFinal(dataFinal);
                    List<NotaFiscal> listaNotasFiscais = new ArrayList<NotaFiscal>(filter.executeFilter());
                    int totalNotasFiscais = 0;
                    int totalItens = 0;

                    if (listaNotasFiscais != null && !listaNotasFiscais.isEmpty()) {
                        pbProgresso.setMaximum(listaNotasFiscais.size());
                        pbProgresso.setValue(0);

                        totalNotasFiscais = listaNotasFiscais.size();

                        StringBuilder conteudoArquivo = new StringBuilder();

                        // Gerar cabe�alho
                        conteudoArquivo.append(cabecalhoArquivo(dataInicio, dataFinal));

                        for (int i = 0; i < listaNotasFiscais.size(); i++) {
                            NotaFiscal nf = listaNotasFiscais.get(i);
                            pbProgresso.setValue(i);
                            pbProgresso.repaint();

                            lblNotaFiscalAtual.setText("Nota Fiscal => " + nf.getNumero());
                            conteudoArquivo.append(dadosNotaFiscal(nf));

                            for (ItemVenda item : nf.getVenda().getItens()) {
                                lblItemAtual.setText("Item => " + item.getProduto().getReferencia());
                                //conteudoArquivo.append(dadosItens(item, nf.getAliquotaIcms(), nf.getCfop()));
                                totalItens++;
                            }

                            conteudoArquivo.append(dadosTotaisNotaFiscal(nf));
                            conteudoArquivo.append(dadosTransporte(nf));
                        }

                        pbProgresso.setValue(listaNotasFiscais.size());
                        pbProgresso.repaint();

                        // Rodape arquivo
                        conteudoArquivo.append("90|");
                        conteudoArquivo.append(FunctionsHelper.fillSpacesLeft(Integer.toString(totalNotasFiscais),5)).append("|");
                        conteudoArquivo.append(FunctionsHelper.fillSpacesLeft(Integer.toString(totalItens),5)).append("|");
                        conteudoArquivo.append(FunctionsHelper.fillSpacesLeft(Integer.toString(totalNotasFiscais),5)).append("|");
                        conteudoArquivo.append(FunctionsHelper.fillSpacesLeft(Integer.toString(totalNotasFiscais),5)).append("|00000");

                        // Gerar arquivo
                        FileOutputStream out = new FileOutputStream(txtArquivoSaida.getText());
                        out.write(conteudoArquivo.toString().getBytes());
                        out.flush();
                        out.close();
                    } else {
                        throw new Exception("Nenhuma nota fiscal a ser processada no per�odo");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }//GEN-LAST:event_btnGerarArquivoActionPerformed

    private String cabecalhoArquivo(Date inicio, Date fim) {
        StringBuilder result = new StringBuilder();
        result.append("10|1,00|11111111111180|");
        result.append(FunctionsHelper.dateFormat(inicio, "dd/MM/yyyy")).append("|");
        result.append(FunctionsHelper.dateFormat(fim, "dd/MM/yyyy"));
        result.append("\r\n");
        return result.toString();
    }

    private String dadosNotaFiscal(NotaFiscal nf) {
        StringBuilder result = new StringBuilder();
        result.append("20|I||");
        //result.append(nf.getCfop().getDescricao()).append("|0|");
        result.append(nf.getNumero()).append("|");
        result.append(FunctionsHelper.dateFormat(nf.getEmissao(), "dd/MM/yyyy HH:mm:ss")).append("|");
        result.append(FunctionsHelper.dateFormat(nf.getEmissao(), "dd/MM/yyyy HH:mm:ss")).append("|1|");
        //result.append(nf.getCfop().getDescricao().substring(0, 4)).append("||||");
        //result.append(nf.getVenda().getCliente().getCnpj().replace(".", "").replace("/", "").replace("-", "").replace(" ","")).append("|");
        result.append(nf.getVenda().getCliente().getNome()).append("|");
        result.append(nf.getVenda().getCliente().getEndereco()).append("|");
        result.append(nf.getVenda().getCliente().getEndereco()).append("||");
        result.append(nf.getVenda().getCliente().getBairro()).append("|");
        result.append(nf.getVenda().getCliente().getCidade().getNome()).append("|");
        result.append(nf.getVenda().getCliente().getCidade().getEstado().getSigla()).append("||Brasil||");
        //result.append(nf.getVenda().getCliente().getCep().replace(".", "").replace("-", "").replace(" ","")).append("|Brasil|");
        //result.append(nf.getVenda().getCliente().getTelefone().replace(".", "").replace("/", "").replace("-", "").replace("(", "").replace(")", "").replace(" ","")).append("|");
        result.append(nf.getVenda().getCliente().getInscricaoEstadual().replace(".", "").replace("-", "").replace("/", "").replace(" ",""));
        result.append("\r\n");
        return result.toString();
    }

    /*private String dadosItens(ItemVenda item, Double icms, CFOP cfop) {
        StringBuilder result = new StringBuilder();
        result.append("30|");
        result.append(item.getProduto().getReferencia()).append("|");
        result.append(item.getProduto().getDescricao()).append("||");
        result.append(item.getProduto().getUnidade()).append("|");
        result.append(FunctionsHelper.numberFormat(item.getQuantidade(), "#0.0000")).append("|");
        result.append(FunctionsHelper.numberFormat(item.getValorVenda().floatValue(), "#0.0000")).append("|");
        result.append(FunctionsHelper.numberFormat(item.getValorVenda().floatValue() * item.getQuantidade(), "#0.00")).append("|");
        if ("5405".endsWith(cfop.getDescricao()))
            result.append("010|");
        else
            result.append("000|");
        result.append(FunctionsHelper.numberFormat(icms, "#0.00")).append("||");
        result.append("\r\n");
        return result.toString();
    }*/

    private String dadosTotaisNotaFiscal(NotaFiscal nf) {
        StringBuilder result = new StringBuilder();
        result.append("40|");
        result.append(FunctionsHelper.numberFormat(new Float(nf.getVenda().getValorTotalProduto()), "#0.00")).append("|");
        /*if (nf.getCfop().getDescricao().startsWith("5405"))
            result.append("0,00|");
        else
            result.append(FunctionsHelper.numberFormat(new Float(nf.getTotalIcms()), "#0.00")).append("|");*/
        result.append("0,00|0,00|");
        result.append(FunctionsHelper.numberFormat(new Float(nf.getVenda().getValorTotalProduto()), "#0.00")).append("|");
        result.append("0,00|0,00|0,00|0,00|0,00|");
        result.append(FunctionsHelper.numberFormat(new Float(nf.getVenda().getValorTotalProduto()), "#0.00")).append("|||\r\n");
        return result.toString();
    }

    private String dadosTransporte(NotaFiscal nf) {
        StringBuilder result = new StringBuilder();
        result.append("50|");
        result.append(Integer.parseInt(nf.getFrete().getCodigo()) - 1).append("||||||||||||||\r\n");
        return result.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NFp().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGerarArquivo;
    private javax.swing.JLabel lblArquivoSaida;
    private javax.swing.JLabel lblDataFinal;
    private javax.swing.JLabel lblDataInicio;
    private javax.swing.JLabel lblItemAtual;
    private javax.swing.JLabel lblNotaFiscalAtual;
    private javax.swing.JProgressBar pbProgresso;
    private javax.swing.JTextField txtArquivoSaida;
    private javax.swing.JFormattedTextField txtDataFinal;
    private javax.swing.JFormattedTextField txtDataInicio;
    // End of variables declaration//GEN-END:variables
}
