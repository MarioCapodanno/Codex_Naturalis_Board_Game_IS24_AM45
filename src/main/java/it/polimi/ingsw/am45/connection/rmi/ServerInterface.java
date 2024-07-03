package it.polimi.ingsw.am45.connection.rmi;

import it.polimi.ingsw.am45.controller.client.cts.ClientToServer;
import it.polimi.ingsw.am45.enumeration.TokenColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The ServerInterface extends the Remote interface and provides methods for remote invocation from another Java virtual machine.
 * Any object that implements this interface can be a remote object.
 * This interface is used to define the methods that a client can invoke on the server.
 */
public interface ServerInterface extends Remote {

    /**
     * Registers a client with the server.
     * @param client The client to be registered.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void registerClient(ClientInterface client) throws RemoteException;

    /**
     * Sends an object from the client to the server.
     * @param client The client sending the object.
     * @param message The object to be sent.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void sendObject(ClientInterface client, ClientToServer message) throws RemoteException;

    /**
     * Creates a new game on the server.
     * @param client The client creating the game.
     * @param Gameid The ID of the game.
     * @param playerNum The number of players.
     * @param value The color of the token.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void newGame(ClientInterface client, int Gameid, int playerNum, TokenColor value) throws RemoteException;

    /**
     * Joins a game on the server.
     * @param client The client joining the game.
     * @param Gameid The ID of the game.
     * @param value The color of the token.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void joinGame(ClientInterface client, int Gameid, TokenColor value) throws RemoteException;

    /**
     * Requests an update from the server.
     * @param client The client requesting the update.
     * @param playing Whether the client is currently playing.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void requestUpdate(ClientInterface client, Boolean playing) throws RemoteException;

    /**
     * Updates the client after joining a game.
     * @param client The client to be updated.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void joinUpdate(ClientInterface client) throws RemoteException;

    /**
     * Flips a card in the game.
     * @param client The client flipping the card.
     * @param position The position of the card.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void flipCard(ClientInterface client, int position) throws RemoteException;

    /**
     * Plays a card in the game.
     * @param client The client playing the card.
     * @param i The index of the card.
     * @param x The x-coordinate of the card.
     * @param y The y-coordinate of the card.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void playCard(ClientInterface client, int i, int x, int y) throws RemoteException;

    /**
     * Chooses an objective card in the game.
     * @param client The client choosing the card.
     * @param card Whether the card is chosen.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void chooseOBJ(ClientInterface client, Boolean card) throws RemoteException;

    /**
     * Draws a card in the game.
     * @param client The client drawing the card.
     * @param card The index of the card.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void drawCard(ClientInterface client, int card) throws RemoteException;

    /**
     * Sends a message to the chat in the game.
     * @param client The client sending the message.
     * @param connectedToChat The message to be sent.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void sendMessage(ClientInterface client, String connectedToChat) throws RemoteException;

    /**
     * Adds a new player to the game.
     * @param client The client adding the new player.
     * @param nickname The nickname of the new player.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void newPlayer(ClientInterface client, String nickname) throws RemoteException;

    /**
     * Sends a ping to the server.
     * @param client The client sending the ping.
     * @throws RemoteException If an error occurs during the remote method call.
     */
    void sendPong(ClientInterface client) throws RemoteException;
}