package it.polimi.ingsw.am45.model.deck.cards.playableCards;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.model.deck.cards.Card;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;


/**
 * Class of playable cards (Resource, Gold and Initial)
 */
public class PlayableCard extends Card {
    private final CardColor cardColor;
    /**
     * Corners of the card: [topLeft, topRight, bottomLeft, bottomRight]
     */
    private final String[][] corners;
    /**
     * Images of the card: [front, back]
     */
    private final String[] images;
    /**
     * Center of the card: [frontCardCenter, backCardCenter]
     */
    private String[][] center;
    /**
     * Points of the card to be added to the player's score when the card is played
     */
    private int instantPoints;
    private String[] currCorners;
    private String currFace;
    private String[] currCenter;
    private int currentPoints = 0;
    private Location location = null;

    /**
     * Constructor of the class
     *
     * @param cardId  id of the card
     * @param images  front and back images of the card
     * @param corners of the card: [topLeft, topRight, bottomLeft, bottomRight]
     */
    public PlayableCard(int cardId, String[] images, String[][] corners, CardColor cardColor) {
        this.images = images.clone();
        this.currFace = this.images[0];
        this.corners = corners.clone();
        this.currCorners = this.corners[0];
        this.cardColor = cardColor;
        this.setCardId(cardId);
    }



    /**
     * Set the location of the card at the specified position
     *
     * @param x position of the card
     * @param y position of the card
     */
    public void setLocation(int x, int y) {
        this.location = new Location(x, y);

    }

    /**
     * Return the location of the card
     *
     * @return the location of the card
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Get the current face of the card as a string.
     *
     * @return The String representation of the current card face.
     */
    public String getCurrFace() {
        return currFace;
    }

    /**
     * Flip the card to the other side and return the image of the flipped card
     *
     * @return the face image of the flipped card
     */
    public String flip() {
        if(currFace.equals("end.png"))
            return "end.png";
        if (currentPoints != 0)
            currentPoints = 0;
        else
            currentPoints = instantPoints;
        int index = this.currFace.equals(this.images[0]) ? 1 : 0;
        this.currCorners = this.corners[index];
        this.currCenter = this.center[index];
        this.currFace = this.images[index];
        return this.currFace;
    }

    /**
     * Set the center resource of the card
     *
     * @param center the center data of front and back
     */
    public void setCenter(String[][] center) {
        this.center = center.clone();
        this.currCenter = center[0].clone();
    }

    /**
     * Method to get the corner data of the card in the specified position.
     *
     * @param angles position of the corner data to be returned.
     * @return the corner data in the specified position.
     */
    public String getCorner(int angles) {
        return this.currCorners[angles];
    }

    /**
     * Method to set the point resource of the card.
     *
     * @param instantPoints of the card to be set.
     */
    public void setInstantPoints(int instantPoints) {
        this.instantPoints = instantPoints;
        this.currentPoints = instantPoints;
    }

    /**
     * @return the points of the card to be added to the player's score when the card is played.
     */
    public int getInstantPoints() {
        return this.currentPoints;
    }

    public int getInstantPoints(Player player, Location location) {
        return 0;
    }

    /**
     * Method to get the resource in the center of the card.
     *
     * @return Array of String containing the center data of the card.
     */
    public String[] getCurrCenter() {
        return currCenter;
    }

    /**
     * Method to get all the corners from the current face of the card.
     *
     * @return Array of String containing the corner data of the card.
     */
    public String[] getCurrCorners() {
        return currCorners;
    }

    /**
     * Get the requirements of the card
     *
     * @return the cost in resources of the card
     */
    public int[] getRequirements() {
        return null;
    }

    /**
     * Method to get the card color of the card.
     *
     * @return the {@link CardColor} associated with the card
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    public String[] getImages() {
        return images;
    }



}
