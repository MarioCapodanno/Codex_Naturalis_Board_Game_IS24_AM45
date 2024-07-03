package it.polimi.ingsw.am45.controller;


import it.polimi.ingsw.am45.connection.rmi.RMIController;
import it.polimi.ingsw.am45.connection.socket.server.ServerHandler;
import it.polimi.ingsw.am45.controller.client.cts.ClientToServer;
import it.polimi.ingsw.am45.controller.server.stc.*;
import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.exception.InvalidNumberOfPlayersException;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.CardData;
import it.polimi.ingsw.am45.utilities.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * This class is responsible for managing the games in the application.
 * It maintains a list of all active games and provides methods to create, delete, and join games.
 * It also handles the communication between the server and the clients.
 */
public class GamesController {
    private static GamesController instance;
    private final HashMap<Integer, Game> games;
    private final HashMap<Game, List<ServerHandler>> clients;
    private static final int invalidPosition = 3;

    /**
     * Returns the singleton instance of the GamesController.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of the GamesController
     */
    public static GamesController getInstance() {
        if (instance == null) {
            instance = new GamesController();
        }
        return instance;
    }

    /**
     * Private constructor for the singleton pattern.
     * Initializes the games and clients maps.
     */
    private GamesController() {
        games = new HashMap<>();
        clients = new HashMap<>();
    }

    /**
     * Creates a new game with the given parameters.
     * If a game with the same ID already exists, returns null.
     *
     * @param gameID        the ID of the game to be created
     * @param numOfPlayers  the number of players in the game
     * @param nickname      the nickname of the player creating the game
     * @param serverHandler the server socket of the player creating the game
     * @return the created game, or null if a game with the same ID already exists
     */
    public synchronized Game createGame(int gameID, int numOfPlayers, String nickname, TokenColor color, ServerHandler serverHandler) {
        if (games.containsKey(gameID)) {
            return null;
        }
        Game game;
        try {
            game = new Game(gameID, numOfPlayers);
        } catch (InvalidNumberOfPlayersException e) {
            throw new RuntimeException(e);
        }
        games.put(gameID, game);
        game.addPlayer(new Player(nickname, color));
        serverHandlerToGame(game, serverHandler);
        return game;
    }

    /**
     * Deletes a game with the given ID.
     * Also removes all clients associated with the game.
     *
     * @param gameID the ID of the game to be deleted
     */
    public synchronized void deleteGame(int gameID) {
        clients.remove(games.get(gameID));
        games.remove(gameID);
        System.out.println("Game deleted: " + gameID);
    }

    /**
     * Sends a message to all clients in the game associated with the server handler.
     *
     * @param message       the message to be sent to all clients. It must be a subclass of ServerToClient.
     * @param serverHandler the server socket of the player to send the message to.
     */
    public void sendMSG(Messages message, ServerHandler serverHandler) {
        sendMessageToServer(message, serverHandler);
    }

    /**
     * Allows a player to join a game.
     * Sends appropriate messages to the client ba on the state of the game and the player's nickname.
     *
     * @param gameID        the ID of the game to join
     * @param nickname      the nickname of the player joining the game
     * @param serverHandler the server socket of the player joining the game
     */
    public synchronized void joinGame(int gameID, String nickname, TokenColor color,ServerHandler serverHandler) {

        if (!games.containsKey(gameID)) {
            sendMSG(Messages.NOGAME, serverHandler);
            return;

        } else if ((!games.get(gameID).isStarted() && (games.get(gameID).containsNickname(nickname)))) {
            sendMSG(Messages.NICKUSED, serverHandler);
            return;

        }else if ((!games.get(gameID).isStarted() && (games.get(gameID).containsColor(color)))) {
            sendMSG(Messages.COLORUSED, serverHandler);
            return;

        } else if ((games.get(gameID).isStarted())) {
            sendMSG(Messages.GAMESTARTED, serverHandler);
            return;

        } else if (games.get(gameID).isReadyToStart()) {
            sendMSG(Messages.FULL, serverHandler);
            return;
        } else {
            serverHandler.setGame(games.get(gameID));
            serverHandler.setGameID(gameID);
            games.get(gameID).addPlayer(new Player(nickname, color));
            serverHandlerToGame(games.get(gameID), serverHandler);
            sendMSG(Messages.JOINED, serverHandler);
        }
        if (games.get(gameID).isReadyToStart()) {
            sendEveryPlayerTCP(games.get(gameID), new MessageUpdate(Messages.STARTING));
            RMIController.getInstance().MessageUpdateRMI(games.get(gameID), Messages.STARTING, clients);
        }
    }

