package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class AboutController {
	@FXML
	private Button zamknij;

	public void zamknij() {
		Stage stage = (Stage) zamknij.getScene().getWindow();
		stage.close();
	}
}
