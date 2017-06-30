package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.Scanner;

public class Controller {
	@FXML
	private TextField wspX;
	@FXML
	private TextField wspY;
	@FXML
	private TextField kat;
	@FXML
	private Button ustawButton;
	@FXML
	private Button podniesButton;
	@FXML
	private TextField dlugosc;
	@FXML
	private Button naprzodButton;
	@FXML
	private TextField katO;
	@FXML
	private TextField kierunek;
	@FXML
	private Button obrotButton;
	@FXML
	private ColorPicker kolor;
	@FXML
	private Canvas canvas;
	@FXML
	private Canvas turtleCanvas;
	@FXML
	private MenuItem otworzItem;
	@FXML
	private MenuItem otworzKrokowoItem;
	@FXML
	private MenuItem zamknij;
	@FXML
	private Button dalejButton;

	private Image img = new Image("turtle.jpg");
	private GraphicsContext gc1;
	private GraphicsContext gc2;
	private double turtleX;
	private double turtleY;
	private double turtleO;
	private boolean czyRysuje = true;
	private Stage stageAbout;
	private Scanner fileScanner;

	public void ustaw() throws OutOfCanvasException {
		try {
			Double x = Double.parseDouble(wspX.getText());
			Double y = Double.parseDouble(wspY.getText());
			Double k = Double.parseDouble(kat.getText());
			ustawWCanvas(x, y, k);

		} catch (NumberFormatException e) {
			alert("Niepoprawny format danych");
		} catch (NullPointerException e) {
			alert("Podaj wszystkie parametry");
		} catch (OutOfCanvasException e) {
			alert("Punkt poza obszarem rysowania");
		}
	}

	public void ustaw(double x, double y, double k) throws OutOfCanvasException {
		try {
			ustawWCanvas(x, y, k);
		} catch (OutOfCanvasException e) {
			alert("Punkt poza obszarem rysowania");
		}
	}

	private void ustawWCanvas(Double x, Double y, Double k) throws OutOfCanvasException {
		gc1 = turtleCanvas.getGraphicsContext2D();
		gc2 = canvas.getGraphicsContext2D();
		turtleX = x;
		turtleY = -y + 540;
		turtleO = k;
		if (turtleX <= 1024 && turtleX >= 0 && turtleY <= 540 && turtleY >= 0) {
			rotuj(turtleO);

		} else {
			throw new OutOfCanvasException("Punkt poza obszarem rysowania");
		}
	}

	public void podnies() {
		if (podniesButton.getText().equals("Podnieœ")) {
			podniesButton.setText("Opuœæ");
			setCzyRysuje(false);
		} else {
			podniesButton.setText("Podnieœ");
			setCzyRysuje(true);
		}
	}

	public void naprzod() throws OutOfCanvasException {
		try {
			double z = Double.parseDouble(dlugosc.getText());
			gc2.setStroke(kolor.getValue());
			rysuj(z);
		} catch (NumberFormatException e) {
			alert("Niepoprawny format danych");
		} catch (NullPointerException e) {
			alert("Podaj d³ugoœæ");
		}
	}

	public void naprzod(double z) throws OutOfCanvasException {
		rysuj(z);
	}

	private void rysuj(double z) throws OutOfCanvasException {
		try {

			double additionalX = Math.cos(turtleO / 360 * 2 * Math.PI) * z;
			double additionalY = Math.sin(turtleO / 360 * 2 * Math.PI) * z;
			if (isCzyRysuje()) {
				if ((turtleX + additionalX) <= 1024 && (turtleX + additionalX) >= 0 && (turtleY + additionalY) <= 540
						&& turtleY + additionalY >= 0) {
					gc2.strokeLine(turtleX, turtleY, turtleX + additionalX, turtleY + additionalY);
					turtleX += additionalX;
					turtleY += additionalY;
				} else {
					throw new OutOfCanvasException("Punkt koncowy poza obszarem rysowania");
				}
			} else {
				turtleX += additionalX;
				turtleY += additionalY;
			}
			gc1.clearRect(turtleX - additionalX, turtleY - additionalY, img.getHeight(), img.getWidth());
			rotuj(turtleO);
		} catch (OutOfCanvasException e) {
			alert("Punkt koñcowy poza obszarem rysowania");
		}
	}

	public void obrot() {
		Double o = Double.parseDouble(katO.getText());
		turtleO += o;

		rotuj(turtleO);
	}

