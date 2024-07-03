package it.polimi.ingsw.am45.model.deck;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.listener.MarketListener;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.*;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.InitialCard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.ResourceCard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


/**
 * The Deck class represents the deck of cards in the game and manages the drawing of cards.
 * It implements the MarketListener interface to be able to interact with the Market.
 * <p>
 * The deck is composed of three stacks of cards: resource cards, gold cards and initial cards, and the information
 * about the cards is extracted from their json file.
 */
public class Deck implements MarketListener {

    private final int NUMBER_OF_FACE = 2;
    public final Stack<ResourceCard> deckResource = new Stack<>();
    public final Stack<GoldCard> deckGold = new Stack<>();
    public final Stack<InitialCard> deckInit = new Stack<>();
    public final Stack<ObjectiveCard> deckObjective = new Stack<>();
    private final String[] images = new String[NUMBER_OF_FACE];


    /**
     * Constructor of the Deck class that creates the decks of cards.
     */
    public Deck() {
        //first deck resource card
        try {
            createDeck("resourceDeck");
            createDeck("goldDeck");
            createDeck("initDeck");
            createObjectiveDeck();
        } catch (IOException e) {
            System.out.println("Error while creating the decks");
        }
        shuffle();
    }


    /**
     * Draw card from resource deck and return it
     *
     * @return the ResourceCard drawn from his deck
     */
    public PlayableCard drawResourceDeck() {
        if(deckResource.isEmpty())
            deckResource.add(new ResourceCard(0, new String[]{"end.png", "end.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "EMPTY"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.NONE));
        return deckResource.pop();
    }


    /**
     * Draw card from gold deck and return it
     *
     * @return the GoldCard drawn from his deck
     */
    public GoldCard drawGoldDeck() {
        if(deckGold.isEmpty()){
            GoldCard disabledCard = new GoldCard(0, new String[]{"end.png", "end.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "EMPTY"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.NONE, new InstantGoldCard(0));
            disabledCard.setCenter(new String[][]{{"EMPTY", "EMPTY", "EMPTY", "EMPTY"}, {"EMPTY"}});
            deckGold.add(disabledCard);
        }
        return deckGold.pop();
    }


    /**
     * Draw card from initial deck and return it
     *
     * @return the InitialCard drawn from his deck
     */
    public PlayableCard drawInitDeck() {
        return deckInit.pop();
    }

    public ObjectiveCard drawObjectiveDeck() {
        return deckObjective.pop();
    }

    /**
     * Shuffle all the decks of the game to randomize the cards.
     */
    public void shuffle() {
        Collections.shuffle(deckResource);
        Collections.shuffle(deckGold);
        Collections.shuffle(deckInit);
        Collections.shuffle(deckObjective);
    }

    /**
     * Get the drawable cards from the Market.
     *
     * @return The List of {@link PlayableCard} that can be drawn from the Market.
     */
    @Override
    public List<PlayableCard> getDrawableCards() {
        //return marketCards;
        return null;
    }

    @Override
    public void selectCardClick(int id) {
        //selectedCard = marketCards.get(id);
        //System.out.println(selectedCard.getCardId());
    }

    /**
     * Create all the decks of cards from a json file and push them into the stacks.
     *
     * @param deck the deck to be created (resource or gold).
     */
    private void createDeck(String deck) throws IOException {
        InputStream is = getClass().getResourceAsStream("/it/polimi/ingsw/am45/json/" + deck + ".json");
        if (is == null) {
            throw new FileNotFoundException("Cannot find resource: .json");
        }
        String deckJsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(deckJsonString);
        JSONArray jsonArray = json.getJSONArray(deck);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            //set images of the card (front and back)
            images[0] = object.getString("frontImagePath");
            images[1] = object.getString("backImagePath");
            int instantPoints = object.getInt("instantPoints");
            CardColor cardColor = extractCardColor(deck, object);

            //take data of the corners of the card
            JSONArray materialValuesArray = object.getJSONArray("materialValues");
            String[][] resourceCorners = new String[materialValuesArray.length()][3];
            ExtractResourceValue(materialValuesArray, resourceCorners);
            //take data of the center of the card
            materialValuesArray = object.getJSONArray("centerValues");
            String[][] resourceCenter = new String[materialValuesArray.length()][];
            ExtractResourceValue(materialValuesArray, resourceCenter);

            switch (deck) {
                case "resourceDeck":
                    ResourceCard card = new ResourceCard(object.getInt("id"), images, resourceCorners, cardColor);

                    card.setCenter(resourceCenter);
                    card.setInstantPoints(instantPoints);
                    deckResource.push(card);
                    break;
                case "goldDeck":
                    materialValuesArray = object.getJSONArray("cardCost");
                    String goldType = object.getString("goldCardType");
                    int[] cardCost = new int[materialValuesArray.length()];

                    for (int j = 0; j < materialValuesArray.length(); j++) {
                        cardCost[j] = materialValuesArray.getInt(j);
                    }

                    GoldCard goldCard = new GoldCard(object.getInt("id"), images, resourceCorners, cardColor, getGoldCardType(goldType, object, instantPoints));
                    goldCard.setCenter(resourceCenter);
                    goldCard.setCardCost(cardCost);
                    goldCard.setInstantPoints(instantPoints);
                    deckGold.push(goldCard);
                    break;
                case "initDeck":
                    InitialCard initialCard = new InitialCard(object.getInt("id"), images, resourceCorners, cardColor);

                    initialCard.setCenter(resourceCenter);
                    initialCard.setInstantPoints(instantPoints);
                    deckInit.push(initialCard);
                    break;
            }
        }
    }

    /**
     * Create the deck of objective cards from a json file and push them into the stack.
     *
     * @throws IOException if the file is not found
     */
    public void createObjectiveDeck() throws IOException {
        InputStream is = getClass().getResourceAsStream("/it/polimi/ingsw/am45/json/objectiveDeck.json");

        if (is == null) {
            throw new FileNotFoundException("Cannot find resource:  .json");
        }

        String deckJsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(deckJsonString);
        JSONArray jsonArray = json.getJSONArray("objectiveDeck");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String objImage = object.getString("imagePath");
            int cardId = object.getInt("id");
            int points = object.getInt("points");
            String objType = object.getString("objectiveType");

            ObjectiveCard objectiveCard = new ObjectiveCard(objImage, cardId, getObjectiveCardType(objType, object, points));
            deckObjective.push(objectiveCard);
        }
    }

    public Stack<GoldCard> getDeckGold() {
        return deckGold;
    }

    /**
     * Extract the card color of the card from the json file based on the deck.
     *
     * @param deck   the deck to be created (resource or gold).
     * @param object the object to be extracted from the json file.
     * @return the card color of the card to be extracted.
     */
    private static CardColor extractCardColor(String deck, JSONObject object) {
        if (deck.equals("initDeck")) {
            return CardColor.NONE;
        } else {
            return CardColor.valueOf(object.getString("cardColor"));
        }
    }

    /**
     * Extract the gold card type of the card from the json file based on the gold type.
     *
     * @param goldType      the String type of the gold card to be extracted.
     * @param object        the object to be extracted from the json file to get the gold card type from.
     * @param instantPoints the instant points of the gold card to be extracted.
     * @return the gold card type to be extracted from the json file.
     */
    private GoldCardType getGoldCardType(String goldType, JSONObject object, int instantPoints) {
        return switch (goldType) {
            case "resource" -> {
                int[] resourceRequired = object.getJSONArray("pointMultiplier")
                        .toList()
                        .stream()
                        .mapToInt(j -> (int) j)
                        .toArray();
                yield new ResourceGoldCard(resourceRequired);
            }
            case "angle" -> new CoveredAngleGoldCard(instantPoints);
            case "instant" -> new InstantGoldCard(instantPoints);
            default -> null;
        };
    }

    /**
     * Extract the objective card type of the card from the json file based on the objective type.
     *
     * @param objectiveType the type of the objective card to be extracted.
     * @param object        the object to be extracted from the json file to get the objective card type from.
     * @param points        the points of the objective card to be extracted.
     * @return the objective card type to be extracted from the json file.
     */
    private ObjectiveCardType getObjectiveCardType(String objectiveType, JSONObject object, int points) {
        switch (objectiveType) {
            case "resourceObjectiveCard":
                int[] resourceRequired = object.getJSONArray("resources")
                        .toList()
                        .stream()
                        .mapToInt(j -> (int) j)
                        .toArray();
                return new ResourceObjectiveCard(resourceRequired, points);
            case "lShapeObjectiveCard":
                CardColor baseColor = CardColor.valueOf(object.getString("baseColor"));
                CardColor columnColor = CardColor.valueOf(object.getString("columnColor"));
                String lShapeDirection = object.getString("direction");
                return new LShapeObjectiveCard(baseColor, columnColor, lShapeDirection, points);
            case "diagonalObjectiveCard":
                CardColor objectiveColor = CardColor.valueOf(object.getString("color"));
                String dShapeDirection = object.getString("direction");
                return new DiagonalObjectiveCard(objectiveColor, points, dShapeDirection);
        }
        return null;
    }

    /**
     * Extract the corner values of the card from the json file.
     *
     * @param materialValuesArray the array of the values of the card corners to be extracted from the json file.
     * @param resourceCorners     the array corners of the card to be filled.
     */
    private void ExtractResourceValue(JSONArray materialValuesArray, String[][] resourceCorners) {
        for (int j = 0; j < materialValuesArray.length(); j++) {
            JSONArray inArray = materialValuesArray.getJSONArray(j);
            resourceCorners[j] = new String[inArray.length()];
            for (int k = 0; k < inArray.length(); k++) {
                resourceCorners[j][k] = inArray.getString(k);
            }
        }
    }
}
