package query;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
        long size = input.readLong();
        byte[] buffer = new byte[8192];
        int count;
        File file = new File(destination);
        try (FileOutputStream fileStream = new FileOutputStream(file)) {
            while ((count = input.read(buffer)) > 0){
                fileStream.write(buffer, 0, count);
            }
            fileStream.flush();
        }
        long actualSize = Files.size(Paths.get(destination));
        if (actualSize != size) {
            file.delete();
            throw new IOException("Wrong file size receive attempt " + size + " but got " + actualSize);
        }
    }
}
