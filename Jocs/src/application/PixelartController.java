package application;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PixelartController {

	private String nivell;
	private Color[][] matriuPixelart;
	@FXML
	private GridPane tauler;
	@FXML
	private Text textBenvingut;
	@FXML
	private Button volverMenu;
	@FXML
	private ColorPicker colorPicker;
	final SimpleBooleanProperty isMousePressed = new SimpleBooleanProperty(false);
	
	public void setNivell(String nivell) {
		System.out.println("Nivell de dificultat:" + nivell);
		this.nivell = nivell;
		construirTablero();
	}

	private void construirTablero() {
		// Inicialitzar tauler
		switch (nivell) {
		case "Menut":
			matriuPixelart = new Color[6][6];
			break;
		case "Mitja":
			matriuPixelart = new Color[9][9];
			break;
		case "Gran":
			matriuPixelart = new Color[12][12];
			break;
		}
		tauler.getChildren().clear();
		crearTauler(matriuPixelart.length, matriuPixelart.length);
	}

	public void crearTauler(int files, int columnes) {

		for (int i = 0; i < files; i++) {
			for (int j = 0; j < columnes; j++) {
				Button casella = new Button();
				double tamanyCasella = 0;

				switch (nivell) {
				case "Menut":
					tamanyCasella = 30;
					break;
				case "Mitja":
					tamanyCasella = 25;
					break;
				case "Gran":
					tamanyCasella = 20;
					break;
				}

				casella.setMinSize(tamanyCasella, tamanyCasella);
				casella.setPrefSize(tamanyCasella, tamanyCasella);
				casella.setMaxSize(tamanyCasella, tamanyCasella);
				tauler.setHgap(0);
				tauler.setVgap(0);
				casella.setPadding(Insets.EMPTY);
				casella.getStyleClass().add("buida");

				final int fila = i;
				final int columna = j;

				casella.setOnMousePressed(evt -> {
					if (evt.getButton() == MouseButton.PRIMARY) {
						pintarCasella(casella, fila, columna);
					} else {
						borrarCasella(casella, fila, columna);
					}
				});

				casella.setOnDragDetected(evt -> {
					casella.startFullDrag();
				});

				casella.setOnMouseDragEntered(evt -> {
					if (evt.getButton() == MouseButton.PRIMARY) {
						pintarCasella(casella, fila, columna);
					} else {
						borrarCasella(casella, fila, columna);
					}
				});

				tauler.add(casella, j, i);
			}
		}
	}

	public void pintarCasella(Button casella, int fila, int columna) {
		Color color = colorPicker.getValue();
		casella.setStyle("-fx-background-color: " + toRgbString(color) + ";");
		matriuPixelart[fila][columna] = color;
	}

	public void borrarCasella(Button casella, int fila, int columna) {
		casella.setStyle("-fx-background-color: #e0e0e0;");
		matriuPixelart[fila][columna] = null;
	}

	private String toRgbString(Color c) {
		int r = (int) (c.getRed() * 255);
		int g = (int) (c.getGreen() * 255);
		int b = (int) (c.getBlue() * 255);
		return String.format("rgb(%d,%d,%d)", r, g, b);
	}

	@FXML
	public void nouTauler(javafx.scene.input.MouseEvent event) {
		try {
			VBox rootPixelArt = FXMLLoader.load(getClass().getResource("TamanyPixelart.fxml"));
			Scene pantallaPixelArt = new Scene(rootPixelArt);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			pantallaPixelArt.getStylesheets().add(getClass().getResource("tamanyPixelart.css").toExternalForm());
			window.setScene(pantallaPixelArt);
			window.setTitle("Pixel Art");
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Boto que envia a la escena menu
	@FXML
	public void obrirMenu(ActionEvent event) {
		try {
			VBox rootMenu = (VBox) FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene pantallaMenu = new Scene(rootMenu);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			pantallaMenu.getStylesheets().add(getClass().getResource("menu.css").toExternalForm());
			window.setScene(pantallaMenu);
			window.setTitle("Menu");
			window.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
