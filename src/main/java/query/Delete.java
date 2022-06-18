package query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Delete extends Query {
    public Delete(String filename) {
        super(Code.DELETE, filename);
    }

    @Override
    protected void send(DataOutputStream output) throws IOException {}

    @Override
    protected void recv(DataInputStream input) throws IOException {}
}
