package it.polimi.ingsw.am45.model;

import it.polimi.ingsw.am45.chat.Chat;
import it.polimi.ingsw.am45.controller.GamesController;
import it.polimi.ingsw.am45.enumeration.GameState;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.exception.InvalidNumberOfPlayersException;
import it.polimi.ingsw.am45.model.boards.GameBoard;
import it.polimi.ingsw.am45.model.boards.ScoreBoard;
import it.polimi.ingsw.am45.model.deck.Deck;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ObjectiveCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;


/**
 * Game is the class that represents the game itself. It contains all the information about the game and is the main
 * class that manages the game.
 * It contains the players, the decks, the current turn, the game status and
 */

public class Game {
    private static final int INITIAL_POSITION_CARD = 3;

    private int numberOfPlayers = 0;
    private int currentTurn;
    private final List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private List<String> winnerPlayers;
    private boolean lastTurn;
    private GameState gameStatus;
    private final int gameID;
    private final Deck deck;
    private final GameBoard gameBoard;
    private final Chat chat;
    private final ScoreBoard scoreBoard;
    private final int maxPlayersNum;
    private boolean started = false;


    /**
     * Constructor for the Game class.
     *
     * @param gameID    The ID of the game.
     * @param playerNum The number of players in the game.
     */
    public Game(int gameID, int playerNum) throws InvalidNumberOfPlayersException {
        if (playerNum < 2 || playerNum > 4) {
            throw new InvalidNumberOfPlayersException();
        }
        this.deck = new Deck();
        this.chat = new Chat(this);
        this.gameBoard = new GameBoard(deck);
        this.scoreBoard = new ScoreBoard(playerNum);
        this.maxPlayersNum = playerNum;
        this.gameID = gameID;
        this.currentTurn = 0;
        this.gameStatus = GameState.CREATED;
        this.winnerPlayers = new ArrayList<>();
        //For debugging purposes, must be removed
        System.out.println("-----Game created " + gameID + " ... " + playerNum);
    }


    /**
     * Adds a player to the game
     *
     * @param player The player to be added.
     */
    public void addPlayer(Player player) throws IllegalArgumentException {
        if (numberOfPlayers == maxPlayersNum) {
            throw new IllegalArgumentException("The game is full");
        }
        if (numberOfPlayers == 0)
            this.currentPlayer = player;
        this.numberOfPlayers += 1;
        this.players.add(player);
        //For debugging purposes, must be removed
        System.out.println("-------Player added: " + player.nickname + " to game: " + this);
    }

    /**
     * Method to start the game with the initialization of the hands for each player,
     * the Market and the turns of the players.
     */
    public void startGame() {
        if (!started) {
            for (Player player : players) {
                player.getHand().addCard(deck.drawResourceDeck());
                player.getHand().addCard(deck.drawResourceDeck());
                player.getHand().addCard(deck.drawGoldDeck());
                player.getHand().setupInitCard(deck.drawInitDeck());
                player.getHand().setupObjChoice(deck.drawObjectiveDeck(), deck.drawObjectiveDeck());
                GamesController.getInstance().sendHand(player.nickname, this, getHandsCardsFromPlayer(player));
                GamesController.getInstance().sendChoice(player.nickname, this, player.getHand().initialCard.getCurrFace(), player.getHand().objectiveCard1.getImage(), player.getHand().objectiveCard2.getImage());
            }
            gameStatus = GameState.CHOICES;
            setMarket();
            started = true;
        }
    }


    /**
     * Disconnects a player from the current game and deletes the game if it is empty.
     *
     * @param player The player associated with the game to be disconnected.
     */
    public void disconnectPlayer(Player player) {
        this.numberOfPlayers -= 1;
        int playerPosition = this.players.indexOf(player);


        this.players.remove(player);
        if (player.equals(getCurrentPlayer()) && numberOfPlayers != 0) {
            currentPlayer = (playerPosition >= players.size()) ? players.get(0) : players.get(playerPosition);
            setTurns();
        }


        if (numberOfPlayers == 1) {
            if(gameStatus != GameState.CREATED)
                endGame();
        }
        if (numberOfPlayers == 0) {
            System.out.println("Game " + gameID + " is empty, deleting it");
            GamesController.getInstance().deleteGame(gameID);
        }

    }

