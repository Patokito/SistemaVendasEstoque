package daos;

import bancodedados.ConnectionDB;
import models.FuncionarioModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FuncionarioDAO {

    public FuncionarioModel login(String usuario, String senha) {

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT f.*, n.nivel AS nivel " +
                             "FROM funcionario f " +
                             "JOIN nivel n ON f.nivel_id = n.id " +
                             "WHERE f.usuario=? AND f.senha=?"
             )) {

            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                FuncionarioModel f = new FuncionarioModel(0, null, null, null, 0);

                f.setIdFuncionario(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setUsuario(rs.getString("usuario"));
                f.setIdNivel(rs.getInt("nivel_id"));

                // 🔥 FALTAVA ISSO
                f.setNomeNivel(rs.getString("nivel"));

                return f;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean cadastrar(FuncionarioModel funcionario) {

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO funcionario (nome, usuario, senha, nivel_id) VALUES (?, ?, ?, ?)"
             )) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getUsuario());
            stmt.setString(3, funcionario.getSenha());
            stmt.setInt(4, funcionario.getIdNivel());

            stmt.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean usuarioExiste(String usuario) {

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id FROM funcionario WHERE usuario = ?"
             )) {

            stmt.setString(1, usuario);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // 🔥 se achou, retorna true

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int buscarIdNivelPorNome(String nomeNivel) {

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id FROM nivel WHERE nivel = ?"
             )) {

            stmt.setString(1, nomeNivel);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0; // se não encontrar
    }
}