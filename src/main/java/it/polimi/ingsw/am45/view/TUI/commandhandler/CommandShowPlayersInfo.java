package it.polimi.ingsw.am45.view.TUI.commandhandler;

import it.polimi.ingsw.am45.view.modelview.Msg;

import java.util.ArrayList;
import java.util.Scanner;

public class CommandShowPlayersInfo extends Command {
    /**
     * Executes the command to show the players' information.
     */
    @Override
    public void execute() {

        Msg.getInstance().printPlayerInfoBox();

        System.out.println("Press enter to return to the menu...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
