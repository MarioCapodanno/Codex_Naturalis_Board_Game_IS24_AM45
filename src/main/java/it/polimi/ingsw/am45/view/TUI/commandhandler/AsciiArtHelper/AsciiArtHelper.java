package it.polimi.ingsw.am45.view.TUI.commandhandler.AsciiArtHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class provides helper methods for handling ASCII art related to objective cards.
 * It includes methods to retrieve ASCII art for a card, print ASCII art for all cards,
 * print descriptions for all cards, and print the ASCII representation of the common goal.
 */
public class AsciiArtHelper {

    /**
     * Retrieves the ASCII art for a card.
     * This method takes a JSON object representing the card and a card number as parameters.
     * It returns a list of strings representing the ASCII art of the card.
     *
     * @param jsonObject The JSON object representing the card.
     * @param cardNumber The number of the card.
     * @return A list of strings representing the ASCII art of the card.
     */
    public List<String> getAsciiArtObjectiveCard(JSONObject jsonObject, int cardNumber) {
        JSONArray asciiArray = jsonObject.getJSONArray("ASCII");
        List<String> asciiArt = new ArrayList<>();
        asciiArt.add("               #" + cardNumber + "              ");
        for (int j = 0; j < asciiArray.length(); j++) {
            asciiArt.add(asciiArray.getString(j));
        }
        return asciiArt;
    }

    public List<String> getAsciiArt(JSONObject jsonObject) {
        JSONArray asciiArray = jsonObject.getJSONArray("ASCIIfront");
        List<String> asciiArt = new ArrayList<>();
        for (int j = 0; j < asciiArray.length(); j++) {
            asciiArt.add(asciiArray.getString(j));
        }
        return asciiArt;
    }

    public List<String> getSmallASciiArt(JSONObject jsonObject){
        JSONArray asciiArray = jsonObject.getJSONArray("ASCIIfront");
        List<String> asciiArt = new ArrayList<>();
        for (int j = 0; j < asciiArray.length(); j++) {
            asciiArt.add(asciiArray.getString(j));
        }
        return asciiArt;
    }

    /**
     * Prints the ASCII art for all cards.
     * This method takes a list of ASCII art for each card as a parameter.
     * It prints the ASCII art for all cards to the console.
     *
     * @param asciiArtList A list of ASCII art for each card.
     */
    public void printAsciiArt(List<List<String>> asciiArtList) {
        for (int i = 0; i < asciiArtList.get(0).size(); i++) {
            for (List<String> asciiArt : asciiArtList) {
                System.out.print("    " + asciiArt.get(i));
            }
            System.out.println();
        }
    }

    /**
     * Prints the descriptions for all cards.
     * This method takes a list of descriptions for each card as a parameter.
     * It prints the descriptions for all cards to the console.
     *
     * @param descriptionList A list of descriptions for each card.
     */
    public void printDescriptions(List<String> descriptionList) {
        int cardNumber = 0;
        for (String description : descriptionList) {
            cardNumber++;
            System.out.println(" #" + cardNumber + " Description: " + description);
        }
    }

