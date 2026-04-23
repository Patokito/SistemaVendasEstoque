package daos;

import bancodedados.ConnectionDB;
import models.CategoriaModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<CategoriaModel> listar() {
        List<CategoriaModel> lista = new ArrayList<>();

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categoria");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategoriaModel categoria = new CategoriaModel(
                        rs.getInt("id"),
                        rs.getString("nome")
                );

                lista.add(categoria);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}