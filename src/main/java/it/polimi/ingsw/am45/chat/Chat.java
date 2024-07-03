package it.polimi.ingsw.am45.chat;

import it.polimi.ingsw.am45.model.Game;

import java.util.Stack;

/**
 * The Chat class manages the chat functionality in the game.
 * It maintains a stack of messages that can be added to and retrieved from.
 */
public class Chat {
    /**
     * Stack of messages in the chat.
     */
    private final Stack<String> messages = new Stack<>();

    /**
     * Constructor for the Chat class.
     * @param game The game in which the chat is being used.
     */
    public Chat(Game game) {
    }

    /**
     * Returns the stack of messages in the chat.
     * @return The stack of messages.
     */
    public Stack<String> getMessages() {
        return messages;
    }

    /**
     * Adds a message to the stack of messages in the chat.
     * @param message The message to be added.
     */
    public void addMessage(String message) {
        messages.push(message);
    }
}