package controlers;

import daos.FuncionarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.FuncionarioModel;
import util.Sessao;

public class CadastrarFuncionarioController {
    @FXML
    private TextField txtUsuario;
    @FXML
    private Button btnSalvar;
    @FXML
    private MenuItem itemCliente;
    @FXML
    private MenuItem itemInicio;
    @FXML
    private MenuItem itemProduto;
    @FXML
    private MenuItem itemSair;
    @FXML
    private MenuItem itemProcessarCliente;
    @FXML
    private MenuItem itemProcessarEstoque;
    @FXML
    private MenuItem itemSobre;
    @FXML
    private RadioButton rbAdmin;
    @FXML
    private RadioButton rbFuncionario;
    @FXML
    private RadioButton rbEstagiario;
    @FXML
    private ToggleGroup tgNivel;
    @FXML
    private TextField txtNome;
    @FXML
    private PasswordField txtSenha;

    public void initialize() {

        itemSair.setOnAction(event -> MenuController.Sair());

        itemInicio.setOnAction(event -> {
            Stage stage = (Stage) itemInicio.getParentPopup().getOwnerWindow();
            MenuController.Inicio(stage);
        });

        itemCliente.setOnAction(event -> {
            Stage stage = (Stage) itemCliente.getParentPopup().getOwnerWindow();
            MenuController.Cliente(stage);
        });

        itemProduto.setOnAction(event -> {
            Stage stage = (Stage) itemProduto.getParentPopup().getOwnerWindow();
            MenuController.Produto(stage);
        });

        itemProcessarCliente.setOnAction(event -> {
            Stage stage = (Stage) itemProcessarCliente.getParentPopup().getOwnerWindow();
            MenuController.ProcessarCliente(stage);
        });

        itemProcessarEstoque.setOnAction(event -> {
            Stage stage = (Stage) itemProcessarEstoque.getParentPopup().getOwnerWindow();
            MenuController.ProcessarEstoque(stage);
        });

        // 🔥 IMPORTANTE
        btnSalvar.setOnAction(e -> salvar());
    }

    @FXML
    private void salvar() {

        String nome = txtNome.getText();
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();

        if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
            alerta("Preencha todos os campos!");
            return;
        }

        FuncionarioDAO dao = new FuncionarioDAO(); // 🔥 tem que vir antes

        if (dao.usuarioExiste(usuario)) {
            alerta("Usuário já existe!");
            return;
        }

        FuncionarioDAO daoNivel = new FuncionarioDAO();

        String nomeNivel;

        if (rbAdmin.isSelected()) {
            nomeNivel = "Admin";
        } else if (rbFuncionario.isSelected()) {
            nomeNivel = "Funcionario";
        } else if (rbEstagiario.isSelected()) {
            nomeNivel = "Estagiario";
        } else {
            alerta("Selecione um nível!");
            return;
        }

        int nivelId = daoNivel.buscarIdNivelPorNome(nomeNivel);

        FuncionarioModel f = new FuncionarioModel(0, nome, usuario, senha, nivelId);

        if (dao.cadastrar(f)) {
            alerta("Funcionário cadastrado com sucesso!");
            limparCampos();
        } else {
            alerta("Erro ao cadastrar!");
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtUsuario.clear();
        txtSenha.clear();
        tgNivel.selectToggle(null);
    }

    private void alerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
