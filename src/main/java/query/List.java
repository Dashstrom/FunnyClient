package query;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class List extends Query {

    private String[] list;

    public List(String filename) {
        super(Code.LIST, filename);
    }

    @Override
    protected void send(DataOutputStream output) throws IOException {
    }

    @Override
    protected void recv(DataInputStream input) throws IOException {
        int size = input.readInt();
        byte[] array = new byte[size];
        int read = input.read(array);
        System.out.println(read + " " + size);
        if (read != size) throw new IllegalStateException("Invalid file size");
        list = new String(array, StandardCharsets.UTF_8).split("\n");
    }

    public String[] result() {
        return list;
    }
}
