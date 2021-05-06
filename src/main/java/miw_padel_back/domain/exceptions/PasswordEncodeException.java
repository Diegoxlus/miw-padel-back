package miw_padel_back.domain.exceptions;

public class PasswordEncodeException extends RuntimeException {
    private static final String DESCRIPTION = "Fatal error on encode password: ";

    public PasswordEncodeException() {
        super(DESCRIPTION + ". " + "invalid algorithm or key");
    }
}
