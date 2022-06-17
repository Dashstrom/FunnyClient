package query;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Download extends Query {

    private final String destination;

    public Download(String filename, String destination) {
        super(Code.DOWNLOAD, filename);
        this.destination = destination;
    }

    public Download(int code, String filename, String destination) {
        super(code, filename);
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    protected void send(DataOutputStream output) throws IOException {}

    @Override
    protected void recv(DataInputStream input) throws IOException {
        int size = input.readInt();
        byte[] array = new byte[size];
        int read = input.read(array);
        if (read != size) throw new IllegalStateException("Invalid file size");
        Files.write(Paths.get(destination), array);
    }
}
