package controlers;

import daos.HistoricoClienteDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ClienteModel;
import models.HistoricoClienteModel;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HistoricoClienteController {

    @FXML private Label lblId;
    @FXML private Label lblNome;
    @FXML private Label lblCpf;
    @FXML private Label lblTelefone;
    @FXML private Label lblCreatedAt;

    @FXML private TextField txtBuscarProduto;

    @FXML private TableView<HistoricoClienteModel> tbHistorico;

    @FXML private TableColumn<HistoricoClienteModel, Integer> colIdProduto;
    @FXML private TableColumn<HistoricoClienteModel, String> colNomeProduto;
    @FXML private TableColumn<HistoricoClienteModel, Integer> colQuantidade;
    @FXML private TableColumn<HistoricoClienteModel, Double> colPrecoUni;
    @FXML private TableColumn<HistoricoClienteModel, Double> colPrecoTotal;
    @FXML private TableColumn<HistoricoClienteModel, String> colDataCompra;

    private ClienteModel cliente;
    private List<HistoricoClienteModel> listaOriginal;

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;

        preencherDadosCliente();
        carregarHistorico();
    }

    @FXML
    public void initialize() {

        colIdProduto.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colNomeProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPrecoUni.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colPrecoTotal.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
        colDataCompra.setCellValueFactory(new PropertyValueFactory<>("data"));

        colPrecoUni.setCellFactory(col -> new TableCell<>() {
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

        colPrecoTotal.setCellFactory(col -> new TableCell<>() {
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

        txtBuscarProduto.textProperty().addListener((obs, oldVal, newVal) -> filtrar(newVal));
    }

    private void preencherDadosCliente() {
        lblId.setText(String.valueOf(cliente.getIdCliente()));
        lblNome.setText(cliente.getNome());
        lblCpf.setText(cliente.getCpf());
        lblTelefone.setText(cliente.getTelefone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        lblCreatedAt.setText(cliente.getCreatedAt().format(formatter));
    }

    private void carregarHistorico() {
        try {
            HistoricoClienteDAO dao = new HistoricoClienteDAO();
            listaOriginal = dao.listarPorCliente(cliente.getIdCliente());
            tbHistorico.setItems(FXCollections.observableArrayList(listaOriginal));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar histórico");
        }
    }

    private void filtrar(String valor) {

        if (valor == null || valor.isEmpty()) {
            tbHistorico.setItems(FXCollections.observableArrayList(listaOriginal));
            return;
        }

        List<HistoricoClienteModel> filtrado = listaOriginal.stream()
                .filter(h -> h.getProduto().toLowerCase().contains(valor.toLowerCase()))
                .collect(Collectors.toList());

        tbHistorico.setItems(FXCollections.observableArrayList(filtrado));
    }
}