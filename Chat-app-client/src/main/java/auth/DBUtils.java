package auth;

import chat.ClientController;
import com.sun.javafx.logging.PlatformLogger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBUtils {

    /**
     * Changes the scene according to the FXML file provided as parameter.
     * @param event - event which precedes the scene change
     * @param fxmlfile - FXML file of the new scene
     * @param title - title of the scene
     * @param username - username provided by user
     */
    public static void changeScene(ActionEvent event, String fxmlfile, String title, String username) {

       Parent root = null;


        if (username != null) { // load the chat scene
            try {
               FXMLLoader loader = new FXMLLoader(DBUtils.class.getClassLoader().getResource(fxmlfile));

                // load objects from fxml file
               root = loader.load();

               // load functionalities from Controller

               ClientController clientController = loader.getController();
               clientController.setUserInformation(username);




            } catch(IOException e) {
                e.printStackTrace();
                Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, e);
            }
        } else {
            try {

                root = FXMLLoader.load(DBUtils.class.getClassLoader().getResource(fxmlfile));
            } catch(IOException e) {
                e.printStackTrace();
                Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();



    }

    /**
     * Attempts to sign the user in, saving provided credentials. Checks whether username already exists in database -
     * if not, username and password are saved and the scene is changed.
     * @param event - event of SignUp button being pressed
     * @param username - credential provided by user
     * @param password - credential provided by user
     */

    public static void signUpUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;


       String jdbcURL = "jdbc:postgresql://localhost:5432/chat-app";

        try {
            connection = DriverManager.getConnection(jdbcURL, "postgres", "postgres");
            System.out.println("Connected");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()) {
                System.out.println("User already exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User already exists.");
                alert.show();
            }

            else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?,?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();

                changeScene(event, "ClientChat.fxml", "Chat", username);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // closing the connection to prevent memory leakage
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * Attempts to log the user in with provided credentials. Checks whether provided username exists in database, then
     * whether provided password matches with the one saved. The scene is changed if these conditions hold, otherwise
     * an alert is displayed.
     * @param event - event of LogIn button being pressed
     * @param username - credential provided by user
     * @param password - credential provided by user
     */

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String jdbcURL = "jdbc:postgresql://localhost:5432/chat-app";


        try {
            connection = DriverManager.getConnection(jdbcURL, "postgres", "postgres");
            preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid username.");
                alert.show();
            } else {
                while (resultSet.next()) {
                    // retrieve password from database and compare it to login credential
                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(password)) {

                        changeScene(event, "ClientChat.fxml", "Chat", username);



                    } else {
                        // passwords do not match
                        System.out.println("Password does not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid password.");
                        alert.show();
                    }

                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // closing the connection to prevent memory leakage
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }


    }


}
