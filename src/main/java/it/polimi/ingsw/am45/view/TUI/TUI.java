package it.polimi.ingsw.am45.view.TUI;

import it.polimi.ingsw.am45.connection.rmi.RMIClient;
import it.polimi.ingsw.am45.connection.socket.client.Client;
import it.polimi.ingsw.am45.connection.socket.client.ClientSocket;
import it.polimi.ingsw.am45.controller.client.cts.JoinGame;
import it.polimi.ingsw.am45.controller.client.cts.NewGame;
import it.polimi.ingsw.am45.controller.server.stc.MessageUpdate;
import it.polimi.ingsw.am45.enumeration.ConnectionType;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.utilities.InputConnectionValidator;
import it.polimi.ingsw.am45.view.ClientView;
import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.listener.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.exit;

/**
 * This class represents the Text User Interface (TUI) of the application.
 * It extends the ClientView class and implements the necessary methods for the TUI.
 */
public class TUI extends ClientView {

    /**
     * BufferedReader to read user input from the console.
     */
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * The connection type of the user (RMI or TCP).
     */
    private final ConnectionType connectionType;

    /**
     * The last known list of players.
     */
    private ArrayList<String> lastKnownPlayers = new ArrayList<>();

    /**
     * The nickname of the user.
     */
    private String nickname;

    /**
     * The IP address of the server.
     */
    private String serverIP;

    /**
     * The color of the user's token.
     */
    private TokenColor tokenColor;

    /**
     * Flag to indicate if the game is ready to start.
     */
    private boolean isGameReadyToStart = false;

    /**
     * Flag to indicate if the user is ready to play.
     */
    boolean readyToPlay = false;

