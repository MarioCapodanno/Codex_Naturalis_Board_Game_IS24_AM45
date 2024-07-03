package it.polimi.ingsw.am45.controller.server.stc;


import it.polimi.ingsw.am45.enumeration.Messages;
import it.polimi.ingsw.am45.view.modelview.Msg;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class MessageUpdate implements ServerToClient, Serializable {
    private final Messages message;

    /**
     * This method updates the message by calling the putMessage method on the Singleton instance of Msg.
     * It passes the current instance of MessageUpdate to the putMessage method.
     */
    @Override
    public void update() {
        Msg.getInstance().putMessage(this);
    }

    /**
     * Getter
     * @return message
     */
    public Messages getMessage() {
        return message;
    }



    /**
     * Class constructor
     * @param message
     */
    public MessageUpdate(Messages message) {
        this.message = message;
    }


    /**
     * This method reads the messages.json file and returns the message corresponding to the message attribute.
     * @return the message corresponding to the message attribute
     * @throws IOException
     */
    public String getExplaination() throws IOException {
        InputStream is = getClass().getResourceAsStream("/it/polimi/ingsw/am45/json/messages.json");
        if (is == null) {
            throw new FileNotFoundException("Cannot find resource:  .json");
        }
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        JSONObject jsonObject = new JSONObject(content);

        return jsonObject.getString(message.toString());
    }
}
