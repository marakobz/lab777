package server.utility;

import common.util.ClientRequest;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


public class Requester{
    private Logger logger = LoggerFactory.getLogger("Requester");
    private ClientRequest userRequest;
    private HandleRequest handleRequest;
    private ServerResponse responseToUser;

    public Requester(ClientRequest userRequest,
                     HandleRequest handleRequest
    ) {
        this.userRequest = userRequest;
        this.handleRequest = handleRequest;

    }

    public void handleRequest(ObjectOutputStream objectOutputStream) {
        try {

            responseToUser = handleRequest.compute(userRequest);
            logger.debug("Request '" + userRequest.getCommandName() + "' is completed.");

            objectOutputStream.writeObject(responseToUser);
            logger.debug("Response on request '" + userRequest.getCommandName() + "' is send.");
            objectOutputStream.flush();


        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
