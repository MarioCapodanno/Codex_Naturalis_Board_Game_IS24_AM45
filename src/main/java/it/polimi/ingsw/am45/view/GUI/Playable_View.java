package it.polimi.ingsw.am45.view.GUI;

import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.view.ViewController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Objects;
/**
 * Class for the representation of player's playable board.
 * Part of Game_View.
 */
public class Playable_View {
    private final Game_View game_view;
    final HashMap<AnchorPane, Point2D> grid;

    /**
     * Constructor for Playable_View.
     * @param game_view parent UI screen.
     */
    public Playable_View(Game_View game_view) {
        this.game_view = game_view;
        grid = new HashMap<>();
    }

    @FXML
    private Button choiceBtn;
    @FXML
    private Button choiceCard;
    @FXML
    private Button obj1Card;
    @FXML
    private Button obj2Card;
    @FXML
    private Button br;
    @FXML
    private Button tr;
    @FXML
    private Button tl;
    @FXML
    private Button bl;
    @FXML
    private AnchorPane parentPane;
    @FXML
    private AnchorPane cutto;
    @FXML
    private ImageView initialImg;


    private AnchorPane selectedAnchorPane = null;
    private Button selectedButton = null;
    private Point2D locationClick;
    private Boolean started = false;
    /**
     * SelectedObj is used to store the selected objective card (true if the first option is selected, false if the second option is selected)
     */
    private Boolean selectedObj = null;

