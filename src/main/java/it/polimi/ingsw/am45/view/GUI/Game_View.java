package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.ClientMain;
import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.utilities.CardData;
import it.polimi.ingsw.am45.view.ClientView;
import it.polimi.ingsw.am45.view.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
/**
 * This class represents the game view in the GUI.
 * It manages the game's hand view, playable view, and market view.
 * It also handles updates from the server and user interactions.
 * It extends the ClientView class and manages the user interface for the game screen.
 */
public class Game_View extends ClientView {
    @FXML
    private AnchorPane handViewAnchorPane;
    @FXML
    private AnchorPane ViewMarket;
    @FXML
    private AnchorPane pane;
    @FXML
    private VBox playableViewVBox;

    public Hand_View handViewController;
    public Playable_View playableViewController;
    public Market_View marketViewController;

    /**
     * Queues for the hand, market, and choosing card views.
     */
    private final Queue<List<String>> handQueue = new LinkedList<>();
    private final Queue<List<String>> marketQueue = new LinkedList<>();
    private final Queue<String[]> chosingQueue = new LinkedList<>();

    private HashMap<String, Stack<CardData>> boards = new HashMap<>();
    private final HashMap<String, Boards_View> boardsView = new HashMap<>();

    private final String nick;

    /**
     * Constructor for Game_View.
     *
     * @param nick The nickname of the player.
     * @param color The color of the player's token.
     */
    public Game_View(String nick, TokenColor color) {
        this.nick = nick;
    }

    /**
     * Returns the nickname of the player.
     *
     * @return The nickname of the player.
     */
    public String getNick() {
        return nick;
    }

