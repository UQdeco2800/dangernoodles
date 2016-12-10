package uq.deco2800.dangernoodles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Dangernoodles extends Application {

	public static void main(String[] args) {
		launch(args);
    }


    /**
     * @param primaryStage
     * */
    @Override
    public void start(Stage primaryStage) throws Exception {
        org.apache.log4j.BasicConfigurator.configure();

        // EDIT THIS BELOW TO ALTER WHAT FILE IS USED TO RENDER SPLASH SCREEN
        Parent root = FXMLLoader.load(getClass().getResource("/SplashScreen.fxml"));

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);

        primaryStage.setOnCloseRequest(t -> {
            // Need to leave the lobby properly
            if (ClientManager.getClientManager().getRestClient().getCurrentLobby() != null) {
                ClientManager.getClientManager().getRestClient().requestLeaveLobby();
            }
            System.exit(0);
        });
    }
}