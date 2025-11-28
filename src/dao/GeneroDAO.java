package dao;

import conexion.DatabaseConnection;
import modelo.Genero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneroDAO {

    public List<Genero> obtenerTodos() {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT id_genero, genero FROM Generos ORDER BY genero";

        Connection conn = DatabaseConnection.getConnection(); // << NO se cierra

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Genero g = new Genero(
                    rs.getInt("id_genero"),
                    rs.getString("genero")
                );
                lista.add(g);
            }

        } catch (SQLException e) {
            System.err.println("Error GeneroDAO.obtenerTodos: " + e.getMessage());
        }

        return lista;
    }

    public Genero obtenerPorId(int id) {
        String sql = "SELECT id_genero, genero FROM Generos WHERE id_genero = ?";
        Genero g = null;

        Connection conn = DatabaseConnection.getConnection(); // << NO se cierra

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                g = new Genero(
                    rs.getInt("id_genero"),
                    rs.getString("genero")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error GeneroDAO.obtenerPorId: " + e.getMessage());
        }

        return g;
    }
}
