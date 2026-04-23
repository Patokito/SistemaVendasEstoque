package daos;

import bancodedados.ConnectionDB;
import models.ProcessarClienteItemModel;

import java.sql.*;
import java.util.List;

public class ProcessarClienteDAO {

    public int criarProcesso(int clienteId, double total) {

        int idGerado = 0;

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO processar_cliente (cliente_id, total) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {

            stmt.setInt(1, clienteId);
            stmt.setDouble(2, total);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return idGerado;
    }

    public int criarProcesso(Connection conn, int clienteId, double total) throws SQLException {

        String sql = "INSERT INTO processar_cliente (cliente_id, total) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, clienteId);
            stmt.setDouble(2, total);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("Erro ao gerar processo");
    }

    public void inserirItens(int processoId, List<ProcessarClienteItemModel> itens) {

        String sql = "INSERT INTO processar_cliente_item " +
                "(processar_cliente_id, produto_id, nome_produto, quantidade, preco_unitario, preco_total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ProcessarClienteItemModel item : itens) {

                stmt.setInt(1, processoId);
                stmt.setInt(2, item.getProdutoId());
                stmt.setString(3, item.getNomeProduto());
                stmt.setInt(4, item.getQuantidade());
                stmt.setDouble(5, item.getPrecoUnitario());
                stmt.setDouble(6, item.getPrecoTotal());

                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserirItens(Connection conn, int processoId, List<ProcessarClienteItemModel> itens) throws SQLException {

        String sql = "INSERT INTO processar_cliente_item " +
                "(processar_cliente_id, produto_id, nome_produto, quantidade, preco_unitario, preco_total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ProcessarClienteItemModel item : itens) {

                stmt.setInt(1, processoId);
                stmt.setInt(2, item.getProdutoId());
                stmt.setString(3, item.getNomeProduto());
                stmt.setInt(4, item.getQuantidade());
                stmt.setDouble(5, item.getPrecoUnitario());
                stmt.setDouble(6, item.getPrecoTotal());

                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }
}