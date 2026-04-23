package daos;

import bancodedados.ConnectionDB;
import models.EstoqueModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {

    // 🔥 MOVIMENTAR ESTOQUE
    public void movimentar(int idProduto, int idFuncionario, int quantidade, String tipo) {

        try (Connection conn = ConnectionDB.getConnection()) {

            // 🔒 VERIFICAR ESTOQUE (para SAÍDA)
            PreparedStatement check = conn.prepareStatement(
                    "SELECT quantidade FROM produto WHERE id=?"
            );
            check.setInt(1, idProduto);

            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                int estoqueAtual = rs.getInt("quantidade");

                if (tipo.equals("SAÍDA") && estoqueAtual < quantidade) {
                    throw new RuntimeException("Estoque insuficiente!");
                }
            }

            // 📝 INSERIR MOVIMENTAÇÃO
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO processar_estoque " +
                            "(produto_id, funcionario_id, quantidade, tipo_operacao, data_processamento) " +
                            "VALUES (?, ?, ?, ?, NOW())"
            );

            insert.setInt(1, idProduto);
            insert.setInt(2, idFuncionario);
            insert.setInt(3, quantidade);
            insert.setString(4, tipo);

            insert.executeUpdate();

            // 🔄 ATUALIZAR ESTOQUE DO PRODUTO
            PreparedStatement update;

            if (tipo.equals("ENTRADA")) {
                update = conn.prepareStatement(
                        "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?"
                );
            } else {
                update = conn.prepareStatement(
                        "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?"
                );
            }

            update.setInt(1, quantidade);
            update.setInt(2, idProduto);

            update.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 📋 LISTAR MOVIMENTAÇÕES (COM JOIN)
    public List<EstoqueModel> listar() {

        List<EstoqueModel> lista = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(

                     "SELECT em.id, em.produto_id, em.funcionario_id, " +
                             "em.quantidade, em.tipo_operacao, em.data_processamento, " +
                             "p.nome AS produto_nome, " +
                             "f.nome AS funcionario_nome " +

                             "FROM processar_estoque em " +

                             "JOIN produto p ON em.produto_id = p.id " +
                             "JOIN funcionario f ON em.funcionario_id = f.id " +

                             "ORDER BY em.data_processamento DESC"
             )) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                EstoqueModel e = new EstoqueModel(
                        rs.getInt("id"),
                        rs.getInt("produto_id"),
                        rs.getString("produto_nome"),
                        rs.getInt("funcionario_id"),
                        rs.getString("funcionario_nome"),
                        rs.getInt("quantidade"),
                        rs.getString("tipo_operacao"),
                        rs.getTimestamp("data_processamento").toLocalDateTime()
                );

                lista.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}