package server.utility;

import common.util.ClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CancellationException;

public class ConnectionHandler implements Runnable{

    private Logger logger = LoggerFactory.getLogger("Connection Handler");
    private ServerManager server;
    private Socket clientSocket;
    private HandleRequest handleRequest;


    public ConnectionHandler(ServerManager server, Socket clientSocket, HandleRequest handleRequest){
        this.server = server;
        this.clientSocket = clientSocket;
        this.handleRequest = handleRequest;

    }
    /**
     * Main handling cycle.
     */
    @Override
    public void run() {
        ClientRequest userRequest = null;

        boolean stopFlag = false;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                try {
                    userRequest = (ClientRequest) clientReader.readObject();

                    Requester requester = new Requester(userRequest, handleRequest);
                    ClientRequest finalUserRequest = userRequest;
                    Runnable task = () -> {
                        requester.handleRequest(clientWriter);
                        logger.info("Request '" + finalUserRequest.getCommandName() + "' from " + clientSocket.getRemoteSocketAddress() + " client is completed and response is send back to client");
                    };

                    Thread thread = new Thread(task);
                    thread.start();
                    thread.join();


                } catch (IOException | ClassNotFoundException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }

                logger.debug("Active threads:" + Thread.activeCount());
            } while (!stopFlag);
        } catch (CancellationException exception) {
            logger.warn("A multithreading error occurred while processing the request");
        } catch (IOException exception) {

            logger.warn("Unexpected disconnection with main.client" + clientSocket.getInetAddress());
        }
    }


}
