package client;

import client.utility.AuthHandler;
import client.utility.ClientManager;
import client.utility.Handler;
import client.utility.UserIO;
import common.exceptions.NotDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.Console;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Client application class. Creates all instances and runs the program.
 *
 * @author Кобзарь Мария P3115
 */

public class Client {

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;

    private static boolean initialize(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongAmountOfElementsException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(Client.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Console.println("Used: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Console.printerror("Port has to be a number");
        } catch (NotDeclaredLimitsException exception) {
            Console.printerror("Port cannot be negative number");
        }
        return false;
    }

    public static void main(String[] args){
        if (!initialize(args)) return;

        Scanner userScanner = new Scanner(System.in);
        var userIO = new UserIO(new Scanner(System.in), new PrintWriter(System.out));

        Handler handler = new Handler(userScanner, userIO);
        AuthHandler authHandler = new AuthHandler(userScanner);
        ClientManager clientManager = new ClientManager(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, handler, authHandler);
        clientManager.run();
        userScanner.close();

    }
}
