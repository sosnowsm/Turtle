package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {

	@Override
	public void start(Stage mainStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		Scene scene = new Scene(root);
		mainStage.setTitle("Grafika ¿ó³wiowa");
		mainStage.setResizable(false);
		mainStage.setScene(scene);
		mainStage.getIcons().add(new Image("zolw.png"));
		mainStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
