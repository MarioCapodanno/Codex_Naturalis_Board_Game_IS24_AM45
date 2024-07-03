package it.polimi.ingsw.am45;

import it.polimi.ingsw.am45.connection.rmi.RMIServer;
import it.polimi.ingsw.am45.connection.socket.server.Server;
import it.polimi.ingsw.am45.utilities.InputConnectionValidator;

import java.io.IOException;

/**
 * The ServerMain class is the entry point of the server application.
 * It provides a command line interface for the user to select the type of connection (TCP or RMI) and the IP address.
 */
public class ServerMain {

    /**
     * The main method of the server application.
     * It provides a command line interface for the user to select the type of connection (TCP or RMI) and the IP address.
     * If the user does not make a valid selection, the application defaults to localhost and specific ports for TCP and RMI.
     */
    public static void main(String[] args) throws IOException {

        String serverPortRMI = "1234";
        String serverPortTCP = "28888";
        String ipAddress = "localhost"; // default IP address

        //check if user ha provided a port number using parametrized execution with -p flag
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-pRMI":
                    if (i + 1 < args.length) {
                        serverPortRMI = args[i + 1];
                    }
                    break;
                case "-pTCP":
                    if (i + 1 < args.length) {
                        serverPortTCP = args[i + 1];
                    }
                    break;
                case "-ip":
                    if (i + 1 < args.length) {
                        ipAddress = args[i + 1];
                    }
                    break;
            }
        }

        // Validate IP address
        if (!InputConnectionValidator.validate(ipAddress)) {
            System.out.println("Invalid IP address, using default IP address (localhost)...");
            ipAddress = "localhost";
        }

        // Bind the server to the port number provided by the user with RMI protocol.
        RMIServer.connect(ipAddress, Integer.parseInt(serverPortRMI));

        Server server = new Server(Integer.parseInt(serverPortTCP), RMIServer.getServer());
        // Start the server on the port number provided by the user with TCP protocol.
        server.startServer();

    }

}