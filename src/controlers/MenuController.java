package controlers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MenuController {
    public static void Sair() {
        System.exit(0);
    }

    public static void Produto(Stage stage) {
        try {
            URL fxmlLocation = MenuController.class.getResource("/telasEstilos/cadastrarProduto.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Arquivo FXML não encontrado!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EletroTech - Sistema de Gerenciamento - Cadastrar Produto");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a interface:");
            e.printStackTrace();
        }
    }

    public static void Inicio(Stage stage) {
        try {
            URL fxmlLocation = MenuController.class.getResource("/telasEstilos/home.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Arquivo FXML não encontrado!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EletroTech - Sistema de Gerenciamento");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a interface:");
            e.printStackTrace();
        }
    }

    public static void Cliente(Stage stage) {
        try {
            URL fxmlLocation = MenuController.class.getResource("/telasEstilos/cadastrarCliente.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Arquivo FXML não encontrado!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EletroTech - Sistema de Gerenciamento - Cadastrar Cliente");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a interface:");
            e.printStackTrace();
        }
    }

    public static void Funcionario(Stage stage) {
        try {
            URL fxmlLocation = MenuController.class.getResource("/telasEstilos/cadastrarFuncionario.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Arquivo FXML não encontrado!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EletroTech - Sistema de Gerenciamento - Cadastrar Funcionário");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a interface:");
            e.printStackTrace();
        }
    }

    public static void ProcessarCliente(Stage stage) {
        try {
            URL fxmlLocation = MenuController.class.getResource("/telasEstilos/processarCliente.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Arquivo FXML não encontrado!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EletroTech - Sistema de Gerenciamento - Processar Cliente");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a interface:");
            e.printStackTrace();
        }
    }

    public static void ProcessarEstoque(Stage stage) {
        try {
            URL fxmlLocation = MenuController.class.getResource("/telasEstilos/processarEstoque.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Arquivo FXML não encontrado!");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EletroTech - Sistema de Gerenciamento - Processar Estoque");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a interface:");
            e.printStackTrace();
        }
    }
}
