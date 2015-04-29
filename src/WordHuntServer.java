import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Karim Ghozlani on 29.04.2015.
 */

public class WordHuntServer implements Runnable {

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;
    //private ArrayList<Socket> clientSockets;

    private StringBuilder handleClient(StringBuilder clientCommand){
        // do stuff
        System.out.println("Data received from client: "+ clientCommand);

        return clientCommand.insert(clientCommand.length()-1," has been read by the server");
    }

    public WordHuntServer(int port) throws  IOException{
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args){
        try {
            WordHuntServer wordHuntServer = new WordHuntServer(1234);
            new Thread(wordHuntServer).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                //ClientWorkers to implement, just ping test
                clientSocket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                StringBuilder sb = new StringBuilder();
                String tmp;
                    while(!((tmp = reader.readLine()).equals(""))) {
                        sb.append(tmp);
                        sb.append("\n");
                    }
                writer.println(handleClient(sb));
                writer.println();
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
