package controlers;

import daos.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.ClienteModel;
import util.Sessao;

import java.util.List;

public class CadastrarClienteController {
    @FXML
    private Button btnHistorico;
    @FXML
    private Button btnSalvar;
    @FXML
    private TableView<ClienteModel> tbClientes;
    @FXML
    private TableColumn<ClienteModel, String> colCpf;
    @FXML
    private TableColumn<ClienteModel, Integer> colId;
    @FXML
    private TableColumn<ClienteModel, String> colNome;
    @FXML
    private TableColumn<ClienteModel, String> colTelefone;
    @FXML
    private MenuItem itemFuncionario;
    @FXML
    private MenuItem itemInicio;
    @FXML
    private MenuItem itemProcessarCliente;
    @FXML
    private MenuItem itemProcessarEstoque;
    @FXML
    private MenuItem itemProduto;
    @FXML
    private MenuItem itemSair;
    @FXML
    private MenuItem itemSobre;
    @FXML
    private Menu menuProcessar;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtNome;
    @FXML
    private Label txtLabel;
    @FXML
    private TextField txtTelefone;
    private javafx.collections.ObservableList<ClienteModel> listar;

    public void initialize() {
        itemSair.setOnAction(event -> MenuController.Sair());
        itemInicio.setOnAction(event -> {
            Stage stage = (Stage) itemInicio.getParentPopup().getOwnerWindow();
            MenuController.Inicio(stage);
        });
        itemProduto.setOnAction(event -> {
            Stage stage = (Stage) itemProduto.getParentPopup().getOwnerWindow();
            MenuController.Produto(stage);
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
        aplicarPermissoes();

        colId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            ListarClientesTab(newVal);
        });
        ListarClientesTab(null);

        tbClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, cliente) -> {
            if (cliente != null) {
                txtID.setText(String.valueOf(cliente.getIdCliente()));
                txtNome.setText(cliente.getNome());
                txtCpf.setText(cliente.getCpf());
                txtTelefone.setText(String.valueOf(cliente.getTelefone()));
            }
        });
    }

    public void ListarClientesTab(String valor) {
        ClienteDAO dao = new ClienteDAO();

        List<ClienteModel> clientes = dao.listar(valor);

        listar = FXCollections.observableArrayList(clientes);
        tbClientes.setItems(listar);
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtID.clear();
    }

    public void SalvarCliente() {

        if (txtNome.getText().isEmpty() ||
                txtCpf.getText().isEmpty() ||
                txtTelefone.getText().isEmpty()) {
            txtLabel.setText("Preencha os campos obrigatórios!");
            return;
        }

        int id = txtID.getText().isEmpty() ? 0 : Integer.parseInt(txtID.getText());

        ClienteModel cliente = new ClienteModel(
                id,
                txtNome.getText(),
                txtCpf.getText(),
                txtTelefone.getText()
        );

        ClienteDAO dao = new ClienteDAO();
        dao.salvar(cliente);

        if (id > 0) {
            txtLabel.setText("Cliente atualizado!");
        } else {
            txtLabel.setText("Cliente cadastrado!");
        }

        limparCampos();
        ListarClientesTab(null);
    }

    public void abrirHistorico() {

        ClienteModel cliente = tbClientes.getSelectionModel().getSelectedItem();

        if (cliente == null) {
            txtLabel.setText("Selecione um cliente!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telasEstilos/historicoCliente.fxml"));
            Parent root = loader.load();

            HistoricoClienteController controller = loader.getController();

            controller.setCliente(cliente);

            Stage stage = new Stage();
            stage.setTitle("Histórico do Cliente");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
