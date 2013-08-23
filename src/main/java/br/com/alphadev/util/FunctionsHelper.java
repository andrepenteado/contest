
package br.com.alphadev.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * @author André Penteado
 * @since 09/11/2004 (15:21:58)
 */
public class FunctionsHelper {

    /** Objeto para log */
    private static final Log4jWrapper log = new Log4jWrapper(FunctionsHelper.class, null);

    /**
     * Transforma um valor numérico para uma string
     * no formato especificado.
     * @param value = Valor numérico a ser formatado.
     * @param format = Formato a ser transformado.
     * @return A string formatada.
     */
    public static String numberFormat(double value, String format) {
        DecimalFormat objDecimalFormat = new DecimalFormat(format);
        return objDecimalFormat.format(value);
    }

    /**
     * Transforma uma data para uma string
     * no formato especificado.
     * @param format = Formato a ser transformado.
     * @param date = Data a ser formatado.
     * @return A string formatada.
     */
    public static String dateFormat(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat objDateFormat = new SimpleDateFormat(format);
        return objDateFormat.format(date);
    }

    /**
     * Retorna a string do StackTrace vindos da exceção
     * @param ex = Exceção
     * @return String do StackTrace
     */
    public static String exceptionToString(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Converte de String para Date. A String deve estar no formato de data
     * @param string String a ser convertida
     * @return Date Data convertida
     * @throws ParseException
     */
    public static Date stringToDate(String string) throws ParseException {
        Date ret = DateFormat.getDateInstance().parse(string);
        return ret;
    }

    /**
     * Codifica uma mensagem usando o algoritmo especificado
     * @param message = Mensagem a ser codificada
     * @param algorithm = Algoritmo a ser utilizado. Validos MD5 e SHA1
     * @return Texto codificado
     */
    public static String encode(String message, String algorithm) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.reset();
            md.update(message.getBytes());

            byte[] digest = md.digest();
            StringBuffer hexSenha = new StringBuffer();

            // Converte cada byte em hexadecimal
            for (int i = 0; i < digest.length; i++) {
                hexSenha.append(Integer.toHexString(0xFF & digest[i]));
            }

            result = hexSenha.toString();
        }
        catch (NoSuchAlgorithmException nsaex) {
            log.error("ALGORITMO PARA CODIFICAÇÃO INVÁLIDO", nsaex);
        }

