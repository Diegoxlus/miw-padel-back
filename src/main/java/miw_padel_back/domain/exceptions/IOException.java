package miw_padel_back.domain.exceptions;

public class IOException extends RuntimeException {
    private static final String DESCRIPTION = "I/O Exception";

    public IOException(String detail) {
        super(DESCRIPTION + ": " + detail);
    }


}
