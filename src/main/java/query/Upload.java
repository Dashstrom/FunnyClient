package query;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Upload extends Query {

    public final String source;

    public Upload(String source, String filename) {
        super(Code.UPLOAD, filename);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    protected void send(DataOutputStream output) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(source));
        output.writeInt(data.length);
        output.write(data);
    }

    @Override
    protected void recv(DataInputStream input) throws IOException {}
}
