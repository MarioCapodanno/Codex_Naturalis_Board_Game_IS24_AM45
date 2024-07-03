package it.polimi.ingsw.am45.connection.rmi;

import it.polimi.ingsw.am45.connection.ClientHandler;
import it.polimi.ingsw.am45.controller.client.cts.ClientToServer;
import it.polimi.ingsw.am45.controller.server.stc.*;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.listener.GameListener;
import it.polimi.ingsw.am45.utilities.CardData;
import it.polimi.ingsw.am45.view.modelview.*;
import javafx.application.Platform;

import java.net.NoRouteToHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * The RMIClient class is the client-side implementation of the RMI connection.
 * It extends UnicastRemoteObject and implements the ClientInterface and ClientHandler interfaces.
 * It is used to connect to the server, send and receive messages, and update the client.
 */
public class RMIClient extends UnicastRemoteObject implements ClientInterface, ClientHandler {
    static int PORT = 1234;
    static String SERVER_ADDRESS;
    private static final String TEST = "test";
    private static ServerInterface stub;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private GameListener listener;
    private static RMIClient instance;
    public String nickname;

    public RMIClient(String address) throws RemoteException {
        super();
        SERVER_ADDRESS = address;
        instance = this;
        if(!address.equals(TEST))
            connect();
    }


    public RMIClient(String address, int port) throws RemoteException {
        super();
        SERVER_ADDRESS = address;
        PORT = port;
        instance = this;
        if(!address.equals(TEST))
            connect();
    }

