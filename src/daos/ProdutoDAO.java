package daos;

import bancodedados.ConnectionDB;
import models.ProdutoModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public List<ProdutoModel> ListarProdutos(String valor) {
        List<ProdutoModel> produtos = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConnection()) {

            PreparedStatement stmt;
            ResultSet resultado;

            if (valor == null || valor.isEmpty()) {
                stmt = conn.prepareStatement(
                        "SELECT p.*, c.nome AS categoria " +
                                "FROM produto p " +
                                "LEFT JOIN categoria c ON p.categoria_id = c.id"
                );
            } else {
                stmt = conn.prepareStatement(
                        "SELECT p.*, c.nome AS categoria " +
                                "FROM produto p " +
                                "LEFT JOIN categoria c ON p.categoria_id = c.id " +
                                "WHERE p.descricao LIKE ? OR p.nome LIKE ? OR c.nome LIKE ?"
                );
                stmt.setString(1, "%" + valor + "%");
                stmt.setString(2, "%" + valor + "%");
                stmt.setString(3, "%" + valor + "%");
            }

            resultado = stmt.executeQuery();

            while (resultado.next()) {
                ProdutoModel p = new ProdutoModel(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getString("descricao"),
                        resultado.getDouble("preco"),
                        resultado.getInt("quantidade"),
                        resultado.getString("codigo_barras"),
                        resultado.getString("categoria")
                );

                produtos.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public void salvar(ProdutoModel produto) {

        try (Connection conn = ConnectionDB.getConnection()) {

            if (produto.getIdProduto() > 0) {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE produto SET nome=?, descricao=?, preco=?, quantidade=?, codigo_barras=?, categoria_id=? WHERE id=?"
                );

                stmt.setString(1, produto.getNome());
                stmt.setString(2, produto.getDescricao());
                stmt.setDouble(3, produto.getPreco());
                stmt.setInt(4, produto.getQuantidade());
                stmt.setString(5, produto.getCodBarras());
                stmt.setInt(6, produto.getCategoriaId());
                stmt.setInt(7, produto.getIdProduto());

                stmt.executeUpdate();

            } else {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO produto (nome, descricao, preco, quantidade, codigo_barras, categoria_id) VALUES (?, ?, ?, ?, ?, ?)"
                );

                stmt.setString(1, produto.getNome());
                stmt.setString(2, produto.getDescricao());
                stmt.setDouble(3, produto.getPreco());
                stmt.setInt(4, produto.getQuantidade());
                stmt.setString(5, produto.getCodBarras());
                stmt.setInt(6, produto.getCategoriaId());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir(int idProduto) {

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM produto WHERE id=?")) {

            stmt.setInt(1, idProduto);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProdutoModel buscarPorId(Connection conn, int id) throws SQLException {

        String sql = "SELECT * FROM produto WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ProdutoModel p = new ProdutoModel(0,null, null, 0, 0, null, null);

                p.setIdProduto(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setDescricao(rs.getString("descricao"));
                p.setPreco(rs.getDouble("preco"));
                p.setQuantidade(rs.getInt("quantidade"));
                p.setCodBarras(rs.getString("codigo_barras"));
                p.setCategoriaId(rs.getInt("categoria_id"));

                return p;
            }
        }

        return null;
    }

    public void atualizarEstoque(Connection conn, ProdutoModel produto) throws SQLException {

        String sql = "UPDATE produto SET quantidade = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getQuantidade());
            stmt.setInt(2, produto.getIdProduto());

            stmt.executeUpdate();
        }
    }
}
