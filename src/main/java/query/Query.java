package query;

import java.io.*;
import java.net.Socket;

public abstract class Query {

    private final int code;
    private final String filename;
    private int exit;

    public Query(int code, String filename) {
        this.code = code;
        this.filename = filename;
        this.exit = Code.NO_REPLY;
    }

    public int getCode() {
        return code;
    }

    public String getFilename() {
        return filename;
    }

    public int getExit() {
        return exit;
    }

    public void run(String host, int port) throws IOException {
        try (
                Socket socketClient = new Socket(host, port);
                DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socketClient.getOutputStream()));
                DataInputStream input = new DataInputStream(new BufferedInputStream(socketClient.getInputStream()))
        ) {
            output.write(code);
            output.writeInt(filename.length());
            output.writeBytes(filename);
            send(output);
            output.flush();
            this.exit = input.readByte();
            if (this.exit == Code.DENY) {
                System.out.println("DENY");
            } else {
                recv(input);
            }
        }
    }

    protected abstract void send(DataOutputStream output) throws IOException ;
    protected abstract void recv(DataInputStream input) throws IOException ;
}
