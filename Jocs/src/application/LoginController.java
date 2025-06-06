package application;

import java.net.URL;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.ResourceBundle;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	// Objecte a compartir amb l'altra escena
	private Usuaris usuariActual;

	// Utilizar atributs Usuaris
	public void setUsuari(Usuaris usuariActual) {
		this.usuariActual = usuariActual;
	}

	public Usuaris getUsuari() {
		return this.usuariActual;
	}

	// Variables
	@FXML
	private ImageView avatarImg;
	@FXML
	private Label textoBienvenidoLogin;
	@FXML
	private Separator separador;
	@FXML
	private TextField textoEmailLogin;
	@FXML
	private TextField textoContrasenyaLogin;
	@FXML
	private Button botoLogin;
	@FXML
	private Button botoRegistre;

	public void iniciarSesio(ActionEvent event) throws ClassNotFoundException {

		if (emailExisteix() && contrasenyaValid()) {
			try {
				this.setUsuari(crearUsuariObj());
				System.out.println("Nom: " + usuariActual.getNom());
				System.out.println("Cognoms: " + usuariActual.getCognoms());
				System.out.println("Email: " + usuariActual.getEmail());
				System.out.println("Poblacio: " + usuariActual.getPoblacio());
				if (usuariActual != null) {
					// obrirMenu(event);
					VBox rootMenu = (VBox) FXMLLoader.load(getClass().getResource("Menu.fxml"));
					Scene pantallaMenu = new Scene(rootMenu);
					Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
					pantallaMenu.getStylesheets().add(getClass().getResource("menu.css").toExternalForm());
					window.setUserData(usuariActual);
					window.setScene(pantallaMenu);
					window.setTitle("Menu");
					window.show();
				} else {
					System.out.println("No sea creat el usuari");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			alertaError("Email - Contraseña", "El email i la contraseny no coinsidixen ");
		}

	}

	// Comprobar que el email esta en la base de datos
	public boolean emailExisteix() {
		try {
			// Variables
			String email = textoEmailLogin.getText();

			// comprobar si esta en blanc
			if (email.isBlank()) {
				alertaError("Email", "No pot estar en blanc. ");
				return false;
			}

			// Carregar el controlador per a la BD
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establir la connexió
			String urlBaseDades = "jdbc:mysql://localhost:3306/cal";
			String usuari = "root";
			String contrasenya = "";

			Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya);

			String sentencia = "SELECT * FROM usuarios WHERE email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, email);

			ResultSet r = s.executeQuery();

			if (r.next()) {// si no existeix
				System.out.println("Email registrar. ");
				return true;
			} else {
				alertaError("Email ", "El email no esta registrat. ");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Comprobar que la contraseña coincideix en el email
	public boolean contrasenyaValid() {
		try {
			// Variables
			String email = textoEmailLogin.getText();
			String contrasenya = textoContrasenyaLogin.getText();

			int iterations = 65536;
			int keyLength = 512; // bits

			if (contrasenya.isBlank()) {
				alertaError("Email", "No pot estar en blanc. ");
				return false;
			}

			// Carregar el controlador per a la BD
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establir la connexió
			String urlBaseDades = "jdbc:mysql://localhost:3306/cal";
			String usuari = "root";
			String contrasenya1 = "";

			Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya1);

			String sentencia = "SELECT contasenyaHash, salt FROM usuarios WHERE email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, email);
			ResultSet r = s.executeQuery();
			
			if (!r.next()) {
				alertaError("Login", "Email no encontrado para validar contraseña.");
				return false;
			}
				
			String hashBD = r.getString("contasenyaHash");
			String saltBase64 = r.getString("salt");
			byte[] salt = Base64.getDecoder().decode(saltBase64);

			System.out.println("Hash BD: " + hashBD);
			System.out.println("Salt BD: " + saltBase64);
			
			// crear hash
			KeySpec spec = new PBEKeySpec(contrasenya.toCharArray(), salt, iterations, keyLength);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hashCalculado = factory.generateSecret(spec).getEncoded();
			String hashCalculadoBase64 = Base64.getEncoder().encodeToString(hashCalculado);

			System.out.println("Hash calculado: " + hashCalculadoBase64);
			
			if (hashCalculadoBase64.equals(hashBD)) {
				System.out.println("Les contrasenyes coincideixen");
				return true;
			} else {
				System.out.println("Les contrasenyes no coincideixen");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Crear el objecte Usuari per a pasar els datos entre controladors
	public Usuaris crearUsuariObj() {
		try {
			String email = textoEmailLogin.getText();

			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establir la connexió
			String urlBaseDades = "jdbc:mysql://localhost:3306/cal";
			String usuari = "root";
			String contrasenya1 = "";

			Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya1);

			String sentencia = "SELECT * FROM usuarios WHERE email = ?";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, email);
			ResultSet r = s.executeQuery();

			if (r.next()) {
				Usuaris u = new Usuaris(r.getString("nom"), r.getString("cognoms"), r.getString("email"),
						r.getString("poblacio"), r.getString("contasenyaHash"), r.getString("salt"),
						r.getString("img"));
				return u;

			} else {
				alertaError("Usuari ", "No a sigut creat. ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Boto registre que envia al registre
	@FXML
	public void obrirRegistre(ActionEvent event) {
		try {
			VBox rootRegistre = (VBox) FXMLLoader.load(getClass().getResource("Registre.fxml"));
			Scene pantallaRegistre = new Scene(rootRegistre);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			pantallaRegistre.getStylesheets().add(getClass().getResource("registre.css").toExternalForm());
			window.setScene(pantallaRegistre);
			window.setTitle("Registre");
			window.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Boto iniciar sesio que envia a la escena menu
	@FXML
	public void obrirMenu(ActionEvent event) {
		try {
			VBox rootMenu = (VBox) FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene pantallaMenu = new Scene(rootMenu);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			pantallaMenu.getStylesheets().add(getClass().getResource("menu.css").toExternalForm());
			window.setUserData(this.usuariActual);
			window.setScene(pantallaMenu);
			window.setTitle("Menu");
			window.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Funcio de les alertes
	public void alertaError(String camp, String error) {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Error en el login del usuari ");
		alerta.setHeaderText("Error en el '" + camp + "'. ");
		alerta.setContentText(error);
		alerta.showAndWait();
	}

	// Mostrar login con texto Bienvenida
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			Stage stage = (Stage) textoBienvenidoLogin.getScene().getWindow();
			Object userData = stage.getUserData();

			if (userData instanceof Usuaris) {
				Usuaris usuari = (Usuaris) userData;
				textoBienvenidoLogin.setText("Bienvenido " + usuari.getNom());
			} else if (userData instanceof String) {
				String nom = (String) userData;
				textoBienvenidoLogin.setText("Bienvenido " + nom);
			} else {
				textoBienvenidoLogin.setText("Bienvenido");
			}
		});
	}
}
