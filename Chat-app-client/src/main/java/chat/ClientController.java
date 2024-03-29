package chat;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Works with JavaFX widgets.
 */

public class ClientController implements Initializable {
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private Label label_welcome;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client(new Socket("localhost", 1234));
            System.out.println("Connected to server");
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error instantiating client. ");
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        client.receiveMessageFromServer(vbox_messages);

        /**
         * When the send button is pressed, the GUI is updated with the new message and the string message is sent to the server.
         */

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                if (!messageToSend.isEmpty()) {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5,5,5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-background-radius: 20px; -fx-background-color:  #61d065; -fx-color: red");


                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0, 0, 0));

                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);



                    client.sendMessageToServer(messageToSend);


                    tf_message.clear();
                }
            }
        });




    }

    /**
     * Updates GUI with the server message.
     * @param messageFromServer - message received from server
     * @param vBox - vertical box where messages appear
     */

    public static void addLabel(String messageFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-radius: 20px; -fx-background-color:  #b0bab0; -fx-color: red");
        textFlow.setPadding(new Insets(5,10,5,10));

        hBox.getChildren().add(textFlow);

        // JavaFX is single-threaded, but there's a workaround with Platform class
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);

            }
        });

    }

    public void setUserInformation(String username) {
        label_welcome.setText("Welcome, " + username + "!");
    }

}
