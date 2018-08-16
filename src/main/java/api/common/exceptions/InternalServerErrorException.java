package api.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

    /** Serial version ID */
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(){
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(Throwable ex) {
        super(ex);
    }

    public InternalServerErrorException(String message, Throwable ex) {
        super(message, ex);
    }
}