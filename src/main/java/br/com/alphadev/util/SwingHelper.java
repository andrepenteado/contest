/*
 * SwingHelper.java
 *
 * Created on 29 de Maio de 2007, 21:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.com.alphadev.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author root
 */
public class SwingHelper {

    private static Timer timer;

    /** Creates a new instance of SwingHelper */
    public SwingHelper() {
    }

    public static void validarMoeda(KeyEvent evt) {
        // Deve ser um digito ou virgula ou ponto ou sinal de negativo
        if (!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != ',' && evt.getKeyChar() != '.' && evt.getKeyChar() != '-')
            evt.consume();
        // Não pode colocar mais de uma virgula ou ponto
        if (((JTextField)evt.getSource()).getText().indexOf(',') > 0 && (evt.getKeyChar() == ',' || evt.getKeyChar() == '.'))
            evt.consume();
        // O sinal de negativo deve ser o 1o. caracter
        if (((JTextField)evt.getSource()).getText().length() > 0 && evt.getKeyChar() == '-')
            evt.consume();
        if (evt.getKeyChar() == '.')
            evt.setKeyChar(',');
    }

    public static void validarNumeroInteiro(KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '-')
            evt.consume();
        if (((JTextField)evt.getSource()).getText().length() > 0 && evt.getKeyChar() == '-')
            evt.consume();
    }

    public static void centralizarFrame(Window frame) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        //  Obtendo a dimensão da tela
        Dimension screenSize = tk.getScreenSize();
        //  Centralizando
        frame.setLocation((screenSize.width - frame.getSize().width) / 2, (screenSize.height - frame.getSize().height) / 2);
    }

    public static void exibeMensagemTemporaria(final JLabel lblMensagem, String mensagem) {
        lblMensagem.setText(mensagem);
        timer = new Timer(4 * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                timer.stop();
                lblMensagem.setText("");
            }
        });
        timer.start();
    }
}