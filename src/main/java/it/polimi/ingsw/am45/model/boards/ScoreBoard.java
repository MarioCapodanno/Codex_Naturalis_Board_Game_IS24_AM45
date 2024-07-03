package it.polimi.ingsw.am45.model.boards;


/**
 * The ScoreBoard class represents a scoreboard for a game.
 * It maintains the scores of each player in a 2D array.
 * The number of players can range from 2 to 4.
 */
public class ScoreBoard {
    private final int[][] scores; // 2D array to store scores
    private final int numPlayers; // Number of players

    /**
     * Constructor for the Scoreboard class.
     * Initializes the score array based on the number of players.
     *
     * @param numPlayers The number of players in the game.
     * @throws IllegalArgumentException if the number of players is not between 2 and 4.
     */
    public ScoreBoard(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 4) {
            System.out.println("Number of players must be between 2 and 4.");
            throw new IllegalArgumentException("Number of players must be between 2 and 4.");
        }
        this.numPlayers = numPlayers;
        this.scores = new int[numPlayers][30]; // Initialize scores array
        //Initialize scores array to 0 and 1 to initial score
        for (int i = 0; i < numPlayers; i++) {
            for (int j = 0; j < 30; j++) {
                scores[i][j] = 0;
            }
            scores[i][0] = 1; // Initial score
        }
    }
    
    /**
     * Updates the score for a player.
     *
     * @param player The player number (based on the order of joining the game).
     * @param score  The new score for the player.
     * @throws IllegalArgumentException if the player number is not valid or the score is not between 0 and 29.
     */
    public void updateScoreBoard(int player, int score) {
        if (player < 0 || player >= numPlayers) {
            System.out.println("Invalid player number is" + player);
            throw new IllegalArgumentException("Invalid player number.");
        }
        if (score < 0 || score > 29) {
            throw new IllegalArgumentException("Score must be between 0 and 29.");
        }
        //update score and set to 0 the previous score
        for (int i = 0; i < score; i++) {
            scores[player][i] = 0;
        }
        scores[player][score] = 1; // Update score
    }


    /**
     * Gets the score for a player.
     *
     * @param player The player number (0-based).
     * @return The score for the player.
     * @throws IllegalArgumentException if the player number is not valid.
     */
    public int getScore(int player) {
        if (player < 0 || player >= numPlayers) {
            throw new IllegalArgumentException("Invalid player number.");
        }
        for (int i = 29; i >= 0; i--) {
            if (scores[player][i] == 1) {
                return i; // Return the highest score
            }
        }
        return 0; // Default score
    }

    public void printScoreBoard() {
        for (int i = 0; numPlayers > i; i++) {
            System.out.print("Player " + i + ": ");
            for (int j = 0; j < 30; j++) {
                if (scores[i][j] == 1)
                    System.out.println(j + ":[X] |");
                else System.out.print(j + ":[] |");
            }
            System.out.println();
        }
    }
}