    /**
     * Prints the ASCII representation of the common goal.
     * This method takes a list of common goals as a parameter.
     * It prints the ASCII representation of the common goal to the console.
     *
     * @param commonGoal A list of common goals.
     */
    public void printCommonGoalASCII(List<String> commonGoal) {
        try (InputStream is = getClass().getResourceAsStream("/it/polimi/ingsw/am45/json/objectiveDeck.json")) {
            String deckJsonString = new String(Objects.requireNonNull(is).readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(new JSONTokener(deckJsonString));
            JSONArray jsonArray = json.getJSONArray("objectiveDeck");

            List<List<String>> asciiArtList = new ArrayList<>();
            List<String> descriptionList = new ArrayList<>();

            for (String goal : commonGoal) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("imagePath");
                    if (name.equals(goal)) {
                        if (jsonObject.has("ASCII")) {
                            asciiArtList.add(getAsciiArtObjectiveCard(jsonObject, asciiArtList.size() + 1));
                        }
                        descriptionList.add(jsonObject.getString("description"));
                        break;
                    }
                }
            }

            printAsciiArt(asciiArtList);
            printDescriptions(descriptionList);

        } catch (Exception e) {
            System.out.println("Personal goal info not found.");
        }
    }

    public void printInitialCardASCII(String initialCard) {
        try (InputStream is = getClass().getResourceAsStream("/it/polimi/ingsw/am45/json/initDeck.json")) {
            String deckJsonString = new String(Objects.requireNonNull(is).readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(new JSONTokener(deckJsonString));
            JSONArray jsonArray = json.getJSONArray("initDeck");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = "";
                if (initialCard.contains("back")) { name = jsonObject.getString("backImagePath");}
                else { name = jsonObject.getString("frontImagePath");}
                if (name.equals(initialCard)) {
                    if (initialCard.contains("front")) {
                        JSONArray asciiArray = jsonObject.getJSONArray("ASCIIfront");
                        List<String> asciiArt = new ArrayList<>();
                        for (int j = 0; j < asciiArray.length(); j++) {
                            asciiArt.add(asciiArray.getString(j));
                        }
                        for (String line : asciiArt) {
                            System.out.println("                     " + line);
                        }
                    } else {
                        JSONArray asciiArray = jsonObject.getJSONArray("ASCIIback");
                        List<String> asciiArt = new ArrayList<>();
                        for (int j = 0; j < asciiArray.length(); j++) {
                            asciiArt.add(asciiArray.getString(j));
                        }
                        for (String line : asciiArt) {
                            System.out.println("                     " + line);
                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Initial card info not found.");
        }
    }

    //TODO: add a parameter to this method to choose if get normal ascii or small ascii
    public ArrayList<String> printPlayableCardASCII(String card) {
        ArrayList<String> output = new ArrayList<>();
        if (card.contains("back")) {
           output.addAll(printBackPlayableCardASCII(card));
           return output;
        }
        int cardId = Integer.parseInt(card.replace(".png", ""));
        JSONArray jsonArray = new JSONArray();
        if (cardId >= 1101 && cardId <= 1410) {
            jsonArray = getDeckJsonString("/it/polimi/ingsw/am45/json/resourceDeck.json");
        } else if (cardId >= 2101 && cardId <= 2410) {
            jsonArray = getDeckJsonString("/it/polimi/ingsw/am45/json/goldDeck.json");
        } else {
            System.out.println("Invalid ID.");
        }


        for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            if (id == cardId) {
                if (jsonObject.has("ASCIIfront")) {
                    List<String> asciiArt = getAsciiArt(jsonObject);
                    String cardColor = jsonObject.getString("cardColor");
                    String ansiColor = getAnsiColor(cardColor);
                    for (String line : asciiArt) {
                        output.add(ansiColor + line + AnsiColor.RESET);
                    }
                }
                break;
            }
        }
        return output;
    }

    private ArrayList<String> printBackPlayableCardASCII(String card) {
        // Print the back of the card
        ArrayList<String> output = new ArrayList<>();
        String cardPath = card.replace(".png", "");
        JSONArray jsonArray;
        if (cardPath.contains("resource")) {
            jsonArray = getDeckJsonString("/it/polimi/ingsw/am45/json/resourceDeck.json");
        } else {
            jsonArray = getDeckJsonString("/it/polimi/ingsw/am45/json/goldDeck.json");
        }

        for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String imagePath = jsonObject.getString("backImagePath");
            if (imagePath.contains(card)) {
                if (jsonObject.has("ASCIIback")) {
                    JSONArray asciiArray = jsonObject.getJSONArray("ASCIIback");
                    List<String> asciiArt = new ArrayList<>();

                    for (int j = 0; j < asciiArray.length(); j++) {
                        asciiArt.add(asciiArray.getString(j));
                    }

                    String cardColor = jsonObject.getString("cardColor");
                    String ansiColor = getAnsiColor(cardColor);

                    for (String line : asciiArt) {
                        output.add(ansiColor + line + AnsiColor.RESET);
                    }
                    break;
                }
            }
        }
        return output;
    }

    private JSONArray getDeckJsonString(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            String deckJsonString = new String(Objects.requireNonNull(is).readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(new JSONTokener(deckJsonString));

            if (path.contains("resource")) {
                return json.getJSONArray("resourceDeck");
            } else {
                return json.getJSONArray("goldDeck");
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("Deck info not found.");
            return null;
        }
    }

    private String getAnsiColor(String cardColor) {
        return switch (cardColor.toUpperCase()) {
            case "RED" -> AnsiColor.RED;
            case "GREEN" -> AnsiColor.GREEN;
            case "YELLOW" -> AnsiColor.YELLOW;
            case "VIOLET" -> AnsiColor.PURPLE;
            case "BLUE" -> AnsiColor.CYAN;
            default -> AnsiColor.RESET;
        };
    }

}