    /**
     * Method to switch the turn to the next player.
     */
    public void nextTurn() {
        if (currentPlayer.equals(players.getLast())) {
            if (currentPlayer.getScore() >= 20 || lastTurn) {
                System.out.println("Begin of the end game phase");
                endGame();
            }
            currentTurn++;
            currentPlayer = players.getFirst();
        } else {
            if (currentPlayer.getScore() >= 20) {
                lastTurn = true;
                System.out.println("Last turn activated");
            }
            currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
        }
        setTurns();

    }


    /**
     * Method to calculate all the points from the public and personal
     * objective card after the last turn. It also declares the winner.
     */
    public void endGame() {
        System.out.println("End of the game");
        gameStatus = GameState.END;
        players.forEach(player -> {
            ObjectiveCard selectedObjectiveCard = player.getHand().getSelectedObjectiveCard();
            int objPointMade = 0;
            if (selectedObjectiveCard != null) {
                objPointMade = selectedObjectiveCard.getPointsMade(player);
            }
            objPointMade += gameBoard.getCommonObjectiveCard().stream().mapToInt(card -> card.getPointsMade(player)).sum();
            player.advancePlayerPos(objPointMade);
        });

        int maxPoints = players.stream().mapToInt(Player::getScore).max().orElse(0);
        winnerPlayers = players.stream().filter(player -> player.getScore() == maxPoints).map(Player::getNickname).collect(Collectors.toList());
        winnerPlayers.forEach(winner -> System.out.println("Winner: " + winner + " with score: " + getPlayerByNickname(winner).getScore()));
        GamesController.getInstance().winnerUpdate(this, winnerPlayers);
        GamesController.getInstance().deleteGame(gameID);

    }

    /**
     * @return The current playing player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method to set the turns of the players in the game and send the information to the players.
     */
    public void setTurns() {
        GamesController.getInstance().turnsUpdate(this, currentPlayer.nickname);

    }

    /**
     * @return boolean value if the game is started or not.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * @param nickname The nickname of the player to be found.
     * @return The player with the given nickname or null if the player is not found.
     */
    public Player getPlayerByNickname(String nickname) throws IllegalArgumentException {
        for (Player player : players) {
            if (player.nickname.equals(nickname)) {
                return player;
            }
        }
        throw new IllegalArgumentException("Player not found");
    }

    /**
     * Method to check if the game has the desired number of players chose by the first player.
     *
     * @return boolean value if the number of players is equal to the maximum number of players.
     */
    public boolean isReadyToStart() {
        return numberOfPlayers == maxPlayersNum;
    }

    /**
     * @return a stack with the nicknames of the players in the game, in the order of their turns.
     */
    public Stack<String> getPlayerNicknames() {
        Stack<String> nicknames = new Stack<>();
        for (Player player : players) {
            nicknames.add(player.nickname);
        }
        return nicknames;
    }

    /**
     * @param player The player to get the Hand cards from.
     * @return The list of the cards in the player's Hand.
     */
    public List<String> getHandsCardsFromPlayer(Player player) {
        List<String> cards = new ArrayList<>();
        cards.add(player.getHand().getHandCards().get(0).getCurrFace());
        cards.add(player.getHand().getHandCards().get(1).getCurrFace());
        cards.add(player.getHand().getHandCards().get(2).getCurrFace());

        return cards;
    }

    /**
     * set the Market cards for the game: adding the cards to the Market.
     */
    public void setMarket() {
        GamesController.getInstance().marketUpdate(this, gameBoard.getMarketCards());

    }

