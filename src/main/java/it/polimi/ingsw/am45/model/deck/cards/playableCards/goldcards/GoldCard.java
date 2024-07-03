package it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;

/**
 * Class to manage the Gold Cards in the game
 */
public class GoldCard extends PlayableCard {
    /**
     * Points of the card to be used for the calculation of the total points.
     */
    private int points;
    /**
     * Cost of the GoldCard to be placed in the Market
     */
    private int[] cost;
    /**
     * Current cost of the GoldCard to be placed in the Market
     */
    private int[] currCost;
    /**
     * GoldCardType of the GoldCard, it can be ResourceGoldCard, CoveredAngleGoldCard, or InstantGoldCard.
     * It is used to calculate the total points made by the player after placing the gold card.
     */
    private final GoldCardType goldCardType;
    private final String currFace;

    /**
     * Constructor for the Gold Card.
     *
     * @param cardId  id of the card.
     * @param images  front and back images of the card.
     * @param corners of the card [topLeft, topRight, bottomLeft, bottomRight].
     */
    public GoldCard(int cardId, String[] images, String[][] corners, CardColor cardcolor, GoldCardType goldCardType) {
        super(cardId, images, corners, cardcolor);
        this.goldCardType = goldCardType;
        this.currFace = images[0];
    }


    /**
     * This method returns the current requirements cost of the card.
     *
     * @return Array integer of resources cost of the card.
     */
    @Override
    public int[] getRequirements() {
        return this.currCost;
    }

    /**
     * This method sets the cost of the card.
     *
     * @param cost Array of integers representing the resource cost of the card.
     */
    public void setCardCost(int[] cost) throws IllegalArgumentException {
        if (cost.length != 7)
            throw new IllegalArgumentException("The cost array must have 7 elements");
        else {
            this.cost = cost.clone();
            this.currCost = cost.clone();
        }
    }

    /**
     * Method to flip the card, changing its resource cost to play.
     *
     * @return String representing the new face of the card flipped.
     */
    @Override
    public String flip() {
        if(currFace.equals("end.png"))
            return "end.png";
        if (currCost != null)
            currCost = null;
        else
            currCost = cost.clone();
        return super.flip();
    }

    /**
     * This method sets the points of the card to be used for the calculation of the total points.
     *
     * @param points of the card to be set.
     */
    public void setInstantPoints(int points) {
        this.points = points;
    }


    /**
     * This method returns the points of the card based on GoldCardType.
     *
     * @param player   the player that placed the gold card and that will gain the points.
     * @param location the location of the card placed in the grid.
     * @return The total points made by the player after placing the gold card.
     */
    public int getInstantPoints(Player player, Location location) {

        if (this.currCost == null) {
            return 0;
        }
        //check if goldCardType is CoveredAngleGoldCard
        if (this.goldCardType instanceof CoveredAngleGoldCard) {
            return ((CoveredAngleGoldCard) this.goldCardType).totalPoints(player, location);
        } else return this.goldCardType.totalPoints(player, points);
    }

    public Object getCardType() {
        return goldCardType;
    }
}
