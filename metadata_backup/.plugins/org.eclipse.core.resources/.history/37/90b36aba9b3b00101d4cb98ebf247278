package application;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class TamanyBuscaminesController {

    @FXML
    private ComboBox<String> comboNivel;

    @FXML
    private Button botoIniciar; 

    @FXML
    public void initialize() {
        comboNivel.setItems(FXCollections.observableArrayList(
            "Fàcil", "Mitjà", "Difícil"
        ));
        comboNivel.getSelectionModel().selectFirst();

      
    }

    @FXML
    private void onAceptarClick() {
        try {
            String nivel = comboNivel.getSelectionModel().getSelectedItem();
        

            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaulerBuscamines.fxml"));
            Parent root = loader.load();
            
            TaulerBuscaminesController controlador = loader.getController();
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
