package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.ClientMain;
import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.view.ClientView;
import it.polimi.ingsw.am45.view.ViewController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Class for the representation of the JoinCreate_View.
 * It extends the ClientView class and manages the user interface for the Join/Create screen.
 */
public class JoinCreate_View extends ClientView {

    /**
     * Constructor for the JoinCreate_View class.
     * @param nick The nickname of the player.
     */
    public JoinCreate_View(String nick) {
        this.nick = nick;
    }

    @FXML
    ToggleButton toggle;
    @FXML
    Button create;
    @FXML
    Button join;
    @FXML
    TextField gameIdTxt;
    @FXML
    Text errorTxt;
    @FXML
    Text pl1;
    @FXML
    private ChoiceBox<Integer> playerChoiceBox;
    @FXML
    private ChoiceBox<String> playerColor;
    @FXML
    AnchorPane playersName;


    private final String nick;
    private boolean canStart = false;
    private boolean isReadyToPlay = true;
    private TokenColor selectedColor;
    private Boolean first = false;

    /**
     * Initializes the JoinCreate_View.
     * It sets the action for the create, join and toggle buttons:
     * to create a game, join an existing game or toggle between the two.
     */
    @FXML
    public void initialize() {
        BooleanBinding isFieldEmpty = gameIdTxt.textProperty().isEmpty();
        create.disableProperty().bind(isFieldEmpty);
        join.disableProperty().bind(isFieldEmpty);
        setStyle();
        errorTxt.setVisible(false);
        setVisibility(false);
        playersName.setVisible(false);

        create.setOnAction(actionEvent -> {
            try {
                create.setVisible(false);
                selectedColor = getTokenColor(playerColor.getValue());
                ViewController.getClientHandler().newGame(Integer.parseInt(gameIdTxt.getText()), playerChoiceBox.getValue(), getTokenColor(playerColor.getValue()));

            } catch (NumberFormatException e) {
                showErrorMsg("Invalid game id");
                create.setVisible(true);
            }
        });

        toggle.setOnAction(event -> {
            if (toggle.isSelected()) {
                toggle.setText("Join Game");
                setVisibility(true);
            } else {
                toggle.setText("Create Game");
                setVisibility(false);
            }
        });

        join.setOnAction(actionEvent -> {
            try {
                join.setVisible(false);
                selectedColor = getTokenColor(playerColor.getValue());
                ViewController.getClientHandler().joinGame(Integer.parseInt(gameIdTxt.getText()), getTokenColor(playerColor.getValue()));
            } catch (NumberFormatException e) {
                showErrorMsg("Invalid game id");
                join.setVisible(true);
            }
        });

    }

    /**
     * Sets the css style for the create, join and toggle buttons.
     */
    private void setStyle(){
        create.setStyle("-fx-background-color: #349748; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-font-size: 30px;");
        create.setTextFill(Color.WHITE);
        join.setStyle("-fx-background-color: #349748; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-font-size: 30px;");
        join.setTextFill(Color.WHITE);
        toggle.setStyle("-fx-font-weight: bold; -fx-background-color: #D37429; -fx-background-radius: 15px; -fx-font-size: 28px;");
        toggle.setTextFill(Color.WHITE);
    }

    /**
     * Gets the color chosen by the user with the choiceBok.
     * @return the color choose by the user
     */
    private TokenColor getTokenColor(String value) {
        return switch (value) {
            case "Blue" -> TokenColor.BLUE;
            case "Green" -> TokenColor.GREEN;
            case "Yellow" -> TokenColor.YEllOW;
            default -> TokenColor.RED;
        };
    }

    /**
     * Hide the join/create view and show the list of players.
     * @param joined A boolean value that indicates if the player has joined the game.
     */
    private void setVisibility(boolean joined) {
        create.setVisible(!joined);
        playerChoiceBox.setVisible(!joined);
        join.setVisible(joined);
    }

    /**
     * Updates the message shown to the user.
     * @param messageUpdate The message from the server.
     */
    @Override
    public void updateMessage(MessageUpdate messageUpdate) {
        switch (messageUpdate.getMessage()) {
            case NOGAME, GAMESTARTED, FULL, NICKUSED, COLORUSED:
                noGameError();
                showServerMsg(messageUpdate);
                break;
            case JOINED, CREATED:
                change();
                showServerMsg(messageUpdate);
                break;
            case ALREADYEXIST:
                showServerMsg(messageUpdate);
                create.setVisible(true);
                break;
            case STARTING:
                canStart = true;
                break;
            case SERVERDOWN:
                ViewController.serverdown();
                break;
        }
    }

    /**
     * Updates the list of players.
     * @param players The list of players.
     * @param pings The list of pings.
     * @param points The list of points.
     */
    @Override
    public void updatePlayers(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        pl1.setText("" + players);
        if (canStart)
            startGame();
    }



    /**
     * Shows the join button after an error.
     */
    private void noGameError() {
        join.setVisible(true);
    }

    /**
     * Changes the view after the player has joined or created the game.
     */
    private void change() {
        toggle.setVisible(false);
        create.setVisible(false);
        playerChoiceBox.setVisible(false);
        playerColor.setVisible(false);
        join.setVisible(false);
        gameIdTxt.setVisible(false);
        playersName.setVisible(true);
        if (!first) { //WARNING
            ViewController.getClientHandler().requestUpdate(false);
            first = true;
        }
    }

    /**
     * Shows the server message.
     * @param messageUpdate The message.
     */
    public void showServerMsg(MessageUpdate messageUpdate) {
        errorTxt.setVisible(true);
        try {
            errorTxt.setText(messageUpdate.getExplaination());
        } catch (IOException e) {
            errorTxt.setText("Error");
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), errorTxt);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }
    /**
     * Shows the message error.
     * @param error The error.
     */
    public void showErrorMsg(String error) {
        errorTxt.setVisible(true);
        errorTxt.setText(error);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), errorTxt);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    /**
     * Starts the game.
     * Change stage to Game_view.
     */
    public void startGame() {
        if (isReadyToPlay) {
            System.out.println("-----------------Game started");
            Timer timer = new Timer();
            countdown("STARTING GAME:  ");
            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    Platform.runLater(() -> {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("GameView.fxml"));
                                    Game_View gameView = new Game_View(nick, selectedColor);
                                    fxmlLoader.setController(gameView);
                                    ViewController.changeScene(fxmlLoader, "Codex Naturalis:" + nick);
                                } catch (IOException e) {
                                    System.out.println("Error changing scene ");
                                    e.printStackTrace();
                                }
                            }
                    );
                }
            };

            timer.schedule(task, 3000);
            isReadyToPlay = false;
        }
    }

    /**
     * Shows the countdown for starting the game and change view.
     * @param msg label for game starting.
     */
    public void countdown(String msg) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int i = 3;

            @Override
            public void run() {
                errorTxt.setVisible(true);
                errorTxt.setText(msg + i);
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), errorTxt);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();
                i--;
                if (i == 0)
                    timer.cancel();
            }
        };
        timer.schedule(task, 0, 1000);

    }
}
