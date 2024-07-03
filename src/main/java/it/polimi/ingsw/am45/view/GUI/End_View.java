package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.ClientMain;
import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.view.ClientView;
import it.polimi.ingsw.am45.view.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Closes the Boards_View.
 * It extends the ClientView class and manages the user interface for the end screen.
 */
public class End_View extends ClientView {


    @FXML
    private AnchorPane parentPane;
    @FXML
    private Button lobbyBtn;


    public Boolean started = false;
    private List<String> winners = new ArrayList<>();
    private final String nick;


    /**
     * Constructor for End_View.
     *
     * @param winners The list of winners.
     * @param nick The nickname of the player.
     */
    public End_View(List<String> winners, String nick) {
        this.nick = nick;
        this.winners = winners;
    }

    /**
     * Initializes the End_View with the css style and the winners.
     */
    @FXML
    private void initialize() {
        started = true;
        lobbyBtn.setStyle("-fx-background-color: green; -fx-background-radius: 15px; -fx-font-weight: bold; -fx-font-size: 28px;");
        lobbyBtn.setTextFill(Color.WHITE);
        double layoutY = 391.0;
        for (String winner : winners) {
            Text winnerText = new Text();
            winnerText.setLayoutX(633.0);
            winnerText.setLayoutY(layoutY);
            winnerText.setText(winner);
            parentPane.getChildren().add(winnerText);
            winnerText.setFill(Color.WHITE);
            winnerText.setFont(new Font(25));
            layoutY += 30;
        }


        lobbyBtn.setOnAction(event -> {
            close();
            try {
                JoinCreate_View joinCreateView = new JoinCreate_View(nick);
                FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("JoinCreateView.fxml"));

                fxmlLoader.setController(joinCreateView);
                ViewController.changeScene(fxmlLoader, "Choose game " + nick);
            } catch (IOException e) {
                System.out.println("Error changing scene");
            }

        });

    }


    /**
     * Listen for the Server messages.
     *
     * @param messageUpdate the server message.
     */
    @Override
    public void updateMessage(MessageUpdate messageUpdate) {
        if (Objects.requireNonNull(messageUpdate.getMessage()) == Messages.SERVERDOWN) {
            ViewController.serverdown();
        }
    }
    /**
     * Closes the End_View.
     */
    public void close() {
        started = false;
    }

}