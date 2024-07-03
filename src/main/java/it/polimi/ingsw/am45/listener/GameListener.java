package it.polimi.ingsw.am45.listener;

/**
 * Listener for communication between the game view and the model
 */
public interface GameListener {

    /**
     * @param id position of the card in the Hand
     */
    void selectCard(int id);

    /**
     * @param id position of the cards in the Hand.
     * @return The string image of the card flipped.
     */
    String flipCard(int id);


    /**
     * @param id position of the cards in the Hand
     * @return the string of the image of the card
     */
    String getCardPng(int id);

}
