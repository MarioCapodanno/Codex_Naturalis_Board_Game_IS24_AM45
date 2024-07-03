package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.view.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;

/**
 * Class for the representation of player's hand.
 * Part of Game_View.
 */
public class Hand_View {
    private final Game_View game_view;


    /**
     * Constructor for the Hand_View class.
     * @param game_view parent class used to communicate with others part of the view.
     */
    public Hand_View(Game_View game_view) {
        this.game_view = game_view;
    }


    private boolean started = false;
    private Button selectedButton = null;


    @FXML
    Button handcard1;
    @FXML
    Button handcard2;
    @FXML
    Button handcard3;
    @FXML
    Button chatBtn;
    @FXML
    ImageView objCard;



    /**
     * Initialize Hand_View and the action of his buttons.
     * Flip hands card.
     * Select hand card.
     */
    @FXML
    private void initialize() {

        started = true;
        fixButton();
        chatBtn.setOnAction(event -> {
            try {
                game_view.showChat();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        handcard1.setOnAction(event -> {
            selectedButton = handcard1;
            fixButton(handcard1, true);

        });
        handcard2.setOnAction(event -> {
            selectedButton = handcard2;
            fixButton(handcard2, true);
        });
        handcard3.setOnAction(event -> {
            selectedButton = handcard3;
            fixButton(handcard3, true);
        });

        handcard1.setOnContextMenuRequested(contextMenuEvent -> ViewController.getClientHandler().flipCard(0));
        handcard2.setOnContextMenuRequested(contextMenuEvent -> ViewController.getClientHandler().flipCard(1));
        handcard3.setOnContextMenuRequested(contextMenuEvent -> ViewController.getClientHandler().flipCard(2));
    }

    /**
     * Make invisible the selected placed card.
     */
    public void selectedButtonCardPlaced() {
        selectedButton.setVisible(false);
        setButtons(true);
    }

    /**
     * Get the selected card.
     * @return the position of the selected card.
     */
    public int getSelectedButton() {
        if (selectedButton == null)
            return 3;
        return switch (selectedButton.getId()) {
            case "handcard1" -> 0;
            case "handcard2" -> 1;
            case "handcard3" -> 2;
            default -> 3;
        };
    }

    /**
     * Disable or enable the buttons.
     * @param t true if the buttons are disabled, false otherwise.
     */
    public void setButtons(Boolean t) {
        handcard1.setDisable(t);
        handcard2.setDisable(t);
        handcard3.setDisable(t);
    }

    /**
     * Fix the card's css style.
     * @param button the card selected.
     * @param t if true then select the card.
     */
    public void fixButton(Button button, Boolean t) {
        handcard1.setStyle("-fx-background-color: #8F258E50;");
        handcard2.setStyle("-fx-background-color: #1C834150;");
        handcard3.setStyle("-fx-background-color: #4BBFB450;");
        if (t)
            button.setStyle("-fx-background-color: red;");
    }

    /**
     * Fix the card's css style.
     */
    public void fixButton() {
        handcard1.setStyle("-fx-background-color: #8F258E50;");
        handcard2.setStyle("-fx-background-color: #1C834150;");
        handcard3.setStyle("-fx-background-color: #4BBFB450;");
    }

    /**
     * Update the hand's cards with their image.
     * @param hand the hand of the player.
     */
    public void updateHandView(List<String> hand) {
        while (!started) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ImageView imageView = (ImageView) handcard1.getGraphic();
        imageView.setImage(new Image(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + hand.get(0))));
        imageView = (ImageView) handcard2.getGraphic();
        imageView.setImage(new Image(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + hand.get(1))));
        imageView = (ImageView) handcard3.getGraphic();
        imageView.setImage(new Image(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + hand.get(2))));

        handcard1.setVisible(true);
        handcard2.setVisible(true);
        handcard3.setVisible(true);
    }

    /**
     * Get the image of the selected card.
     * @return image of the selected card.
     */
    public Image getSelectedImage() {
        ImageView imageView = (ImageView) selectedButton.getGraphic();
        selectedButton = null;
        Platform.runLater(this::fixButton);

        return imageView.getImage();
    }

    /**
     * Set the personal card objective in the hand.
     * @param objImage the image of the object card.
     */
    public void setOBJ(Image objImage) {
        objCard.setImage(objImage);
    }

}
