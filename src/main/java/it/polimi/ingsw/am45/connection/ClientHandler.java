package it.polimi.ingsw.am45.connection;

import it.polimi.ingsw.am45.controller.client.cts.ClientToServer;
import it.polimi.ingsw.am45.enumeration.TokenColor;

/**
 * The ClientHandler interface defines the methods for handling client-side operations.
 * It is implemented by classes that handle client-side operations such as sending messages, joining games, updating requests, flipping cards, playing cards, choosing objectives, drawing cards, sending messages, adding new players, and sending pings.
 */
public interface ClientHandler {

    /**
     * Sends an object to the server.
     * @param message The object to be sent.
     */
    void sendObject(ClientToServer message);

    /**
     * Creates a new game.
     * @param Gameid The ID of the game.
     * @param playerNum The number of players.
     * @param value The color of the token.
     */
    void newGame(int Gameid, int playerNum, TokenColor value);

    /**
     * Joins a game.
     * @param Gameid The ID of the game.
     * @param value The color of the token.
     */
    void joinGame(int Gameid, TokenColor value);

    /**
     * Requests an update from the server.
     * @param playing Whether the client is currently playing.
     */
    void requestUpdate(Boolean playing);

    /**
     * Updates the client after joining a game.
     */
    void joinUpdate();

    /**
     * Flips a card.
     * @param position The position of the card.
     */
    void flipCard(int position);

    /**
     * Plays a card.
     * @param i The index of the card.
     * @param x The x-coordinate of the card.
     * @param y The y-coordinate of the card.
     */
    void playCard(int i, int x, int y);

    /**
     * Chooses an objective card.
     * @param card Whether the card is chosen.
     */
    void chooseOBJ(Boolean card);

    /**
     * Draws a card.
     * @param card The index of the card.
     */
    void drawCard(int card);

    /**
     * Sends a message to the chat.
     * @param connectedToChat The message to be sent.
     */
    void sendMessage(String connectedToChat);

    /**
     * Adds a new player to the game.
     * @param nickname The nickname of the new player.
     */
    void newPlayer(String nickname);

    /**
     * Sends a ping to the server.
     */
    void sendPong();

    /**
     * Returns the nickname of the client.
     * @return The nickname of the client.
     */
    String getNick();
}