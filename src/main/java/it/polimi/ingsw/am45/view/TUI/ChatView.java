package it.polimi.ingsw.am45.view.TUI;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * The ChatView class represents the chat view in the game.
 * It handles the display of the global and private chat messages.
 * It also processes the chat updates and updates the chat view accordingly.
 */
public class ChatView {

    private final GameView gameView;
    private final Queue<Stack<String>> chatUpdatesQueue = new LinkedList<>();
    private volatile boolean isStarted = false;

    private CountDownLatch latch;

    final Stack<String> messages = new Stack<>();

    final Stack<String> globalMessages = new Stack<>();

    final Stack<String> privateMessages = new Stack<>();

    /**
     * Constructs a new ChatView with the specified game view.
     * Sets the instance of this class to this newly created object.
     *
     * @param gameView the game view to be used
     */
    public ChatView(GameView gameView) {
        this.gameView = gameView;
        this.latch = new CountDownLatch(1);
    }

    /**
     * Initializes the ChatView.
     * Sets the started flag to true and updates the chat with the messages in the chat updates queue.
     */
    public synchronized void init() {
        isStarted = true;
        while (!chatUpdatesQueue.isEmpty()) {
            updateChat(chatUpdatesQueue.poll());
        }
    }

    /**
     * Updates the chat with the specified messages.
     * If the ChatView has not started, the messages are added to the chat updates queue.
     * Otherwise, a new TUIUpdateTask is created and started in a new thread.
     *
     * @param messages the messages to update the chat with
     */
    public synchronized void updateChat(Stack<String> messages) {

        if (!isStarted) {
            chatUpdatesQueue.add(messages);
            return;
        }

        // Create a new TUIUpdateTask and start it in a new thread
        TUIUpdateTask updateTask = new TUIUpdateTask(this, messages);
        new Thread(updateTask).start();
        // Decrement the latch when the update is completed
        latch.countDown();

    }

    /**
     * Closes the ChatView.
     * Sets the started flag to false.
     */
    public synchronized void closeChat() {
        isStarted = false;
    }

    /**
     * Returns the global messages.
     *
     * @return a new ArrayList containing the global messages
     */
    public synchronized ArrayList<String> getGlobalMessages() {
            return new ArrayList<>(globalMessages);
    }

    /**
     * Returns the private messages.
     *
     * @return a new ArrayList containing the private messages
     */
    public synchronized ArrayList<String> getPrivateMessages() {
        return new ArrayList<>(privateMessages);
    }

    /**
     * Prints the global chat.
     * Waits for the latch to reach zero, then prints the global chat messages.
     */
    public void printGlobalChat() {

        try {
            // Wait for the latch to reach zero
            latch.await();

            // Create a copy of globalMessages
            List<String> copyOfGlobalMessages;
            synchronized (this.globalMessages) {
                copyOfGlobalMessages = new ArrayList<>(globalMessages);
            }

            StringBuilder chatMessages = new StringBuilder();
            chatMessages.append(" ┌──────────────────────────────────────┐\n");
            chatMessages.append(" │             GLOBAL CHAT              │\n");
            chatMessages.append(" │ (type 'exit' to return to the menu ) │\n");
            chatMessages.append(" └──────────────────────────────────────┘\n");
            for (String message : copyOfGlobalMessages) {
                chatMessages.append(message).append("\n");
            }
            chatMessages.append(" └──────────────────────────────────────┘\n");
            System.out.println(chatMessages);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        this.latch = new CountDownLatch(1);
    }

    /**
     * Prints the private chat.
     * Prints the private chat messages.
     */
    public void printFirstChat() {

        // Create a copy of globalMessages
        List<String> copyOfGlobalMessages;
        synchronized (this.globalMessages) {
            copyOfGlobalMessages = new ArrayList<>(globalMessages);
        }

        StringBuilder chatMessages = new StringBuilder();
        chatMessages.append(" ┌──────────────────────────────────────┐\n");
        chatMessages.append(" │                 CHAT                 │\n");
        chatMessages.append(" │ (type 'exit' to return to the menu ) │\n");
        chatMessages.append(" └──────────────────────────────────────┘\n");
        for (String message : copyOfGlobalMessages) {
            chatMessages.append(message).append("\n");
        }
        chatMessages.append(" └──────────────────────────────────────┘\n");
        System.out.println(chatMessages);
    }

    /**
     * Handles the sending of a message.
     * Calls the sendMessage method of the game view with the specified message.
     *
     * @param message the message to send
     */
    public void handleSendMessage(String message) {
        gameView.sendMessage(message);
    }

    /**
     * Sets whether the chat is open.
     * Calls the isChatOpen method of the game view with the specified value.
     *
     * @param isChatOpen true if the chat is open, false otherwise
     */
    public void setChatOpen(Boolean isChatOpen) {
        gameView.isChatOpen(isChatOpen);
    }

    /**
     * Sets whether the private chat is open.
     * Calls the isPrivateChatOpen method of the game view with the specified value.
     *
     * @param isPrivateChatOpen true if the private chat is open, false otherwise
     */
    public void setPrivateChatOpen(Boolean isPrivateChatOpen) {
        gameView.isPrivateChatOpen(isPrivateChatOpen);
    }

    /**
     * Returns the game view.
     *
     * @return the game view
     */
    public GameView getGameView() {
        return this.gameView;
    }

    /**
     * Updates the timer.
     * Sleeps the current thread for 500 milliseconds.
     */
    public void timerUpdate(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
            Thread.currentThread().interrupt();
        }
    }
}