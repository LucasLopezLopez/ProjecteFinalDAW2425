package application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override

	public void start(Stage primaryStage) {

			try {
				VBox rootLogin = (VBox) FXMLLoader.load(getClass().getResource("Login.fxml"));
				Scene scene = new Scene(rootLogin, 450, 600);
				scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setTitle("Login");
				primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	public static void main(String[] args) {
		launch(args);
	}

}

