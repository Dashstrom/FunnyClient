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
        long size = Files.size(Paths.get(source));
        output.writeLong(size);
        int count;
        byte[] buffer = new byte[8192];
        File file = new File(source);
        try (FileInputStream streamFile = new FileInputStream(file)) {
            while ((count = streamFile.read(buffer)) > 0){
                output.write(buffer, 0, count);
            }
        }
    }

    @Override
    protected void recv(DataInputStream input) throws IOException {}
}
