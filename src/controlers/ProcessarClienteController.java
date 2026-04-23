package controlers;

import bancodedados.ConnectionDB;
import daos.ClienteDAO;
import daos.HistoricoClienteDAO;
import daos.ProcessarClienteDAO;
import daos.ProdutoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.ClienteModel;
import models.ProcessarClienteItemModel;
import models.ProdutoModel;
import util.Sessao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProcessarClienteController {

    @FXML
    private ComboBox<ClienteModel> cbCliente;
    @FXML
    private ComboBox<ProdutoModel> cbProduto;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private Label txtLabel;
    @FXML
    private TextField txtBuscarCarrinho;
    @FXML
    private TableView<ProcessarClienteItemModel> tbCarrinho;
    @FXML
    private TableColumn<ProcessarClienteItemModel, String> colProduto;
    @FXML
    private TableColumn<ProcessarClienteItemModel, Integer> colQtd;
    @FXML
    private TableColumn<ProcessarClienteItemModel, Double> colPrecoUni;
    @FXML
    private TableColumn<ProcessarClienteItemModel, Double> colPrecoTotal;
    @FXML
    private MenuItem itemCliente;
    @FXML
    private MenuItem itemFuncionario;
    @FXML
    private MenuItem itemProcessarEstoque;
    @FXML
    private MenuItem itemProduto;
    @FXML
    private MenuItem itemInicio;
    @FXML
    private Menu menuProcessar;
    @FXML
    private MenuItem itemSair;
    private ProcessarClienteItemModel itemSelecionado;
    private ObservableList<ProcessarClienteItemModel> carrinho = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        itemSair.setOnAction(e -> MenuController.Sair());
        itemInicio.setOnAction(e -> {
            Stage stage = (Stage) itemInicio.getParentPopup().getOwnerWindow();
            MenuController.Inicio(stage);
        });
        itemCliente.setOnAction(e -> {
            Stage stage = (Stage) itemCliente.getParentPopup().getOwnerWindow();
            MenuController.Cliente(stage);
        });
        itemFuncionario.setOnAction(e -> {
            Stage stage = (Stage) itemFuncionario.getParentPopup().getOwnerWindow();
            MenuController.Funcionario(stage);
        });
        itemProduto.setOnAction(e -> {
            Stage stage = (Stage) itemProduto.getParentPopup().getOwnerWindow();
            MenuController.Produto(stage);
        });
        itemProcessarEstoque.setOnAction(event -> {
            Stage stage = (Stage) itemProcessarEstoque.getParentPopup().getOwnerWindow();
            MenuController.ProcessarEstoque(stage);
        });

        aplicarPermissoes();

        colProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPrecoUni.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colPrecoTotal.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));

        txtBuscarCarrinho.textProperty().addListener((obs, oldVal, newVal) -> {

            String filtro = newVal.toLowerCase();

            List<ProcessarClienteItemModel> filtrado = carrinho.stream()
                    .filter(item ->
                            item.getNomeProduto().toLowerCase().contains(filtro)
                    )
                    .toList();

            tbCarrinho.getItems().setAll(filtrado);
        });

        tbCarrinho.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldItem, newItem) -> {

                    if (newItem != null) {

                        itemSelecionado = newItem;

                        for (ProdutoModel p : cbProduto.getItems()) {
                            if (p.getIdProduto() == newItem.getProdutoId()) {
                                cbProduto.setValue(p);
                                break;
                            }
                        }

                        txtQuantidade.setText(String.valueOf(newItem.getQuantidade()));
                    }
                }
        );
        tbCarrinho.setItems(carrinho);

        cbCliente.setItems(FXCollections.observableArrayList());
        cbProduto.setItems(FXCollections.observableArrayList());

        configurarComboCliente();
        configurarComboProduto();
    }

    public void atualizarItemCarrinho() {

        if (itemSelecionado == null) {
            System.out.println("Selecione um item primeiro");
            return;
        }

        try {

            int novaQtd = Integer.parseInt(txtQuantidade.getText());

            if (novaQtd <= 0) {
                System.out.println("Quantidade inválida");
                return;
            }

            itemSelecionado.setQuantidade(novaQtd);

            double precoUnitario = itemSelecionado.getPrecoUnitario();
            itemSelecionado.setPrecoTotal(precoUnitario * novaQtd);

            tbCarrinho.refresh();

            System.out.println("Item atualizado!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarComboCliente() {

        cbCliente.setEditable(true);
        ClienteDAO dao = new ClienteDAO();

        cbCliente.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal == null || newVal.isEmpty()) return;

            List<ClienteModel> lista = dao.listar(newVal);

            if (lista != null && !lista.isEmpty()) {
                cbCliente.setItems(FXCollections.observableArrayList(lista));
                cbCliente.show();
            }
        });

        cbCliente.setOnAction(e -> {
            Object value = cbCliente.getValue();

            if (value instanceof ClienteModel c) {
                cbCliente.getEditor().setText(c.getNome());
            } else {
                cbCliente.setValue(null);
            }
        });

        cbCliente.setConverter(new StringConverter<>() {
            @Override
            public String toString(ClienteModel obj) {
                return obj != null ? obj.getNome() : "";
            }

            @Override
            public ClienteModel fromString(String string) {
                if (string == null || string.isEmpty()) return null;

                for (ClienteModel c : cbCliente.getItems()) {
                    if (c.getNome().equalsIgnoreCase(string)) {
                        return c;
                    }
                }
                return null;
            }
        });
    }

    private void configurarComboProduto() {

        cbProduto.setEditable(true);
        ProdutoDAO dao = new ProdutoDAO();

        cbProduto.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal == null || newVal.isEmpty()) return;

            List<ProdutoModel> lista = dao.ListarProdutos(newVal);

            if (lista != null && !lista.isEmpty()) {
                cbProduto.setItems(FXCollections.observableArrayList(lista));
                cbProduto.show();
            }
        });

        cbProduto.setOnAction(e -> {
            Object value = cbProduto.getValue();

            if (value instanceof ProdutoModel p) {
                cbProduto.getEditor().setText(p.getNome());
            } else {
                cbProduto.setValue(null);
            }
        });

        cbProduto.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProdutoModel obj) {
                return obj != null ? obj.getNome() : "";
            }

            @Override
            public ProdutoModel fromString(String string) {
                if (string == null || string.isEmpty()) return null;

                for (ProdutoModel p : cbProduto.getItems()) {
                    if (p.getNome().equalsIgnoreCase(string)) {
                        return p;
                    }
                }
                return null;
            }
        });
    }

    public void adicionarProduto() {

        ProdutoModel produto = cbProduto.getValue();

        if (produto == null) {
            txtLabel.setText("Selecione um produto");
            return;
        }

        int qtd;

        try {
            qtd = Integer.parseInt(txtQuantidade.getText());
        } catch (Exception e) {
            txtLabel.setText("Quantidade inválida");
            return;
        }

        if (qtd > produto.getQuantidade()) {
            txtLabel.setText("Estoque insuficiente");
            return;
        }

        carrinho.add(new ProcessarClienteItemModel(
                0,
                produto.getIdProduto(),
                produto.getNome(),
                qtd,
                produto.getPreco()
        ));

        txtQuantidade.clear();

        cbProduto.getSelectionModel().clearSelection();
        cbProduto.setValue(null);
        cbProduto.getEditor().clear();

        txtLabel.setText("Produto adicionado");
    }

    public void removerItem() {

        ProcessarClienteItemModel item =
                tbCarrinho.getSelectionModel().getSelectedItem();

        if (item == null) {
            txtLabel.setText("Selecione um item");
            return;
        }

        carrinho.remove(item);
    }

    public void finalizarCompra() {

        ClienteModel cliente = cbCliente.getValue();

        if (cliente == null) {
            txtLabel.setText("Selecione um cliente");
            return;
        }

        if (carrinho.isEmpty()) {
            txtLabel.setText("Carrinho vazio");
            return;
        }

        double total = carrinho.stream()
                .mapToDouble(ProcessarClienteItemModel::getPrecoTotal)
                .sum();

        Connection conn = null;

        try {
            conn = ConnectionDB.getConnection();
            conn.setAutoCommit(false);

            ProcessarClienteDAO processoDAO = new ProcessarClienteDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();
            HistoricoClienteDAO historicoDAO = new HistoricoClienteDAO();

            int processoId = processoDAO.criarProcesso(conn, cliente.getIdCliente(), total);

            processoDAO.inserirItens(conn, processoId, carrinho);

            for (ProcessarClienteItemModel item : carrinho) {

                ProdutoModel produto = produtoDAO.buscarPorId(conn, item.getProdutoId());

                if (produto == null) {
                    throw new Exception("Produto não encontrado");
                }

                if (produto.getQuantidade() < item.getQuantidade()) {
                    throw new Exception("Estoque insuficiente");
                }

                produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
                produtoDAO.atualizarEstoque(conn, produto);

                historicoDAO.inserir(conn,
                        cliente.getIdCliente(),
                        item.getProdutoId(),
                        item.getNomeProduto(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                );
            }

            conn.commit();

            carrinho.clear();
            txtLabel.setText("Compra finalizada com sucesso!");

        } catch (Exception e) {

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            txtLabel.setText("Erro ao finalizar compra");

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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