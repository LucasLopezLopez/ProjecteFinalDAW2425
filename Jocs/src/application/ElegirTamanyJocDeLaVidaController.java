package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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

	int zombies = 0;
	int iniciales = 0;
	int numCeldas = 0;
	String grande = "";
	String fallo = "";
	
	
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
	private Text engrisecer;
	@FXML
	private Text engrisecer2;
	@FXML
	private Text engrisecer3;
	@FXML
	private Text textoDesa;
	@FXML
	private Button jugar;
	@FXML 
	private TextField numCasillas;
	@FXML
	private TextField numZombies;
	@FXML
	private TextField numInicials;
	
	// Objecte a compartir amb l'altra escena
	private Usuaris usuariActual;

	public void setUsuari(Usuaris usuariActual) {
		this.usuariActual = usuariActual;
	}

	public Usuaris getUsuari() {
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
		numCasillas.setVisible(false);
		//engrisecer.setStyle("-fx-fill: grey;");
		//engrisecer2.setStyle("-fx-fill: grey;");
		//engrisecer3.setStyle("-fx-fill: grey;");
	}
	
	@FXML
	public void clicarMed(MouseEvent e) {
		peq.setSelected(false);
		gran.setSelected(false);
		pers.setSelected(false);
		desactivar.setDisable(true);
		textoDesa.setVisible(false);
		numCasillas.setVisible(false);
		//engrisecer.setStyle("-fx-fill: grey;");
		//engrisecer2.setStyle("-fx-fill: grey;");
		//engrisecer3.setStyle("-fx-fill: grey;");
	}
	
	@FXML
	public void clicarGran(MouseEvent e) {
		peq.setSelected(false);
		med.setSelected(false);
		pers.setSelected(false);
		desactivar.setDisable(true);
		textoDesa.setVisible(false);
		numCasillas.setVisible(false);
		//engrisecer.setStyle("-fx-fill: grey;");
		//engrisecer2.setStyle("-fx-fill: grey;");
		//engrisecer3.setStyle("-fx-fill: grey;");
	}
	
	@FXML
	public void activar(MouseEvent e) {
		peq.setSelected(false);
		med.setSelected(false);
		gran.setSelected(false);
		if (pers.isSelected()) {
			desactivar.setDisable(false);
			textoDesa.setVisible(true);
			engrisecer.setVisible(true);
			engrisecer2.setVisible(true);
			//engrisecer3.setVisible(true);
			numCasillas.setVisible(true);
			numInicials.setVisible(true);
			//engrisecer.setStyle("-fx-fill: black;");
			//engrisecer2.setStyle("-fx-fill: black;");
			//engrisecer3.setStyle("-fx-fill: black;");
			
		} else {
			desactivar.setDisable(true);
			textoDesa.setVisible(false);
			engrisecer.setVisible(false);
			engrisecer2.setVisible(false);
			//engrisecer3.setVisible(false);
			numCasillas.setVisible(false);
			numInicials.setVisible(false);
			//engrisecer.setStyle("-fx-fill: grey;");
			//engrisecer2.setStyle("-fx-fill: grey;");
			//engrisecer3.setStyle("-fx-fill: grey;");
		}
	}
	
	@FXML
	public void juego() {
		
		try {
			numCeldas = Integer.parseInt(numCasillas.getText());
			if (((int) Math.sqrt(numCeldas)) == Math.sqrt(numCeldas)) {
				fallo = "";
			} else if (numCeldas < 9 || numCeldas > 1369) {
				fallo = "limites";
			} else {
				fallo = "noDisponible";
			}
			
			iniciales = Integer.parseInt(numInicials.getText());
			
			if (iniciales > numCeldas) {
				fallo = "sobrepoblacion";
			} else if (zombies > numCeldas) {
				fallo = "sobrepoblacionZombie";
			} else if ((zombies + iniciales) > numCeldas) {
				fallo = "sobrepoblacionTotal";
			}
			
		} catch (Exception e) {
			fallo = "noNumero";
		}
		
		
		int columnas = 0;
		if (peq.isSelected()) {
			try {
				columnas = 17;
				grande = "peq";
				iniciales = ((columnas * columnas) * 20) / 100;
				abrirNuevaVentana();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (med.isSelected()) {
			try {
				columnas = 25;
				grande = "med";
				iniciales = ((columnas * columnas) * 20) / 100;
				abrirNuevaVentana();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (gran.isSelected()) {
			try {
				columnas = 33;
				iniciales = ((columnas * columnas) * 20) / 100;
				grande = "grande";
				abrirNuevaVentana();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!peq.isSelected() && !med.isSelected() && !gran.isSelected() && !pers.isSelected()) {
			
		} else if (pers.isSelected()) {
			if (numCasillas.getText().equals("")) {
				
			} else if (numInicials.getText().equals("")) {
				
			} else if (fallo.equals("sobrepoblacionZombie")) {
				alerta("No pueden haber más células zombies de las creadas");
			} else if (fallo.equals("sobrepoblacion")) {
				alerta("No pueden haber más células iniciales de las creadas");
			} else if (fallo.equals("sobrepoblacionTotal")) {
				alerta("No pueden haber más células iniciales y zombies de las creadas");
			} else if (fallo.equals("noNumero")) {
				alerta("Debes usar números enteros");
			} else if (fallo.equals("noDisponible")) {
				alerta("Lo siento, la cifra de células marcadas no está disponible");
			} else if (fallo.equals("limites")) {
				alerta("Debe elegir un número del 9 al 1.369");
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
		
		if (fallo.equals("sobrepoblacionZombie")) {
			zombies = 0;
			numZombies.setText("");
		} else if (fallo.equals("sobrepoblacionTotal")) {
			zombies = 0;
			iniciales = 0;
			numInicials.setText("");
			numZombies.setText("");
		} else if (fallo.equals("sobrepoblacion")) {
			iniciales = 0;
			numInicials.setText("");
		} else if (fallo.equals("limites")) {
			numCasillas.setText("");
		} else if (fallo.equals("noNumero")) {
			
		} else if (resultado.isPresent() && resultado.get() == botonOkay) {
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
        root.getStylesheets().add(getClass().getResource("tamanyBuscamines.css").toExternalForm());

        JocDeLaVidaController controller = loader.getController();

        controller.celdas = numCeldas;
		controller.celdas = numCeldas;
		//controller.zombies = zombies;
		controller.celulas = iniciales;
        controller.inicializarTablero(grande); 

        Stage nuevaVentana = new Stage();
        nuevaVentana.setTitle("Joc De La Vida - Tablero");
        nuevaVentana.setScene(new Scene(root));

        Stage ventanaActual = (Stage) jugar.getScene().getWindow();
        nuevaVentana.initOwner(ventanaActual.getOwner()); 
        nuevaVentana.initModality(Modality.NONE);

        nuevaVentana.show();

        // Cerramos la ventana de selección
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
		engrisecer.setVisible(false);
		engrisecer2.setVisible(false);
		//engrisecer3.setVisible(false);
		numCasillas.setVisible(false);
		//numZombies.setVisible(false);
		numInicials.setVisible(false);
		//engrisecer.setStyle("-fx-fill: grey;");
		//engrisecer2.setStyle("-fx-fill: grey;");
		//engrisecer3.setStyle("-fx-fill: grey;");
	}



	
}