    /**
     *
     *  Initializes the market, hand and playable views.
     *  Handle also the queues of the server updates.
     *
     * @throws IOException If an I/O error occurs during loading.
     */
    @FXML
    public void initialize() {
        try {
            ViewController.setClientView(this);
            ViewController.getClientHandler().joinUpdate();

            //init views
            FXMLLoader handViewLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am45/HandView.fxml"));
            handViewController = new Hand_View(this);
            handViewLoader.setController(handViewController);
            AnchorPane handView = handViewLoader.load();
            handViewAnchorPane.getChildren().add(handView);

            FXMLLoader playableViewLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am45/PlayableView.fxml"));
            playableViewController = new Playable_View(this);
            playableViewLoader.setController(playableViewController);
            AnchorPane playableView = playableViewLoader.load();
            playableViewVBox.getChildren().add(playableView);

            //common Market view from game
            FXMLLoader marketViewLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am45/MarketView.fxml"));
            marketViewController = new Market_View(this);
            marketViewLoader.setController(marketViewController);
            AnchorPane marketViewPane = marketViewLoader.load();
            ViewMarket.getChildren().add(marketViewPane);

            processHandQueue();
            processChoosingQueue();
            processMarketQueue();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Chooses an objective card.
     *
     * @param card The objective card to be chosen.
     */
    public void ChooseObjective(Boolean card) {
        ViewController.getClientHandler().chooseOBJ(card);
    }

    /**
     * Sets the turn of the player by disabling the button.
     *
     * @param t Boolean to set the turn of the specific player.
     */
    public void setTurn(Boolean t) {
        // t= false -> not his turn, disable buttons
        handViewController.setButtons(!t);
        marketViewController.updateObjective();
    }

    //Da aggiungere alla tui
    /**
     * Remove the played card from the hand view.
     */
    public void updateHandView() {
        handViewController.selectedButtonCardPlaced();
    }





    //Da aggiungere alla tui
    /**
     * Attempts to play a card at a given location.
     * If the selected position is 3, it updates the message to indicate that no card was selected.
     * @param location The location on the board where the card is to be played.
     */
    public void tryPlayCard(Point2D location) {
        int pos = handViewController.getSelectedButton();
        if (pos != 3) {
            ViewController.getClientHandler().playCard(pos, (int) location.getX(), (int) location.getY());
        } else {
            updateMessage(new MessageUpdate(Messages.NOCARDSELECTED));
        }

    }

    /**
     * Plays a card at a given location.
     *
     * This method is used to play a card at a specified location on the board.
     *
     * @param location The location on the board where the card is to be played.
     */
    public void playCard(Point2D location) {
        ViewController.getClientHandler().playCard(3, (int) location.getX(), (int) location.getY());

    }

    /**
     *
     * Draw a card from the deck.
     * If the card is 6, it updates the message to indicate that no card was selected.
     *
     * @param card The card in the market to be drawn.
     */
    public void drawCard(int card) {
        if (card != 6) {
            ViewController.getClientHandler().drawCard(card);
        } else {
            updateMessage(new MessageUpdate(Messages.NOCARDSELECTED));
        }
    }

    /**
     * Show an alert asking the user if they want to see a specific player's board.
     *
     * @param s The name of the player.
     */
    public void showAlertBoard(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you wanna see " + s + " board?");

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> showBoard(s));
    }

    /**
     * Show an alert asking the user if they want to see the leaderboard.
     */
    public void showAlertScore() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you wanna see the score board?");

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        showScoreBoard();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Shows the score board if not already open.
     **
     * @throws IOException If an I/O error occurs during loading a new stage.
     */
    public void showScoreBoard() throws IOException {
        if (Score_View.getInstance(this).started) {
            this.marketViewController.showServerMsg(new MessageUpdate(Messages.ALREADYOPEN));
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am45/ScoreView.fxml"));
        loader.setController(Score_View.getInstance(this));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Scoreboard");
        stage.setOnCloseRequest(windowEvent -> Score_View.getInstance(Game_View.this).close());
        stage.show();

    }

    /**
     * Shows the player's board if not already open.
     **
     */
    public void showBoard(String nickname) {
        if (boards.containsKey(nickname)) {
            try {
                FXMLLoader loader = createBoardLoader(nickname);
                if (loader != null) {
                    displayStage(loader, nickname);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a board loader for a specific board. If the board view has already started, it shows an error message.
     *
     * @param nickname The nickname of the player whose board is to be shown.
     * @return The FXMLLoader for the board.
     * @throws IOException If an I/O error occurs during loading a new stage.
     */
    private FXMLLoader createBoardLoader(String nickname) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am45/BoardView.fxml"));
        if (boardsView.containsKey(nickname)) {
            if (boardsView.get(nickname).started) {
                this.marketViewController.showServerMsg(new MessageUpdate(Messages.ALREADYOPEN));
                return null;
            }
            loader.setController(boardsView.get(nickname));
            boardsView.get(nickname).reloadBoard(boards.get(nickname));
        } else {
            Boards_View boards_view = new Boards_View(this, boards.get(nickname));
            boardsView.put(nickname, boards_view);
            loader.setController(boards_view);
            boards_view.reloadBoard(boards.get(nickname));
        }
        return loader;
    }

    /**
     * Display a stage for a specific board.
     *
     * @param loader The FXMLLoader for the board.
     * @param nickname The nickname of the player whose board is to be shown.
     * @throws IOException If an I/O error occurs during loading a new stage.
     */
    private void displayStage(FXMLLoader loader, String nickname) throws IOException {
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle(nickname + "'s board");
        stage.setOnCloseRequest(windowEvent -> boardsView.get(nickname).close());
        stage.show();
    }

    /**
     * Update the hand view with new cards from server.
     *
     * @param hand The list of cards to update the hand view with.
     */
    @Override
    public void updateHand(List<String> hand) {
        if (this.handViewController == null) {
            handQueue.add(hand);
        } else {
            this.handViewController.updateHandView(hand);
        }
    }

    /**
     * Update the market view with new cards from server.
     *
     * @param cards The list of cards to update the market view with.
     */
    @Override
    public void updateMarket(List<String> cards) {
        if (this.marketViewController == null) {
            marketQueue.add(cards);
        } else {
            this.marketViewController.updateMarketView(cards);
        }
    }

    /**
     * Show the player the messages from the server.
     *
     * @param messageUpdate message from the server.
     */
    @Override
    public void updateMessage(MessageUpdate messageUpdate) {
        switch (messageUpdate.getMessage()) {
            case CARDPLAYED:
                updateHandView();
                this.playableViewController.playedCard();
                this.marketViewController.setButtons(false);

                break;
            case CANTPLAYCARD, NOCARDSELECTED:
                this.marketViewController.showServerMsg(messageUpdate);
                break;
            case SERVERDOWN:
                ViewController.serverdown();
                break;
        }
    }

    /**
     * Update the playableView with the cards to choose from.
     *
     * @param card initial card to flip.
     * @param obj1 objective card 1.
     * @param obj2 objective card 2.
     */
    @Override
    public void updateChoosingCard(String card, String obj1, String obj2) {
        if (this.playableViewController == null) {
            chosingQueue.add(new String[]{card, obj1, obj2});
        } else {
            this.playableViewController.updateChoosableCard(card, obj1, obj2);
        }
    }

    /**
     * @param nickname the nickname of the player to check if it's his turn.
     */
    @Override
    public void updateTurns(String nickname) {
        setTurn(ViewController.getClientHandler().getNick().equals(nickname));
    }


    /**
     * Update the resource of the player in the market.
     *
     * @param resource The list of resources to update the player's resource with.
     */
    @Override
    public void updateResource(List<Integer> resource) {
        this.marketViewController.updateResource(resource);
    }


    /**
     * Update the chat with the messages from other players.
     *
     * @param messages of the chat.
     */
    @Override
    public void updateChat(Stack<String> messages) {
        Chat_View.getInstance(this).updateChat(messages);
    }


    /**
     * Gets the image of the card choose to be played.
     **
     * @return The image of the card.
     */
    public Image getPlayedCard() {
        return handViewController.getSelectedImage();
    }

    /**
     * Update the player's data in the market.
     *
     * @param players The list of player's name.
     * @param pings The list of player's pings.
     * @param points The list of player's points.
     */
    @Override
    public void updatePlayers(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        this.marketViewController.updatePlayerView(players, pings, points);
        Score_View.getInstance(this).updateScore(players, pings, points);
    }

    //Da aggiungere alla tui
    /**
     * Updates the boards view with the card played by others.
     *
     * @param boards The hashmap of boards of the rest of the players .
     */
    public void updateBoards(HashMap<String, Stack<CardData>> boards) {
        this.boards = boards;
        for (String s : boards.keySet()) {
            if (boardsView.containsKey(s))
                boardsView.get(s).reloadBoard(boards.get(s));
        }
    }
    /**
     * Start the chat view in new scene. Check if it's already open.
     *
     * @throws IOException If an I/O error occurs during loading of a new stage.
     */
    public void showChat() throws IOException {
        if (Chat_View.getInstance(this).started) {
            this.marketViewController.showServerMsg(new MessageUpdate(Messages.ALREADYOPEN));
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/am45/ChatView.fxml"));
        loader.setController(Chat_View.getInstance(this));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Chat");
        stage.setOnCloseRequest(windowEvent -> Chat_View.getInstance(Game_View.this).close());
        ViewController.getClientHandler().sendMessage("connected to chat");
        stage.show();
    }

    /**
     * Sends a message to the server via the client handler.
     * If the message is empty, the method will return without sending anything.
     *
     * @param message The message to be sent to the server.
     */
    public void sendMessage(String message) {
        if (message.isEmpty())
            return;
        ViewController.getClientHandler().sendMessage(message);
    }

    /**
     * Load the end view in a new scene with the winner/s.
     *
     * @param winners The list of winners of the game.
     */
    public void updateEnd(List<String> winners) {
        System.out.println("Game Over " + winners.toString());
        Platform.runLater(() -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("EndView.fxml"));
                        End_View endView = new End_View(winners, nick);
                        fxmlLoader.setController(endView);
                        ViewController.changeScene(fxmlLoader, "vintus " + nick);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * Processes the hand queue.
     *
     */
    public void processHandQueue() {
        if (!handQueue.isEmpty()) {
            while (!handQueue.isEmpty()) {
                this.handViewController.updateHandView(handQueue.poll());
            }
        }
    }
    /**
     * Processes the market queue.
     *
     */
    public void processMarketQueue() {
        if (!marketQueue.isEmpty()) {
            while (!marketQueue.isEmpty()) {
                this.marketViewController.updateMarketView(marketQueue.poll());
            }
        }
    }
    /**
     * Processes the choosing card queue.
     *
     */
    public void processChoosingQueue() {
        if (!chosingQueue.isEmpty()) {
            while (!chosingQueue.isEmpty()) {
                String[] cards = chosingQueue.poll();
                int OBJ2 = 2;
                int OBJ1 = 1;
                int INITIALCARD = 0;
                this.playableViewController.updateChoosableCard(cards[INITIALCARD], cards[OBJ1], cards[OBJ2]);
            }
        }
    }

}