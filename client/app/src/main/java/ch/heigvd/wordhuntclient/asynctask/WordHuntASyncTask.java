package ch.heigvd.wordhuntclient.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import ch.heigvd.wordhuntclient.activities.IWHView;
import ch.heigvd.wordhuntclient.activities.MainActivity;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;


/**
 * <code>WordHuntASyncTask</code>
 *
 * @author Valentin MINDER
 *
 */

public class WordHuntASyncTask extends AsyncTask<WHMessage, Object, WHMessage> {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    final String charset = "UTF-8";
    private IWHView caller;

    private Logger logger = Logger.getLogger(getClass().getName());

    public WordHuntASyncTask(IWHView caller) {
        this.caller = caller;
    }

    private void init() throws UnknownHostException, IOException {
        logger.info("Initialization of IO task.");

        String dstAddress = MainActivity.preferences.getString(
                MainActivity.prefLastIP, "localhost");
        int port = MainActivity.preferences.getInt("SERVER_PORT", 1234);
        socket = new Socket(dstAddress, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),
                charset));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                charset));
    }

    @Override
    protected void onPostExecute(WHMessage reply) {
        logger.info("IOTask recieved a reply");
        logger.finest(reply.toString());
        caller.reply(reply);
    }

    /**
     * /!\ ONLY FIRST OBJECT IS SENT <br>
     * ONE QUERY <=> ONE REPLY.
     */
    @Override
    protected WHMessage doInBackground(WHMessage... params) {
        if (null == socket) {
            try {
                init();
            } catch (UnknownHostException e) {
                logger.severe("FAIL IOTASK - host not found");
                e.printStackTrace();
                return new WHMessage(WHProtocol.WHMessageHeader.NETWORK_ERROR,
                        "Network error.");
            } catch (IOException e) {
                logger.warning("IO error in IOTask INIT");
                e.printStackTrace();
                return new WHMessage(WHProtocol.WHMessageHeader.NETWORK_ERROR,
                        "Network error.");
            }
        }
        logger.info("IOTask sending data.");
        // only first object is sent !!!
        if (params.length > 1) {
            return new WHMessage(WHProtocol.WHMessageHeader.BAD_REQUEST_400,
                    "Too many arguments provided: max one query at a time.");
        }
        for (WHMessage object : params) {
            logger.finest("Sending:" + object.toString());
            boolean result = object.writeMessage(out);
            if (result) {
                try {
                    WHMessage reply = WHMessage.readMessage(in);
                    if(reply == null) {
                        Log.d("Reply", "NULL reply");
                    } else {
                        Log.d("Reply", "reply ok");
                    }
                    logger.fine("IOTask recieved a reply.");
                    logger.finest("Recieving:" + reply.toString());
                    return reply;
                } catch (IOException e) {
                    logger.warning("IO error in IOTask - when reading.");
                    e.printStackTrace();
                }

            } else {
                logger.warning("error in IOTask - when sending: "
                        + object.toString());
            }
            return new WHMessage(WHProtocol.WHMessageHeader.NETWORK_ERROR,
                    "Network error.");
        }
        return new WHMessage(WHProtocol.WHMessageHeader.BAD_REQUEST_400,
                "No arguments provided");
    }
}