        return result;
    }

    /**
     * Envia mensagem de email
     * @param from = Remetente
     * @param to = Destinatarios
     * @param cc = Copias
     * @param bcc = Copias ocultas
     * @param subject = Assunto
     * @param message = Mensagem
     * @param attachments = Paths dos anexos
     * @exception MessagingException = Erro no envio
     */
    public static void sendEmail(String from, String[] to, String[] cc, String bcc[], String subject, String message, String[] attachments)
                    throws MessagingException {
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", ConfigHelper.get().getString("mail.server"));
        Session mailSession = Session.getInstance(mailProps);

        MimeMessage mimeMessage = new MimeMessage(mailSession);
        mimeMessage.setFrom(new InternetAddress(from));
        mimeMessage.setSubject(subject);
        mimeMessage.setSentDate(new Date());

        if (to != null && to.length > 0) {
            for (int i = 0; i < to.length; i++)
                mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to[i]));
        }
        if (cc != null && cc.length > 0) {
            for (int i = 0; i < cc.length; i++)
                mimeMessage.addRecipient(RecipientType.CC, new InternetAddress(cc[i]));
        }
        if (bcc != null && bcc.length > 0) {
            for (int i = 0; i < bcc.length; i++)
                mimeMessage.addRecipient(RecipientType.BCC, new InternetAddress(bcc[i]));
        }

        // Anexar arquivos
        if (attachments == null) {
            mimeMessage.setText(message, "ISO-8859-1");
        }
        else {
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(message);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (int i = 0; i < attachments.length; i++) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachments[i]);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(source.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            mimeMessage.setContent(multipart);
        }

        Transport.send(mimeMessage);
    }

    /**
     * Formatar o path para que as barras de
     * separação de diretórios fiquem corretas
     * @param path
     * @return Path formatado
     */
    public static String formatPath(String path) {
        path = path.replace("\\", "/").replace("//", "/");
        path = path.endsWith("/") ? path : path + "/";
        return path;
    }

    /**
     * Retorna o valor string do objeto comparando se o objeto é null.
     * Caso seja null, retorna vazio. Utilizado para diminuir ocorrências
     * de exceções NullPointerException
     * @param possibleNullValue = Objeto que possivelmente seja nulo
     * @return Valor string do objeto nunca nulo
     */
    public static String toStringNotNull(Object possibleNullValue) {
        if (possibleNullValue == null)
            return "";
        return possibleNullValue.toString();
    }

    /**
     * @return = Diretório raíz do ClassPath da aplicação
     */
    public static String getRootClassPath(Class<? extends Object> klass) {
        String classDir = "/".concat(klass.getName().replace('.', '/')).concat(".class");
        String fullPath = klass.getResource(classDir).toString();
        return fullPath.replace("file:", "").replace(classDir, "");
    }

    /**
     * Gerar uma senha aleatória
     *
     * @return = Nova Senha
     */
    public static String generateRandomPassword() {
        Random rand = new Random(System.currentTimeMillis());

        int len = 8;
        char[] password = new char[len];

        for (int x = 0; x < len; x++) {
            int randDecimalAsciiVal = rand.nextInt(26) + 97; // Letra minusculas
            if (x % 2 == 0)
                password[x] = (char)randDecimalAsciiVal;
            else
                password[x] = Integer.toString(randDecimalAsciiVal + 3).toCharArray()[2];
        }

        return String.valueOf(password);
    }

    /**
     * Retorna a extensão do arquivo conforme seu Content-Type
     * 
     * @param contentType = O Content-Type do arquivo
     * @return
     */
    public static String getExtensaoArquivo(String contentType) {
        if (contentType.toLowerCase().endsWith("application/pdf"))
            return "pdf";
        else if (contentType.toLowerCase().endsWith("application/msword"))
            return "doc";
        else if (contentType.toLowerCase().endsWith("application/ms-powerpoint"))
            return "ppt";
        else if (contentType.toLowerCase().endsWith("application/vnd.ms-excel"))
            return "xls";
        else if (contentType.toLowerCase().endsWith("image/jpeg"))
            return "jpg";
        else if (contentType.toLowerCase().endsWith("image/gif"))
            return "gif";
        else if (contentType.toLowerCase().endsWith("text/plain"))
            return "txt";
        else if (contentType.toLowerCase().endsWith("text/csv"))
            return "csv";
        else if (contentType.toLowerCase().endsWith("text/html"))
            return "html";
        else
            return null;
    }

    /**
     * Retorna o contentType do arquivo a partir de sua extensão
     * @param nomeArquivo = Nome do arquivo com extensão
     * @return ContentType do arquivo
     */
    public static String getContentType(String nomeArquivo) {
        if (nomeArquivo.toLowerCase().endsWith("pdf"))
            return "application/pdf";
        else if (nomeArquivo.toLowerCase().endsWith("doc"))
            return "application/msword";
        else if (nomeArquivo.toLowerCase().endsWith("ppt"))
            return "application/ms-powerpoint";
        else if (nomeArquivo.toLowerCase().endsWith("xls"))
            return "application/vnd.ms-excel";
        else if (nomeArquivo.toLowerCase().endsWith("jpg"))
            return "image/jpeg";
        else if (nomeArquivo.toLowerCase().endsWith("gif"))
            return "image/gif";
        else if (nomeArquivo.toLowerCase().endsWith("txt"))
            return "text/plain";
        else if (nomeArquivo.toLowerCase().endsWith("csv"))
            return "text/csv";
        else if (nomeArquivo.toLowerCase().endsWith("html"))
            return "text/html";
        else
            return null;
    }

    /**
     * Formata um texto colocando depois de espacos a primeira letra maiuscula e
     * as restantes minusculas ate o proximo espaco em branco.
     * 
     * @param text Texto a se formatado
     * @return Texto formatado
     */
    public static String textFormatter(String text) {
        if (text == null || text.trim().length() == 0)
            return "";

        String retorno = text.substring(0, 1).toUpperCase();
        boolean espaco = false;

        for (int i = 1; i < text.length(); i++) {
            String atual = text.substring(i, i + 1);

            if (espaco && !atual.equals(" ")) {
                retorno += atual.toUpperCase();
                espaco = false;
            }
            else if (!espaco && !atual.equals(" ")) {
                retorno += atual.toLowerCase();
            }

            if (atual.equals(" ") && !espaco) {
                espaco = true;
                retorno += " ";
            }
        }

        return retorno;
    }

    /**
     * Preencher com espaços a direita caso o texto precise
     *
     * @param text Texto a ser preenchido
     * @param size Tamanho máximo do texto
     */
    public static String fillSpacesRight(String text, int size) {
        StringBuilder result = new StringBuilder();
        int tamanhoTexto = text.length();
        if (tamanhoTexto > size)
            result.append(text.substring(0, size));
        else {
            result.append(text);
            for (int i = 0; i < (size - tamanhoTexto); i++)
                result.append(" ");
        }
        return result.toString();
    }

    /**
     * Preencher com espaços a esquerda caso o texto precise
     *
     * @param text Texto a ser preenchido
     * @param size Tamanho máximo do texto
     */
    public static String fillSpacesLeft(String text, int size) {
        StringBuilder result = new StringBuilder();
        int tamanhoTexto = text.length();
        if (tamanhoTexto > size)
            result.append(text.substring(0, size));
        else {
            for (int i = 0; i < (size - tamanhoTexto); i++)
                result.append(" ");
            result.append(text);
        }
        return result.toString();
    }

    /**
     * Centralizar a string colocando espaços a direita e esquerda
     * 
     * @param text = Texto a ser centralizado
     * @param size = Tamanho máximo do texto
     */
    public static String fillSpacesLeftRight(String text, int size) {
        StringBuilder result = new StringBuilder();
        int tamanhoTexto = text.length();
        if (tamanhoTexto > size)
            result.append(text.substring(0, size));
        else {
            int meioLargura = size / 2;
            int meioTexto = tamanhoTexto / 2;
            int espacoEsquerda = meioLargura - meioTexto;
            int espacoDireita = size - (espacoEsquerda + tamanhoTexto);
            for (int i = 0; i < espacoEsquerda; i++)
                result.append(" ");
            result.append(text);
            for (int i = 0; i < espacoDireita; i++)
                result.append(" ");
        }
        return result.toString();
    }

    /**
     * @return nome da máquina local (onde roda a JVM) sem domínio
     */
    public static String getLocalHostName() {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            int point = hostName.indexOf(".");
            if (point != -1)
                hostName = hostName.substring(0, point);
        }
        catch (UnknownHostException uhex) {
            hostName = "";
        }
        catch (Exception ex) {
            throw new RuntimeException("ERRO AO BUSCAR NOME DA MÁQUINA LOCAL");
        }
        return hostName;
    }

    private static char acentos[] = { 'à', 'á', 'â', 'ä', 'ã', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ò', 'ó', 'ô', 'ö', 'õ', 'ù', 'ú', 'û', 'ü',
                    'ç' };

    private static char normais[] = { 'a', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'i', 'o', 'o', 'o', 'o', 'o', 'u', 'u', 'u', 'u',
                    'c' };

    public static String noAccent(String str) {
        if (str == null)
            return "";
        for (int i = 0; i < acentos.length; i++) {
            str = str.replace(acentos[i], normais[i]);
            str = str.replace(String.valueOf(acentos[i]).toUpperCase(), String.valueOf(normais[i]).toUpperCase());
        }
        return str;
    }
}