package controlers;

import daos.CategoriaDAO;
import daos.ProdutoDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.CategoriaModel;
import models.ProdutoModel;
import util.Sessao;

import java.util.List;

public class CadastrarProdutoController {
    @FXML
    private TableColumn<ProdutoModel, String> colCategoria;
    @FXML
    private TableColumn<ProdutoModel, String> colCodBarras;
    @FXML
    private TableColumn<ProdutoModel, String> colDescricao;
    @FXML
    private TableColumn<ProdutoModel, String> colNome;
    @FXML
    private TableColumn<ProdutoModel, Integer> colQuantidade;
    @FXML
    private TableColumn<ProdutoModel, Integer> colid;
    @FXML
    private TableColumn<ProdutoModel, Double> colPreco;
    @FXML
    private TableView<ProdutoModel> tbProdutos;
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<CategoriaModel> txtCategoria;
    @FXML
    private TextField txtCodBarras;
    @FXML
    private TextField txtDescricao;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private Label txtLabel;
    @FXML
    private MenuItem itemCliente;
    @FXML
    private MenuItem itemProcessarEstoque;
    @FXML
    private MenuItem itemFuncionario;
    @FXML
    private MenuItem itemProcessarCliente;
    @FXML
    private MenuItem itemSair;
    @FXML
    private MenuItem itemInicio;
    @FXML
    private Menu menuProcessar;
    @FXML
    private MenuItem itemSobre;
    private javafx.collections.ObservableList<ProdutoModel> listaProdutos;

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

        txtCodBarras.setText(ProdutoModel.gerarEAN13());
        aplicarPermissoes();

        colid.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colCodBarras.setCellValueFactory(new PropertyValueFactory<>("codBarras"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        carregarCategorias();
        ListarProdutosTab(null);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            ListarProdutosTab(newVal);
        });

        tbProdutos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, produto) -> {

            if (produto != null) {
                txtID.setText(String.valueOf(produto.getIdProduto()));
                txtNome.setText(produto.getNome());
                txtDescricao.setText(produto.getDescricao());
                txtPreco.setText(String.valueOf(produto.getPreco()));
                txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
                txtCodBarras.setText(produto.getCodBarras());

                for (CategoriaModel cat : txtCategoria.getItems()) {
                    if (cat.getNome().equals(produto.getCategoria())) {
                        txtCategoria.setValue(cat);
                        break;
                    }
                }
            }
        });
    }

    public void ListarProdutosTab(String valor) {
        ProdutoDAO dao = new ProdutoDAO();

        List<ProdutoModel> produtos = dao.ListarProdutos(valor);

        listaProdutos = FXCollections.observableArrayList(produtos);
        tbProdutos.setItems(listaProdutos);
        tbProdutos.refresh();
    }

    private void limparCampos() {
        txtNome.clear();
        txtDescricao.clear();
        txtPreco.clear();
        txtQuantidade.clear();
        txtID.clear();
    }

    private void carregarCategorias() {
        CategoriaDAO dao = new CategoriaDAO();
        List<CategoriaModel> lista = dao.listar();

        txtCategoria.setItems(FXCollections.observableArrayList(lista));
    }

    public void SalvarProduto() {

        if (txtNome.getText().isEmpty() ||
                txtDescricao.getText().isEmpty() ||
                txtCategoria.getValue() == null ||
                txtPreco.getText().isEmpty()) {

            txtLabel.setText("Preencha os campos obrigatórios!");
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(txtPreco.getText());
        } catch (Exception e) {
            txtLabel.setText("Preço inválido!");
            return;
        }

        int quantidade = 0;
        if (!txtQuantidade.getText().isEmpty()) {
            try {
                quantidade = Integer.parseInt(txtQuantidade.getText());
            } catch (Exception e) {
                txtLabel.setText("Quantidade inválida!");
                return;
            }
        }

        CategoriaModel cat = txtCategoria.getValue();

        int id = txtID.getText().isEmpty() ? 0 : Integer.parseInt(txtID.getText());

        ProdutoModel produto = new ProdutoModel(
                id,
                txtNome.getText(),
                txtDescricao.getText(),
                preco,
                quantidade,
                txtCodBarras.getText(),
                cat.getNome()
        );

        produto.setCategoriaId(cat.getIdCategoria());

        ProdutoDAO dao = new ProdutoDAO();
        dao.salvar(produto);

        if (id > 0) {
            txtLabel.setText("Produto atualizado!");
        } else {
            txtLabel.setText("Produto cadastrado!");
        }

        limparCampos();
        ListarProdutosTab(null);
    }

    public void ExcluirProduto() {

        ProdutoModel selecionado = tbProdutos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            txtLabel.setText("Selecione um produto!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Deseja excluir este produto?");
        alert.showAndWait();

        if (alert.getResult() != ButtonType.OK) {
            return;
        }

        ProdutoDAO dao = new ProdutoDAO();
        dao.excluir(selecionado.getIdProduto());

        txtLabel.setText("Produto excluído!");

        limparCampos();
        ListarProdutosTab(null);
    }

    private void aplicarPermissoes() {

        String nivel = Sessao.getUsuario().getNomeNivel();

        if (nivel.equalsIgnoreCase("Estagiario")) {
            itemFuncionario.setDisable(true);
            menuProcessar.setVisible(false);
        }

        if (nivel.equalsIgnoreCase("Funcionario")) {
            itemFuncionario.setDisable(true);
        }
    }
}
