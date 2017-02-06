import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    final static String SERVER_HOST = "localhost";
    final static int SERVER_PORT = 4580;
    ServerSocket serverSocket;
    private static GeoSearchManager gsm;

    public static void main(String[] args) throws Exception {

        // we'd want to instantiate and data structures
        // or connections we'd want to share among threads
        // here. then, be careful not to write to them!
        System.out.println("[Server] Starting Server");
        gsm = new GeoSearchManager();
        System.out.println("[Server] Connecting to socket");
        new Server().run();
    }

    public void run() throws Exception {
        serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("[Server] Server Started On Port " + SERVER_PORT);
        acceptRequests();

    }

    private void acceptRequests() throws Exception {

        while(true){
            Socket s = serverSocket.accept();
            // server handler is the thread
            ServerHandler gsh = new ServerHandler(s, gsm);
            // this starts it and calls the server handler run() method automatically
            gsh.start();
        }
    }
}