    /**
     * Flag to indicate if it's the first time the user is playing.
     */
    boolean first = true;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    public TUI(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * This method initializes the TUI, prompting the user for their nickname and server IP.
     */
    public void init() {
        readerClientConnectionInput();
    }

    /**
     * This method changes the scene to the welcome screen (aka the join/create game screen).
     */
    private void changeScene() {
        System.out.println("Welcome to the game, " + this.nickname + "!");
        InitListener();
        ViewController.setClientView(this);
        // Implement the rest of the scene change logic here
        createJoinGame();
    }

    /**
     * This method initializes the listeners for the TUI.
     */
    private void InitListener() {
        new msgListener();
        new handListener();
        new turnListener();
        new boardsListener();
        new marketListener();
        new endListener();
    }

    /**
     * This method starts the client connection to the server based on the user's input.
     */
    private void startClient() {
        int serverPort = 28888;
        if (connectionType == ConnectionType.RMI) {

            try {
                RMIClient rmiClient = new RMIClient(serverIP, 1234);
                ViewController.setClientHandler(rmiClient);
                rmiClient.newPlayer(nickname);
                rmiClient.nickname = nickname;
                ViewController.setConnectionType(ConnectionType.RMI);
                changeScene();

            } catch (RemoteException e) {
                System.out.println("Invalid server address: " + e.getMessage());
            }
        } else {
            try {
                Client client = new Client(serverIP, serverPort, nickname);
                client.startClient();
                ViewController.setConnectionType(ConnectionType.TCP);
                changeScene();
            } catch (UnknownHostException e) {
                System.out.println(("Invalid server address: " + e.getMessage()));
            } catch (IllegalArgumentException e) {
                System.out.println(("Invalid server port: " + e.getMessage()));
            } catch (IOException e) {
                System.out.println(("An error occurred: " + e.getMessage()));
            }
        }
    }

    /**
     * This method starts the game when all players are ready to play.
     */
    public void startGame() {
        if (isGameReadyToStart) {
            System.out.println("-----------------Game started");
            Timer timer = new Timer();
            countdown();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    new GameView(nickname, tokenColor).start();
                }
            };

            timer.schedule(task, 3000);
            isGameReadyToStart = false;
        }

    }

    /**
     * This method starts a countdown before the game starts.
     */
    private void countdown() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int i = 3;

            @Override
            public void run() {
                System.out.println("STARTING GAME:  " + i);
                i--;
                if (i == 0)
                    timer.cancel();
            }
        };
        timer.schedule(task, 0, 1000);

    }

    /**
     * This method prompts the user to enter their nickname, server IP, and token color before starting the client connection.
     */
    private void readerClientConnectionInput() {
        String input = "";
    try {
        while (input.isEmpty()) {
            System.out.println("Enter your nickname:");
            input = reader.readLine();
        }
        this.nickname = input;
        System.out.println("Enter the server IP:");
        this.serverIP = reader.readLine();

        // Validate the server IP address before starting the client connection
        if (InputConnectionValidator.validate(serverIP)) {
            startClient();
        } else {
            System.out.println("Invalid server address");
            exit(-1);
        }
    } catch (IOException e) {
        System.out.println("An error occurred: " + e.getMessage());
    }
}

    /**
     * This method prompts the user to create or join a game.
     */
    private void createJoinGame() {

        System.out.println("Do you want to create or join a game?");
        System.out.println("1. Create");
        System.out.println("2. Join");
        System.out.println("3. Quit");
        createJoinInputReader();

    }

    /**
     * This method reads the user's input to create or join a game based on the user's choice.
     */
    private void createJoinInputReader() {
        int chosenGameId;
        int numberOfPlayer = 2;
        do {
            try {

                String input = "";
                while (input.isEmpty()) {
                    input = (reader.readLine()); // Wait for the next input if the user just pressed 'enter'
                }
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        System.out.println("Enter the number of players (2/3/4 default 2):");

                        try {
                            numberOfPlayer = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input, creating a 2 player game");
                        }
                        if (numberOfPlayer < 2 || numberOfPlayer > 4) {
                            System.out.println("Invalid number of players, creating a 2 player game");
                            numberOfPlayer = 2;
                        }
                        this.tokenColor = setTokenColor();

                        // Generate a random int for the game id
                        int gameId = (int) (Math.random() * 1000);
                        createGame(gameId, numberOfPlayer);
                        System.out.println("Game created with ID: " + gameId);
                        readyToPlay = true;
                        break;
                    case 2:
                        this.tokenColor = setTokenColor();
                        System.out.println("Enter the game ID:");
                        try {
                            chosenGameId = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid game id");
                            createJoinGame();
                            break;
                        }
                        joinGame(chosenGameId);
                        readyToPlay = true;
                        break;
                    case 3:
                        System.out.println("Exiting the game, goodbye!");
                        exit(0);
                    default:
                        System.out.println("Invalid choice, please try again");
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        } while (!readyToPlay);
    }

    /**
     * This method creates a game with the specified game ID and number of players.
     *
     * @param gameId         The game ID.
     * @param numberOfPlayer The number of players.
     */
    private void createGame(int gameId, int numberOfPlayer) {
        try {
            if (ViewController.getConnection() == ConnectionType.TCP)
                ClientSocket.getInstance().sendObject(new NewGame(gameId, numberOfPlayer, this.tokenColor));
            else {
                try {
                    RMIClient.getInstance().newGame(gameId, numberOfPlayer, this.tokenColor);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid game id");
            exit(-1);
        }
    }

    /**
     * This method joins a game with the specified game ID.
     *
     * @param gameId The game ID.
     */
    private void joinGame(int gameId) {
        System.out.println("Joining game with ID: " + gameId);
        try {
            if (ViewController.getConnection() == ConnectionType.TCP)
                ClientSocket.getInstance().sendObject(new JoinGame(gameId, this.tokenColor));
            else {
                try {
                    RMIClient.getInstance().joinGame(gameId, this.tokenColor);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid game id");
        }
    }

    public void updateMessage(MessageUpdate messageUpdate) {
        switch (messageUpdate.getMessage()) {
            case NOGAME, GAMESTARTED, FULL, NICKUSED, COLORUSED:
                System.out.println(ANSI_RED + messageUpdate.getMessage() + ANSI_RESET);
                switch (messageUpdate.getMessage().name()) {
                    case "NICKUSED" -> {
                        System.out.println("Please enter a different nickname");
                        readerClientConnectionInput();
                    }
                    case "COLORUSED" -> {
                        System.out.println("Color already used, closing...");
                        exit(-1);
                    }
                    case "FULL" -> {
                        System.out.println("The game is full, please use another game ID");
                        readerClientConnectionInput();
                    }
                    case "GAMESTARTED" -> {
                        System.out.println("The game has already started, please use another game ID");
                        readerClientConnectionInput();
                    }
                    case "NOGAME" -> {
                        System.out.println("The game does not exist, please use another game ID");
                        readerClientConnectionInput();
                    }
                    default -> createJoinGame();
                }
                break;
            case JOINED, CREATED:
                System.out.println(messageUpdate.getMessage());
                if (!first) {
                    ViewController.getClientHandler().requestUpdate(false);
                    first = true;
                }
                System.out.println("Waiting for other players to join...");
                break;
            case ALREADYEXIST:
                System.out.println(messageUpdate.getMessage());
                createJoinInputReader();
                break;
            case STARTING:
                isGameReadyToStart = true;
                break;
            case SERVERDOWN:
                System.out.println("Server is down, exiting the game");
                exit(0);
                break;
        }
    }

    @Override
    public void updatePlayers(Stack<String> players, Stack<Long> pings, Stack<Integer> points) {
        ArrayList<String> currentPlayers = new ArrayList<>(players);
        // The player list is shown only if a new player has joined or left the lobby
        if (!currentPlayers.equals(lastKnownPlayers)) {
            System.out.println("Current player in lobby");
            for (String player : currentPlayers) {
                System.out.println(player);
            }
            lastKnownPlayers = currentPlayers;
        }
        if (isGameReadyToStart)
            startGame();
    }

    private TokenColor setTokenColor() {
        while (true) {
            System.out.println("Enter the desired token color [red, green, blue, yellow]:");
            try {
                String colorInput = reader.readLine();
                switch (colorInput.toLowerCase()) {
                    case "red", "r":
                        return TokenColor.RED;
                    case "green", "g":
                        return TokenColor.GREEN;
                    case "blue", "b":
                        return TokenColor.BLUE;
                    case "yellow", "y":
                        return TokenColor.YEllOW;
                    default:
                        System.out.println("Invalid color input. Please enter red, green, blue, or yellow.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}