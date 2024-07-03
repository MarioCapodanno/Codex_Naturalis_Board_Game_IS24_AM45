package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.ClientMain;
import it.polimi.ingsw.am45.connection.rmi.RMIClient;
import it.polimi.ingsw.am45.connection.socket.client.Client;
import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.view.ClientView;
import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.listener.*;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 * This class represents the welcome view of the application.
 * It extends the ClientView class and manages the user interface for the welcome screen.
 */
public class GUI extends ClientView {

    @FXML
    Button continueBtn;
    @FXML
    TextField nick;
    @FXML
    TextField ip;
    @FXML
    Text errortxt;

    /**
     * Initializes the welcome view.
     * It sets the action for the continued button.
     */
    @FXML
    public void initialize() {
        BooleanBinding areFieldsEmpty = nick.textProperty().isEmpty().or(ip.textProperty().isEmpty());
        continueBtn.disableProperty().bind(areFieldsEmpty);

        continueBtn.setOnAction(actionEvent -> {
            if (!nick.getText().isEmpty() && !ip.getText().isEmpty()) {
                startClient();
            } else {
                showErrorMsg("Fill all fields");
            }
        });
    }

    /**
     * Displays an error message to the user.
     *
     * @param error The error message to be displayed.
     */
    public void showErrorMsg(String error) {
        errortxt.setVisible(true);
        errortxt.setText(error);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), errortxt);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    /**
     * Changes the scene to the JoinCreateView.
     */
    public void changeScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("JoinCreateView.fxml"));
        fxmlLoader.setController(new JoinCreate_View(nick.getText()));
        InitListener();
        Platform.runLater(() -> {
            try {
                ViewController.changeScene(fxmlLoader, "Waiting Room " + nick.getText());
            } catch (IOException e) {
                System.out.println("Error changing scene");
            }
        });
    }

    /**
     * Initializes the listeners for the view of the server updates.
     */
    private void InitListener() {
        new msgListener();
        new handListener();
        new turnListener();
        new boardsListener();
        new marketListener();
        new endListener();
    }

    /**
     * Starts the client connection.
     * It creates a new Client object and attempts to start the client.
     * If the client start is successful, it changes the scene.
     * If an IOException occurs, it shows an error message.
     */
    private void startClient() {
        String serverAddress = "localhost";
        int serverPort = 28888;
        if (ip.getText().contains("rmi-")) {

            try {
                RMIClient rmiClient = new RMIClient(ip.getText().replace("rmi-", ""));
                ViewController.setClientHandler(rmiClient);
                rmiClient.newPlayer(nick.getText());
                rmiClient.nickname = nick.getText();
                ViewController.setConnectionType(ConnectionType.RMI);
                changeScene();
            } catch (RemoteException e) {
                showErrorMsg("Invalid server address");

            }
        } else {
            try {
                Client client = new Client(ip.getText(), serverPort, nick.getText());
                client.startClient();
                ViewController.setConnectionType(ConnectionType.TCP);
                changeScene();
            } catch (UnknownHostException e) {
                showErrorMsg("Invalid server address: "+e.getMessage());
            } catch (IllegalArgumentException e) {
                showErrorMsg("Invalid server port: "+ e.getMessage());
            } catch (IOException e) {
                showErrorMsg("An error occurred: "+e.getMessage());
            }
        }
    }

}