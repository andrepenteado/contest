
package br.com.alphadev.exception;

/**
 * @author André Penteado
 * @since 16/04/2007 - 17:14:45
 */
public class ServiceValidationException extends Exception {

    private static final long serialVersionUID = -8001671207689681825L;

    public ServiceValidationException() {
        super();
    }

    public ServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceValidationException(String message) {
        super(message);
    }

    public ServiceValidationException(Throwable cause) {
        super(cause);
    }

}
