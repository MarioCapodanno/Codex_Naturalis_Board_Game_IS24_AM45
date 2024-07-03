package it.polimi.ingsw.am45.view;

import it.polimi.ingsw.am45.ClientMain;
import it.polimi.ingsw.am45.connection.ClientHandler;
import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.view.GUI.GUI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * This class is responsible for controlling the view of the application.
 * It manages the current stage, scene, and view, as well as the connection type and client handler.
 */
public class ViewController {
    private static Stage currentStage;
    public static Scene activeScene;
    private static ClientView clientView;
    private static ConnectionType connection;
    private static ClientHandler clientHandler;

    /**
     * Sets up the stage with the given stage and FXMLLoader.
     * If a stage is already set, it is closed before the new stage is set up (for server down case).
     *
     * @param stage The stage to set up.
     * @param fxmlLoader The FXMLLoader to use to load the scene.
     * @throws IOException If an I/O error occurs.
     */
    public static void setUpStage(Stage stage, FXMLLoader fxmlLoader) throws IOException {
        if(currentStage != null) {
            currentStage.close();
        }
        Scene scene = new Scene(fxmlLoader.load());
        currentStage = stage;
        activeScene = scene;
        clientView = fxmlLoader.getController();
        stage.setResizable(false);
        stage.setTitle("Codex Naturalis");
        stage.setScene(scene);
    }
    /**
     * Sets the connection type of the client.
     *
     * @param connectionType The connection type to set.
     */
    public static void setConnectionType(ConnectionType connectionType) {
        connection = connectionType;
    }

    /**
     * @return The connection type of the client.
     */
    public static ConnectionType getConnection() {
        return connection;
    }
    /**
     * Changes the scene of the application to the scene loaded by the given FXMLLoader.
     * The title of the stage is also updated.
     *
     * @param fxmlLoader The FXMLLoader to use to load the scene.
     * @param title The title to set for the stage.
     * @throws IOException If an I/O error occurs.
     */
    public static void changeScene(FXMLLoader fxmlLoader, String title) throws IOException {
        Parent root = fxmlLoader.load();
        activeScene.setRoot(root);
        clientView = fxmlLoader.getController();
        currentStage.setTitle(title);
        currentStage.setWidth(1400);
        currentStage.setHeight(750);
        currentStage.centerOnScreen();
        currentStage.setOnCloseRequest(event -> System.exit(0));
        currentStage.show();

    }
    /**
     *
     * @return The current view of the client.
     */
    public static ClientView getClientView() {
        return clientView;
    }
    /**
     *
     * @param clientView The view to set.
     */
    public static void setClientView(ClientView clientView) {
        ViewController.clientView = clientView;
    }
    /**
     *
     * @param clientHandler The client handler to set.
     */
    public static void setClientHandler(ClientHandler clientHandler) {
        ViewController.clientHandler = clientHandler;
    }
    /**
     *
     * @return The client handler for the client.
     */
    public static ClientHandler getClientHandler() {
        return clientHandler;
    }

    /**
     * Handles a server down event.
     * This method is called when the server goes down.
     * It sets up a new stage with the welcome view.
     */
    public static void serverdown(){
        Platform.runLater(() -> {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("WelcomeView.fxml"));
            GUI welcomeView = new GUI();
            fxmlLoader.setController(welcomeView);
            try {
                setUpStage(stage, fxmlLoader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.show();
        });
    }
}
