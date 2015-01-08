
package br.com.alphadev.exception;

/**
 * @author André Penteado
 * @since 16/04/2007 - 17:14:45
 */
public class IllegalServiceClassException extends RuntimeException {

    private static final long serialVersionUID = 5419132620388279952L;

    public IllegalServiceClassException() {
        super();
    }

    public IllegalServiceClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalServiceClassException(String message) {
        super(message);
    }

    public IllegalServiceClassException(Throwable cause) {
        super(cause);
    }

}
