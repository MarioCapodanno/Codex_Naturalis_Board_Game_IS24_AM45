package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.ClientMain;
import it.polimi.ingsw.am45.view.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
/**
 * This class represents the starting GUI of the application.
 * It extends the Application class provided by JavaFX.
 */
public class StartingGUI extends Application {

    /**
     * This method is called when the application is launched.
     * Loads the WelcomeView.fxml file and sets the controller to the GUI class.
     *
     * @param stage the stage of the application
     * @throws Exception if an error occurs
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("WelcomeView.fxml"));
        GUI welcomeView = new GUI();
        fxmlLoader.setController(welcomeView);
        ViewController.setUpStage(stage, fxmlLoader);
        stage.show();

    }

    public void start() {
        launch();
    }


}
