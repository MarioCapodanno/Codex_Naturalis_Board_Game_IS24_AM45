package it.polimi.ingsw.am45.view.TUI.commandhandler;

import it.polimi.ingsw.am45.view.TUI.ChatView;
import it.polimi.ingsw.am45.view.TUI.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The CommandGlobalChat class represents a command to open the global chat.
 * It extends the Command class and overrides the execute method to perform the command.
 */
public class CommandGlobalChat extends Command {

    /**
     * BufferedReader to read user input from the console.
     */
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * ChatView instance to handle the chat view.
     */
    private final ChatView chatView;

    /**
     * Boolean to check if the chat has started.
     */
    private boolean isStarted = false;

    private final GameView gameView;

    /**
     * Constructs a new CommandGlobalChat.
     * Initializes the chatView with the instance of ChatView.
     */
    public CommandGlobalChat(GameView gameView) {
        this.gameView = gameView;
        this.chatView = gameView.getChatViewController();

    }

    /**
     * Executes the command.
     * Handles the global chat between users.
     */
    @Override
    public void execute() {
        String userMessage = "";

        while (!userMessage.equals("exit")) {
            chatView.init();
            chatView.timerUpdate();

            if (!isStarted) {
                System.out.println("Global chat opened. Type 'exit' to leave the chat.");
                System.out.println("Enter your message: ");
                isStarted = true;
            }
            try {
                userMessage = reader.readLine();
            } catch (IOException e) {
                System.out.println("Error reading input from the console.");
                throw new RuntimeException(e);
            }

            if (!userMessage.trim().isEmpty() && !userMessage.equals("exit")) {
                chatView.handleSendMessage(userMessage);
            } else if (userMessage.trim().isEmpty()) {
                System.out.println("Cannot send an empty message.");
            }
            chatView.closeChat();

        }
        chatView.setChatOpen(false);
    }
}