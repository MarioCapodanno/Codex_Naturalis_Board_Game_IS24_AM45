package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
/**
 * Class for the representation of player's market.
 * Part of Game_View.
 */
public class Market_View {
    private final Game_View gameView;

    private ImageView imageView;
    private PlayableCard card1;
    private PlayableCard card2;
    private PlayableCard card3;
    private Boolean started = false;
    private Button selectedButton = null;

    @FXML
    Text openScore;
    @FXML
    Button deckResource_1;
    @FXML
    Button deckResource_2;
    @FXML
    Button deckResource_3;
    @FXML
    Button deckGold_1;
    @FXML
    Button deckGold_2;
    @FXML
    Button deckGold_3;
    @FXML
    Button drawBtn;
    @FXML
    Text errorTxt;
    @FXML
    Text animalTxt;
    @FXML
    Text fungiTxt;
    @FXML
    Text plantTxt;
    @FXML
    Text insectTxt;
    @FXML
    Text scrollTxt;
    @FXML
    Text potionTxt;
    @FXML
    Text featherTxt;
    @FXML
    Text pl1;
    @FXML
    Text pl2;
    @FXML
    Text pl3;
    @FXML
    Text pl4;
    @FXML
    ImageView obj1;
    @FXML
    ImageView obj2;
    private Text[] playerTexts;

    /**
     * Queue for the player's market.
     */
    private List<String> marketQueue;
    private Stack<String> playerQueue;
    private Stack<Long> pingQueue;
    private Stack<Integer> pointQueue;

    /**
     * Constructor for the Market_View.
     * @param game_view parent UI screen.
     */
    public Market_View(Game_View game_view) {
        this.gameView = game_view;
    }

    /**
     * Initialize Market_View and the action of his buttons.
     * Select market card.
     * Draw card.
     * Show player's score and boards.
     */
    @FXML
    private void initialize() {
        playerTexts = new Text[]{pl1, pl2, pl3, pl4};
        started = true;
        if (playerQueue != null && pingQueue != null && pointQueue != null) {
            updatePlayerTexts(playerQueue, pingQueue, pointQueue);
            playerQueue = null;
            pingQueue = null;
            pointQueue = null;
        }

        if (marketQueue != null) {
            updateImages(marketQueue);
            marketQueue = null;
        }
        fixButton(deckResource_1, false);
        obj1.setVisible(false);
        obj2.setVisible(false);
        setButtons(true);

        deckResource_1.setOnAction(event -> {
            selectedButton = deckResource_1;
            fixButton(deckResource_1, true);
        });
        deckResource_2.setOnAction(event -> {
            selectedButton = deckResource_2;
            fixButton(deckResource_2, true);
        });
        deckResource_3.setOnAction(event -> {
            selectedButton = deckResource_3;
            fixButton(deckResource_3, true);
        });
        deckGold_1.setOnAction(event -> {
            selectedButton = deckGold_1;
            fixButton(deckGold_1, true);
        });
        deckGold_2.setOnAction(event -> {
            selectedButton = deckGold_2;
            fixButton(deckGold_2, true);
        });
        deckGold_3.setOnAction(event -> {
            selectedButton = deckGold_3;
            fixButton(deckGold_3, true);
        });
        drawBtn.setOnAction(event -> {
            if (selectedButton != null) {
                fixButton(deckResource_1, false);
                gameView.drawCard(getSelectedCard());
                setButtons(true);
                selectedButton = null;
            } else {
                gameView.updateMessage(new MessageUpdate(Messages.NOCARDSELECTED));
            }
        });

        pl1.setOnMouseClicked(event -> {
            if (!pl1.getText().isEmpty())
                showAlert(pl1);
        });
        pl2.setOnMouseClicked(event -> {
            if (!pl2.getText().isEmpty())
                showAlert(pl2);
        });
        pl3.setOnMouseClicked(event -> {
            if (!pl3.getText().isEmpty())
                showAlert(pl3);
        });
        pl4.setOnMouseClicked(event -> {
            if (!pl4.getText().isEmpty())
                showAlert(pl4);
        });

        openScore.setOnMouseClicked(event -> gameView.showAlertScore());

    }

    /**
     * Show alert for opening player's board.
     * @param text the player's name.
     */
    public void showAlert(Text text) {
        gameView.showAlertBoard(text.getText().split("/")[0]);
    }

    /**
     * Disable/Enable market cards based on turn.
     * @param t true if the buttons are disabled, false otherwise.
     */
    public void setButtons(Boolean t) {
        if(!checkMarket()){
            gameView.drawCard(10);
            return;
        }
        deckResource_1.setDisable(t);
        deckResource_2.setDisable(t);
        deckResource_3.setDisable(t);
        deckGold_1.setDisable(t);
        deckGold_2.setDisable(t);
        deckGold_3.setDisable(t);
        drawBtn.setDisable(t);
    }

    /**
     * Check market's cards.
     * @return true if there are cards in the market, false otherwise.
     */
    private Boolean checkMarket(){
        return deckResource_1.isVisible() || deckResource_2.isVisible() || deckResource_3.isVisible() || deckGold_1.isVisible() || deckGold_2.isVisible() || deckGold_3.isVisible();
    }

    /**
     * Fix the card's css style.
     * @param button the card to fix.
     * @param t true if the card is selected, false otherwise.
     */
    public void fixButton(Button button, Boolean t) {

        deckResource_1.setStyle("-fx-background-color: #8F258E50;");
        deckResource_2.setStyle("-fx-background-color: #1C834150;");
        deckResource_3.setStyle("-fx-background-color: #4BBFB450;");
        deckGold_1.setStyle("-fx-background-color: #8F258E50;");
        deckGold_2.setStyle("-fx-background-color: #1C834150;");
        deckGold_3.setStyle("-fx-background-color: #4BBFB450;");

        if (t)
            button.setStyle("-fx-background-color: red;");
    }

