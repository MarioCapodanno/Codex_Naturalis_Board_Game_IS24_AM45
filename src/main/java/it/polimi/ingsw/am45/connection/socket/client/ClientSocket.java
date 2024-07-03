package it.polimi.ingsw.am45.connection.socket.client;


import it.polimi.ingsw.am45.connection.ClientHandler;
import it.polimi.ingsw.am45.controller.client.cts.*;
import it.polimi.ingsw.am45.controller.server.stc.ServerToClient;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClientSocket implements Runnable, ClientHandler {
    private final Socket socket;
    private static ClientSocket instance;

    private ObjectOutputStream output;
    private final ObjectInputStream input;
    public String nickname;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;


    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        instance = this;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Singleton pattern method to get the instance of ClientSocket.
     * Returns the existing instance of the class.
     */
    public static synchronized ClientSocket getInstance() {
        return instance;
    }

    /**
     * Starts a new thread to continuously read incoming messages from the server.
     * Each received message is cast to ServerToClient and its update method is called.
     */
    @Override
    public void run() {
        //System.out.println("Received message: " + message);
        Thread readThread = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    Object object = input.readObject();
                    ServerToClient message = (ServerToClient) object;
                    //System.out.println("Received message: " + message);
                    message.update();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println();
            }
        });
        readThread.start();
    }

    /**
     * Sends a PongUpdate message to the server.
     * This is used to acknowledge a "ping" request from the server.
     */
    @Override
    public synchronized void sendPong() {
        sendObject(new PongUpdate());

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
     * No parameters are required.
     */
    @Override
    public String getNick() {
        return this.nickname;
    }

    /**
     * Sends a ClientToServer message to the server.
     * The message is sent using the TCP protocol.
     * @param message the message to send
     */
    @Override
    public synchronized void sendObject(ClientToServer message) {
        sendObjectTCP(message);


    }

    /**
     * Sends a NewGame message to the server.
     * The game ID and number of players are sent to the server.
     *
     * @param Gameid the game ID
     * @param playerNum the number of players
     * @param value the player's token color
     */
    @Override
    public void newGame(int Gameid, int playerNum, TokenColor value) {
        sendObject(new NewGame(Gameid, playerNum, value));
    }

    /**
     * Sends a JoinGame message to the server.
     * The game ID is sent to the server.
     *
     * @param Gameid the game ID
     * @param value the player's token color
     */
    @Override
    public void joinGame(int Gameid, TokenColor value) {
        sendObject(new JoinGame(Gameid, value));
    }

    /**
     * Sends a RequestUpdate message to the server.
     * The playing status is sent to the server.
     *
     * @param playing the playing status
     */
    @Override
    public void requestUpdate(Boolean playing) {
        sendObject(new RequestUpdate(playing));
    }

    /**
     * Sends a JoinUpdate message to the server.
     * No parameters are required.
     */
    @Override
    public void joinUpdate() {
        sendObject(new JoinUpdate());
    }

    /**
     * Sends a FlipCard message to the server.
     * The card position is sent to the server.
     *
     * @param position the card position
     */
    @Override
    public void flipCard(int position) {
        sendObject(new FlipCard(position));
    }

    /**
     * Sends a PlayCard message to the server.
     * The card index and coordinates are sent to the server.
     *
     * @param i the card index
     */
    @Override
    public void playCard(int i, int x, int y) {
        sendObject(new PlayCard(i, x, y));
    }

    /**
     * Sends a ChooseOBJ message to the server.
     * The card status is sent to the server.
     *
     * @param card the card status
     */
    @Override
    public void chooseOBJ(Boolean card) {
        sendObject(new ChooseOBJ(card));
    }

    /**
     * Sends a DrawCard message to the server.
     * The card index is sent to the server.
     *
     * @param card the card index
     */
    @Override
    public void drawCard(int card) {
        sendObject(new DrawCard(card));
    }

    /**
     * Notifies the server that the player connected to the chat.
     * The message to send is sent to the server.
     *
     * @param connectedToChat the message to send
     */
    @Override
    public void sendMessage(String connectedToChat) {
        sendObject(new SendMessage(connectedToChat));
    }

    /**
     * Sends a NewPlayer message to the server.
     * The player's nickname is sent to the server.
     *
     * @param nickname the player's nickname
     */
    @Override
    public void newPlayer(String nickname) {
        sendObject(new NewPlayer(nickname));
    }

    /**
     * Sends an object to the server using the TCP protocol.
     * @param message the message to send
     */
    public void sendObjectTCP(ClientToServer message) {
        try {
            output.writeObject(message);
            output.reset();
        } catch (IOException e) {
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(message);
                output.reset();
            } catch (IOException ignored) {
                System.out.println("Can't send object");
            }
        }
    }


}