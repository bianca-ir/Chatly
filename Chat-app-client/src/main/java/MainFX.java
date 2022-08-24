import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;



public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    //    Parent root = FXMLLoader.load(getClass().getResource("ClientChat.fxml"));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientChat.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 478, 396);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Client chat");
            primaryStage.show();
        } catch (Exception e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app "+e);
            alert.showAndWait();
        }





    }
}