    /**
     * Clean player's score.
     */
    public void cleanPlayers() {
        for (Text playerText : playerTexts) {
            playerText.setText("");
        }
    }

    /**
     * queue for market's information if view not started.
     * @param market the market's cards.
     */
    public void updateMarketView(List<String> market) {
        if (!started) {
            this.marketQueue = market;
        } else {
            updateImages(market);
        }

    }

    /**
     * Update the common objective cards image.
     * @param market the market's cards.
     */
    private void updateImages(List<String> market) {
        checkResourceDeck(market);
        checkGoldDeck(market);
        obj1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(6)))));
        obj2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(7)))));
    }


    /**
     * Set the market's resource cards image.
     *If the card is not available, the button is disabled.
     * @param market the market's cards.
     */
    private void checkResourceDeck(List<String> market) {
        if(market.get(0).equals("end.png")) {
            deckResource_1.setVisible(false);
            deckResource_1.setStyle("-fx-background-color: null;");
        } else {
            deckResource_1.setVisible(true);
            imageView = (ImageView) deckResource_1.getGraphic();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(0)))));
        }
        if(market.get(1).equals("end.png")) {
            deckResource_2.setVisible(false);
            deckResource_2.setStyle("-fx-background-color: null;");
        } else {
            deckResource_2.setVisible(true);
            imageView = (ImageView) deckResource_2.getGraphic();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(1)))));
        }
        if(market.get(2).equals("end.png")) {
            deckResource_3.setVisible(false);
            deckResource_3.setStyle("-fx-background-color: null;");
        } else {
            deckResource_3.setVisible(true);
            imageView = (ImageView) deckResource_3.getGraphic();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(2)))));
        }
    }


    /**
     * Set the market's gold cards image.
     *If the card is not available, the button is disabled.
     * @param market the market's cards.
     */
    private void checkGoldDeck(List<String> market){
        if(market.get(3).equals("end.png")) {
            deckGold_1.setVisible(false);
            deckGold_1.setStyle("-fx-background-color: null;");
        } else {
            deckGold_1.setVisible(true);
            imageView = (ImageView) deckGold_1.getGraphic();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(3)))));

        }
        if(market.get(4).equals("end.png")) {
            deckGold_2.setVisible(false);
            deckGold_2.setStyle("-fx-background-color: null;");
        } else {
            deckGold_2.setVisible(true);
            imageView = (ImageView) deckGold_2.getGraphic();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(4)))));

        }
        if(market.get(5).equals("end.png")) {
            deckGold_3.setVisible(false);
            deckGold_3.setStyle("-fx-background-color: null;");
        } else {
            deckGold_3.setVisible(true);
            imageView = (ImageView) deckGold_3.getGraphic();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + market.get(5)))));
        }
    }

    /**
     * Set params in queue if not started.
     * If started, call function to update leaderboard.
     *
     * @param players the player's name.
     * @param pings the player's ping.
     * @param points the player's score.
     */
    public void updatePlayerView(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        if (!started) {
            this.playerQueue = players;
            this.pingQueue = pings;
            this.pointQueue = points;
        } else {
            updatePlayerTexts(players, pings, points);
        }
    }

    /**
     * Update leaderboard.
     * @param players the player's name.
     * @param pings the player's ping.
     * @param points the player's score.
     */
    private void updatePlayerTexts(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        cleanPlayers();

        for (int i = 0; i < players.size(); i++) {
            if (pings != null)
                playerTexts[i].setText(players.get(i) + "/" + pings.get(i) + "ms  " + points.get(i));
            else
                playerTexts[i].setText(players.get(i));
            showPlayerOrderedByPoints(i, playerTexts);
        }
    }

    /**
     * Update player's resource.
     * @param resource of the player.
     */
    public void updateResource(List<Integer> resource) {
        animalTxt.setText(resource.get(0).toString());
        fungiTxt.setText(resource.get(3).toString());
        plantTxt.setText(resource.get(1).toString());
        insectTxt.setText(resource.get(2).toString());
        scrollTxt.setText(resource.get(6).toString());
        potionTxt.setText(resource.get(5).toString());
        featherTxt.setText(resource.get(4).toString());
    }

    /**
     * Show server message.
     * @param messageUpdate the message to show.
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
     * Get the selected card.
     * @return the position of the selected card.
     */
    public int getSelectedCard() {
        if (selectedButton == null)
            return 6;
        return switch (selectedButton.getId()) {
            case "deckResource_1" -> 0;
            case "deckResource_2" -> 1;
            case "deckResource_3" -> 2;
            case "deckGold_1" -> 3;
            case "deckGold_2" -> 4;
            case "deckGold_3" -> 5;
            default -> 6;
        };
    }

    /**
     * Update the common objective.
     */
    public void updateObjective() {
        obj1.setVisible(true);
        obj2.setVisible(true);
    }

    /**
     * Show player's position in leaderboard.
     * @param i the player's position.
     * @param playerTexts the player's name.
     */
    public static void showPlayerOrderedByPoints(int i, Text[] playerTexts) {
        switch (i) {
            case 0:
                playerTexts[0].setText(playerTexts[0].getText() + "\uD83E\uDD47");
                break;
            case 1:
                playerTexts[1].setText(playerTexts[1].getText() + "\uD83E\uDD48");
                break;
            case 2:
                playerTexts[2].setText(playerTexts[2].getText() + "\uD83E\uDD49");
                break;
        }
    }

}
