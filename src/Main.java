import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Caminho seguro do FXML
            URL fxmlLocation = getClass().getResource("/telasEstilos/login.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Arquivo FXML não encontrado!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setTitle("EletroTech - Sistema de Gerenciamento");
            stage.setWidth(1280);
            stage.setHeight(720);
            stage.setScene(scene);
            stage.setResizable(true);

            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a interface:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}