    /**
     * Starts a game with the given ID.
     * Checks if all players are in the game before starting it.
     *
     * @param GameID the ID of the game to start
     */
    public void startGame(int GameID) {
        Game game = games.get(GameID);
        synchronized (game) {
            List<ServerHandler> serverSO = clients.get(games.get(GameID));
            boolean allInGame = true;
            for (ServerHandler serverHandler : serverSO) {
                if (!serverHandler.inGame) {
                    allInGame = false;
                    break;
                }
            }
            if (allInGame) {
                games.get(GameID).startGame();
            }
        }
    }

    /**
     * Handles a command received from a client.
     *
     * @param message the command received from the client
     */
    public synchronized void onCommandReceived(ClientToServer message) {
        message.update();
    }

    /**
     * Handles a client disconnecting from a game.
     * Removes the client from the game and sends an update to all other players.
     *
     * @param serverHandler the server socket of the client that disconnected
     * @param game          the game from which the client disconnected
     */
    public void onDisconnect(ServerHandler serverHandler, Game game) {
        synchronized (game) {
            List<ServerHandler> serverSO = clients.get(game);
            if ((serverSO != null)) {
                serverSO.remove(serverHandler);
            }
            if(game!=null) {
                game.disconnectPlayer(game.getPlayerByNickname(serverHandler.getNickname()));
                sendEveryPlayerTCP(game, new PlayersUpdate(game.getPlayerNicknames()));
                RMIController.getInstance().playerUpdateRMI(game, game.getPlayerNicknames(), clients);
            }
            }
    }

    /**
     * Sends a message to all players in a game.
     *
     * @param game    the game to send the message to
     * @param message the message to send
     */
    public void sendEveryPlayerTCP(Game game, ServerToClient message) {
        List<ServerHandler> serverSO = clients.get(game);
        if (serverSO != null) {
            for (ServerHandler serverHandler : serverSO) {
                if (serverHandler.getConnectionType() == ConnectionType.TCP)
                    serverHandler.sendMessage(message);
            }
        }
    }

    /**
     * This method associates a ServerHandler with a Game in the clients map.
     * If the Game is not already in the map, it adds a new entry with an empty list of ServerHandlers.
     *
     * @param game          The game to associate the ServerHandler with.
     * @param serverHandler The ServerHandler to associate with the Game.
     */
    private synchronized void serverHandlerToGame(Game game, ServerHandler serverHandler) {
        if (!clients.containsKey(game)) {
            clients.put(game, new ArrayList<>());
        }
        clients.get(game).add(serverHandler);
    }

    /**
     * Sends an update to all players in a game.
     *
     * @param gameID the game to send the update to
     */
    public void requestUpdate(int gameID) {
        sendEveryPlayerTCP(games.get(gameID), new PlayersUpdate(games.get(gameID).getPlayerNicknames()));
        RMIController.getInstance().playerUpdateRMI(games.get(gameID), games.get(gameID).getPlayerNicknames(), clients);
    }


    /**
     *     This method associates a ServerHandler with a Game in the client's map.
     *     If the Game is not already in the map, it adds a new entry with an empty list of ServerHandlers.
     *
     *     @param gameID         The game to associate the ServerHandler with.
     *     @param serverRequestSocket  The ServerHandler to associate with the Game.
     */

    public void requestPlayingUpdate(int gameID, ServerHandler serverRequestSocket) {
        Game game = games.get(gameID);
        synchronized (game) {
            List<ServerHandler> serverSIO = clients.get(game);
            Stack<String> nicknames = new Stack<>();
            Stack<Long> pings = new Stack<>();
            Stack<Integer> points = new Stack<>();
            if (serverSIO != null) {
                for (ServerHandler serverHandler : serverSIO) {
                    nicknames.add(serverHandler.getNickname());
                    pings.add(serverHandler.getLastPing());
                    points.add(game.getPlayerByNickname(serverHandler.getNickname()).getScore());
                    for (int i = 0; i < points.size(); i++) {
                        if (game.getPlayerByNickname(serverHandler.getNickname()).getScore() >= points.get(i)) {
                            nicknames.removeLast();
                            pings.removeLast();
                            points.removeLast();
                            nicknames.add(i, serverHandler.getNickname());
                            pings.add(i, serverHandler.getLastPing());
                            points.add(i, game.getPlayerByNickname(serverHandler.getNickname()).getScore());
                            break;
                        }
                    }
                }
            }
            if (serverRequestSocket.getConnectionType() == ConnectionType.TCP)
                serverRequestSocket.sendMessage(new PlayersUpdate(nicknames, pings, points));
            else
                RMIController.getInstance().sendPlayersUpdate(nicknames, pings, points, serverRequestSocket);
        }
    }

