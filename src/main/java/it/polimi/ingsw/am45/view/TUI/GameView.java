package it.polimi.ingsw.am45.view.TUI;

import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.utilities.CardData;
import it.polimi.ingsw.am45.view.TUI.commandhandler.CommandHandler;
import it.polimi.ingsw.am45.view.ClientView;
import it.polimi.ingsw.am45.view.GUI.Boards_View;
import it.polimi.ingsw.am45.view.ViewController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.System.exit;

/**
 * The GameView class is responsible for managing the view of the game playing phase.
 * It handles the game's state, player's hand, market, chat, and other game-related views.
 * It also handles the communication with the server and processes the game events.
 */
public class GameView extends ClientView {

    // Flag to check if the game is running
    private boolean isRunning = true;

    // Flag to check if the player is choosing cards
    private boolean isChoosing = true;

    // Flag to check if the player is in the menu
    private boolean isInMenu = false;

    // Flag to check if the player is waiting for other players
    private boolean waitingOtherPlayers = true;

    // Variable to store the index of the card played
    private int cardPlayed = 0;

    // BufferedReader to read user input from the console
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // Controllers for the hand, playable, market, and chat views
    public HandView handViewController;
    public PlayableView playableViewController;
    public MarketView marketViewController;
    public ChatView chatViewController;

    // Maps to store the boards and their views
    private final HashMap<String, Stack<CardData>> boards = new HashMap<>();
    private final HashMap<String, Boards_View> boardsView = new HashMap<>();

    // Queues to store the game events related to the hand, market, and choosing of cards
    private final Queue<List<String>> handQueue = new LinkedList<>();
    private final Queue<List<String>> marketQueue = new LinkedList<>();
    private final Queue<String[]> chosingQueue = new LinkedList<>();

    // The player's nickname
    private final String nick;

    // Lock object for synchronization
    private final Object lock = new Object();

    // Printer for the game interface
    private GameInterfacePrinter gameInterfacePrinter;

    /**
     * Constructs a new GameView with the specified nickname and token color.
     *
     * @param nick       the player's nickname
     * @param tokenColor the player's token color
     */
    public GameView(String nick, TokenColor tokenColor) {
        this.nick = nick;
        // The player's token color
    }

    /**
     * This method starts the game thread and handles the client input.
     * It initializes the game interface printer, hand view controller, playable view controller, market view controller, and chat view controller.
     * It also starts a new thread to handle the user's input and game events.
     */
    public void start() {

        ViewController.setClientView(this);
        ViewController.getClientHandler().joinUpdate();

        handViewController = new HandView(this);
        marketViewController = new MarketView();
        playableViewController = new PlayableView(this);
        chatViewController = new ChatView(this);
        gameInterfacePrinter = new GameInterfacePrinter(nick, this);

        handViewController.init();
        playableViewController.init();
        marketViewController.init();

        processHandQueue();
        processChoosingQueue();
        processMarketQueue();

        while (isChoosing) {
            System.out.println("Game started, choose your objective card and your initial card!");
            playableViewController.handleSelection();
        }

        while (waitingOtherPlayers) {
            clearConsole();
            System.out.println("Waiting for other players to chose their initial card and objective...");
            timerBeforeRefresh();
            clearConsole();
        }

        CommandHandler commandHandler = new CommandHandler(this);

        while (isRunning) {
            if (gameInterfacePrinter.getIsPlaying()) {
                System.out.println("It's your turn!");
            } else {
                System.out.println("It's not your turn!");
            }
            gameInterfacePrinter.printGameInterface();
            timerBeforeRefresh();
            synchronized (lock) {
                handleUserCommand(commandHandler);
            }
        }
        System.out.println("Server is down, closing the game...");
    }

    /**
     * This method is used to stop the game.
     * It sets the isRunning flag to false, which will stop the game loop.
     */
    public void killGame() {
        this.isRunning = false;
    }

    /**
     * This method is used to choose an objective card in the game.
     * It calls the chooseOBJ method of the client handler, passing the card status.
     *
     * @param card The Objective Card chosen by the player (true if the card is the first one, false if it's the second one).
     */
    public void chooseObjective(Boolean card) {
        ViewController.getClientHandler().chooseOBJ(card);
    }

    /**
     * This method is used to set the turn status of the player.
     * If the player's turn is set to true, the game interface printer's isPlaying flag is also set to true.
     *
     * @param t Boolean value representing whether it's the player's turn or not.
     */
    public void setTurn(Boolean t) {
        // t= false -> not his turn, disable buttons
        this.gameInterfacePrinter.setIsPlaying(t);
    }

