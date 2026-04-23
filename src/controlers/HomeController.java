package controlers;

import daos.ProdutoDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.ProdutoModel;
import util.Sessao;

import java.util.List;

public class HomeController {
    @FXML
    private TableColumn<ProdutoModel, String> colCategoria;
    @FXML
    private TableColumn<ProdutoModel, String> colDescricao;
    @FXML
    private TableColumn<ProdutoModel, String> colNome;
    @FXML
    private TableColumn<ProdutoModel, Double> colPreco;
    @FXML
    private TableColumn<ProdutoModel, Integer> colid;
    @FXML
    private MenuItem itemCliente;
    @FXML
    private MenuItem itemProcessarEstoque;
    @FXML
    private MenuItem itemFuncionario;
    @FXML
    private MenuItem itemProcessarCliente;
    @FXML
    private MenuItem itemProduto;
    @FXML
    private MenuItem itemSair;
    @FXML
    private MenuItem itemSobre;
    @FXML
    private TableView<ProdutoModel> tbEstoque;
    @FXML
    private TableColumn<ProdutoModel, Integer> colQuantidade;
    @FXML
    private TextField txtBuscar;
    @FXML private Menu menuCadastrar;
    @FXML private Menu menuProcessar;
    private javafx.collections.ObservableList<ProdutoModel> listaProdutos;

    @FXML
    public void initialize() {
        itemSair.setOnAction(event -> MenuController.Sair());
        itemProduto.setOnAction(event -> {
            Stage stage = (Stage) itemProduto.getParentPopup().getOwnerWindow();
            MenuController.Produto(stage);
        });
        itemCliente.setOnAction(event -> {
            Stage stage = (Stage) itemCliente.getParentPopup().getOwnerWindow();
            MenuController.Cliente(stage);
        });
        itemFuncionario.setOnAction(event -> {
            Stage stage = (Stage) itemFuncionario.getParentPopup().getOwnerWindow();
            MenuController.Funcionario(stage);
        });
        itemProcessarCliente.setOnAction(event -> {
            Stage stage = (Stage) itemProcessarCliente.getParentPopup().getOwnerWindow();
            MenuController.ProcessarCliente(stage);
        });
        itemProcessarEstoque.setOnAction(event -> {
            Stage stage = (Stage) itemProcessarEstoque.getParentPopup().getOwnerWindow();
            MenuController.ProcessarEstoque(stage);
        });

        colid.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idProduto"));
        colNome.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("descricao"));
        colCategoria.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("categoria"));
        colPreco.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("preco"));
        colQuantidade.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantidade"));

        colPreco.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);

                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", valor));
                }
            }
        });

        ListarProdutosTab(null);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            ListarProdutosTab(newVal);
        });
        aplicarPermissoes();
    }

    public void ListarProdutosTab(String valor) {
        ProdutoDAO dao = new ProdutoDAO();

        List<ProdutoModel> produtos = dao.ListarProdutos(valor);

        listaProdutos = FXCollections.observableArrayList(produtos);
        tbEstoque.setItems(listaProdutos);
    }

    private void aplicarPermissoes() {

        String nivel = Sessao.getUsuario().getNomeNivel();

        if (nivel.equalsIgnoreCase("Estagiario")) {
            itemProduto.setDisable(true);
            itemFuncionario.setDisable(true);
            menuProcessar.setVisible(false);
        }

        if (nivel.equalsIgnoreCase("Funcionario")) {
            itemFuncionario.setDisable(true);
        }
    }
}