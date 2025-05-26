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
	  
	            FXMLLoader loader = new FXMLLoader(
	                getClass().getResource("/application/Wordle.fxml")
	            );
	            VBox root = loader.load();

	            Scene scene = new Scene(root, 800, 500);
	         
	            scene.getStylesheets().add(
	                getClass().getResource("/application/Wordle.css").toExternalForm()
	            );

	            primaryStage.setTitle("Polno");
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}

