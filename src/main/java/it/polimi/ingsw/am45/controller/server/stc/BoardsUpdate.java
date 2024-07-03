package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.utilities.CardData;
import it.polimi.ingsw.am45.view.modelview.Boards;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Stack;

public class BoardsUpdate implements ServerToClient, Serializable {
    private final HashMap<String, Stack<CardData>> boards;

    /**
     This method updates the state of the boards by calling the updateBoards method on the Singleton instance of Boards.
     It passes the current instance of BoardsUpdate to the updateBoards method.
     */
    @Override
    public void update() {
        Boards.getInstance().updateBoards(this);
    }


    /**
     * Class constructor
     * @param boards
     */
    public BoardsUpdate(HashMap<String, Stack<CardData>> boards) {
        this.boards = boards;
    }

    /**
     * Getter
     * @return boards
     */
    public HashMap<String, Stack<CardData>> getBoards() {
        return boards;
    }

}
