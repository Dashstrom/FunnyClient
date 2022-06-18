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

    public void run(String host, int port) throws IOException {
        try (
                Socket socketClient = new Socket(host, port);
                DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socketClient.getOutputStream()));
                DataInputStream input = new DataInputStream(new BufferedInputStream(socketClient.getInputStream()))
        ) {
            socketClient.setSoTimeout(10000);
            System.out.println("Running");
            output.write(code);
            output.writeUTF(filename);
            System.out.println("Start send...");
            send(output);
            output.flush();
            System.out.println("Send done !");
            System.out.println("Start recv...");
            this.exit = input.readByte();
            if (this.exit == Code.DENY) {
                throw new DeniedActionException();
            } else {
                recv(input);
            }
            System.out.println("Done recv !");
        }
    }

    protected abstract void send(DataOutputStream output) throws IOException ;
    protected abstract void recv(DataInputStream input) throws IOException ;
}
