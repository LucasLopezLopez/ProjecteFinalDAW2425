package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class TaulerBuscaminesController {

	@FXML
	private GridPane tauler;
	private String nivell;
	private int segonsDuracio = 0;
	private int numBanderes;
	@FXML
	private Text timer;
	@FXML
	private Text banderes;
	@FXML
	private Text textBuscamines;
	@FXML
	private ImageView imgBandera;
	@FXML
	private ImageView imgReloj;
	@FXML
	private Button volverMenu;
	@FXML
	private Button volverTamanyBuscamines;
	@FXML
	private VBox recordsPopup;
	@FXML
	private TableView<Record> recordsTable;
	@FXML
	private TableColumn<Record, String> colNom;
	@FXML
	private TableColumn<Record, String> colNivell;
	@FXML
	private TableColumn<Record, Integer> colTemps;
	@FXML
	private StackPane rootPane;
	@FXML
	private Label textRecords;

	private Timeline timeline;
	private boolean jocAcabat = false;
	private int casellesBuides;
	private boolean matriuTauler[][];
	ThreadLocalRandom rnd = ThreadLocalRandom.current();

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

	// Boto enviar a la eleccio del tamany
	@FXML
	public void obrirBuscaMines(ActionEvent event) {
		try {
			VBox rootBuscaMines = (VBox) FXMLLoader.load(getClass().getResource("TamanyBuscamines.fxml"));
			Scene pantallaBuscaMines = new Scene(rootBuscaMines);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			pantallaBuscaMines.getStylesheets().add(getClass().getResource("tamanyBuscamines.css").toExternalForm());
			window.setScene(pantallaBuscaMines);
			window.setTitle("Busca Mines");
			window.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setNivell(String nivell) {
		System.out.println("Nivell de dificultat:" + nivell);
		this.nivell = nivell;
		construirTablero();

	}

	private void construirTablero() {
		int files = 0;
		int columnes = 0;
		int numMines = 0;

		// Inicialitzar tauler
		switch (nivell) {
		case "Fàcil":
			files = columnes = 6;
			matriuTauler = new boolean[6][6];
			numMines = 6;
			casellesBuides = 30;
			break;
		case "Mitjà":
			files = columnes = 9;
			matriuTauler = new boolean[9][9];
			numMines = 15;
			casellesBuides = 66;
			break;
		case "Difícil":
			files = columnes = 12;
			matriuTauler = new boolean[12][12];
			numMines = 25;
			casellesBuides = 119;
			break;
		}
		numBanderes = numMines;
		banderes.setText(numBanderes + "");
		tauler.getChildren().clear();
		// Colocar les mines
		colocarMines(numMines, matriuTauler);
		// Crear Tauler
		crearTauler(files, columnes);

	}

	private void iniciarTemporizador() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			segonsDuracio++;
			timer.setText(String.format("%03d", segonsDuracio));
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	@FXML
	public void initialize() {

		iniciarTemporizador();
		colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		colNivell.setCellValueFactory(new PropertyValueFactory<>("nivell"));
		colTemps.setCellValueFactory(new PropertyValueFactory<>("temps"));

	}

	public void colocarMines(int numMines, boolean[][] matriuTauler) {
		int minesColocades = 0;

		while (minesColocades < numMines) {
			int numFila = rnd.nextInt(matriuTauler.length);
			int numColumna = rnd.nextInt(matriuTauler.length);

			if (!matriuTauler[numFila][numColumna]) {
				matriuTauler[numFila][numColumna] = true;
				minesColocades++;
			}
		}

		for (int i = 0; i < matriuTauler.length; i++) {
			for (int j = 0; j < matriuTauler.length; j++) {
				System.out.print(matriuTauler[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void crearTauler(int files, int columnes) {
		// Crear el tauler
		Image bandera = new Image(getClass().getResourceAsStream("/application/img/bandera_españa.png"));
		Image mina = new Image(getClass().getResourceAsStream("/application/img/bomba_pixel.png"));

		jugar: for (int i = 0; i < files; i++) {
			for (int j = 0; j < columnes; j++) {
				Button casella = new Button();
				double tamanyCasella = 0;

				switch (nivell) {
				case "Fàcil":
					tamanyCasella = 30;
					break;
				case "Mitjà":
					tamanyCasella = 25;
					break;
				case "Difícil":
					tamanyCasella = 20;
					break;

				}

				casella.setMinSize(tamanyCasella, tamanyCasella);
				casella.setPrefSize(tamanyCasella, tamanyCasella);
				casella.setMaxSize(tamanyCasella, tamanyCasella);

				casella.setPadding(Insets.EMPTY);

				ImageView flagViewBandera = new ImageView(bandera);
				flagViewBandera.setFitWidth(tamanyCasella);
				flagViewBandera.setFitHeight(tamanyCasella);
				flagViewBandera.setPreserveRatio(true);

				ImageView flagViewMina = new ImageView(mina);
				flagViewMina.setFitWidth(tamanyCasella);
				flagViewMina.setFitHeight(tamanyCasella);
				flagViewMina.setPreserveRatio(true);

				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					casella.getStyleClass().add("clar");
				} else {
					casella.getStyleClass().add("oscur");
				}

				final int fila = i;
				final int columna = j;

				casella.setOnMouseClicked(evt -> {
					if (evt.getButton() == MouseButton.PRIMARY) {
						if (!matriuTauler[fila][columna]) {
							casella.getStyleClass().add("buida");
							switch (nivell) {
							case "Fàcil":
								casella.setStyle("-fx-font-size: 24px;");
								break;
							case "Mitjà":
								casella.setStyle("-fx-font-size: 18px;");
								break;
							case "Difícil":
								casella.setStyle("-fx-font-size: 14px;");
								break;
							}
							if (numMinesVoltant(fila, columna) != 0) {
								casella.setText(numMinesVoltant(fila, columna) + "");
							}
							obrirCasellesBuides(fila, columna);
							if (casellesBuides == 0) {
								partidaGuanyada();
							}
						} else {
							casella.getStyleClass().add("mina");
							casella.setGraphic(flagViewMina);
							casella.setDisable(true);
							acabarPartida();
						}
					} else {
						if (casella.getGraphic() == null) {
							casella.setGraphic(flagViewBandera);
							numBanderes--;
							banderes.setText(numBanderes + "");
						} else {
							casella.setGraphic(null);
							numBanderes++;
							banderes.setText(numBanderes + "");
						}
					}
				});

				tauler.add(casella, j, i);
			}
		}
	}

	public int numMinesVoltant(int fila, int columna) {
		int minesTocant = 0;
		if (fila == 0 && columna == 0) {
			if (matriuTauler[0][1] == true) {
				minesTocant++;
			}
			if (matriuTauler[1][0] == true) {
				minesTocant++;
			}
			if (matriuTauler[1][1] == true) {
				minesTocant++;
			}
		} else if (fila == 0 && columna == matriuTauler.length - 1) {
			if (matriuTauler[0][matriuTauler.length - 2] == true) {
				minesTocant++;
			}
			if (matriuTauler[1][matriuTauler.length - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[1][matriuTauler.length - 2] == true) {
				minesTocant++;
			}
		} else if (fila == 0) {
			if (matriuTauler[0][columna - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[0][columna + 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[1][columna - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[1][columna] == true) {
				minesTocant++;
			}
			if (matriuTauler[1][columna + 1] == true) {
				minesTocant++;
			}
		} else if (fila == matriuTauler.length - 1 && columna == 0) {
			if (matriuTauler[matriuTauler.length - 1][1] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 2][0] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 2][1] == true) {
				minesTocant++;
			}
		} else if (fila == matriuTauler.length - 1 && columna == matriuTauler.length - 1) {
			if (matriuTauler[matriuTauler.length - 1][matriuTauler.length - 2] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 2][matriuTauler.length - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 2][matriuTauler.length - 2] == true) {
				minesTocant++;
			}
		} else if (fila == matriuTauler.length - 1) {
			if (matriuTauler[matriuTauler.length - 1][columna - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 1][columna + 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 2][columna - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 2][columna] == true) {
				minesTocant++;
			}
			if (matriuTauler[matriuTauler.length - 2][columna + 1] == true) {
				minesTocant++;
			}
		} else if (columna == 0) {
			if (matriuTauler[fila - 1][columna] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila - 1][columna + 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila][columna + 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila + 1][columna] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila + 1][columna + 1] == true) {
				minesTocant++;
			}
		} else if (columna == matriuTauler.length - 1) {
			if (matriuTauler[fila - 1][columna] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila - 1][columna - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila][columna - 1] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila + 1][columna] == true) {
				minesTocant++;
			}
			if (matriuTauler[fila + 1][columna - 1] == true) {
				minesTocant++;
			}
		} else {
			if (matriuTauler[fila - 1][columna - 1]) {
				minesTocant++;
			}
			if (matriuTauler[fila - 1][columna]) {
				minesTocant++;
			}
			if (matriuTauler[fila - 1][columna + 1]) {
				minesTocant++;
			}
			if (matriuTauler[fila][columna - 1]) {
				minesTocant++;
			}
			if (matriuTauler[fila][columna + 1]) {
				minesTocant++;
			}
			if (matriuTauler[fila + 1][columna - 1]) {
				minesTocant++;
			}
			if (matriuTauler[fila + 1][columna]) {
				minesTocant++;
			}
			if (matriuTauler[fila + 1][columna + 1]) {
				minesTocant++;
			}
		}
		return minesTocant;
	}

	public void acabarPartida() {
		jocAcabat = true;

		for (Node node : tauler.getChildren()) {
			if (node instanceof Button) {
				((Button) node).setDisable(true);
			}
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Fi del joc");
		alert.setHeaderText(null);
		alert.setContentText("Has pulsat una mina.");
		alert.showAndWait();
		timeline.stop();
	}

	public void partidaGuanyada() {
		jocAcabat = true;

		for (Node node : tauler.getChildren()) {
			if (node instanceof Button) {
				((Button) node).setDisable(true);
			}
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Fi del joc");
		alert.setHeaderText(null);
		alert.setContentText("Has guanyat la partida.");
		alert.showAndWait();
		timeline.stop();
		int temps = Integer.parseInt(timer.getText());
		;
		guardarRecord(usuariActual, temps, nivell);
		mostrarRecords();
	}

	private void obrirCasellesBuides(int fila, int columna) {

		if (fila < 0 || columna < 0 || fila >= matriuTauler.length || columna >= matriuTauler[0].length) {
			return;
		}

		Button casella = getCasella(fila, columna);
		if (casella == null || casella.isDisabled() || matriuTauler[fila][columna]) {
			return;
		}

		int minesAdjacents = numMinesVoltant(fila, columna);
		casella.getStyleClass().add("buida");
		if (minesAdjacents > 0) {
			casella.setText(String.valueOf(minesAdjacents));
		}
		casella.setDisable(true);
		casellesBuides--;

		if (minesAdjacents == 0) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 || y != 0) {
						obrirCasellesBuides(fila + x, columna + y);
					}
				}
			}
		}
	}

	private Button getCasella(int fila, int columna) {
		for (Node node : tauler.getChildren()) {
			if (!(node instanceof Button))
				continue;

			Integer rowIndex = GridPane.getRowIndex(node);
			Integer colIndex = GridPane.getColumnIndex(node);

			int row = (rowIndex == null) ? 0 : rowIndex;
			int col = (colIndex == null) ? 0 : colIndex;

			if (row == fila && col == columna) {
				return (Button) node;
			}
		}
		return null;
	}

	public void guardarRecord(String usuariActual, int temps, String nivell) {	
        // Validar que el username no sea null ni vacío
        if (usuariActual == null || usuariActual.trim().isEmpty()) {
            System.out.println("ERROR: El username es null o vacío, no se puede guardar el record.");
            return; 
        }
        
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Establir connexio
			String urlBaseDades = "jdbc:mysql://localhost:3306/cal";
			String usuari = "root";
			String contrasenya1 = "";

			Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya1);

			String sentencia = "INSERT INTO records (username, nivell, temps) VALUES (?, ?, ?)";
			PreparedStatement ps = c.prepareStatement(sentencia);
			ps.setString(1, usuariActual);
			ps.setString(2, nivell);
			ps.setInt(3, temps);

			ps.executeUpdate();

			System.out.println("Record guardat per a l'usuari " + usuariActual + ", temps: " + temps + ", nivell: " + nivell);

			ps.close();
			c.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mostrarRecords() {
		String urlBaseDades = "jdbc:mysql://localhost:3306/cal?";
		String usuari = "root";
		String contrasenya1 = "";

		ObservableList<Record> records = FXCollections.observableArrayList();

		String sql = "SELECT username, nivell, temps FROM records " + "ORDER BY CASE nivell " + "WHEN 'facil' THEN 1 "
				+ "WHEN 'medio' THEN 2 " + "WHEN 'dificil' THEN 3 " + "ELSE 4 END, temps ASC";

		try (Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya1);
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				records.add(new Record(rs.getString("username"), rs.getString("nivell"), rs.getInt("temps")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		recordsTable.setItems(records);
		recordsPopup.setVisible(true);
	}
	
	@FXML
	private void tancarPopupRecords(ActionEvent event) {
        recordsPopup.setVisible(false);
	}


}
