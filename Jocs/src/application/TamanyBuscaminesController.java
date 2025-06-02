package application;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TamanyBuscaminesController {

	@FXML
	private ComboBox<String> comboNivel;

	@FXML
	private Button botoIniciar;

	@FXML
	private Button botoMenu;

	@FXML
	private ImageView imgBandera;

	@FXML
	private Label textBenvingut;

	@FXML
	private Text textNivell;

	private String usuariActual;

	public void setUsername(String username) {
		this.usuariActual = username;

	}

	public String getUsuari() {
		return this.usuariActual;
	}

	@FXML
	public void initialize() {
		comboNivel.setItems(FXCollections.observableArrayList("Fàcil", "Mitjà", "Difícil"));
		comboNivel.getSelectionModel().selectFirst();
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

	@FXML
	private void onAceptarClick() {
		try {
			String nivel = comboNivel.getSelectionModel().getSelectedItem();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("TaulerBuscamines.fxml"));
			Parent root = loader.load();

			TaulerBuscaminesController controlador = loader.getController();

			controlador.setUsername(usuariActual);
			controlador.setNivell(nivel);

			Stage stage = (Stage) comboNivel.getScene().getWindow();
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("taulerBuscamines.css").toExternalForm());

			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
