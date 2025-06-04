package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JocDeLaVidaController implements Initializable {

	@FXML
	private VBox pagina;
	
	@FXML
	private GridPane taula;
	@FXML
	private Button volverMenu;
	@FXML
	private Label labelVivas;
	
	@FXML
	private Label labelZombies;
	
	@FXML
	private Label labelMuertas;
	
	@FXML
	private Label labelGen;
	
	@FXML
	private Button reiniciar;
	
	@FXML
	private Button pausar;
	
	@FXML
	private Button velocidad;
	
	
	public int[]vivasRonda = new int [6];
	private Timeline timeline;
	String velocidadActual = "";
	String estado = "";
	int temporizador = 1000;
	String dimensiones = "";
	public int generacion = 0;
	public int zombies = 0;
	public int vivas = 0;
	public int muertas = 0;
	public int celulas = 0;
	public int celdas = 0;
	public int columnas = 0;
	public double tamanyo = 0;
	public Label [][] matriu;
	Celula [][] comunitat;
	Celula [][][] arrayMatriu = new Celula[4][][];
	

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
	
	
	public void juego() {
		celInicials();
		timeline = new Timeline(new KeyFrame(Duration.millis(temporizador), e -> {
			mostrar();
            evolucion();
            System.out.println(generacion);
	    }));
	    timeline.setCycleCount(1000);
	    timeline.play();
	}
	
	public void pausa() {
		if (estado.equals("")) {
			timeline.pause();
			estado = "pausado";
			pausar.setText("Reanudar");
		} else if (estado.equals("pausado")) {
			timeline.play();
			estado = "";
			pausar.setText("Pausar");
		}
	}
	
	
	public void aumentar() {
		switch(velocidadActual) {
		case "": 
				velocidadActual = "x2";
				temporizador = 500;
				velocidad.setText("x2");
			break;
			
		case "x2":
				velocidadActual = "x4";
				temporizador = 250;
				velocidad.setText("x4");
			break;
			
		case "x4":
				velocidadActual = "x5";
				temporizador = 200;
				velocidad.setText("x5");
			break;
		
		case "x5":
				velocidadActual = "x10";
				temporizador = 100;
				velocidad.setText("x10");
			break;
			
		case "x10":
				velocidadActual = "";
				temporizador = 1000;
				velocidad.setText("x1");
			break;
			
		}
		
		 if (timeline != null) {
	        timeline.stop();
	        timeline = new Timeline(new KeyFrame(Duration.millis(temporizador), e -> {
	            mostrar();
	            evolucion();
	            System.out.println(generacion);
	        }));
	        timeline.setCycleCount(Timeline.INDEFINITE);
	        if (!estado.equals("pausado")) {
	            timeline.play();
	        }
	    }
	}
	
	public void reinicio() {
		timeline.stop();
		generacion = 0;
		zombies = 0;
		vivas = 0;
		muertas = 0;
		double gridPaneLado = 0;
		if (dimensiones.equals("peq")) {
			columnas = 17;
			gridPaneLado = 170;
			tamanyo = gridPaneLado / columnas;

		    taula.setMaxSize(gridPaneLado, gridPaneLado);
		    taula.setPrefSize(gridPaneLado, gridPaneLado);
		    taula.setMinSize(gridPaneLado, gridPaneLado);
			
		} else if (dimensiones.equals("med")) {
			columnas = 25;
			gridPaneLado = 250;
			tamanyo = gridPaneLado / columnas;

		    taula.setMaxSize(gridPaneLado, gridPaneLado);
		    taula.setPrefSize(gridPaneLado, gridPaneLado);
		    taula.setMinSize(gridPaneLado, gridPaneLado);
			
		} else if (dimensiones.equals("grande")) {
			columnas = 33;
			gridPaneLado = 330;
			tamanyo = gridPaneLado / columnas;

		    taula.setMaxSize(gridPaneLado, gridPaneLado);
		    taula.setPrefSize(gridPaneLado, gridPaneLado);
		    taula.setMinSize(gridPaneLado, gridPaneLado);
			
			
		} else if (dimensiones.equals("pers")) {
			double raiz = Math.sqrt(celdas);
			columnas = (int) raiz;
			gridPaneLado = 350;
			tamanyo = gridPaneLado / columnas;
			taula.setMaxHeight(350);
			taula.setMinWidth(350);
		}
		
		matriu = new Label[columnas][columnas];
		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				Label lb = new Label();
				matriu[i][j] = lb;
				lb.setMinSize(tamanyo, tamanyo);
				lb.setMaxSize(tamanyo, tamanyo);
				taula.add(lb, j, i);
			}
		}
		
		taula.setStyle("-fx-alignment: center;");
		labelGen.setText("" + generacion);
		labelVivas.setText("" + celulas);
		labelMuertas.setText("0");
		//labelZombies.setText("" + zombies);
		pausar.setText("Pausar");
		temporizador = 1000;
		velocidad.setText("x1");
		juego();
	}
	

	public void celInicials() {
		comunitat = new Celula[columnas][columnas];
		arrayMatriu = new Celula[4][comunitat.length][comunitat.length];
		System.out.println(comunitat.length);
		int contador = 0;
		while (contador != celulas) {
			int randomX = (int) (Math.random() * columnas);
			int randomY = (int) (Math.random() * columnas);
			
			if (comunitat[randomX][randomY] == null) {
				comunitat[randomX][randomY] = new Celula(0, true, "nace");
				contador++;
			}
			
		}
		
		contador = 0;
		while (contador != zombies) {
			int randomX = (int) (Math.random() * columnas);
			int randomY = (int) (Math.random() * columnas);
			
			if (comunitat[randomX][randomY] == null) {
				comunitat[randomX][randomY] = new Celula(0, true, "zombie");
				contador++;
			}
			
		}
		
		for (int i = 0; i < comunitat.length; i++) {
			for (int j = 0; j < comunitat.length; j++) {
				if (comunitat[i][j] == null) {
					comunitat[i][j] = new Celula(0, false, "muerta");
				    arrayMatriu[generacion][i][j] = new Celula(comunitat[i][j].getId(), comunitat[i][j].isViva(), comunitat[i][j].getEstado());
				        
				}
			}
		}	
	}
	
	public void evolucion() {
		int vivasActuales = 0;
		Celula [][] copia = new Celula[comunitat.length][comunitat.length];
		for (int i = 0; i < comunitat.length; i++) {
		    for (int j = 0; j < comunitat[i].length; j++) {
		    	copia[i][j] = new Celula(comunitat[i][j].getId(), comunitat[i][j].isViva(), comunitat[i][j].getEstado());
		    }
		}
		
		for (int i = 0; i < copia.length; i++) {
			for (int j = 0; j < copia.length; j++) {
				if (comunitat[i][j].isViva()) {
					vivasActuales++;
				}
			}
		}
		
		
		
		
		generacion++;
		int colindantes = 0;
		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				colindantes = 0;
				if (i == 0 && j == 0) {
					if (comunitat[i][j + 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j + 1].isViva()) {
						colindantes++;
					}
					
				} else if (i == matriu.length - 1 && j == 0) {
					if (comunitat[i - 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j + 1].isViva()) {
						colindantes++;
					} if (comunitat[i][j + 1].isViva()) {
						colindantes++;
					}
				} else if (j == matriu.length - 1 && i == 0) {
					if (comunitat[i][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j].isViva()) {
						colindantes++;
					}
				} else if (j == 0) {
					if (comunitat[i - 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j + 1].isViva()) {
						colindantes++;
					} if (comunitat[i][j + 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j + 1].isViva()) {
						colindantes++;
					}
				} else if (i == 0) {
					if (comunitat[i][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i][j + 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j + 1].isViva()) {
						colindantes++;
					}
				} else if (i == matriu.length - 1 && j == matriu.length - 1) {
					if (comunitat[i][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j].isViva()) {
						colindantes++;
					}
				} else if (i == matriu.length - 1) {
					if (comunitat[i - 1][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j + 1].isViva()) {
						colindantes++;
					} if (comunitat[i][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i][j + 1].isViva()) {
						colindantes++;
					}
				} else if (j == matriu.length - 1) {
					if (comunitat[i - 1][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i][j - 1].isViva()) {
						colindantes++;
					}if (comunitat[i + 1][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j].isViva()) {
						colindantes++;
					}
				} else {
					if (comunitat[i - 1][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i - 1][j + 1].isViva()) {
						colindantes++;
					}if (comunitat[i][j - 1].isViva()) {
						colindantes++;
					} if (comunitat[i][j + 1].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j - 1].isViva()) {
						colindantes++;
					}if (comunitat[i + 1][j].isViva()) {
						colindantes++;
					} if (comunitat[i + 1][j + 1].isViva()) {
						colindantes++;
					}
				}
				
				if (colindantes < 2 || colindantes > 3) {
					copia[i][j].setViva(false);
					
				} else if (colindantes == 3) {
					copia[i][j].setViva(true);
				}
				
				if (!comunitat[i][j].isViva() && copia[i][j].isViva()) {
					copia[i][j].setEstado("nace");
					vivas++;
				} else if (comunitat[i][j].isViva() && !copia[i][j].isViva()) {
					copia[i][j].setEstado("muere");
					muertas++;
				} else if (comunitat[i][j].isViva() && copia[i][j].isViva()) {
					copia[i][j].setEstado("viva");
					
				} else if (!comunitat[i][j].isViva() && !copia[i][j].isViva()){
					copia[i][j].setEstado("muerta");
				}
				
			}
		}
		
		for (int i = 0; i < copia.length; i++) {
			for (int j = 0; j < copia.length; j++) {
				comunitat[i][j] = copia[i][j];
			}
		}
		
		acabar(vivasActuales);
		
	}
	
	public void acabar(int num) {

		int contador = generacion - 1;

		if (generacion < 7) {

			vivasRonda[contador] = num;

			

			

		} else {

			

			for (int i = 0; i < vivasRonda.length - 1; i++) {

				vivasRonda[i] = vivasRonda[i + 1];

			}

			

		}

		

		boolean acabar = false;

		

		if ((vivasRonda[0] == vivasRonda[2] && vivasRonda[0] == vivasRonda[3]) || (vivasRonda[0] == vivasRonda[2] && vivasRonda[0] == vivasRonda[4])) {

			acabar = true;

		}


		if (acabar) { 

			timeline.stop();

		}

		

		

	}
	
	public void mostrar() {
		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				
				if (comunitat[i][j].getEstado().equals("nace")) {
					matriu[i][j].setStyle("-fx-background-color: #009dff;");
				} else if (comunitat[i][j].getEstado().equals("viva")) {
					matriu[i][j].setStyle("-fx-background-color: #0006ff;");
				} else if (comunitat[i][j].getEstado().equals("muere")) {
					matriu[i][j].setStyle("-fx-background-color: black;");
				} else if (comunitat[i][j].getEstado().equals("muerta")){
					matriu[i][j].setStyle("-fx-background-color: white;");
				}
			}
		}
		
		labelGen.setText("" + generacion);
		labelVivas.setText("" + (celulas + vivas));
		labelMuertas.setText("" + muertas);
		//labelZombies.setText("" + zombies);
		
	}
	
	
	public void acabar() {
		boolean compro = true;
		
		if (generacion <= 4) {
		    for (int i = 0; i < comunitat.length; i++) {
		        for (int j = 0; j < comunitat.length; j++) {
		            arrayMatriu[generacion][i][j] = new Celula(
		                comunitat[i][j].getId(),
		                comunitat[i][j].isViva(),
		                comunitat[i][j].getEstado()
		            );
		        }
		    }
		} else {
			for (int l = 0; l < 2; l++) {
				for (int i = 0; i < comunitat.length; i++) {
					for (int j = 0; j < comunitat.length; j++) {
						this.arrayMatriu[l][i][j] = this.arrayMatriu[l + 1][i][j];
						
					}
				}
			}
			
			for (int i = 0; i < comunitat.length; i++) {
				for (int j = 0; j < comunitat.length; j++) {
					arrayMatriu[3][i][j] = new Celula(comunitat[i][j].getId(), comunitat[i][j].isViva(), comunitat[i][j].getEstado());
					
				}
			}

			for (int l = 0; l < 2; l++) {
				for (int i = 0; i < comunitat.length; i++) {
					for (int j = 0; j < comunitat.length; j++) {
						if (arrayMatriu[l][i][j].equals(arrayMatriu[l + 1][i][j])) {
							
						} else {
							compro = false;
						}
						
					}
				}
			}				
		}
		
		
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void inicializarTablero(String grandaria) {
		dimensiones = grandaria;
		double gridPaneLado = 0;
		if (grandaria.equals("peq")) {
			columnas = 17;
			gridPaneLado = 170;
			tamanyo = gridPaneLado / columnas;

		    taula.setMaxSize(gridPaneLado, gridPaneLado);
		    taula.setPrefSize(gridPaneLado, gridPaneLado);
		    taula.setMinSize(gridPaneLado, gridPaneLado);
			
		} else if (grandaria.equals("med")) {
			columnas = 25;
			gridPaneLado = 250;
			tamanyo = gridPaneLado / columnas;

		    taula.setMaxSize(gridPaneLado, gridPaneLado);
		    taula.setPrefSize(gridPaneLado, gridPaneLado);
		    taula.setMinSize(gridPaneLado, gridPaneLado);
			
		} else if (grandaria.equals("grande")) {
			columnas = 33;
			gridPaneLado = 330;
			tamanyo = gridPaneLado / columnas;

		    taula.setMaxSize(gridPaneLado, gridPaneLado);
		    taula.setPrefSize(gridPaneLado, gridPaneLado);
		    taula.setMinSize(gridPaneLado, gridPaneLado);
			
			
		} else if (grandaria.equals("pers")) {
			double raiz = Math.sqrt(celdas);
			columnas = (int) raiz;
			gridPaneLado = 350;
			tamanyo = gridPaneLado / columnas;
			taula.setMaxHeight(350);
			taula.setMinWidth(350);
		}
		
		if (grandaria.equals("pers")) {
			
			for (int i = 0; i < columnas; i++) {
			    ColumnConstraints col = new ColumnConstraints();
			    col.setPrefWidth(tamanyo);
			    col.setMinWidth(tamanyo);
			    col.setMaxWidth(tamanyo);
			    taula.getColumnConstraints().add(col);
			    
			    RowConstraints row = new RowConstraints();
			    row.setPrefHeight(tamanyo);
			    row.setMinHeight(tamanyo);
			    row.setMaxHeight(tamanyo);
			    taula.getRowConstraints().add(row);
			}
		}
		
		
		matriu = new Label[columnas][columnas];
		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[i].length; j++) {
				Label lb = new Label();
				matriu[i][j] = lb;
				lb.setMinSize(tamanyo, tamanyo);
				lb.setMaxSize(tamanyo, tamanyo);
				taula.add(lb, j, i);
			}
		}
		
		taula.setStyle("-fx-alignment: center;");
		labelGen.setText("" + generacion);
		labelVivas.setText("" + celulas);
		labelMuertas.setText("0");
		//labelZombies.setText("" + zombies);
		juego();
	}
	
		
}

