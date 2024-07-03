package it.polimi.ingsw.am45.connection.rmi;

import it.polimi.ingsw.am45.connection.socket.server.Server;
import it.polimi.ingsw.am45.connection.socket.server.ServerHandler;
import it.polimi.ingsw.am45.controller.GamesController;
import it.polimi.ingsw.am45.controller.client.cts.ClientToServer;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.model.Game;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * RMI Server class that manages the connection between the server and the client through the RMI protocol.
 */
public class RMIServer extends UnicastRemoteObject implements ServerInterface {
    private static RMIServer remoteServer;
    private Server server;

    private static Registry registry;
    private final HashMap<ClientInterface, ServerHandler> clienthandlers = new HashMap<>();


    public RMIServer() throws RemoteException {
        super();
    }

    /**
     * This method creates a new RMI server and binds it to the registry.
     *
     * @return the RMI server
     */
    public static RMIServer connect(String ipAddress, int port) {
        try {
            if (ipAddress.contains("localhost"))
                System.setProperty("java.rmi.server.hostname", "localhost");
            else System.setProperty("java.rmi.server.hostname", ipAddress);

            remoteServer = new RMIServer();

            registry = LocateRegistry.createRegistry(port);
            getRegistry().bind("ServerInterface", remoteServer);
            System.out.println("RMI Server ready on port " + port + " and IP " + System.getProperty("java.rmi.server.hostname"));
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Server exception: " + e);
        }
        return getServer();
    }

    /**
     * This method returns the registry.
     *
     * @return the registry
     * @throws RemoteException if the registry is not found
     */
    public synchronized static Registry getRegistry() throws RemoteException {
        return registry;
    }

    /**
     * This method returns the RMI server.
     *
     * @return the RMI server
     */
    public synchronized static RMIServer getServer() {
        if (remoteServer == null) {
            try {
                remoteServer = new RMIServer();
            } catch (RemoteException e) {
                System.err.println("Server exception: " + e);
            }
        }
        return remoteServer;
    }


    /**
     * This method registers a new client to the server.
     *
     * @param client the client to register
     * @throws RemoteException if the client is not found
     */
    @Override
    public void registerClient(ClientInterface client) throws RemoteException {
        clienthandlers.put(client, server.addClient(client));
        System.out.println("Client registered");

    }

    /**
     * This method sends an object to the client.
     *
     * @param client  the client to send the object to
     * @param message the message to send
     * @throws RemoteException if the client is not found
     */
    @Override
    public void sendObject(ClientInterface client, ClientToServer message) throws RemoteException {
        if (clienthandlers.containsKey(client))
            clienthandlers.get(client).handleMessagesRMI(message);
    }

    /**
     * This method creates a new game.
     *
     * @param client    the client that creates the game
     * @param GameId    the game ID
     * @param playerNum the number of players
     * @param value
     * @throws RemoteException if the client is not found
     */
    @Override
    public void newGame(ClientInterface client, int GameId, int playerNum, TokenColor value) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        Game game = GamesController.getInstance().createGame(GameId, playerNum, serverHandler.getNickname(), value, serverHandler);
        if (game != null) {
            serverHandler.setGameID(GameId);
            serverHandler.setGame(game);
            System.out.println("Game created by" + client);
            client.MessageUpdate(Messages.CREATED);
        } else {
            client.MessageUpdate(Messages.ALREADYEXIST);
        }
    }

    /**
     * This method joins a game.
     *
     * @param client the client that joins the game in.
     * @param Gameid the game ID of the game to join in.
     * @param value the token color of the client that joins the game.
     * @throws RemoteException if the client is not found
     */
    @Override
    public void joinGame(ClientInterface client, int Gameid, TokenColor value) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        GamesController.getInstance().joinGame(Gameid, serverHandler.getNickname(), value,serverHandler);

    }

    /**
     * This method requests an update.
     *
     * @param client  the client that requests the update
     * @param playing if the client is playing
     * @throws RemoteException if the client is not found
     */
    @Override
    public void requestUpdate(ClientInterface client, Boolean playing) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        if (!playing)
            GamesController.getInstance().requestUpdate(serverHandler.getGameID());
    }

    /**
     * This method updates the client.
     *
     * @param client the client to update
     * @throws RemoteException if the client is not found
     */
    @Override
    public void joinUpdate(ClientInterface client) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        serverHandler.inGame = true;
        GamesController.getInstance().startGame(serverHandler.getGameID());
    }

    /**
     * This method flips a card.
     *
     * @param client   the client that flips the card
     * @param position the position of the card
     * @throws RemoteException if the client is not found
     */
    @Override
    public void flipCard(ClientInterface client, int position) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        GamesController.getInstance().flipCard(position, serverHandler);
    }

    /**
     * This method allows a client to play a card at a specific position on the game board.
     *  It retrieves the ServerHandler for the client and calls the playCard method in the GamesController.
     *  @param client the client that plays the card
     */
    @Override
    public void playCard(ClientInterface client, int position, int x, int y) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        GamesController.getInstance().playCard(position, x, y, serverHandler);
    }

    /**
     * This method allows a client to choose an objective card in the game.
     * // It retrieves the ServerHandler for the client and calls the chooseObj method in the GamesController.
     * @param client the client that chooses the objective card
     * @param card the objective card to choose
     */

    @Override
    public void chooseOBJ(ClientInterface client, Boolean card) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        GamesController.getInstance().chooseObj(card, serverHandler);

    }

    /**
     * This method allows a client to draw a card.
     * It retrieves the ServerHandler for the client and calls the drawCard method in the GamesController.
     * @param client the client that draws the card
     * @param card the card to draw
     */
    @Override
    public void drawCard(ClientInterface client, int card) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        GamesController.getInstance().drawCard(card, serverHandler);
    }


    /**
     * This method allows a client to send a message.
     * It retrieves the ServerHandler for the client and calls the sendMessage method in the GamesController.
     * @param client the client that sends the message
     * @param message the message to send
     */
    @Override
    public void sendMessage(ClientInterface client, String message) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        GamesController.getInstance().sendMessage(message, serverHandler);
    }

    /**
     * This method assigns a new nickname to a client in the game.
     * It retrieves the ServerHandler for the client and sets the nickname.
     *
     * @param client the client that leaves the game
     */
    @Override
    public void newPlayer(ClientInterface client, String nickname) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        serverHandler.setNickname(nickname);
    }


    /**
     * This method sends a "pong" response to a client to acknowledge a "ping" request.
     * It retrieves the ServerHandler for the client and calls the pongReceived method.
     *
     * @param client the client that sent the "ping" request
     */

    @Override
    public void sendPong(ClientInterface client) throws RemoteException {
        ServerHandler serverHandler = clienthandlers.get(client);
        serverHandler.pongReceived();
    }


    /**
     *     This method assigns a Server instance to the 'server' field of the RMIServer class.
     *     It is used to set up the server that the RMIServer will interact with.
     *
     *     @param server the Server instance to assign to the 'server' field
     */
    public void addServer(Server server) {
        this.server = server;
    }
}
