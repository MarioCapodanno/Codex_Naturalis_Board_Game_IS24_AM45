package it.polimi.ingsw.am45.connection.socket.server;

import it.polimi.ingsw.am45.connection.rmi.ClientInterface;
import it.polimi.ingsw.am45.connection.rmi.RMIServer;
import it.polimi.ingsw.am45.controller.GamesController;
import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.controller.server.stc.PingUpdate;
import it.polimi.ingsw.am45.controller.server.stc.ServerToClient;
import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.enumeration.Messages;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The Server class is responsible for managing the server-side of the client-server architecture.
 * It handles client connections, sends messages to all clients, and processes received pong messages.
 */
public class Server {
    public final List<ServerHandler> clients;
    private java.net.ServerSocket serverSocket;
    private final GamesController gamesController;
    private long sendTime;
    private static Server instance;

    /**
     * Constructs a new Server with the specified port number.
     *
     * @param port the port number for the server to be created.
     * @param server the RMIServer instance to which the server will be added.
     */
    public Server(int port, RMIServer server) {
        server.addServer(this);
        this.gamesController = GamesController.getInstance();
        this.clients = new ArrayList<>();
        try {
            this.serverSocket = new java.net.ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.out.println("Error creating server socket: " + e.getMessage());
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server is shutting down...");

            sendToAllClients(new MessageUpdate(Messages.SERVERDOWN));
        }));
    }

    /**
     * Starts the server and listens for incoming client connections.
     * It also schedules a task to send PingUpdate messages to all clients every 10 seconds.
     *
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    public void startServer() throws IOException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> sendToAllClients(new PingUpdate());
        executorService.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);


        while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            try {
                ServerHandler client = new ServerHandler(socket, this);
                clients.add(client);
                new Thread(client, "socket_server" + socket.getInetAddress()).start();
            } catch (StreamCorruptedException e) {
                System.out.println("RMI?");
            }
        }
    }

    /**
     * Sends a message to all connected clients by calling the sendMessage method on each client.
     *
     * @param message the message to be sent to all clients. It must be a subclass of ServerToClient.
     */
    public void sendToAllClients(ServerToClient message) {
        sendTime = System.currentTimeMillis();
        for (ServerHandler client : clients) {
            if ((!client.getNickname().isEmpty())&&(client.connection)) {
                if (client.getConnectionType() == ConnectionType.RMI) {
                    client.sendMessageRMI(message);
                } else if (client.getConnectionType() == ConnectionType.TCP) {
                    client.sendMessage(message);

                }
                client.pong = false;
            }

        }
    }

    /**
     * Processes received pong messages from a client.
     * It updates the ping time for the client and requests a playing update if the client is in a game.
     *
     * @param client the client from which the pong message was received.
     */
    public synchronized void receivedPong(ServerHandler client) {
        client.setPing(System.currentTimeMillis() - sendTime);
        if (client.getGame() != null) {
            gamesController.requestPlayingUpdate(client.getGameID(), client);
        }
    }

    /**
     * Returns the instance of the GamesController.
     *
     * @return the instance of the GamesController.
     */
    public static Server getInstance() {
        return instance;
    }

    public ServerHandler addClient(ClientInterface client) {
        ServerHandler serverclient = new ServerHandler(client, this);
        clients.add(serverclient);
        return serverclient;
    }

}