    /**
     * This method flips a card at a given position in the game associated with the server handler.
     * The action is performed on behalf of the player associated with the server handler.
     *
     * @param position      The position of the card to flip.
     * @param serverHandler The server handler associated with the game.
     */
    public void flipCard(int position, ServerHandler serverHandler) {
        Game game = serverHandler.getGame();
        synchronized (game) {
            game.flipCard(position, serverHandler.getNickname());
        }
    }

    /**
     * This method flips a market card at a given position in the game associated with the server handler.
     *
     * @param position      The position of the market card to flip.
     * @param serverHandler The server handler associated with the game.
     */

    public void flipMarketCard(int position, ServerHandler serverHandler) {
        Game game = serverHandler.getGame();
        synchronized (game) {
            game.flipMarketCard(position, serverHandler.getNickname());
        }
    }

    /**
     * This method plays a card at a given position in the game associated with the server handler.
     *
     * @param position      The position of the card to play.
     * @param x             The x coordinate of the card to play.
     * @param y             The y coordinate of the card to play.
     * @param serverHandler The server handler associated with the game.
     */
    public void playCard(int position, int x, int y, ServerHandler serverHandler) {
        Game game = serverHandler.getGame();
        synchronized (game) {
            if (!game.playCard(position, new Location(x, y), serverHandler.getNickname())) {
                serverHandler.sendMessage(new MessageUpdate(Messages.CANTPLAYCARD));
                RMIController.getInstance().messageUpdate(serverHandler, Messages.CANTPLAYCARD);
            } else if (position != invalidPosition) {
                serverHandler.sendMessage(new MessageUpdate(Messages.CARDPLAYED));
                RMIController.getInstance().messageUpdate(serverHandler, Messages.CARDPLAYED);
                updatePlayersBoard(game);
            }
        }
    }

    /**
     * This method sends a message to all players in the game associated with the server handler.
     *
     * @param message       The message to send to the players.
     * @param serverHandler The server handler associated with the game.
     */
    public void sendMessage(String message, ServerHandler serverHandler) {
        Game game = serverHandler.getGame();
        synchronized (game) {
            message = serverHandler.getNickname() + " - " + message;
            game.sendMessage(message);
        }
    }

    /**
     * This method draws a card at a given position in the game associated with the server handler.
     *
     * @param position      The position of the card to draw.
     * @param serverHandler The server handler associated with the game.
     */
    public void drawCard(int position, ServerHandler serverHandler) {
    Game game = serverHandler.getGame();
    synchronized (game) {
        if(position!=10)
            game.drawCard(position, serverHandler.getNickname());
        game.nextTurn();
    }
}



    /**
     * This method chooses an object based on the position in the game associated with the server handler.
     *
     * @param position      The position of the object to choose.
     * @param serverHandler The server handler associated with the game.
     */
    public void chooseObj(Boolean position, ServerHandler serverHandler) {
        Game game = serverHandler.getGame();
        synchronized (game) {
            game.chooseObj(position, serverHandler.getNickname());
        }
    }


    /**
     * This method sends a player's hand to the client and updates the RMI controller.
     *
     * @param nickname The player's nickname.
     * @param game     The game instance.
     * @param hand     The player's hand.
     */
    public void sendHand(String nickname, Game game, List<String> hand) {
        synchronized (game){
            sendMessageToPlayer(nickname, game, new HandUpdate(hand));
            RMIController.getInstance().sendHandRMI(game, nickname, clients, hand);
            System.out.println("mano" + hand);
        }
    }

    /**
     * This method sends the choice to a specific player and updates the RMI controller.
     *
     * @param nickname The player's nickname.
     * @param game     The game instance.
     * @param choice     The choice to be sent.
     * @param obj1     The first object to be sent.
     * @param obj2     The second object to be sent.
     */
    public  void sendChoice(String nickname, Game game, String choice, String obj1, String obj2) {
        synchronized (game){
            sendMessageToPlayer(nickname, game, new ChoosingUpdate(choice, obj1, obj2));
            RMIController.getInstance().sendChoiceRMI(game, nickname, clients, choice, obj1, obj2);
        }
    }

