package daos;

import bancodedados.ConnectionDB;
import models.ClienteModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void salvar(ClienteModel cliente) {

        try (Connection conn = ConnectionDB.getConnection()) {

            if (cliente.getIdCliente() > 0) {

                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE cliente SET nome=?, cpf=?, telefone=? WHERE id=?"
                );

                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getCpf());
                stmt.setString(3, cliente.getTelefone());
                stmt.setInt(4, cliente.getIdCliente());

                stmt.executeUpdate();

            } else {

                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO cliente (nome, cpf, telefone) VALUES (?, ?, ?)"
                );

                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getCpf());
                stmt.setString(3, cliente.getTelefone());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ClienteModel> listar(String valor) {

        List<ClienteModel> lista = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConnection()) {

            PreparedStatement stmt;

            if (valor == null || valor.isEmpty()) {
                stmt = conn.prepareStatement("SELECT * FROM cliente");
            } else {
                stmt = conn.prepareStatement(
                        "SELECT * FROM cliente WHERE nome LIKE ? OR cpf LIKE ?"
                );
                stmt.setString(1, "%" + valor + "%");
                stmt.setString(2, "%" + valor + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new ClienteModel(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}