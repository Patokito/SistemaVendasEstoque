package controlers;

import daos.FuncionarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.FuncionarioModel;
import util.Sessao;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;
    @FXML private Button btnEntrar;

    @FXML
    public void initialize() {

        txtSenha.setOnAction(e -> login());
        btnEntrar.setOnAction(e -> login());
    }

    @FXML
    private void login() {

        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha usuário e senha!");
            return;
        }

        FuncionarioDAO dao = new FuncionarioDAO();
        FuncionarioModel funcionario = dao.login(usuario, senha);

        if (funcionario != null) {

            Sessao.setUsuario(funcionario);

            abrirHome();

        } else {
            lblMensagem.setText("Usuário ou senha inválidos!");
        }
    }

    private void abrirHome() {
        try {

            Stage stage = (Stage) btnEntrar.getScene().getWindow();

            MenuController.Inicio(stage);

        } catch (Exception e) {
            e.printStackTrace();
            lblMensagem.setText("Erro ao abrir sistema!");
        }
    }
}