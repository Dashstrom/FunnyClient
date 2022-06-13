import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class App extends Application {

    public static void main(String[] args) {
        System.out.println("Start running");
        run();
        launch(args);
    }

    public static void run() {
        // Server Host
        final String serverHost = "localhost";

        Socket clientSocket = null;
        BufferedWriter output = null;
        BufferedReader input = null;
        try {
            clientSocket = new Socket(serverHost, 9999);
            output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + serverHost);
            return;
        }
        try {
            Scanner sc = new Scanner(System.in);
            output.write(sc.nextLine());
            output.flush();
            System.out.println(input.readLine());
            output.close();
            input.close();
            clientSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    @Override
    public void start(Stage stage) {
        stage.show();
    }
}
