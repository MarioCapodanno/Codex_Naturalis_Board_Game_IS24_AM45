package it.polimi.ingsw.am45.view.modelview;

import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.controller.server.stc.PlayersUpdate;
import it.polimi.ingsw.am45.view.listener.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class represents a message in the game.
 * It uses the Singleton design pattern to ensure that only one instance of the message exists.
 */
public class Msg {
    private static Msg instance;

    protected final List<Listener> listeners = new Stack<>();
    private final Stack<MessageUpdate> messages = new Stack<>();
    private Stack<String> nicknames = new Stack<>();
    private Stack<Long> pings = new Stack<>();
    private Stack<Integer> points = new Stack<>();

    /**
     * This method returns the instance of the Msg class.
     * If the instance does not exist, it creates a new one.
     * @return The instance of the Msg class.
     */
    public static synchronized Msg getInstance() {
        if (instance == null) instance = new Msg();
        return instance;
    }

    private Msg() {
    }

    /**
     * This method registers a listener to the message.
     * @param listener The listener to be registered.
     */
    public synchronized void registerObserver(Listener listener) {
        listeners.add(listener);
    }

    /**
     * This method removes a listener from the message.
     * @param listener The listener to be removed.
     */
    public synchronized void removeObserver(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * This method notifies all listeners of an update.
     */
    public void notifyObserversUpdate() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    /**
     * This method notifies all listeners of a players update.
     */
    public synchronized void notifyObserversPlayers() {
        for (Listener listener : listeners) {
            listener.updatePlayers();
        }
    }

    /**
     * This method adds a message update to the stack and notifies all listeners.
     * @param message The message update to be added.
     */
    public void putMessage(MessageUpdate message) {
        messages.push(message);
        notifyObserversUpdate();
    }

    /**
     * This method updates the nicknames, pings, and points stacks with a players update and notifies all listeners.
     * @param playersUpdate The players update to be used.
     */
    public void updateNicknames(PlayersUpdate playersUpdate) {
        this.nicknames = playersUpdate.getPlayers();
        this.pings = playersUpdate.getPings();
        this.points = playersUpdate.getPoints();
        notifyObserversPlayers();
    }

    /**
     * This method returns the top message update from the stack.
     * @return The top message update from the stack.
     */
    public MessageUpdate lookMessage() {
        return messages.peek();
    }

    /**
     * This method returns the nicknames stack.
     * @return The nicknames stack.
     */
    public Stack<String> lookNicknames() {
        return nicknames;
    }

    /**
     * This method returns the ping stack (in milliseconds) from the message.
     * @return The Stack of the player pings ordered by the player index.
     */
    public Stack<Long> lookPings() {
        return pings;
    }

    /**
     * This method returns the point stack from the message.
     * @return The Stack of the player points ordered by the player index.
     */
    public Stack<Integer> lookPoints() {
        return points;
    }

    /**
     * This method adds all message updates to an output list and returns it.
     * @param output The output list to be used.
     * @return The output list with all message updates added.
     */
    public ArrayList<String> printMessagesTUI(ArrayList<String> output) {
        for (MessageUpdate message : messages) {
            output.add(message.toString());
        }
        return output;
    }

    /**
     * This method adds all player information to an output list and returns it.
     * @param output The output list to be used.
     * @return The output list with all player information added.
     */
    public ArrayList<String> printPlayerInfoTUI(ArrayList<String> output) {
    // Find the maximum length of the player names to align the output
    int maxNameLength = nicknames.stream().mapToInt(String::length).max().orElse(0);

    for (int i = 0; i < nicknames.size(); i++) {
        // Format each player's information with aligned names
        String formattedInfo = String.format("Player: %-" + maxNameLength + "s Points: %-5d Ping: %-5d",
                                             nicknames.get(i), points.get(i), pings.get(i));
        output.add(formattedInfo);
    }
    return output;
}
    public void printPlayerInfoBox() {
        StringBuilder chatMessages = new StringBuilder();
        ArrayList<String> playerInfo = Msg.getInstance().printPlayerInfoTUI(new ArrayList<>());

        // Top border
        chatMessages.append(" ┌───────────────────────────────────────────────┐\n");

        // Player info
        for (String info : playerInfo) {
            chatMessages.append(" │ ").append(info);
            // Fill the rest of the line with spaces to align with the box
            int spacesToFill = 46 - info.length(); // Adjust 46 based on the box width
            for (int i = 0; i < spacesToFill; i++) {
                chatMessages.append(" ");
            }
            chatMessages.append("│\n");
        }

        // Bottom border
        chatMessages.append(" └───────────────────────────────────────────────┘\n");


        System.out.println(chatMessages.toString());
    }


}