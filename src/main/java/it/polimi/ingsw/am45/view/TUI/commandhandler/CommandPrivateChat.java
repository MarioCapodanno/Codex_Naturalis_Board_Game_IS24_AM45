package it.polimi.ingsw.am45.view.TUI.commandhandler;


import it.polimi.ingsw.am45.view.TUI.ChatView;
import it.polimi.ingsw.am45.view.TUI.GameView;
import it.polimi.ingsw.am45.view.modelview.Msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * The CommandPrivateChat class represents a command to initiate a private chat.
 * It extends the Command class and overrides the execute method to perform the command.
 */
public class CommandPrivateChat extends Command {

    private final GameView gameView;

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

    /**
     * Constructs a new CommandPrivateChat.
     * Initializes the chatView with the instance of ChatView.
     */
    public CommandPrivateChat(GameView gameView) {
        this.gameView = gameView;
        this.chatView = gameView.getChatViewController();
    }

    /**
     * Executes the command.
     * Handles the private chat between users.
     */
    @Override
    public void execute() {
        String userMessage = "";
        String receiver = "";


        while (!userMessage.equals("exit")) {
            chatView.init();
            chatView.timerUpdate();

            if (!isStarted) {
                System.out.println("Private chat opened. Type 'exit' to leave the chat.");
                System.out.println("Enter the nickname of the user you want to send a message to: ");
                isStarted = true;
            }
            try {
                receiver = reader.readLine();
                if (receiver.trim().equals("exit"))
                    break;
            } catch (IOException e) {
                System.out.println("Error reading input from the console.");
            }
            System.out.println("Enter your message: ");
            try {
                userMessage = reader.readLine();
            } catch (IOException e) {
                System.out.println("Error reading input from the console.");
            }
            userMessage = getUserMessage(userMessage, receiver);
            chatView.closeChat();
        }
        chatView.setChatOpen(false);
    }

    /**
     * Validates the user message and receiver, and sends the message if valid.
     *
     * @param userMessage the message to be sent
     * @param receiver the receiver of the message
     * @return the user message
     */
    private String getUserMessage(String userMessage, String receiver) {
        if (isMessageValid(userMessage) && isMessageValid(receiver) && isUserOnline(receiver)) {
            userMessage = "/"+ receiver +" "+ userMessage;
            chatView.handleSendMessage(userMessage);
        } else if (userMessage.trim().isEmpty()) {
            System.out.println("Cannot send an empty message!");
            timerBeforeRefresh();
        } else if (!isUserOnline(receiver)) {
            System.out.println("The user you are trying to send a message to is not online!");
            timerBeforeRefresh();
            System.out.println("Enter the nickname of the user you want to send a message to: ");
        }
        return userMessage;
    }

    /**
     * Checks if the user is online.
     *
     * @param nickname the nickname of the user
     * @return true if the user is online, false otherwise
     */
    private boolean isUserOnline(String nickname) {
        return Msg.getInstance().lookNicknames().contains(nickname);
    }

    /**
     * Checks if the message is valid.
     *
     * @param userMessage the message to be checked
     * @return true if the message is valid, false otherwise
     */
    private boolean isMessageValid(String userMessage) {
        return !userMessage.trim().isEmpty() && !userMessage.equals("exit");
    }

    /**
     * Pauses the thread for 2 seconds.
     */
    private void timerBeforeRefresh() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Error in the timer.");
            throw new RuntimeException(e);
        }
    }
}