    /**
     * This method is used to play a card in the game.
     * It calls the playCard method of the client handler, passing the hand index and the card location.
     * It then waits for the server's response to check if the card was played successfully.
     *
     * @param handIndex    The index of the card in the player's hand.
     * @param cardLocation The location to play the card at.
     * @return true if the card was played successfully, false otherwise.
     */
    @SuppressWarnings("BusyWait")
    public boolean playCard(int handIndex, int[] cardLocation) {
        if (handIndex == 3) {
            updateMessage(new MessageUpdate(Messages.NOCARDSELECTED));
            return false;
        }

        ViewController.getClientHandler().playCard(handIndex, cardLocation[0], cardLocation[1]);

        while (cardPlayed == 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        boolean cardPlayedSuccessfully = cardPlayed == 1;
        cardPlayed = 0;

        return cardPlayedSuccessfully;
    }

    /**
     * This method is used to play the initial card in the game.
     * It calls the playCard method of the client handler, passing the fixed parameters for the initial card.
     */
    public void playInitialCard() {
        ViewController.getClientHandler().playCard(3, 0, 0);
    }

    /**
     * This method is used to draw a card from the deck.
     * If the card index is valid, it calls the drawCard method of the client handler, passing the card index.*
     *
     * @param card The index of the card to be drawn.
     */
    public boolean drawCard(int card) {
        if (card != 6) {
            ViewController.getClientHandler().drawCard(card);
            return true;
        } else {
            updateMessage(new MessageUpdate(Messages.NOCARDSELECTED));
            return false;
        }
    }

    /**
     * This method is used to update the hand view with the given list of cards.
     * If the hand view controller is not initialized yet, it adds the hand to the hand queue.
     * If the hand view controller is initialized, it calls the updateHandView method of the hand view controller, passing the hand.
     *
     * @param hand The list of cards to update the hand view with.
     */
    @Override
    public void updateHand(List<String> hand) {
        if (this.handViewController == null) {
            handQueue.add(hand);
        } else {
            this.handViewController.updateHandView(hand);
        }
        if (!isChoosing && !waitingOtherPlayers) {
            refreshTerminal();
        }
    }

    /**
     * This method is used to update the market view with the given list of cards.
     * If the market view controller is not initialized yet or the player is still choosing cards, it adds the market cards to the market queue.
     * If the market view controller is initialized and the player is not choosing cards, it calls the updateMarketView method of the market view controller, passing the market cards.
     *
     * @param cards The list of cards to update the market view with.
     */
    @Override
    public void updateMarket(List<String> cards) {
        if (this.marketViewController == null || isChoosing) {
            marketQueue.add(cards);
        } else {
            this.marketViewController.updateMarketView(cards);
        }
        if (!isChoosing && !waitingOtherPlayers) {
            refreshTerminal();
        }
    }

    /**
     * This method is used to update the game view based on the server message.
     * It processes the server message and updates the game view accordingly.
     *
     * @param messageUpdate The server message to process.
     */
    public void updateMessage(MessageUpdate messageUpdate) {
        switch (messageUpdate.getMessage()) {
            case CARDPLAYED:
                System.out.println("Card correctly played");
                cardPlayed = 1;
                break;
            case CANTPLAYCARD, NOCARDSELECTED:
                showServerMsg(messageUpdate);
                cardPlayed = 2;
                break;
            case SERVERDOWN:
                System.out.println("Server is down, closing the game...");
                killGame();
                break;
        }
    }

    /**
     * This method is used to update the choosing view with the given cards.
     * If the playable view controller is not initialized yet, it adds the cards to the choosing queue.
     * If the playable view controller is initialized, it calls the updateChoosableCard method of the playable view controller, passing the cards.
     *
     * @param card The initial card to be displayed on the console.
     * @param obj1 The first objective card to be displayed on the console.
     * @param obj2 The second objective card to be displayed on the console.
     */
    public void updateChoosingCard(String card, String obj1, String obj2) {
        if (this.playableViewController == null) {
            chosingQueue.add(new String[]{card, obj1, obj2});
        } else {
            clearConsole();
            this.playableViewController.updateChoosableCard(card, obj1, obj2);
        }
    }

    /**
     * This method is used to update the turn status of the players.
     * If the player is waiting for other players, it sets the waitingOtherPlayers flag to false.
     * If the player is not choosing cards, it calls the setTurn method of the game interface printer, passing the turn status.
     *
     * @param nickname the nickname of the player to check if it's his turn.
     */
    @Override
    public void updateTurns(String nickname) {
        if (waitingOtherPlayers) {
            waitingOtherPlayers = false;
        }
        if (!isChoosing && !waitingOtherPlayers) {
            setTurn(ViewController.getClientHandler().getNick().equals(nickname));
            clearConsole();
            refreshTerminal();
        } else setTurn(ViewController.getClientHandler().getNick().equals(nickname));
    }

    /**
     * This method is used to update the game view when the game ends.
     * It prints the list of winners and clears the console.
     *
     * @param winners The list of winners to be printed.
     */
    public void updateEnd(List<String> winners) {
        clearConsole();
        System.out.println("             | |                           ");
        System.out.println("  ___ _ __   __| | __ _  __ _ _ __ ___   ___ ");
        System.out.println(" / _ \\ '_ \\ / _` |/ _` |/ _` | '_ ` _ \\ / _ \\");
        System.out.println("|  __/ | | | (_| | (_| | (_| | | | | | |  __/");
        System.out.println(" \\___|_| |_|\\__,_|\\__, |\\__,_|_| |_| |_|\\___|");
        System.out.println("                   __/ |                     ");
        System.out.println("                  |___/                      ");
        this.gameInterfacePrinter.setIsPlaying(false);
        this.gameInterfacePrinter.printWinner(winners);
        System.out.println("The winners are:");
        for (String winner : winners) {
            System.out.println(winner);
        }
        System.out.println("Game ended, closing the application...");
        exit(-1);
    }

    /**
     * This method is used to update the resource view with the given list of resources.
     * If the player is not choosing cards, it calls the updateResources method of the game interface printer, passing the resources.
     *
     * @param resource The list of resources to update the resource view with.
     */
    @Override
    public void updateResource(List<Integer> resource) {
        if (!isChoosing && !waitingOtherPlayers) {
            clearConsole();
            refreshTerminal();
        }
    }

    /**
     * This method is used to update the players' view with the given list of players, pings, and points.
     * If the player is not choosing cards, it calls the updatePlayers method of the game interface printer, passing the players, pings, and points.
     *
     * @param players The list of players to update the players view with.
     * @param pings   The list of pings to update the players view with.
     * @param points  The list of points to update the players view with.
     */
    @Override
    public void updatePlayers(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        if (!isChoosing && !waitingOtherPlayers) {
            this.marketViewController.updatePlayerView(players, pings, points);
            int myIndex = players.indexOf(nick);
            Long myPing = pings.get(myIndex);
        }
    }

    /**
     * This method is used to update the chat view with the given list of messages.
     * If the player is not choosing cards, it calls the updateChat method of the chat view controller, passing the messages.
     *
     * @param messages The list of messages to update the chat view with.
     */
    public void updateChat(Stack<String> messages) {
        if (!isChoosing && !waitingOtherPlayers) {
            chatViewController.updateChat(messages);
        }
    }

    /**
     * This method is responsible for processing the hand queue in the game.
     * The hand queue is a queue of game events related to the player's hand.
     * If the hand queue is not empty, it enters a while loop that continues until the hand queue is empty.
     * Inside the loop, it calls the `updateHandView()` method of the handViewController` object,
     * passing the head of the `handQueue` as an argument.
     */
    public void processHandQueue() {
        if (!handQueue.isEmpty()) {
            while (!handQueue.isEmpty()) {
                this.handViewController.updateHandView(handQueue.poll());
            }
        }
    }

    /**
     * This method is responsible for processing the market queue in the game.
     * The market queue is a queue of game events related to the market.
     * If the market queue is not empty, it enters a while loop that continues until the market queue is empty.
     * Inside the loop, it calls the {@updateMarketView()} method of the marketViewController` object,
     * passing the head of the queue.
     */
    public void processMarketQueue() {
        if (!marketQueue.isEmpty()) {
            while (!marketQueue.isEmpty()) {
                this.marketViewController.updateMarketView(marketQueue.poll());
            }
        }
    }

    /**
     * This method is responsible for processing the choosing queue in the game.
     * The choosing queue is a queue of game events related to the choosing of cards.
     * If the choosing queue is not empty, it enters a while loop that continues until the choosing queue is empty.
     * Inside the loop, it retrieves and removes the head of the `chosingQueue` and stores it in the `cards` array.
     * It then calls the `updateChoosableCard()` method of the `playableViewController` object,
     * passing the elements of the `cards` array as arguments.
     */
    public void processChoosingQueue() {
        if (!chosingQueue.isEmpty()) {
            while (!chosingQueue.isEmpty()) {
                String[] cards = chosingQueue.poll();
                int INITIALCARD = 0;
                int OBJ1 = 1;
                int OBJ2 = 2;
                this.playableViewController.updateChoosableCard(cards[INITIALCARD], cards[OBJ1], cards[OBJ2]);
            }
        }
    }

    /**
     * Sends a message to the server via the client handler.
     * If the message is empty, the method will return without sending anything.
     *
     * @param message The message to be sent to the server.
     */
    public void sendMessage(String message) {
        if (message.isEmpty())
            return;
        if (!isChoosing && !waitingOtherPlayers) {
            chatViewController.setChatOpen(true);
            ViewController.getClientHandler().sendMessage(message);
            refreshTerminal();
        }

    }

    /**
     * Clears the console by printing a special character sequence.
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * This method is used to show a server message on the console.
     * It processes the server message and prints it on the console.
     *
     * @param messageUpdate The server message to process.
     */
    public void showServerMsg(MessageUpdate messageUpdate) {
        try {
            System.out.println(messageUpdate.getExplaination());
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * @return The player's nickname as a string.
     */
    public String getNick() {
        return this.nick;
    }

    /**
     * This method is used to refresh the terminal.
     * It clears the console and updates the game interface printer.
     */
    public void refreshTerminal() {
        clearConsole();
        gameInterfacePrinter.update();
    }

    /**
     * This method is used to handle a user command.
     * It reads the user's input from the console and processes it using the command handler.
     *
     * @param commandHandler The command handler to process the user's input.
     */
    private void handleUserCommand(CommandHandler commandHandler) {
        try {
            setIsInMenu(false);
            String input = reader.readLine();
            while (input.isEmpty()) {
                input = reader.readLine(); // Wait for the next input if the user just pressed 'enter'
            }
            int commandChose;
            try {
                commandChose = Integer.parseInt(input);
                setIsInMenu(true);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                timerBeforeRefresh();
                return;
            }
            clearConsole();
            commandHandler.handleCommand(commandChose);
        } catch (IOException e) {
            System.out.println("Error reading input from the console.");
        }
    }

    /**
     * This method is used to set the chat open status.
     * If the chat is open, it sets the chatOpen flag of the game interface printer to true.
     *
     * @param isChatOpen The chat open status.
     */
    public void isChatOpen(Boolean isChatOpen) {
        this.gameInterfacePrinter.chatOpened(isChatOpen);
    }

    /**
     * This method is used to set the private chat open status.
     * If the private chat is open, it sets the privateChatOpen flag of the game interface printer to true.
     *
     * @param isPrivateChatOpen The private chat open status.
     */
    public void isPrivateChatOpen(Boolean isPrivateChatOpen) {
        this.gameInterfacePrinter.privateChatOpened(isPrivateChatOpen);
    }

    /**
     * @param isPlaying The boolean to set if the player is playing.
     */
    public void setIsPlaying(boolean isPlaying) {
        this.gameInterfacePrinter.setIsPlaying(isPlaying);
    }

    /**
     * This method is used to get the current choosing status of the player.
     *
     * @return boolean value representing whether the player is in the choosing phase or not.
     */
    public boolean getIsChoosing() {
        return isChoosing;
    }

    /**
     * This method is used to set the choosing status of the player.
     *
     * @param isChoosing The boolean value to set the choosing status of the player.
     */
    public void setIsChoosing(boolean isChoosing) {
        this.isChoosing = isChoosing;
    }

    /**
     * This method is used to set the menu status of the player.
     *
     * @param isInMenu The boolean value to set the menu status of the player.
     */
    public void setIsInMenu(boolean isInMenu) {
        this.isInMenu = isInMenu;
    }

    /**
     * This method is used to get the current menu status of the player.
     *
     * @return boolean value representing whether the player is in the menu or not.
     */
    public boolean isInMenu() {
        return this.isInMenu;
    }

    /**
     * This method is used to get the HandView controller.
     *
     * @return HandView controller instance.
     */
    public HandView getHandViewController() {
        return handViewController;
    }

    /**
     * This method is used to get the MarketView controller.
     *
     * @return MarketView controller instance.
     */
    public MarketView getMarketViewController() {
        return marketViewController;
    }

    public ChatView getChatViewController() {
        return chatViewController;
    }

    private void timerBeforeRefresh() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
            Thread.currentThread().interrupt();
        }
    }

}
