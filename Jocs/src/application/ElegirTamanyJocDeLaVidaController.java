package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ElegirTamanyJocDeLaVidaController implements Initializable {

	int numCeldas = 0;
	String grande = "";

	@FXML
	private CheckBox peq;
	@FXML
	private CheckBox med;
	@FXML
	private CheckBox gran;
	@FXML
	private CheckBox pers;
	@FXML
	private HBox desactivar;
	@FXML
	private Text textoDesa;
	@FXML
	private Button jugar;
	@FXML
	private TextField numCasillas;
	@FXML
	private Label textBenvingut;
	@FXML
	private Text textTamany;
	
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

	@FXML
	public void clicarPeq(MouseEvent e) {
		med.setSelected(false);
		gran.setSelected(false);
		pers.setSelected(false);
		desactivar.setDisable(true);
		textoDesa.setVisible(false);
	}

	@FXML
	public void clicarMed(MouseEvent e) {
		peq.setSelected(false);
		gran.setSelected(false);
		pers.setSelected(false);
		desactivar.setDisable(true);
		textoDesa.setVisible(false);
	}

	@FXML
	public void clicarGran(MouseEvent e) {
		peq.setSelected(false);
		med.setSelected(false);
		pers.setSelected(false);
		desactivar.setDisable(true);
		textoDesa.setVisible(false);
	}

	@FXML
	public void activar(MouseEvent e) {
		peq.setSelected(false);
		med.setSelected(false);
		gran.setSelected(false);
		if (pers.isSelected()) {
			desactivar.setDisable(false);
			textoDesa.setVisible(true);
			numCasillas.setVisible(true);
		} else {
			desactivar.setDisable(true);
			textoDesa.setVisible(false);
			numCasillas.setVisible(false);
		}
	}

	@FXML
	public void juego() {
		int fallo = 0;

		try {
			numCeldas = Integer.parseInt(numCasillas.getText());
			if (((int) Math.sqrt(numCeldas)) == Math.sqrt(numCeldas)) {
				fallo = 0;
			} else {
				fallo = 2;
			}
		} catch (Exception e) {
			fallo = 1;
		}

		if (peq.isSelected()) {
			try {
				grande = "peq";
				abrirNuevaVentana();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (med.isSelected()) {
			try {
				grande = "med";
				abrirNuevaVentana();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (gran.isSelected()) {
			try {
				grande = "grande";
				abrirNuevaVentana();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!peq.isSelected() && !med.isSelected() && !gran.isSelected() && !pers.isSelected()) {
			System.out.println("gay");
		} else if (pers.isSelected()) {
			if (numCasillas.getText().equals("")) {
				System.out.println("doble gay");
			} else if (fallo == 1) {
				alerta("Debes usar números enteros");
			} else if (fallo == 2) {
				alerta("Lo siento, la cifra de células marcadas no está disponible");
			} else {

				try {
					grande = "pers";
					abrirNuevaVentana();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void alerta(String mensaje) {
		ButtonType botonOkay = new ButtonType("De acuerdo", ButtonData.OK_DONE);
		Alert alertaGanar = new Alert(Alert.AlertType.CONFIRMATION);
		alertaGanar.setTitle("Hola");
		alertaGanar.setHeaderText(mensaje);
		alertaGanar.getButtonTypes().setAll(botonOkay);
		Optional<ButtonType> resultado = alertaGanar.showAndWait();

		if (resultado.isPresent() && resultado.get() == botonOkay) {
			sugerenciaNum();
		} else {
			sugerenciaNum();
		}
	}

	public void sugerenciaNum() {
		boolean correcte = false;
		int auxArriba = numCeldas;
		int auxAbajo = numCeldas;

		while (!correcte) {
			auxArriba++;
			auxAbajo--;
			if (auxArriba > 22500) {

			} else if (auxAbajo < 9) {

			} else if (Math.sqrt(auxArriba) == (int) Math.sqrt(auxArriba)) {
				numCeldas = auxArriba;
				correcte = true;

			} else if (Math.sqrt(auxAbajo) == (int) Math.sqrt(auxAbajo)) {
				numCeldas = auxAbajo;
				correcte = true;

			}
		}

		numCasillas.setText("" + numCeldas);
	}

	public void abrirNuevaVentana() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/JocDeLaVida.fxml"));
        Parent root = loader.load();

        JocDeLaVidaController controller = loader.getController();

        controller.celdas = numCeldas;  
        controller.inicializarTablero(grande); 

        Stage nuevaVentana = new Stage();
        nuevaVentana.setTitle("Joc De La Vida - Tablero");
        nuevaVentana.setScene(new Scene(root));

        Stage ventanaActual = (Stage) jugar.getScene().getWindow();
        nuevaVentana.initOwner(ventanaActual.getOwner()); 
        nuevaVentana.initModality(Modality.NONE);

        nuevaVentana.show();

        ventanaActual.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		peq.setSelected(false);
		med.setSelected(false);
		gran.setSelected(false);
		pers.setSelected(false);
		desactivar.setDisable(true);
		textoDesa.setVisible(false);
		numCasillas.setVisible(false);
	}

}
