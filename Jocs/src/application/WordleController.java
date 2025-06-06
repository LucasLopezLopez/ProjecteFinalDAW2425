package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WordleController implements Initializable {

	public final int numeroPalabras = 228;

	int contarBien = 0;
	Label[][] matriu = new Label[6][5];
	Label[] array = new Label[27];
	int fila = 0;
	int columna = 0;
	String palabra = "";
	String[] letras = { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l",
			"z", "x", "c", "v", "b", "n", "m" };
	String[] letrasVerdes = new String[6];
	int numLetraV = 0;

	@FXML
	private GridPane mygridPane;
	@FXML
	private Button volverMenu;
	@FXML
	private Label textBenvingut;
	@FXML
	private VBox root;
	@FXML
	private HBox primeraFila;
	@FXML
	private HBox segundaFila;
	@FXML
	private HBox terceraFila;

	// Objacte usuari
	private String usuariActual;

	public void setUsername(String username) {
		this.usuariActual = username;

	}

	public String getUsuari() {
		return this.usuariActual;
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

	//////////////// CONTROLADOR DE EVENTOS\\\\\\\\\\\\\\\\\\\\\
	@FXML
	public void enter(KeyEvent e) {
		if (e.getCode() == KeyCode.F5) {
			reintentar();
		}

		if (e.getCode() == KeyCode.ENTER) {
			if (columna < 5) {

			} else {
				contarBien();
				fila++;
				columna = 0;
				System.out.println(e.getCode());

			}

		} else if (e.getCode().isLetterKey()) {
			if (columna > 4) {

			} else if (fila > 5) {

			} else {
				matriu[fila][columna].setText("" + e.getCode());
				matriu[fila][columna].setStyle(
						"-fx-border-color: black; -fx-background-color: d8d8d8; -fx-alignment: center; -fx-font-size: 23px;");
				columna++;
				System.out.println(e.getCode());

			}

		} else if (e.getCode() == KeyCode.BACK_SPACE) {

			if (columna == 0) {

			} else if (fila > 5) {

			} else {
				columna--;
				matriu[fila][columna].setText("");
				matriu[fila][columna].setStyle("-fx-border-color: black; -fx-alignment: center;");
				System.out.println("borrar");
			}
		}
	}

	public void contarBien() {
		contarBien = 0;
		int restar = 0;
		String respuesta = cadena();
		HashMap<String, Integer> mapa = contarLetras();
		String[] color = new String[5];

		for (int i = 0; i < 5; i++) {
			boolean ejecutar = true;
			if (("" + respuesta.charAt(i)).equalsIgnoreCase("" + palabra.charAt(i))) {
				color[i] = "verde";
				restar = mapa.get(("" + respuesta.charAt(i)).toLowerCase()).intValue() - 1;
				mapa.put(("" + respuesta.charAt(i)).toLowerCase(), restar);

				for (int j = 0; j < 5; j++) {
					if (letrasVerdes[j] == null) {

					} else if (letrasVerdes[j].equalsIgnoreCase("" + respuesta.charAt(i))) {
						ejecutar = false;
						break;
					}
				}

				if (ejecutar == true) {
					letrasVerdes[numLetraV] = "" + respuesta.charAt(i);
					numLetraV++;

				}
				contarBien++;
				verde(("" + respuesta.charAt(i)));

			}
		}

		for (int i = 0; i < 5; i++) {
			if (mapa.get(("" + respuesta.charAt(i)).toLowerCase()) != null) {

				if (mapa.get(("" + respuesta.charAt(i)).toLowerCase()) == 0) {
					if (color[i] != null) {

					} else {
						color[i] = "gris";
						gris(("" + respuesta.charAt(i)));
					}

				} else {
					if (color[i] != null) {

					} else {
						color[i] = "amarillo";
						restar = mapa.get(("" + respuesta.charAt(i)).toLowerCase()).intValue() - 1;
						mapa.put(("" + respuesta.charAt(i)).toLowerCase(), restar);
						amarillo(("" + respuesta.charAt(i)));
					}
				}
			} else {
				color[i] = "gris";
				gris(("" + respuesta.charAt(i)));
			}
		}

		for (int i = 0; i < color.length; i++) {
			if (color[i].equals("verde")) {
				matriu[fila][i].setStyle(
						"-fx-border-color: black; -fx-background-color: #3dff4a; -fx-alignment: center; -fx-font-size: 23px;");

			} else if (color[i].equals("amarillo")) {
				matriu[fila][i].setStyle(
						"-fx-border-color: black; -fx-background-color: f4ff49; -fx-alignment: center; -fx-font-size: 23px;");

			} else {
				matriu[fila][i].setStyle(
						"-fx-border-color: black; -fx-background-color: #a1a1a1; -fx-alignment: center; -fx-font-size: 23px;");

			}
		}

		if (contarBien == 5) {
			letreroGanar();
		} else if (fila == 5) {
			letreroPerder();
		}

	}

	//////////////// PINTA EL TECLADO AUXILIAR DE VERDE\\\\\\\\\\\\\\\\\\\\\

	public void verde(String color) {

		for (int i = 0; i < 26; i++) {
			if (i < 10) {
				if (array[i].getText().equalsIgnoreCase(color)) {
					primeraFila.getChildren().get(i).setStyle(
							"-fx-border-color: black; -fx-background-color: #3dff4a; -fx-alignment: center; -fx-font-size: 20px;");
				}
			} else if (i < 20) {
				if (array[i].getText().equalsIgnoreCase(color)) {
					segundaFila.getChildren().get(i - 10).setStyle(
							"-fx-border-color: black; -fx-background-color: #3dff4a; -fx-alignment: center; -fx-font-size: 20px;");
				}
			} else {
				if (array[i].getText().equalsIgnoreCase(color)) {
					terceraFila.getChildren().get(i - 20).setStyle(
							"-fx-border-color: black; -fx-background-color: #3dff4a; -fx-alignment: center; -fx-font-size: 20px;");
				}
			}
		}
	}

	//////////////// PINTA EL TECLADO AUXILIAR DE AMARILLO\\\\\\\\\\\\\\\\\\\\\

	public void amarillo(String color) {

		boolean ejecutar = true;

		for (int i = 0; i < letrasVerdes.length; i++) {
			if (letrasVerdes[i] != null) {
				if (letrasVerdes[i].equalsIgnoreCase(color)) {
					ejecutar = false;
					break;
				}
			}
		}

		if (ejecutar == true) {

			for (int i = 0; i < 26; i++) {

				if (array[i].getText().equalsIgnoreCase(color)) {

					if (i < 10) {
						primeraFila.getChildren().get(i).setStyle(
								"-fx-border-color: black; -fx-background-color: #f4ff49; -fx-alignment: center; -fx-font-size: 20px;");
					} else if (i < 20) {
						segundaFila.getChildren().get(i - 10).setStyle(
								"-fx-border-color: black; -fx-background-color: #f4ff49; -fx-alignment: center; -fx-font-size: 20px;");
					} else {
						terceraFila.getChildren().get(i - 20).setStyle(
								"-fx-border-color: black; -fx-background-color: #f4ff49; -fx-alignment: center; -fx-font-size: 20px;");
					}

				}
			}
		}
	}

	//////////////// PINTA EL TECLADO AUXILIAR DE GRIS\\\\\\\\\\\\\\\\\\\\\

	public void gris(String color) {

		boolean ejecutar = true;

		for (int i = 0; i < letrasVerdes.length; i++) {
			if (letrasVerdes[i] != null) {
				if (letrasVerdes[i].equalsIgnoreCase(color)) {
					ejecutar = false;
				}
			}
		}

		if (ejecutar == true) {

			for (int i = 0; i < 26; i++) {
				if (i < 10) {
					if (array[i].getText().equalsIgnoreCase(color)) {
						primeraFila.getChildren().get(i).setStyle(
								"-fx-border-color: black; -fx-background-color: #a1a1a1; -fx-alignment: center; -fx-font-size: 20px;");
					}
				} else if (i < 20) {
					if (array[i].getText().equalsIgnoreCase(color)) {
						segundaFila.getChildren().get(i - 10).setStyle(
								"-fx-border-color: black; -fx-background-color: #a1a1a1; -fx-alignment: center; -fx-font-size: 20px;");
					}
				} else {
					if (array[i].getText().equalsIgnoreCase(color)) {
						terceraFila.getChildren().get(i - 20).setStyle(
								"-fx-border-color: black; -fx-background-color: #a1a1a1; -fx-alignment: center; -fx-font-size: 20px;");
					}
				}
			}
		}
	}

	public void letreroGanar() {

		ButtonType botonSiguiente = new ButtonType("Siguiente palabra!", ButtonData.OK_DONE);
		Alert alertaGanar = new Alert(Alert.AlertType.CONFIRMATION);
		alertaGanar.setTitle("FELICIDADES!!");
		alertaGanar.setHeaderText("Has ganado a wordle!!");
		alertaGanar.getButtonTypes().setAll(botonSiguiente);
		Optional<ButtonType> resultado = alertaGanar.showAndWait();

		if (resultado.isPresent() && resultado.get() == botonSiguiente) {
			reiniciar();
		} else {
			fila = 11;
		}
	}

	public void letreroPerder() {

		ButtonType botonSiguiente = new ButtonType("Siguiente palabra!", ButtonData.OK_DONE);
		Alert alertaPerder = new Alert(Alert.AlertType.CONFIRMATION);
		alertaPerder.setTitle("Otra vez será");
		alertaPerder.setHeaderText("La palabra era: " + palabra.toUpperCase());

		alertaPerder.getButtonTypes().setAll(botonSiguiente);
		Optional<ButtonType> resultado = alertaPerder.showAndWait();

		if (resultado.isPresent() && resultado.get() == botonSiguiente) {
			reiniciar();
		} else {
			fila = 11;
		}
	}

	public void reintentar() {
		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				matriu[i][j].setText("");
				matriu[i][j].setStyle("-fx-border-color: black; -fx-alignment: center;");
			}
		}

		primeraFila.getChildren().clear();
		segundaFila.getChildren().clear();
		terceraFila.getChildren().clear();

		int contador = 0;

		for (int i = 0; i < 10; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[i]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			primeraFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;
		}

		for (int i = 0; i < 10; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[contador]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			segundaFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;
		}

		for (int i = 0; i < 6; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[contador]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			terceraFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;

		}

		String aux = palabra;
		while (palabra.equalsIgnoreCase(aux)) {
			int random = (int) (Math.random() * 228);
			palabra = palabraFinal(random);
		}

		for (int i = 0; i < letrasVerdes.length; i++) {
			letrasVerdes[i] = null;
		}

		System.out.println(palabra);
		volverMenu.setFocusTraversable(false);
		Platform.runLater(() -> root.requestFocus());
		columna = 0;
		fila = 0;
	}

	public void reiniciar() {

		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				matriu[i][j].setText("");
				matriu[i][j].setStyle("-fx-border-color: black; -fx-alignment: center;");
			}
		}

		primeraFila.getChildren().clear();
		segundaFila.getChildren().clear();
		terceraFila.getChildren().clear();

		int contador = 0;

		for (int i = 0; i < 10; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle(
					"-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[i]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			primeraFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;
		}

		for (int i = 0; i < 10; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle(
					"-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[contador]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			segundaFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;
		}

		for (int i = 0; i < 6; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle(
					"-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[contador]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			terceraFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;

		}

		for (int i = 0; i < letrasVerdes.length; i++) {
			letrasVerdes[i] = null;
		}

		String aux = palabra;
		while (palabra.equalsIgnoreCase(aux)) {
			int random = (int) (Math.random() * 228);
			palabra = palabraFinal(random);
		}

		System.out.println(palabra);
		volverMenu.setFocusTraversable(false);
		Platform.runLater(() -> root.requestFocus());
		numLetraV = 0;
		columna = 0;
		fila = -1;
	}

	public HashMap<String, Integer> contarLetras() {
		int contador = 0;
		HashMap<String, Integer> mapa = new HashMap<>();
		for (int i = 0; i < 5; i++) {
			contador = 0;
			for (int j = 0; j < 5; j++) {

				if (palabra.charAt(i) == palabra.charAt(j)) {
					contador++;

				}
			}

			mapa.put(("" + palabra.charAt(i)).toLowerCase(), contador);
		}

		return mapa;
	}

	public String cadena() {

		String cadena = "";

		for (int i = 0; i < 5; i++) {
			cadena += matriu[fila][i].getText();
		}

		return cadena;

	}

	public String palabraFinal(int num) {
		int contador = 0;
		String aux = "";
		try (BufferedReader br = new BufferedReader(new FileReader("src\\application\\paraules.txt"))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (contador == num) {
					aux = linea;
					break;
				}
				contador++;
			}
		} catch (Exception e) {

		}

		return aux;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				Label lb = new Label();
				matriu[i][j] = lb;
				lb.setMinSize(36, 36);
				lb.setMaxSize(36, 36);
				lb.setStyle("-fx-border-color: black; -fx-alignment: center;");
				mygridPane.add(lb, j, i);

			}
		}

		int contador = 0;

		for (int i = 0; i < 10; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle(
					"-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[i]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			primeraFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;
		}

		for (int i = 0; i < 10; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle(
					"-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[contador]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			segundaFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;
		}

		for (int i = 0; i < 6; i++) {
			Label lb = new Label();
			lb.setMinSize(36, 36);
			lb.setMaxSize(36, 36);
			lb.setStyle(
					"-fx-border-color: black; -fx-alignment: center; -fx-font-size: 20px; -fx-background-color: #d8d8d8");
			lb.setText(letras[contador]);
			HBox.setMargin(lb, new Insets(0, 0, 0, 5));
			terceraFila.getChildren().add(i, lb);
			array[contador] = lb;
			contador++;

		}

		int random = (int) (Math.random() * 228);
		palabra = palabraFinal(random);
		System.out.println(palabra);
		volverMenu.setFocusTraversable(false);
		Platform.runLater(() -> root.requestFocus());
	}


}
