package it.polimi.ingsw.am45.view.TUI;

import it.polimi.ingsw.am45.view.listener.Listener;
import it.polimi.ingsw.am45.view.modelview.Boards;
import it.polimi.ingsw.am45.view.modelview.Market;
import it.polimi.ingsw.am45.view.modelview.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameInterfacePrinter class is responsible for printing the game interface.
 * It handles the display of the game menu, chat, and other game-related views.
 * It also processes the game events and updates the game interface accordingly.*
 * This class implements the Listener interface to receive updates from the game model.
 */
public class GameInterfacePrinter implements Listener {
    private final GameView gameView;
    private final String nickname;

    private boolean isChatOpened = false;

    private boolean isPrivateChatOpened = false;

    private boolean isPlaying = false;

    /**
     * Constructs a new GameInterfacePrinter with the specified nickname and game view.
     * Registers itself as an observer of the Market.
     *
     * @param nickname the player's nickname
     * @param gameView the game view to be used
     */
    public GameInterfacePrinter(String nickname, GameView gameView) {
        this.gameView = gameView;
        this.nickname = nickname;
        Market.getInstance().registerObserver(this);

    }

    /**
     * Prints the game interface.
     * This includes the game menu, chat, and other game-related views.
     */
    public void printGameInterface() {
        ArrayList<String> menuOptions = getMenuOptions();
        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    /**
     * Returns the menu options.
     * The menu options include the commands that the player can execute during their turn.
     *
     * @return the menu options
     */
    private ArrayList<String> getMenuOptions() {
        ArrayList<String> menuOptions = new ArrayList<>();
        menuOptions.add("                                   ┌──────────────────────────COMMANDS─────────────────────────┐");
        menuOptions.add("                                   │___________________________________________________________│");
        menuOptions.add("                                   │   1. Show personal goal                                   │");
        menuOptions.add("                                   │   2. Show common goal                                     │");
        menuOptions.add("                                   │   3. Open global chat                                     │");
        menuOptions.add("                                   │   4. Open private chat                                    │");
        menuOptions.add("                                   │   5. Info about card and resources                        │");
        menuOptions.add("                                   │   6. Show players info                                    │");
        menuOptions.add("                                   │   7. See your hand card                                   │");
        if (this.isPlaying)
            menuOptions.add("                                   │   8. Play a card from the hand                            │");
        menuOptions.add("                                   └───────────────────────────────────────────────────────────┘");
        return menuOptions;
    }

    /**
     * Updates the game interface.
     * This method is called when the game model changes state.
     * It updates the game interface to reflect the new state of the game model.
     */
    @Override
    public synchronized void update() {
        if (gameView.getIsChoosing()){
            return;
        }

        if (gameView.isInMenu()) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            printOpenChat();
            return;
        }
        ArrayList<String> output = new ArrayList<>();

        output = gameView.getHandViewController().printHandTUI(output);
        output.add("                                            MARKET                                            ");
        output = gameView.getMarketViewController().printMarketTUI(output);
        output = Market.getInstance().printResourceTUI(output);
        output = Boards.getInstance().printBoardTui(output, nickname);

        output = Msg.getInstance().printPlayerInfoTUI(output);
        output.addAll(getMenuOptions());
        output.add("Choose an option:");
        output.forEach(System.out::println);

        printOpenChat();


    }


    /**
     * Returns whether the player is playing.
     * This is used to determine if the player can execute commands during their turn.
     *
     * @return true if the player is playing, false otherwise
     */
    public boolean getIsPlaying() {
        return isPlaying;
    }

    /**
     * Sets whether the player is playing.
     * This is used to enable or disable the player's ability to execute commands during their turn.
     *
     * @param isPlaying true if the player is playing, false otherwise
     */
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * Prints the open chat.
     * This includes the global chat and the private chat if they are open.
     */
    private void printOpenChat() {
        if (isChatOpened) {
            gameView.getChatViewController().printGlobalChat();
            System.out.println("Enter your message: ");
        }

        if (isPrivateChatOpened) {
            gameView.getChatViewController().printPrivateChat();
            System.out.println("Enter the nickname of the user you want to send a message to: ");
        }
    }

    @Override
    public void updatePlayers() {

    }

    /**
     * Sets whether the chat is opened.
     * This is used to determine if the chat should be displayed in the game interface.
     *
     * @param isChatOpened true if the chat is opened, false otherwise
     */
    public void chatOpened(Boolean isChatOpened) {
        this.isChatOpened = isChatOpened;
    }

    /**
     * Sets whether the private chat is opened.
     * This is used to determine if the private chat should be displayed in the game interface.
     *
     * @param isPrivateChatOpen true if the private chat is opened, false otherwise
     */
    public void privateChatOpened(Boolean isPrivateChatOpen) {
        this.isPrivateChatOpened = isPrivateChatOpen;
    }

    public void printWinner(List<String> winner) {
        if (winner.contains(nickname)) {
            System.out.println("                                           _ ");
            System.out.println("                                         | |");
            System.out.println("  _   _  ___  _   _  __      _____  _ __ | |");
            System.out.println(" | | | |/ _ \\| | | | \\ \\ /\\ / / _ \\| '_ \\| |");
            System.out.println(" | |_| | (_) | |_| |  \\ V  V / (_) | | | |_|");
            System.out.println("  \\__, |\\___/ \\__,_|   \\_/\\_/ \\___/|_| |_(_)");
            System.out.println("   __/ |                                    ");
            System.out.println("  |___/                                     ");
        } else {
            System.out.println("                      _                _ ");
            System.out.println("                     | |              | |");
            System.out.println("  _   _  ___  _   _  | | ___  ___  ___| |");
            System.out.println(" | | | |/ _ \\| | | | | |/ _ \\/ __|/ _ \\ |");
            System.out.println(" | |_| | (_) | |_| | | | (_) \\__ \\  __/_|");
            System.out.println("  \\__, |\\___/ \\__,_| |_|\\___/|___/\\___(_)");
            System.out.println("   __/ |                                 ");
            System.out.println("  |___/                           ");
        }
    }

}
