package it.polimi.ingsw.am45.model.player;

import it.polimi.ingsw.am45.enumeration.GameState;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.model.boards.PlayerBoard;
import it.polimi.ingsw.am45.model.boards.ScoreBoard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.utilities.Location;

import java.util.List;
import java.util.Stack;


/**
 * Player class represents a player in the game.
 */
public class Player {
    /**
     * The player's game info
     */
    public final String nickname;
    public final TokenColor tokenColor;
    /**
     * The player's board where he places his cards
     */
    private final PlayerBoard playerBoard;
    /**
     * The stack of played cards ordered by the time they were played
     */
    private final Stack<PlayableCard> playedCards;

    private final Hand hand;
    /**
     * The position of the player in the scoreboard (equal to the points made by the player in the game
     */
    private int scoreBoardPos;
    private GameState gameState;

    /**
     * Player constructor
     *
     * @param nickname of the player
     */
    public Player(String nickname, TokenColor color) {
        this.tokenColor = color;
        this.gameState = GameState.CHOICES;
        this.nickname = nickname;
        this.hand = new Hand();
        this.playerBoard = new PlayerBoard();
        this.playedCards = new Stack<>();
        this.scoreBoardPos = 0;

    }

    /**
     * @return The current player score.
     */
    public int getScore() {
        return scoreBoardPos;
    }

    /**
     * @return The list of {@link it.polimi.ingsw.am45.enumeration.Resource} collected by the player.
     */
    public List<Integer> getResources() {
        return playerBoard.getResources();
    }

    public boolean addPlayedCard(Location location, PlayableCard card, ScoreBoard scoreBoard, int playerPos) {

        if (playerBoard.hasCard(location.x, location.y) || !canPlaceCard(location, card)) {
            return false;
        }

        gameState = GameState.GAME;
        advancePlayerPos(card.getInstantPoints());
        advancePlayerPos(card.getInstantPoints(this, location));
        scoreBoard.updateScoreBoard(playerPos, this.scoreBoardPos);
        playerBoard.placeCard(location.x, location.y, card);
        hand.removePlayedCard(card);
        playedCards.push(card);
        playerBoard.addResources(card.getCurrCorners());
        playerBoard.addResources(card.getCurrCenter());
        return true;
    }

    /**
     * @return The class Hand associated with this class.
     */
    public Hand getHand() {
        return hand;
    }


    /**
     * @return The player's nickname as a string.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param points The points to add to the player's score.
     */
    public void advancePlayerPos(int points) {
        scoreBoardPos += points;
    }

    /**
     * @return The {@link PlayerBoard} object of the player instance.
     */
    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    /**
     * @return The stack of {@link PlayableCard} objects played by the player.
     */
    public Stack<PlayableCard> getPlayedCards() {
        return playedCards;
    }

    /**
     * @param location The location where the card is going to be placed.
     * @param card     The card that is going to be placed.
     * @return True if the card can be placed in the location, false otherwise.
     */
    boolean canPlaceCard(Location location, PlayableCard card) {
        if (playedCards.isEmpty()) {
            return true;
        }

        if (card.getRequirements() != null && !playerBoard.checkRequirements(card.getRequirements())) {
            return false;
        }

        return !((playerBoard.hasCard(location.x + 1, location.y) && playerBoard.getCardFromLocation(location.x + 1, location.y).getCorner(0).isEmpty()) ||
                (playerBoard.hasCard(location.x - 1, location.y) && playerBoard.getCardFromLocation(location.x - 1, location.y).getCorner(2).isEmpty()) ||
                (playerBoard.hasCard(location.x, location.y + 1) && playerBoard.getCardFromLocation(location.x, location.y + 1).getCorner(1).isEmpty()) ||
                (playerBoard.hasCard(location.x, location.y - 1) && playerBoard.getCardFromLocation(location.x, location.y - 1).getCorner(3).isEmpty()));
    }

    public GameState getState() {
        return gameState;
    }


}
