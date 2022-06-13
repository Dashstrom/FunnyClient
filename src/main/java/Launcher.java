public class Launcher {

    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}