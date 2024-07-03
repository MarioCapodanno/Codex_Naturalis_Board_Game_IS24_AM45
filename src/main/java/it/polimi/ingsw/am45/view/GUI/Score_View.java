package it.polimi.ingsw.am45.view.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
/**
 * Class for the representation of leaderboard.
 * It uses the Singleton design pattern to ensure that only one instance of the Score_View exists.
 */
public class Score_View {
    private static Score_View instance;
    public boolean started = false;
    private VBox[] panes;

    /**
     * Class for the representation of other players the boards.
     */
    private final Queue<Pair<Stack<String>, Pair<Stack<Long>, Stack<Integer>>>> scoreUpdatesQueue = new LinkedList<>();
    /**
     * Vbox used to display the score.
     */
    @FXML
    private VBox pane0, pane1, pane2, pane3, pane4, pane5, pane6, pane7, pane8, pane9, pane10,
            pane11, pane12, pane13, pane14, pane15, pane16, pane17, pane18, pane19, pane20,
            pane21, pane22, pane23, pane24, pane25, pane26, pane27, pane28, pane29;


    /**
     * Constructs a new Score_View with the given game view.
     *
     * @param game_view The game view associated with this score view.
     */
    public Score_View(Game_View game_view) {
    }
    /**
     * Returns the singleton instance of the Score_View.
     * If the instance does not exist, it is created.
     *
     * @param game_view .
     * @return The singleton instance of the Score_View.
     */
    public static Score_View getInstance(Game_View game_view) {
        if (instance == null) {
            instance = new Score_View(game_view);
        }
        return instance;
    }

    /**
     * Initializes the score view.
     * Set up the Vbox and empty the messages queue.
     */
    @FXML
    private void initialize() {
        panes = new VBox[]{pane0, pane1, pane2, pane3, pane4, pane5, pane6, pane7, pane8, pane9, pane10,
                pane11, pane12, pane13, pane14, pane15, pane16, pane17, pane18, pane19, pane20,
                pane21, pane22, pane23, pane24, pane25, pane26, pane27, pane28, pane29};
        started = true;

        while (!scoreUpdatesQueue.isEmpty()) {
            Pair<Stack<String>, Pair<Stack<Long>, Stack<Integer>>> scoreUpdate = scoreUpdatesQueue.poll();
            updateScore(scoreUpdate.getKey(), scoreUpdate.getValue().getKey(), scoreUpdate.getValue().getValue());
        }
    }
    /**
     * Updates the score with the given players, pings, and points.
     *
     * @param players The players to update the score for.
     * @param pings The pings of the players.
     * @param points The points of the players.
     */
    public void updateScore(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        if (!started) {
            scoreUpdatesQueue.add(new Pair<>(players, new Pair<>(pings, points)));
            return;
        }
        Platform.runLater(() -> {
            cleanBox();
            for (int i = 0; i < players.size(); i++) {
                String player = players.get(i);
                int point = points.get(i);
                Label label = new Label(player);
                label.setFont(Font.font("System", FontWeight.BOLD, 13)); // Set the font to bold
                VBox vbox = getPaneByPoint(point);
                vbox.getChildren().add(label);
            }
        });
    }
    /**
     * Cleans the leaderboard view by clearing all panes.
     */
    private void cleanBox() {
        for (VBox pane : panes) {
            pane.getChildren().clear();
        }
    }
    /**
     * Returns the pane associated with the given point.
     *
     * @param point The point to get the pane for.
     * @return The pane associated with the given point.
     */
    private VBox getPaneByPoint(int point) {

        if (point < 0 || point >= panes.length) {
            return panes[0];
        }
        return panes[point];
    }
    /**
     * Closes the score view.
     */
    public void close() {
        started = false;
    }
}
