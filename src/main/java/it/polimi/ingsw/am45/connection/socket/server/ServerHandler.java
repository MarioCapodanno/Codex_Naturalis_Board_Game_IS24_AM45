package it.polimi.ingsw.am45.connection.socket.server;

import it.polimi.ingsw.am45.connection.rmi.ClientInterface;
import it.polimi.ingsw.am45.controller.GamesController;
import it.polimi.ingsw.am45.controller.client.cts.ClientToServer;
import it.polimi.ingsw.am45.controller.server.stc.PingUpdate;
import it.polimi.ingsw.am45.controller.server.stc.ServerToClient;
import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.model.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

/**
 * This class handles the server-side logic for a client connection.
 */
public class ServerHandler implements Runnable {
    private java.net.Socket clientSocket;
    private final Server server;
    private long lastPing;
    public boolean connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    public boolean pong = false;
    private String nickname;
    private int gameID;
    private Game game;
    public Boolean inGame = false;
    private final ConnectionType connectionType;
    public ClientInterface clientInterface;

    /**
     * Constructor for a TCP connection.
     *
     * @param socket The client's socket.
     * @param server The server instance.
     * @throws IOException If an I/O error occurs when creating the object streams.
     */
    public ServerHandler(java.net.Socket socket, Server server) throws IOException {
        this.connection = true;
        this.clientSocket = socket;
        this.server = server;
        this.connectionType = ConnectionType.TCP;
        this.game = null;
        this.lastPing = 0;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Constructor for an RMI connection.
     *
     * @param client The client's RMI interface.
     * @param server The server instance.
     */
    public ServerHandler(ClientInterface client, Server server) {
        this.server = server;
        this.connection = true;
        this.clientInterface = client;
        this.connectionType = ConnectionType.RMI;
        this.game = null;
        this.lastPing = 0;
    }

    /**
     * The main loop for handling messages from the client.
     */
    @Override
    public void run() {
        try {
            handleMessages();
        } catch (IOException e) {
            try {
                disconnect();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Disconnects the client and cleans up resources.
     *
     * @throws IOException If an I/O error occurs when closing the socket or streams.
     */
    public void disconnect() throws IOException {
        if (game != null) {
            GamesController.getInstance().onDisconnect(this, game);
        }
        game = null;
        clientSocket.close();
        server.clients.remove(this);
        output.close();
        connection = false;
    }

    /**
     * Handles incoming messages from the client.
     *
     * @throws IOException            If an I/O error occurs when reading the object.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private void handleMessages() throws IOException, ClassNotFoundException {
        while (connection) {
            Object object = input.readObject();
            ClientToServer message = (ClientToServer) object;
            message.setServerSocket(this);
            message.setGameId(gameID);
            message.setGame(game);
            message.setNickname(nickname);
            GamesController.getInstance().onCommandReceived(message);
        }
    }

    /**
     * Handles incoming RMI messages from the client.
     *
     * @param message The incoming message.
     */
    public void handleMessagesRMI(ClientToServer message) {
        message.setServerSocket(this);
        message.setGameId(gameID);
        message.setGame(game);
        message.setNickname(nickname);
        GamesController.getInstance().onCommandReceived(message);
    }

    /**
     * Sends a message to the client.
     *
     * @param message The message to send.
     */
    public synchronized void sendMessage(ServerToClient message) {
        if (connectionType == ConnectionType.TCP)
            sendMessageTCP(message);
    }

    /**
     * Sends a TCP message to the client.
     *
     * @param message The message to send.
     */
    public void sendMessageTCP(ServerToClient message) {
        try {
            output.writeObject(message);
            output.reset();
        } catch (IOException e) {
            try {
                output.close();
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                output.writeObject(message);
                output.reset();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Sends an RMI message to the client.
     *
     * @param message The message to send.
     */
    public synchronized void sendMessageRMI(ServerToClient message) {
        if(connection){
        try {
            this.clientInterface.writeObject(message);
        } catch (RemoteException e) {
            if (game != null) {
                GamesController.getInstance().onDisconnect(this, game);
            }
            game = null;
            connection = false;
            }
        }
    }

    /**
     * Updates the pong status when a pong is received from the client.
     */
    public void pongReceived() {
        pong = true;
        server.receivedPong(this);
    }

    /**
     * Sets the nickname for the client and sends a ping update to all clients.
     * @param nickname The nickname to be set.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        server.sendToAllClients(new PingUpdate());
    }

    /**
     * Sets the game ID for the client.
     * @param gameID The game ID to be set.
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Sets the game for the client.
     * @param game The game to be set.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Returns the nickname of the client.
     * No parameters are required.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the game ID of the client.
     * No parameters are required.
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Returns the game of the client.
     * No parameters are required.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the last ping time for the client.
     * @param ping The ping time to be set.
     */
    public void setPing(long ping) {
        this.lastPing = ping;
    }

    /**
     * Returns the last ping time of the client.
     * No parameters are required.
     */
    public long getLastPing() {
        return lastPing;
    }

    /**
     * Returns the connection type of the client.
     * No parameters are required.
     */
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    /**
     * Sets the output stream for the client.
     * @param output The output stream to be set.
     */
    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    /**
     * Sets the input stream for the client.
     * @param input The input stream to be set.
     */
    public void setInput(ObjectInputStream input) {
        this.input = input;
    }
}