    /**
     * Initializes the Playable_View and its buttons.
     * Load the first choosable card and its objectives.
     */
    @FXML
    private void initialize() {
        started = true;
        setButtonStage(true);
        fixButton(obj1Card, false);

        choiceCard.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                ViewController.getClientHandler().flipCard(3);
            }
        });
        obj2Card.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedObj = false;
                fixButton(obj2Card, true);
            }
        });
        obj1Card.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedObj = true;
                fixButton(obj1Card, true);
            }
        });

        choiceBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (selectedObj == null) {
                    game_view.marketViewController.showServerMsg(new MessageUpdate(Messages.CHOOSE));
                    return;
                }
                locationClick = new Point2D(0, 0);
                grid.put(cutto, locationClick);
                game_view.playCard(locationClick);
                setButtonStage(false);
                game_view.ChooseObjective(selectedObj);
                if (selectedObj)
                    game_view.handViewController.setOBJ(((ImageView) obj1Card.getGraphic()).getImage());
                else
                    game_view.handViewController.setOBJ(((ImageView) obj2Card.getGraphic()).getImage());
            }
        });

        br.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedAnchorPane = cutto;
                selectedButton = br;
                locationClick = new Point2D(grid.get(cutto).getX() + 1, grid.get(cutto).getY());
                game_view.tryPlayCard(locationClick);
                System.out.println(locationClick.toString());
            }
        });
        tl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedAnchorPane = cutto;
                selectedButton = tl;
                locationClick = new Point2D(grid.get(cutto).getX() - 1, grid.get(cutto).getY());
                game_view.tryPlayCard(locationClick);
                System.out.println(locationClick.toString());
            }
        });
        tr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedAnchorPane = cutto;
                selectedButton = tr;
                locationClick = new Point2D(grid.get(cutto).getX(), grid.get(cutto).getY() - 1);
                game_view.tryPlayCard(locationClick);
                System.out.println(locationClick.toString());
            }
        });
        bl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedAnchorPane = cutto;
                selectedButton = bl;
                locationClick = new Point2D(grid.get(cutto).getX(), grid.get(cutto).getY() + 1);
                game_view.tryPlayCard(locationClick);
                System.out.println(locationClick.toString());
            }
        });
    }

    /**
     * Creates an anchor pane for the played card and load the card on top of it.
     * @param button angle that triggered the event.
     * @param oldAnchor old anchor pane of the card with the triggered angle.
     */
    @FXML
    private void createAnchorPane(Button button, AnchorPane oldAnchor) {
        button.setVisible(false);
        AnchorPane newAnchorPane = new AnchorPane();
        Button button1 = new Button("");
        button1.setId("tl");
        Button button2 = new Button("");
        button2.setId("tr");
        Button button3 = new Button("");
        button3.setId("bl");
        Button button4 = new Button("");
        button4.setId("br");
        double offsety = 60.0;
        double offsetx = 110.0;
        if (button.getId().contains("tr")) {
            newAnchorPane.setLayoutX(oldAnchor.getLayoutX() + offsetx);
            newAnchorPane.setLayoutY(oldAnchor.getLayoutY() - offsety);
        } else if (button.getId().contains("bl")) {
            newAnchorPane.setLayoutX(oldAnchor.getLayoutX() - offsetx);
            newAnchorPane.setLayoutY(oldAnchor.getLayoutY() + offsety);
        } else if (button.getId().contains("br")) {
            newAnchorPane.setLayoutX(oldAnchor.getLayoutX() + offsetx);
            newAnchorPane.setLayoutY(oldAnchor.getLayoutY() + offsety);
        } else {
            newAnchorPane.setLayoutX(oldAnchor.getLayoutX() - offsetx);
            newAnchorPane.setLayoutY(oldAnchor.getLayoutY() - offsety);
        }
        ImageView imageView = new ImageView();
        imageView.setImage(game_view.getPlayedCard());
        imageView.setFitHeight(145.0);
        imageView.setFitWidth(145.0);
        imageView.setPreserveRatio(true);

        AnchorPane.setTopAnchor(button1, 0.0);
        AnchorPane.setLeftAnchor(button1, 0.0);
        AnchorPane.setTopAnchor(button2, 0.0);
        AnchorPane.setRightAnchor(button2, 0.0);
        AnchorPane.setBottomAnchor(button3, 0.0);
        AnchorPane.setLeftAnchor(button3, 0.0);
        AnchorPane.setBottomAnchor(button4, 0.0);
        AnchorPane.setRightAnchor(button4, 0.0);

        newAnchorPane.getChildren().addAll(imageView, button1, button2, button3, button4);

        Platform.runLater(() -> parentPane.getChildren().add(newAnchorPane));

        grid.put(newAnchorPane, locationClick);
        button1.setOnAction(actionEvent -> {
            selectedAnchorPane = newAnchorPane;
            selectedButton = button1;
            locationClick = new Point2D(grid.get(newAnchorPane).getX() - 1, grid.get(newAnchorPane).getY());
            game_view.tryPlayCard(locationClick);

        });
        button2.setOnAction(actionEvent -> {
            selectedAnchorPane = newAnchorPane;
            selectedButton = button2;
            locationClick = new Point2D(grid.get(newAnchorPane).getX(), grid.get(newAnchorPane).getY() - 1);
            game_view.tryPlayCard(locationClick);

        });
        button3.setOnAction(actionEvent -> {
            selectedAnchorPane = newAnchorPane;
            selectedButton = button3;
            locationClick = new Point2D(grid.get(newAnchorPane).getX(), grid.get(newAnchorPane).getY() + 1);
            game_view.tryPlayCard(locationClick);

        });
        button4.setOnAction(actionEvent -> {
            selectedAnchorPane = newAnchorPane;
            selectedButton = button4;
            locationClick = new Point2D(grid.get(newAnchorPane).getX() + 1, grid.get(newAnchorPane).getY());
            game_view.tryPlayCard(locationClick);
        });
        button1.setOpacity(0.5);
        button2.setOpacity(0.5);
        button3.setOpacity(0.5);
        button4.setOpacity(0.5);

    }

    /**
     * Card played successfully.
     * Load the view of the played card.
     */
    public void playedCard() {
        createAnchorPane(selectedButton, selectedAnchorPane);
    }

    /**
     * Load the image for the choosable card.
     */
    public void updateChoosableCard(String card, String obj1, String obj2) {
        while (!started) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ImageView imageView = (ImageView) choiceCard.getGraphic();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + card))));
        initialImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + card))));
        imageView = (ImageView) obj1Card.getGraphic();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + obj1))));
        imageView = (ImageView) obj2Card.getGraphic();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am45/cardpng/" + obj2))));
    }

    /**
     * Set the card and button visibility.
     * @param t .
     */
    public void setButtonStage(Boolean t) {
        cutto.setVisible(!t);
        choiceCard.setVisible(t);
        obj1Card.setVisible(t);
        obj2Card.setVisible(t);
        choiceBtn.setVisible(t);
    }

    /**
     * Set the initial choose css style.
     * @param button selected card.
     * @param t boolean to set the color.
     */
    public void fixButton(Button button, Boolean t) {
        obj2Card.setStyle("-fx-background-color: null;");
        obj1Card.setStyle("-fx-background-color: null;");
        choiceCard.setStyle("-fx-background-color: null;");
        if (t)
            button.setStyle("-fx-background-color: red;");
    }

}