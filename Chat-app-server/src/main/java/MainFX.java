import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {



        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));

           Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Log in!");
            primaryStage.show();
        } catch (Exception e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app "+e);
            alert.showAndWait();
            e.printStackTrace();
        }
    }


}