    /**
     * This method sends a resource update to a specific player and updates the RMI controller.
     *
     * @param nickname The player's nickname.
     * @param game     The game instance.
     * @param resource The resources to be sent.
     */
    public  void sendResource(String nickname, Game game, List<Integer> resource) {
        synchronized (game){

            sendMessageToPlayer(nickname, game, new ResourceUpdate(resource));
            RMIController.getInstance().sendResourceRMI(game, nickname, clients, resource);
        }
    }

    /**
     * This method sends a message to a specific player.
     *
     * @param nickname The player's nickname.
     * @param game     The game instance.
     * @param message  The message to be sent.
     */
    public void sendMessageToPlayer(String nickname, Game game, ServerToClient message) {
        synchronized (game){

            List<ServerHandler> serverSIO = clients.get(game);
            if (serverSIO != null) {
                for (ServerHandler serverHandler : serverSIO) {
                    if (serverHandler.getNickname().equals(nickname))
                        serverHandler.sendMessage(message);
                }
            }
        }
    }


    /**
     * This method updates the boards of all players in a game and sends the updates to the clients.
     *
     * @param game The game instance whose player boards are to be updated.
     */
    public void updatePlayersBoard(Game game) {
        synchronized (game){

            HashMap<String, Stack<CardData>> boardsMap = new HashMap<>();
            List<ServerHandler> serverSIO = clients.get(game);
            if (serverSIO != null) {
                for (ServerHandler serverHandler : serverSIO) {
                    boardsMap.put(serverHandler.getNickname(), serverHandler.getGame()
                            .getPlayerByNickname(serverHandler.getNickname())
                            .getPlayerBoard().getViewGrid());
                }
                sendEveryPlayerTCP(game, new BoardsUpdate(boardsMap));
                RMIController.getInstance().sendBoardsUpdateRMI(game, boardsMap, clients);
            }
        }
    }


    /**
     * This method checks if a given nickname is associated with any ServerHandler in a specific game.
     *
     * @param nick The nickname to check.
     * @param game The game instance to search in.
     * @return true if the nickname is found, false otherwise.
     */
    private boolean containsSocketName(String nick, Game game) {
        for (ServerHandler serverHandler : clients.get(game)) {
            if (serverHandler.getNickname().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns the map of games and their associated server handlers.
     *
     * @return A HashMap where the key is a Game instance, and the value is a list of ServerHandlers.
     */
    public HashMap<Game, List<ServerHandler>> getClients() {
        return clients;
    }

    /**
     * This method sends a winner update to all players in a game and updates the RMI controller.
     *
     * @param game          The game instance.
     * @param winnerPlayers The list of players who won the game.
     */
    public void winnerUpdate(Game game, List<String> winnerPlayers) {
        synchronized (game){

            sendEveryPlayerTCP(game, new WinnerUpdate(winnerPlayers));
            RMIController.getInstance().winnerUpdateRMI(game, winnerPlayers, clients);
        }
    }

    /**
     * This method sends a turn update to all players in a game and updates the RMI controller.
     *
     * @param game     The game instance.
     * @param nickname The nickname of the player whose turn it is.
     */
    public void turnsUpdate(Game game, String nickname) {
        synchronized (game){
            GamesController.getInstance().sendEveryPlayerTCP(game, new TurnsUpdate(nickname));
            RMIController.getInstance().turnsUpdate(game, nickname, clients);
        }
    }

    /**
     * This method sends a market update to all players in a game and updates the RMI controller.
     *
     * @param game         The game instance.
     * @param marketUpdate The list of market updates.
     */
    public void marketUpdate(Game game, List<String> marketUpdate) {
        synchronized (game) {

            GamesController.getInstance().sendEveryPlayerTCP(game, new MarketUpdate(marketUpdate));
            RMIController.getInstance().MarketUpdateRMI(game, marketUpdate);
        }
    }

    /**
     * This method sends a chat update to all players in a game and updates the RMI controller.
     *
     * @param game     The game instance.
     * @param messages The list of chat messages.
     */
    public void chatUpdate(Game game, Stack<String> messages) {
        synchronized (game){
            GamesController.getInstance().sendEveryPlayerTCP(game, new ChatUpdate(messages));
            RMIController.getInstance().ChatUpdateRMI(game, messages, clients);
        }
    }

    /**
     * @return the map of games and their associated server handlers.
     */
    public HashMap<Integer, Game> getGames() {
        return games;
    }

    private static void sendMessageToServer(Messages message, ServerHandler serverHandler) {
        if (serverHandler.getConnectionType() == ConnectionType.TCP)
            serverHandler.sendMessage(new MessageUpdate(message));
        else {
            RMIController.getInstance().messageUpdate(serverHandler, message);
        }
    }
}
