package it.polimi.ingsw.am45.view.modelview;

import it.polimi.ingsw.am45.controller.server.stc.BoardsUpdate;
import it.polimi.ingsw.am45.controller.server.stc.ChatUpdate;
import it.polimi.ingsw.am45.utilities.CardData;
import it.polimi.ingsw.am45.view.listener.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * The Boards class is a singleton that manages the game boards and chat messages.
 * It maintains a list of listeners that are notified when the boards or chat messages are updated.
 */
public class Boards {
    private static Boards instance;
    protected final List<Listener> listeners = new Stack<>();
    private HashMap<String, Stack<CardData>> boards = new HashMap<>();
    private Stack<String> messages = new Stack<>();

    /**
     * Returns the singleton instance of the Boards class.
     * If the instance does not exist, it is created.
     */
    public static Boards getInstance() {
        if (instance == null) instance = new Boards();
        return instance;
    }

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private Boards() {
    }

    /**
     * Registers a listener that will be notified when the boards or chat messages are updated.
     */
    public void registerObserver(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener so that it will no longer be notified when the boards or chat messages are updated.
     */
    public void removeObserver(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the boards have been updated.
     */
    public void notifyObserversBoards() {
        for (Listener listener : listeners) {
            listener.updatePlayers();
        }
    }

    /**
     * Notifies all registered listeners that the chat messages have been updated.
     */
    public void notifyObserversChat() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    /**
     * Updates the boards with the data from a BoardsUpdate and notifies all registered listeners.
     */
    public void updateBoards(BoardsUpdate boardsUpdate) {
        this.boards = boardsUpdate.getBoards();
        notifyObserversBoards();
    }

    /**
     * Updates the chat messages with the data from a ChatUpdate and notifies all registered listeners.
     */
    public void updateChat(ChatUpdate chatUpdate) {
        this.messages = chatUpdate.getMessages();
        notifyObserversChat();
    }

    /**
     * Returns the current state of the boards.
     */
    public HashMap<String, Stack<CardData>> getBoards() {
        return boards;
    }

    /**
     * Returns the current chat messages.
     */
    public Stack<String> getMessages() {
        return messages;
    }

    /**
     * Prints the board of a specific player to the terminal user interface (TUI).
     */
    public ArrayList<String> printBoardTui(ArrayList<String> output, String playerNickname) {
        for (String player : boards.keySet()) {
            if (player.equals(playerNickname)) {
                output.add("     BOARD    ");
                Stack<CardData> board = boards.get(player);
                for (CardData card : board) {
                    output.add("Card" + card.getPath() + " in location: " + card.getLocation().getX() + " " + card.getLocation().getY());
                }
            }
        }
        return output;
    }
}