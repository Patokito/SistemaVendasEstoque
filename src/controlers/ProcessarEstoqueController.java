package controlers;

import daos.EstoqueDAO;
import daos.FuncionarioDAO;
import daos.ProdutoDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.EstoqueModel;
import models.FuncionarioModel;
import models.ProdutoModel;
import util.Sessao;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessarEstoqueController {

    // MENU
    @FXML private MenuItem itemInicio;
    @FXML private MenuItem itemSair;
    @FXML private MenuItem itemFuncionario;
    @FXML private MenuItem itemCliente;
    @FXML private MenuItem itemProduto;
    @FXML private MenuItem itemProcessarCliente;
    @FXML private MenuItem itemSobre;
    @FXML private Menu menuProcessar;

    // CAMPOS
    @FXML private ComboBox<ProdutoModel> cbProduto;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtBuscar;
    @FXML private Button btnProcessar;

    // TABELA
    @FXML private TableView<EstoqueModel> tbEstoque;
    @FXML private TableColumn<EstoqueModel, Integer> colId;
    @FXML private TableColumn<EstoqueModel, Integer> colIdProduto;
    @FXML private TableColumn<EstoqueModel, String> colProduto;
    @FXML private TableColumn<EstoqueModel, Integer> colIdFuncionario;
    @FXML private TableColumn<EstoqueModel, String> colNomeFuncionario;
    @FXML private TableColumn<EstoqueModel, Integer> colQuantidade;
    @FXML private TableColumn<EstoqueModel, String> colTipo;
    @FXML private TableColumn<EstoqueModel, String> colDataHora;

    private List<EstoqueModel> listaOriginal;

    @FXML
    public void initialize() {

        // MENU
        itemSair.setOnAction(e -> MenuController.Sair());

        itemInicio.setOnAction(e -> {
            Stage stage = (Stage) itemInicio.getParentPopup().getOwnerWindow();
            MenuController.Inicio(stage);
        });

        itemProduto.setOnAction(e -> {
            Stage stage = (Stage) itemProduto.getParentPopup().getOwnerWindow();
            MenuController.Produto(stage);
        });

        itemCliente.setOnAction(e -> {
            Stage stage = (Stage) itemCliente.getParentPopup().getOwnerWindow();
            MenuController.Cliente(stage);
        });

        itemFuncionario.setOnAction(e -> {
            Stage stage = (Stage) itemFuncionario.getParentPopup().getOwnerWindow();
            MenuController.Funcionario(stage);
        });

        itemProcessarCliente.setOnAction(event -> {
            Stage stage = (Stage) itemProcessarCliente.getParentPopup().getOwnerWindow();
            MenuController.ProcessarCliente(stage);
        });

        aplicarPermissoes();

        // TABELA
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdProduto.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        colIdFuncionario.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));
        colNomeFuncionario.setCellValueFactory(new PropertyValueFactory<>("funcionarioNome"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        colDataHora.setCellValueFactory(cell -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(
                    cell.getValue().getDataHora().format(formatter)
            );
        });

        // CARREGAR PRODUTOS NO COMBO
        carregarProdutos();

        // LISTAR MOVIMENTAÇÕES
        listarEstoque();

        // FILTRO
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrar(newVal));

        // BOTÃO
        btnProcessar.setOnAction(e -> processar());
    }

    private void carregarProdutos() {
        ProdutoDAO dao = new ProdutoDAO();
        List<ProdutoModel> lista = dao.ListarProdutos(null);

        cbProduto.setItems(FXCollections.observableArrayList(lista));

        // 🔥 CONVERTER (ESSENCIAL PRA CORRIGIR SEU ERRO)
        cbProduto.setConverter(new javafx.util.StringConverter<>() {

            @Override
            public String toString(ProdutoModel produto) {
                if (produto == null) return "";
                return produto.getNome() + " | R$ " + produto.getPreco() + " | Estoque: " + produto.getQuantidade();
            }

            @Override
            public ProdutoModel fromString(String string) {
                return cbProduto.getItems().stream()
                        .filter(p -> toString(p).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // MOSTRAR NOME NA LISTA
        cbProduto.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ProdutoModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getNome() + " | R$ " + item.getPreco() + " | Estoque: " + item.getQuantidade());
            }
        });

        // MOSTRAR NO CAMPO SELECIONADO
        cbProduto.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProdutoModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getNome() + " | R$ " + item.getPreco() + " | Estoque: " + item.getQuantidade());
            }
        });
    }

    private void listarEstoque() {
        EstoqueDAO dao = new EstoqueDAO();
        listaOriginal = dao.listar();

        tbEstoque.setItems(FXCollections.observableArrayList(listaOriginal));
    }

    private void filtrar(String valor) {

        if (valor == null || valor.isEmpty()) {
            tbEstoque.setItems(FXCollections.observableArrayList(listaOriginal));
            return;
        }

        List<EstoqueModel> filtrado = listaOriginal.stream()
                .filter(e -> e.getProdutoNome().toLowerCase().contains(valor.toLowerCase()))
                .collect(Collectors.toList());

        tbEstoque.setItems(FXCollections.observableArrayList(filtrado));
    }

    @FXML
    private void processar() {
        ProdutoModel produto = cbProduto.getValue();

        if (produto == null) {
            alerta("Selecione um produto válido!");
            return;
        }

        String tipo = cbTipo.getValue();
        String qtdTexto = txtQuantidade.getText();

        if (tipo == null || qtdTexto.isEmpty()) {
            alerta("Preencha todos os campos!");
            return;
        }

        int quantidade;

        try {
            quantidade = Integer.parseInt(qtdTexto);

            if (quantidade <= 0) {
                alerta("Quantidade deve ser maior que 0!");
                return;
            }

        } catch (Exception e) {
            alerta("Quantidade inválida!");
            return;
        }

        int idFuncionario = Sessao.getUsuario().getIdFuncionario();

        EstoqueDAO dao = new EstoqueDAO();

        dao.movimentar(
                produto.getIdProduto(),
                idFuncionario,
                quantidade,
                tipo
        );

        alerta("Movimentação realizada com sucesso!");

        limparCampos();
        listarEstoque();
    }

    private void limparCampos() {
        cbProduto.setValue(null);
        cbTipo.setValue(null);
        txtQuantidade.clear();
    }

    private void alerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
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