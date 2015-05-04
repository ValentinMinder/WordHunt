import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import protocol.ActualRequest;
import protocol.Protocol;

/**
 * Created by Karim Ghozlani on 29.04.2015.
 */

public class WordHuntServer implements Runnable {

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;
    //private ArrayList<Socket> clientSockets;

    private ActualRequest handleClient(ActualRequest clientCommand){
        // do stuff
        System.out.println("RECV: "+ clientCommand);        

        return new ActualRequest(Protocol.Request.PING_REPLY, clientCommand.getPayload() + " - has been read by the server");
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
            	System.out.println("Hello. I'm listening on one of these IP");
            	Enumeration e = NetworkInterface.getNetworkInterfaces();
            	while(e.hasMoreElements())
            	{
            	    NetworkInterface n = (NetworkInterface) e.nextElement();
            	    Enumeration ee = n.getInetAddresses();
            	    while (ee.hasMoreElements())
            	    {
            	        InetAddress i = (InetAddress) ee.nextElement();
            	        System.out.println(i.getHostAddress() + "-" + i.getCanonicalHostName());
            	    }
            	}
                clientSocket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                ActualRequest ar = ActualRequest.readCommand(reader);
                ActualRequest.writeCommand(writer, handleClient(ar));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
