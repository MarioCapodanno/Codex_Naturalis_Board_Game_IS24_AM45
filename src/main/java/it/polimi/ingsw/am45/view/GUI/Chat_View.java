package it.polimi.ingsw.am45.view.GUI;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * This class represents the view of the chat in the game.
 * It uses the Singleton design pattern to ensure that only one instance of the chat view exists.
 */
public class Chat_View {
    private final Game_View game_view;
    private static Chat_View instance;
    private final Queue<Stack<String>> chatUpdatesQueue = new LinkedList<>();
    public boolean started = false;
    private Stack<String> messages = new Stack<>();

    /**
     * Constructor for the Chat_View class.
     * @param game_view The view of the game.
     */
    public Chat_View(Game_View game_view) {
        this.game_view = game_view;
    }

    /**
     * This method returns the instance of the Chat_View class.
     * If the instance does not exist, it creates a new one.
     * @param game_view The view of the game.
     * @return The instance of the Chat_View class.
     */
    public static Chat_View getInstance(Game_View game_view) {
        if (instance == null) {
            instance = new Chat_View(game_view);
        }
        return instance;
    }

    @FXML
    Button sendBtn;
    @FXML
    TextField textPane;
    @FXML
    VBox chatBox;
    @FXML
    ScrollPane scrollPane;

    /**
     * This method initializes the chat view.
     * It sets up the send button to send messages and binds the text field to the button.
     * The private messages are displayed in the chat using the format "private message: message".
     * The global messages are displayed as "nick: message".
     */
    @FXML
    private void initialize() {
        BooleanBinding isFieldEmpty = textPane.textProperty().isEmpty();
        sendBtn.disableProperty().bind(isFieldEmpty);
        chatBox = new VBox();
        started = true;

        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game_view.sendMessage(textPane.getText());
                textPane.clear();
            }
        });
        while (!chatUpdatesQueue.isEmpty()) {
            updateChat(chatUpdatesQueue.poll());
        }
    }

    /**
     * This method updates the chat with new messages.
     * If the chat view has not been started yet, the messages are added to the chat updates queue.
     *
     * @param messages The new messages to be added to the chat.
     */
    public void updateChat(Stack<String> messages) {
        if (!started) {
            chatUpdatesQueue.add(messages);
            return;
        }

        Platform.runLater(() -> {
            if (this.messages != messages) {
                this.messages = messages;
                if (chatBox != null)
                    chatBox.getChildren().clear();
                for (String message : messages) {
                    int start = message.indexOf("/");
                    int end = message.indexOf(" ", start);
                    if (start != -1 && end != -1) {
                        String nick = message.substring(start + 1, end);
                        if (nick.equals(game_view.getNick())) {
                            message = message.replace("/" + nick, "private message: ");
                        }
                    }
                    Label messageLabel = new Label(message);
                    chatBox.getChildren().add(messageLabel);
                }
            }
            Label messageLabel = new Label("");
            chatBox.getChildren().add(messageLabel);
            scrollPane.setContent(chatBox);
            scrollPane.setVvalue(1.0);
        });
    }

    /**
     * This method closes the chat view by setting the started flag to false.
     */
    public void close() {
        started = false;
    }
}