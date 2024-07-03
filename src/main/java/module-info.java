module it.polimi.ingsw.am45 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.json;
    requires java.rmi;

    opens it.polimi.ingsw.am45 to javafx.fxml;
    exports it.polimi.ingsw.am45;
    exports it.polimi.ingsw.am45.view;
    exports it.polimi.ingsw.am45.model;
    exports it.polimi.ingsw.am45.connection;
    opens it.polimi.ingsw.am45.view to javafx.fxml;
    exports it.polimi.ingsw.am45.connection.rmi;
    exports it.polimi.ingsw.am45.view.GUI;
    opens it.polimi.ingsw.am45.view.GUI to javafx.fxml;

}