    /**
     * Connects to the server using RMI.
     * Retrieves the registry, looks up the ServerInterface, and registers the client.
     */
    public void connect() {

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(SERVER_ADDRESS, PORT);
            stub = (ServerInterface) registry.lookup("ServerInterface");
            stub.registerClient(this);
            System.out.println("Client logged: ");
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Client exception: " + e);
        }
    }

    /**
     * Singleton pattern method to get the instance of RMIClient.
     * If the instance is null, it creates a new one; otherwise, it returns the existing instance.
     * @return the instance of RMIClient
     * @throws RemoteException if there's an issue with the remote connection
     */
    public static RMIClient getInstance() throws RemoteException {
        if (instance == null) {
            instance = new RMIClient(SERVER_ADDRESS);
        }
        return instance;
    }

    /**
     * Receives a ServerToClient message and updates the client.
     * This method is synchronized to avoid concurrent modification issues.
     *
     * @param message the message to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public synchronized void writeObject(ServerToClient message) throws RemoteException {
        message.update();
    }

    /**
     * Updates the client with a new message from the server.
     * The message is added to the Msg instance.
     *
     * @param message the message to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void MessageUpdate(Messages message) throws RemoteException {
        System.out.println("Message received: "+ message);
        Msg.getInstance().putMessage(new MessageUpdate(message));
    }

    /**
     * Updates the client with the current list of players.
     * The players are added to the Msg instance.
     *
     * @param players the list of players to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void PlayersUpdate(Stack<String> players) throws RemoteException {
        Msg.getInstance().updateNicknames(new PlayersUpdate(players));
    }

    /**
     * Updates the client's hand with the given list of cards.
     * The hand is updated in the Hand instance.
     *
     * @param hand the list of cards to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void HandUpdate(List<String> hand) throws RemoteException {
        Hand.getInstance().updateHands(new HandUpdate(hand));
    }

    /**
     * Updates the client's hand with the given choices.
     * The choices are updated in the Hand instance.
     *
     * @param choice the choice to update the client with
     * @param obj1 the first object to update the client with
     * @param obj2 the second object to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void ChoosingUpdate(String choice, String obj1, String obj2) throws RemoteException {
        Hand.getInstance().updateHands(new ChoosingUpdate(choice, obj1, obj2));
    }

    /**
     * Updates the client's resources with the given list of resources.
     * The resources are updated in the Market instance.
     *
     * @param resource the list of resources to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void ResourceUpdate(List<Integer> resource) throws RemoteException {
        Market.getInstance().updateMarket( new ResourceUpdate(resource));
    }

    /**
     * Updates the client's game board with the given list of cards.
     * The game board is updated in the Market instance.
     *
     * @param gameboard the list of cards to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void MarketUpdate(List<String> gameboard) throws RemoteException {
        Market.getInstance().updateMarket(new MarketUpdate(gameboard));
    }

    /**
     * Updates the client's list of players with the given list of players, pings, and points.
     * The players are updated in the Players instance.
     *
     * @param players the list of players to update the client with
     * @param pings the list of pings to update the client with
     * @param points the list of points to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void PlayersUpdate(Stack<String> players, Stack<Long> pings, Stack<Integer> points) throws RemoteException {
        Msg.getInstance().updateNicknames(new PlayersUpdate(players, pings, points));
    }

    /**
     * Updates the client's boards with the given map of boards.
     * The boards are updated in the Boards instance.
     *
     * @param boardsMap the map of boards to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void boardsUpdate(HashMap<String, Stack<CardData>> boardsMap) throws RemoteException {
        Boards.getInstance().updateBoards(new BoardsUpdate(boardsMap));
    }

    /**
     * Updates the client with the given list of winners.
     * The winners are updated in the End instance.
     *
     * @param winnerPlayers the list of winners to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void winnerUpdate(List<String> winnerPlayers) throws RemoteException {
        End.getInstance().updateWinners(new WinnerUpdate(winnerPlayers));
    }

    /**
     * Updates the client with the given nickname.
     * The nickname is updated in the Turn instance.
     *
     * @param nickname the nickname to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void turnsUpdate(String nickname) throws RemoteException {
        Turn.getInstance().updateTurns(new TurnsUpdate(nickname));

    }

    /**
     * Updates the client's chat with the given list of messages.
     * The messages are updated in the Chat instance.
     *
     * @param messages the list of messages to update the client with
     * @throws RemoteException if there's an issue with the remote connection
     */
    @Override
    public void chatUpdate(Stack<String> messages) throws RemoteException {
        Boards.getInstance().updateChat(new ChatUpdate(messages));
    }

    /**
     * Sends a ClientToServer message to the server.
     * This method is synchronized to avoid concurrent modification issues.
     *
     * @param message the message to send to the server
     */
    @Override
    public synchronized void sendObject(ClientToServer message) {
        try {
            stub.sendObject(this, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to create a new game.
     * The game ID and number of players are sent to the server.
     *
     * @param Gameid the game ID
     * @param playerNum the number of players
     * @param value the token color
     */
    @Override
    public synchronized void newGame(int Gameid, int playerNum, TokenColor value) {
        try {
            stub.newGame(this, Gameid, playerNum, value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to join a game.
     * The game ID is sent to the server.
     *
     * @param Gameid the game ID
     * @param value the token color
     */
    @Override
    public synchronized void joinGame(int Gameid, TokenColor value) {
        try {
            stub.joinGame(this, Gameid, value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server for an update.
     * The playing status is sent to the server.
     *
     * @param playing the playing status
     */
    @Override
    public void requestUpdate(Boolean playing) {
        try {
            stub.requestUpdate(this, playing);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server for a join update.
     * No parameters are sent to the server.
     */
    @Override
    public void joinUpdate() {
        try {
            stub.joinUpdate(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to flip a card.
     * The position of the card is sent to the server.
     *
     * @param position the position of the card
     */
    @Override
    public void flipCard(int position) {
        try {
            stub.flipCard(this, position);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to play a card.
     * The card index and its x and y coordinates are sent to the server.
     *
     * @param i the card index
     * @param x the x coordinate
     * @param y the y coordinate
     */
    @Override
    public void playCard(int i, int x, int y) {
        try {
            stub.playCard(this, i, x, y);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to choose an object.
     * The card status is sent to the server.
     *
     * @param card the card status
     */
    @Override
    public void chooseOBJ(Boolean card) {
        try {
            stub.chooseOBJ(this, card);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to draw a card.
     * The card index is sent to the server.
     *
     * @param card the card index
     */
    @Override
    public void drawCard(int card) {
        try {
            stub.drawCard(this, card);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    /**
     * Sends a request to the server to send a message.
     * The message is sent to the server.
     *
     * @param connectedToChat the message to send
     */
    @Override
    public void sendMessage(String connectedToChat) {
        try {
            stub.sendMessage(this, connectedToChat);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to create a new player.
     * The nickname is sent to the server.
     *
     * @param nickname the nickname of the player
     */
    @Override
    public void newPlayer(String nickname) {
        try {
            stub.newPlayer(this, nickname);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to send a pong.
     * No parameters are sent to the server.
     */
    @Override
    public void sendPong() {
        try {
            stub.sendPong(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }

        Runnable task = () -> {
            Platform.exit();
            System.out.println("10 seconds have passed since the last Ping");
        };
        scheduledFuture = scheduler.schedule(task, 10, TimeUnit.SECONDS);

    }

    /**
     * Returns the client's nickname.
     */
    @Override
    public String getNick() {
        return this.nickname;
    }


    //Example of using method from ServerInterface
    /*
     public void createGame(args) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(IP_RMI, PORT_RMI);
        stub = (ServerInterface) registry.lookup("ServerInterface");
        stub.createGame(args);
        nickname = nick;
    }
     **/

}