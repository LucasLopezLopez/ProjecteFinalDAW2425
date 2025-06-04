package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController implements Initializable {

	// Objecte a compartir amb l'altra escena
	private Usuaris usuariActual;

	public void setUsuari(Usuaris usuariActual) {
		this.usuariActual = usuariActual;
	}

	public Usuaris getUsuari() {
		return this.usuariActual;
	}

	// Atributos Usuaris
	private String emailUsuari;

	// variables
	@FXML
	private VBox root;
	@FXML
	private ImageView avatarImg;
	@FXML
	private Label textoBienvenido;
	@FXML
	private Label textoJuegos;
	@FXML
	private ImageView imgAjustes;
	@FXML
	private ImageView imgBuscaMines;
	@FXML
	private ImageView imgPixelArt;
	@FXML
	private ImageView imgJocVida;
	@FXML
	private ImageView imgWordles;
	@FXML
	private Button botoBuscaMines;
	@FXML
	private Button botoPixelArt;
	@FXML
	private Button botoJocVida;
	@FXML
	private Button botoWordles;

	// Variables menu ajustes
	private ContextMenu menuDesplegable;
	private MenuItem itemCambiarFoto;
	private MenuItem itemBorrarUsuari;
	private MenuItem itemTanca;
	private SeparatorMenuItem separador;

	// Variable finestres obertes
	private Stage windowBuscamines = null;
	private Stage windoePixelArt = null;
	private Stage windowJocVida = null;
	private Stage windowWordle = null;

	// Menu ajustes desplegable
	@FXML
	public void menuDesplegable(MouseEvent event) {
		Platform.runLater(() -> {
			try {
	            if (menuDesplegable != null && menuDesplegable.isShowing()) {
	                return;
	            }

				// menu de austes
				menuDesplegable = new ContextMenu();
				// opcions del menu
				itemCambiarFoto = new MenuItem("Cambiar foto de perfil");
				itemBorrarUsuari = new MenuItem("Eliminar cuenta");
				itemTanca = new MenuItem("Cerrar sesión");
				separador = new SeparatorMenuItem();

				itemCambiarFoto.setStyle("-fx-font-family: 'CutePixel'; -fx-font-size: 14px;");
				itemBorrarUsuari.setStyle("-fx-font-family: 'CutePixel'; -fx-font-size: 14px;");
				itemTanca.setStyle("-fx-font-family: 'CutePixel'; -fx-font-size: 14px;");

				// Añadir les opcions al menu
				menuDesplegable.getItems().addAll(itemCambiarFoto, separador, itemBorrarUsuari, itemTanca);

				// Funcio cambiar foto de perfil desde el menu
				itemCambiarFoto.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Cambiar foto de perfil. ");
						try {
							cambiarAvatar(event);
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
				});

				// Tancar sesio desde el menu
				itemTanca.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							logout(event);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				// Borrar usuari de la base de datos desde el menu
				itemBorrarUsuari.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Borrar usuari. ");
						try {
							borrarUsuari(event);
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
				});

				// Mostrar Menu Ajustes , abajo izquierda
				Node node = (Node) event.getSource();
				menuDesplegable.show(node, Side.BOTTOM, -105, 0);

	            menuDesplegable.setOnHidden(e -> {
	                menuDesplegable = null;
	            });
	            
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	// Boton borrar usuairo
	@FXML
	public void borrarUsuari(ActionEvent event) throws ClassNotFoundException, IOException {
		try {
			// Carregar el controlador per a la BD
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establir la connexió
			String urlBaseDades = "jdbc:mysql://localhost:3306/cal";
			String usuari = "root";
			String contrasenya = "";

			Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya);
			String sentencia = "DELETE FROM usuarios where email = ? ";
			PreparedStatement s = c.prepareStatement(sentencia);
			s.setString(1, emailUsuari);

			int resultat = s.executeUpdate();

			if (resultat > 0) {
				System.out.println("Usuari eliminat. ");
				logout(event);
			} else {
				System.out.println("No existeix ningun email .");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Cambiar img del avatar
	// Pasar la img a fichero bite i el fichero guardarlo en la base de datos
	@FXML
	public void cambiarAvatar(ActionEvent event) throws ClassNotFoundException, IOException {
		try {
			try {
				// Carregar el controlador per a la BD
				Class.forName("com.mysql.cj.jdbc.Driver");

				// Establir la connexió
				String urlBaseDades = "jdbc:mysql://localhost:3306/cal";
				String usuari = "root";
				String contrasenya = "";
				Connection c = DriverManager.getConnection(urlBaseDades, usuari, contrasenya);

				// Elegir el path de la img
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Selecciona una imagen");

				File file = fileChooser.showOpenDialog(null);

				if (file != null) {
					// Actualizar img en la base de datos segun el email
					FileInputStream inputStream = new FileInputStream(file);
					String sentencia = "UPDATE usuarios SET img = ? WHERE email = ?";
					PreparedStatement s = c.prepareStatement(sentencia);
					s.setBinaryStream(1, inputStream, (int) file.length());
					s.setString(2, emailUsuari);
					int filas = s.executeUpdate();

					if (filas > 0) {
						// Añadir la foto visualment
						System.out.println("Avatar actualizat");
						Image imgAvatar = new Image(file.toURI().toString());
						avatarImg.setImage(imgAvatar);
					} else {
						System.out.println("No se pudo actualizar la imagen");
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {

		}
	}

	// Boto per a entrar als jocs
	@FXML
	public void obrirBuscaMines(MouseEvent event) {
		// si la ventana existe y se esta mostrando , muestra error
		if (windowBuscamines != null && windowBuscamines.isShowing()) {
			alertaError("BuscaMines", "El juego ya esta abierto.");
			return;
		}
		try {
			// Crear el FXMLLoader para acceder al controlador
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TamanyBuscamines.fxml"));
			VBox rootBuscaMines = loader.load();

			Scene pantallaBuscaMines = new Scene(rootBuscaMines);
			pantallaBuscaMines.getStylesheets().add(getClass().getResource("tamanyBuscamines.css").toExternalForm());

			// Obtener el controlador
			TamanyBuscaminesController controlador = loader.getController();
			controlador.setUsername(usuariActual.getEmail());

			// Crear y configurar la nueva ventana
			windowBuscamines = new Stage();
			windowBuscamines.setScene(pantallaBuscaMines);
			windowBuscamines.setTitle("Buscamines");

			// obtener la nueva ventana , aplicar el menu como ventana principal i modalidad ninguna para poder intractuar con el menu
			Stage windowMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
			windowBuscamines.initOwner(windowMenu);
			windowBuscamines.initModality(Modality.NONE);

			// si pulsa 'X' cierra la ventana y guarda la partida
			windowBuscamines.setOnCloseRequest(x -> {
				if (controlador != null) {
					//controlador.guardarPartida();
				}
				windowBuscamines = null; 
			});
			
			// mostrar ventana
			windowBuscamines.show();

		} catch (Exception e) {
			e.printStackTrace();
			alertaError("Error al abrir BuscaMines","Ha ocurrido un error al intentar abrir el juego. Intenta de nuevo.");
		}
	}

	@FXML
	public void obrirPixelArt(MouseEvent event) {
		if (windoePixelArt != null && windoePixelArt.isShowing()) {
			alertaError("Pixel Art", "El juego ya esta abierto.");
			return;
		}
		try {
			// cargar el FXML i obeter el controlado
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TamanyPixelart.fxml"));
			VBox rootPixelArt = (VBox) loader.load();
			
			// carear Scene i css
			Scene pantallaPixelArt = new Scene(rootPixelArt);
			pantallaPixelArt.getStylesheets().add(getClass().getResource("tamanyPixelart.css").toExternalForm());

			// obtener controlador del fxml anterior i enviar el email del usuairo
			TamanyPixelartController controlador = loader.getController();
			controlador.setUsername(usuariActual.getEmail());
			
			// crar una ventana para el jugo aparte del menu
			windoePixelArt = new Stage();
			windoePixelArt.setScene(pantallaPixelArt);
			windoePixelArt.setTitle("Pixel Art");

			// obtener la ventana
			Stage windowMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
			windoePixelArt.initOwner(windowMenu);
			windoePixelArt.initModality(Modality.NONE);

			windoePixelArt.setOnCloseRequest(e -> {
				if (controlador != null) {
					//controlador.guardarPartida();
				}
				windoePixelArt = null; 
			});
			
			windoePixelArt.show();

		} catch (Exception e) {
			e.printStackTrace();
			alertaError("Error al abrir Pixel Art",
					"Ha ocurrido un error al intentar abrir el juego. Intenta de nuevo.");
		}

	}

	@FXML
	public void obrirJocVida(MouseEvent event) {
		if (windowJocVida != null && windowJocVida.isShowing()) {
			alertaError("Joc De La Vida", "El juego ya esta abierto.");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ElegirTamanyJocDeLaVida.fxml"));
			VBox rootJocVida = (VBox) loader.load();
			
			Scene pantallaJocVida = new Scene(rootJocVida);
			pantallaJocVida.getStylesheets().add(getClass().getResource("ElegirTamanyJocDeLaVida.css").toExternalForm());

			ElegirTamanyJocDeLaVidaController controlador = loader.getController();
			//controlador.setUsername(usuariActual.getEmail());
			
			windowJocVida = new Stage();
			windowJocVida.setScene(pantallaJocVida);
			windowJocVida.setTitle("Joc De La Vida");

			Stage windowMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
			windowJocVida.initOwner(windowMenu);
			windowJocVida.initModality(Modality.NONE);

			windowJocVida.setOnCloseRequest(e -> {
				if (controlador != null) {
					//controlador.guardarPartida();
				}
				windowJocVida = null; 
			});
			
			windowJocVida.show();

		} catch (Exception e) {
			e.printStackTrace();
			alertaError("Error al abrir Joc De La Vida",
					"Ha ocurrido un error al intentar abrir el juego. Intenta de nuevo.");
		}
	}

	@FXML
	public void obrirWordles(MouseEvent event) {
		if (windowWordle != null && windowWordle.isShowing()) {
			alertaError("Wordle", "El juego ya esta abierto.");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Wordle.fxml"));
			VBox rootWordles = (VBox) (VBox) loader.load();
			
			Scene pantallaWordles = new Scene(rootWordles);
			pantallaWordles.getStylesheets().add(getClass().getResource("Wordle.css").toExternalForm());

			WordleController controlador = loader.getController();
			controlador.setUsername(usuariActual.getEmail());
			
			windowWordle = new Stage();
			windowWordle.setScene(pantallaWordles);
			windowWordle.setTitle("Wordle");

			Stage windowMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
			windowWordle.initOwner(windowMenu);
			windowWordle.initModality(Modality.NONE);

			windowWordle.setOnCloseRequest(e -> {
				if (controlador != null) {
					//controlador.guardarPartida();
				}
				windowWordle = null; 
			});
			
			windowWordle.show();

		} catch (Exception e) {
			e.printStackTrace();
			alertaError("Error al abrir Wordle",
					"Ha ocurrido un error al intentar abrir el juego. Intenta de nuevo.");
		}
	}
	// Boton cerrar sesion

	public void logout(ActionEvent event) {
		try {
			if (windowBuscamines != null) {
				windowBuscamines.close();
				windowBuscamines = null;
			}
			if (windoePixelArt != null) {
				windoePixelArt.close();
				windoePixelArt = null;
			}
			if (windowJocVida != null) {
				windowJocVida.close();
				windowJocVida = null;
			}
			if (windowWordle != null) {
				windowWordle.close();
				windowWordle = null;
			}

			VBox rootLogout = (VBox) FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene pantallaIrLogin = new Scene(rootLogout);
			Stage windowLogin = new Stage();
			windowLogin.setScene(pantallaIrLogin);
			windowLogin.setTitle("Login");
			windowLogin.show();

			MenuItem menuItem = (MenuItem) event.getSource();
			Stage windowMenu = (Stage) menuItem.getParentPopup().getOwnerWindow();
			windowMenu.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Funcio de les alertes
	public void alertaError(String camp, String error) {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Error abriendo un juego. ");
		alerta.setHeaderText("Error en el '" + camp + "'. ");
		alerta.setContentText(error);
		alerta.showAndWait();
	}

	// Recuperar els datos del objecte usaris
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		Platform.runLater(() -> {
			Stage window = (Stage) root.getScene().getWindow();
			if (window != null) {
				this.usuariActual = (Usuaris) window.getUserData();
				if (usuariActual != null) {
					this.emailUsuari = usuariActual.getEmail(); // recuperar el email per a poder eliminar el usuari segun el email
					textoBienvenido.setText("Bienvenido " + usuariActual.getNom());// Texto de bienvenida
				} else {
					//System.out.println("El objecte usuari es null. ");
				}
			} else {
				System.out.println("La stage es null, no se mostra. ");
			}

		});
	}

}