    /**
     * @param nick The nickname of the player to be checked.
     * @return boolean value if the player is in the game or not.
     */
    public boolean containsNickname(String nick) {
        for (Player player : players) {
            if (player.nickname.equals(nick)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param color The color of the player to be checked.
     * @return boolean value if the color is being used or not
     */
    public boolean containsColor(TokenColor color) {
        for (Player player : players) {
            if (player.tokenColor==color) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return The list of the players that won the game.
     */
    public List<String> getWinnerPlayer() {
        return winnerPlayers;
    }

    /**
     * The method to flip a card in the player's hand.
     *
     * @param position The position of the card to be flipped.
     * @param nick     The nickname of the player that flipped the card.
     */
    public void flipCard(int position, String nick) {
        if (position == 3) {
            getPlayerByNickname(nick).getHand().initialCard.flip();
            GamesController.getInstance().sendChoice(nick, this, getPlayerByNickname(nick).getHand().initialCard.getCurrFace(), getPlayerByNickname(nick).getHand().objectiveCard1.getImage(), getPlayerByNickname(nick).getHand().objectiveCard2.getImage());
            return;
        }
        getPlayerByNickname(nick).getHand().flipCard(position);
        GamesController.getInstance().sendHand(nick, this, getHandsCardsFromPlayer(getPlayerByNickname(nick)));

    }

    /**
     * The method to flip a card in the Market.
     *
     * @param position The position of the card to be flipped.
     * @param nick     The nickname of the player that flipped the card.
     */
    public void flipMarketCard(int position, String nick) {
        gameBoard.getCard(position).flip();
        setMarket();

    }

    /**
     * The method to draw a card from the {@link GameBoard} of the game.
     *
     * @param position The position of the card to be drawn.
     * @param nick     The nickname of the player that drew the card.
     */
    public void drawCard(int position, String nick) {
        getPlayerByNickname(nick).getHand().addCard(gameBoard.getCard(position));
        gameBoard.updateDeckCards(position);
        if(gameBoard.checkEmptyMarket()){
            lastTurn = true;
            System.out.print("Last turn activated, market is empty.");
        }
        GamesController.getInstance().sendHand(nick, this, getHandsCardsFromPlayer(getPlayerByNickname(nick)));
        setMarket();

    }

    /**
     * The method to choose the secret objective card of the player from the two given.
     *
     * @param position The position of the card to be drawn.
     * @param nick     The nickname of the player that drew the card.
     */
    public void chooseObj(Boolean position, String nick) {
        getPlayerByNickname(nick).getHand().setObj(position);

    }

    /**
     * The method to play a card from the player's hand to the game board in the given location-
     *
     * @param position The position of the card in the player's hand to be played.
     * @param location The location where the card is played.
     * @param nick     The nickname of the player that played the card.
     * @return boolean value if the card was played or not.
     */
    public boolean playCard(int position, Location location, String nick) {
        int playerPos = players.indexOf(getPlayerByNickname(nick));
        if (position == INITIAL_POSITION_CARD) {
            if (getPlayerByNickname(nick).addPlayedCard(location, getPlayerByNickname(nick).getHand().initialCard, scoreBoard, playerPos)) {
                GamesController.getInstance().sendResource(nick, this, getPlayerByNickname(nick).getResources());
                checkState();
                return true;
            }
            return false;
        }
        if (getPlayerByNickname(nick).addPlayedCard(location, getPlayerByNickname(nick).getHand().getHandCards().get(position), scoreBoard, playerPos)) {
            System.out.println("Playing the hand card at the position: " + position);
            GamesController.getInstance().sendResource(nick, this, getPlayerByNickname(nick).getResources());
            return true;
        }
        return false;
    }

    /**
     * The method to send a message to the chat of the game.
     *
     * @param message The message to be sent to the chat.
     */
    public void sendMessage(String message) {
        chat.addMessage(message);
        GamesController.getInstance().chatUpdate(this, chat.getMessages());

    }

    /**
     * @return The {@link Deck} of the current game.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return The {@link ScoreBoard} of the current game.
     */
    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    /**
     * @return The list of the players in the game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    public void checkState() {
        boolean allInGameStateGame = players.stream()
                .allMatch(player -> player.getState() == GameState.GAME);

        if (allInGameStateGame)
            setTurns();

    }

    public int getGameID() {
        return gameID;
    }
}


