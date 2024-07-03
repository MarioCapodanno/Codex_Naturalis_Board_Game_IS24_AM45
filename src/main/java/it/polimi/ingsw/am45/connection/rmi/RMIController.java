package it.polimi.ingsw.am45.connection.rmi;

import it.polimi.ingsw.am45.connection.socket.server.ServerHandler;
import it.polimi.ingsw.am45.controller.GamesController;
import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.utilities.CardData;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * The RMIController class manages the RMI connection between the server and the client.
 * It provides methods for updating players, the market, messages, hands, choices, resources, boards, winners, turns, and chats using RMI.
 * It also provides methods for sending players updates, message updates, boards updates, winner updates, turns updates, and chat updates to specific server handlers.
 */
public class RMIController {
    private static RMIController instance = null;

    public static RMIController getInstance() {
        if (instance == null) {
            instance = new RMIController();
        }
        return instance;
    }

    /**
     * This method updates the players in a game using RMI. It iterates over each ServerHandler associated with the game,
     * and if the connection type is RMI, it calls the PlayersUpdate method on the client interface.
     *
     * @param game the game to update
     * @param players the players to update
     * @param clients the clients to update
     */
    public void playerUpdateRMI(Game game, Stack<String> players, HashMap<Game, List<ServerHandler>> clients) {

        List<ServerHandler> serverSO = clients.get(game);
        if (serverSO != null) {
            for (ServerHandler serverSocket : serverSO) {
                if (serverSocket.getConnectionType() == ConnectionType.RMI) {
                    try {
                        serverSocket.clientInterface.PlayersUpdate(players);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    /**
     * This method updates the market in a game using RMI. It iterates over each ServerHandler associated with the game,
     * and if the connection type is RMI, it calls the MarketUpdate method on the client interface.
     *
     * @param game the game to update
     * @param gameboard the gameboard to update
     */
    public void MarketUpdateRMI(Game game, List<String> gameboard) {
        List<ServerHandler> serverSO = GamesController.getInstance().getClients().get(game);
        if (serverSO != null) {
            for (ServerHandler serverSocket : serverSO) {
                if (serverSocket.getConnectionType() == ConnectionType.RMI) {
                    try {
                        serverSocket.clientInterface.MarketUpdate(gameboard);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    /**
     * This method updates the message in a game using RMI. It iterates over each ServerHandler associated with the game,
     * and if the connection type is RMI, it calls the MessageUpdate method on the client interface.
     *
     * @param game the game to update
     * @param message the message to update
     * @param clients the clients to update
     */
    public void MessageUpdateRMI(Game game, Messages message, HashMap<Game, List<ServerHandler>> clients) {
        List<ServerHandler> serverSO = clients.get(game);
        if (serverSO != null) {
            for (ServerHandler serverSocket : serverSO) {
                if (serverSocket.getConnectionType() == ConnectionType.RMI) {
                    try {
                        serverSocket.clientInterface.MessageUpdate(message);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }
    /**
     * This method retrieves a ServerHandler for a specific player in a game using RMI.
     * It iterates over each ServerHandler associated with the game, and returns the one that matches the provided nickname.
     *
     * @param game the game to retrieve the player from
     * @param nick the nickname of the player to retrieve
     * @param clients the clients to update
    */
    public ServerHandler getPlayer(Game game, String nick, HashMap<Game, List<ServerHandler>> clients) {
        List<ServerHandler> serverSIO = clients.get(game);
        if (serverSIO != null) {
            for (ServerHandler serverSocket : serverSIO) {
                if (serverSocket.getNickname().equals(nick)) {
                    if (serverSocket.getConnectionType() == ConnectionType.RMI)
                        return serverSocket;
                }
            }
        }
        return null;
    }

    /**
     * This method sends a hand to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the HandUpdate method on the client interface.
     *
     * @param game the game to send the hand to
     * @param nick the nickname of the player to send the hand to
     * @param clients the clients to update
     * @param hand the hand to send
     */
    public void sendHandRMI(Game game, String nick, HashMap<Game, List<ServerHandler>> clients, List<String> hand) {
        ServerHandler serverHandler = getPlayer(game, nick, clients);
        if (serverHandler == null) {
            return;
        }
        try {
            serverHandler.clientInterface.HandUpdate(hand);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a choice to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the ChoosingUpdate method on the client interface.
     *
     * @param game the game to send the choice to
     * @param nick the nickname of the player to send the choice to
     * @param clients the clients to update
     * @param choice the choice to send
     * @param obj1 the first object to send
     * @param obj2 the second object to send
     */
    public void sendChoiceRMI(Game game, String nick, HashMap<Game, List<ServerHandler>> clients, String choice, String obj1, String obj2) {
        ServerHandler serverHandler = getPlayer(game, nick, clients);
        if (serverHandler == null) {
            return;
        }
        try {
            serverHandler.clientInterface.ChoosingUpdate(choice, obj1, obj2);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a resource to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the ResourceUpdate method on the client interface.
     *
     * @param game the game to send the resource to
     * @param nick the nickname of the player to send the resource to
     * @param clients the clients to update
     * @param resource the resource to send
     */
    public void sendResourceRMI(Game game, String nick, HashMap<Game, List<ServerHandler>> clients, List<Integer> resource) {
        ServerHandler serverHandler = getPlayer(game, nick, clients);
        if (serverHandler == null) {
            return;
        }
        try {
            serverHandler.clientInterface.ResourceUpdate(resource);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a players update to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the PlayersUpdate method on the client interface.
     *
     * @param players the players to update
     * @param pings the pings to update
     * @param points the points to update
     * @param serverHandler the server handler to update
     */
    public void sendPlayersUpdate(Stack<String> players, Stack<Long> pings, Stack<Integer> points, ServerHandler serverHandler) {
        try {
            serverHandler.clientInterface.PlayersUpdate(players, pings, points);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message update to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the MessageUpdate method on the client interface.
     *
     * @param message the message to update
     * @param serverHandler the server handler to update
     */
    public void messageUpdate(Messages message, ServerHandler serverHandler) {
        try {
            serverHandler.clientInterface.MessageUpdate(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method sends a boards update to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the boardsUpdate method on the client interface.
     *
     * @param game the game to send the boards to
     * @param boardsMap the boards to send
     * @param clients the clients to update
     */
    public void sendBoardsUpdateRMI(Game game, HashMap<String, Stack<CardData>> boardsMap, HashMap<Game, List<ServerHandler>> clients) {
        List<ServerHandler> serverSO = clients.get(game);
        if (serverSO != null) {
            for (ServerHandler serverSocket : serverSO) {
                if (serverSocket.getConnectionType() == ConnectionType.RMI) {
                    try {
                        serverSocket.clientInterface.boardsUpdate(boardsMap);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    /**
     * This method sends a winner update to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the winnerUpdate method on the client interface.
     *
     * @param game the game to send the winner to
     * @param winnerPlayers the winners to send
     */
    public void winnerUpdateRMI(Game game, List<String> winnerPlayers, HashMap<Game, List<ServerHandler>> clients) {
        List<ServerHandler> serverSO = clients.get(game);
        if (serverSO != null) {
            for (ServerHandler serverSocket : serverSO) {
                if (serverSocket.getConnectionType() == ConnectionType.RMI) {
                    try {
                        serverSocket.clientInterface.winnerUpdate(winnerPlayers);
                    } catch (RemoteException e) {
                        System.out.println("Game ended");
                    }
                }

            }
        }

    }

    /**
     * This method sends a turn update to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the turnsUpdate method on the client interface.
     *
     * @param game the game to send the turns to
     * @param nickname the nickname of the player to send the turns to
     * @param clients the clients to update
     */
    public void turnsUpdate(Game game, String nickname, HashMap<Game, List<ServerHandler>> clients) {
        List<ServerHandler> serverSO = clients.get(game);
        if (serverSO != null) {
            for (ServerHandler serverSocket : serverSO) {
                if (serverSocket.getConnectionType() == ConnectionType.RMI) {
                    try {
                        serverSocket.clientInterface.turnsUpdate(nickname);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    /**
     * This method sends a chat update to a player in a game using RMI.
     * It retrieves the ServerHandler for the player, and if it is not null, it calls the chatUpdate method on the client interface.
     *
     * @param game the game to send the chat to
     * @param messages the messages to send
     * @param clients the clients to update
     */
    public void ChatUpdateRMI(Game game, Stack<String> messages, HashMap<Game, List<ServerHandler>> clients) {
        List<ServerHandler> serverSO = clients.get(game);
        if (serverSO != null) {
            for (ServerHandler serverSocket : serverSO) {
                if (serverSocket.getConnectionType() == ConnectionType.RMI) {
                    try {
                        serverSocket.clientInterface.chatUpdate(messages);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    /**
     * This method sends a message update to a specific server handler if the connection type is not TCP.
     * It calls the MessageUpdate method on the client interface of the server handler.
     *
     * @param serverHandler the server handler to update
     * @param messages the message to update
     */

    public void messageUpdate(ServerHandler serverHandler, Messages messages) {
        if (serverHandler.getConnectionType() == ConnectionType.TCP)
            return;
        try {
            serverHandler.clientInterface.MessageUpdate(messages);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
