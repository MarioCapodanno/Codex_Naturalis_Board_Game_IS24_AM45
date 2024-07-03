package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.utilities.CardData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;
/**
 * Class for the representation of other players the boards.
 */
public class Boards_View {
    @FXML
    private AnchorPane parentPane;


    public Boolean started = false;
    final HashMap<AnchorPane, Point2D> grid;

    /**
     * Constructor for Boards_View.
     *
     * @param game_view The game view.
     * @param board The stack of cards to load.
     */
    public Boards_View(Game_View game_view, Stack<CardData> board) {
        grid = new HashMap<>();
    }


    @FXML
    private void initialize() {
        started = true;
    }

    /**
     * Reloads the game board with the new cards.
     *
     * @param board The stack cards to load.
     */
    @FXML
    public void reloadBoard(Stack<CardData> board) {

        for (CardData cardData : board) {
            ImageView imageView = new ImageView();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + cardData.getPath()))));
            double heightImg = 145;
            imageView.setFitHeight(heightImg);
            double widthImg = 145;
            imageView.setFitWidth(widthImg);
            imageView.setPreserveRatio(true);

            double offsetX = 110.0;
            double startX = 2000.0;
            imageView.setLayoutX(startX - (offsetX * (cardData.getLocation().getY() - cardData.getLocation().getX())));
            double offsetY = 60;
            double startY = 1000.0;
            imageView.setLayoutY(startY + (offsetY * (cardData.getLocation().getY() + cardData.getLocation().getX())));

            Platform.runLater(() -> parentPane.getChildren().add(imageView));
        }

    }
    /**
     * Closes the player board.
     */
    public void close() {
        started = false;
    }

}