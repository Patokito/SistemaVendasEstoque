package daos;

import bancodedados.ConnectionDB;
import models.HistoricoClienteModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoricoClienteDAO {

    public void inserir(Connection conn,
                        int clienteId,
                        int produtoId,
                        String produtoNome,
                        int quantidade,
                        double precoUnitario) throws SQLException {

        String sql = "INSERT INTO historico_cliente " +
                "(cliente_id, produto_id, produto_nome, quantidade, preco_unitario, preco_total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            double precoTotal = quantidade * precoUnitario;

            stmt.setInt(1, clienteId);
            stmt.setInt(2, produtoId);
            stmt.setString(3, produtoNome);
            stmt.setInt(4, quantidade);
            stmt.setDouble(5, precoUnitario);
            stmt.setDouble(6, precoTotal);

            stmt.executeUpdate();
        }
    }

    public List<HistoricoClienteModel> listarPorCliente(int clienteId) {

        List<HistoricoClienteModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM historico_cliente WHERE cliente_id=? ORDER BY data DESC";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new HistoricoClienteModel(
                        rs.getInt("produto_id"),
                        rs.getString("produto_nome"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario"),
                        rs.getDouble("preco_total"),
                        rs.getTimestamp("data").toLocalDateTime()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}