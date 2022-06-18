package query;

import java.io.IOException;

public class DeniedActionException extends IOException {

    public DeniedActionException() {
        super("Action refused");
    }
}