	public void obrot(double o) {
		turtleO += o;

		rotuj(turtleO);
	}

	private void rotuj(Double o) {
		kierunek.setText("" + turtleO);
		Rotate r = new Rotate(o, turtleX, turtleY);
		gc1.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		gc1.save();
		gc1.clearRect(-200, -200, 1224, 740);
		gc1.drawImage(img, turtleX - 16, turtleY - 16);
		gc1.restore();
	}

	public void about() throws Exception {
		stageAbout = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("About.fxml"));
		Scene scene = new Scene(root);
		stageAbout.setTitle("O programie");
		stageAbout.setResizable(false);
		stageAbout.setScene(scene);
		stageAbout.getIcons().add(new Image("zolw.png"));
		stageAbout.show();

	}

	public void czytajZPliku() throws FileNotFoundException, OutOfCanvasException, NumberFormatException {

		File inFile = otworzPlik();
		try (Scanner fileScanner = new Scanner(inFile)) {
			while (fileScanner.hasNextLine()) {
				parsujNaKomendy(fileScanner);
			}
		} catch (NumberFormatException e) {
			alert("Niepoprawny format danych");
		} catch (NullPointerException e) {
			alert("Nie wybrano ¿adnego pliku");
		} catch (FileNotFoundException e) {
			alert("Plik nie zosta³ znaleziony");
		}
	}

	private void parsujNaKomendy(Scanner fileScanner) throws OutOfCanvasException {
		Scanner lineScanner = new Scanner(fileScanner.next());
		lineScanner.useDelimiter(";");
		String komenda = lineScanner.next();
		if (komenda.equals("ustaw")) {
			Double x = Double.parseDouble(lineScanner.next());
			Double y = Double.parseDouble(lineScanner.next());
			Double k = Double.parseDouble(lineScanner.next());
			ustaw(x, y, k);
		}
		if (komenda.equals("opusc")) {
			czyRysuje = true;
		}
		if (komenda.equals("podnies")) {
			czyRysuje = false;
		}
		if (komenda.equals("naprzod")) {
			Double z = Double.parseDouble(lineScanner.next());
			naprzod(z);

		}
		if (komenda.equals("obrot")) {
			Double o = Double.parseDouble(lineScanner.next());
			obrot(o);

		}
		if (komenda.equals("kolor")) {
			gc2 = canvas.getGraphicsContext2D();
			int r = Integer.parseInt(lineScanner.next());
			int g = Integer.parseInt(lineScanner.next());
			int b = Integer.parseInt(lineScanner.next());
			Color c = new Color(r / 255.0, g / 255.0, b / 255.0, 1);
			gc2.setStroke(c);
		}
		lineScanner.close();
	}

	public void czytajZPlikuKrokowo()
			throws FileNotFoundException, OutOfCanvasException, NumberFormatException {
		try{

		File inFile = otworzPlik();
		fileScanner = new Scanner(inFile);
		dalejButton.setDisable(false);
		}catch (NumberFormatException e) {
			alert("Niepoprawny format danych");}
		catch (NullPointerException e) {
			alert("Nie wybrano ¿adnego pliku");
		} catch (FileNotFoundException e) {
			alert("Plik nie zosta³ znaleziony");
		}
		
	}

	public void czytajZPlikuPrzyciskiem() throws OutOfCanvasException, NullPointerException, FileNotFoundException {
		try {
			parsujNaKomendy(fileScanner);
			if (!fileScanner.hasNextLine()) {
				dalejButton.setDisable(true);
			}
		}

		catch (NumberFormatException e) {
			alert("Niepoprawny format danych");
		} catch (NullPointerException e) {
			alert("Nie wybrano ¿adnego pliku");
		}
	}

	private File otworzPlik() {
		Stage fileOpen = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Otwórz plik");
		ExtensionFilter rozszerzenie = new ExtensionFilter("Pliki zolw", "*.zolw");
		fileChooser.getExtensionFilters().add(rozszerzenie);
		File inFile = fileChooser.showOpenDialog(fileOpen);
		return inFile;
	}

	public boolean isCzyRysuje() {
		return czyRysuje;
	}

	public void setCzyRysuje(boolean czyRysuje) {
		this.czyRysuje = czyRysuje;
	}

	public void alert(String s) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("B³¹d");
		alert.setContentText(s);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	public void zamknij() {
		Platform.exit();
	}

}
