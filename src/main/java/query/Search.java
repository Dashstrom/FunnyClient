package query;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search extends Query {

    private ArrayList<String> list;

    public Search(String filename) {
        super(Code.LIST, filename);
        this.list = new ArrayList<>();
    }

    @Override
    protected void send(DataOutputStream output) throws IOException {
    }

    @Override
    protected void recv(DataInputStream input) throws IOException {
        list = new ArrayList<>();
        while (true) {
            try {
                list.add(input.readUTF());
            } catch (EOFException ignored) {
                break;
            }
        }
    }

    public List<String> result() {
        return list;
    }
}
