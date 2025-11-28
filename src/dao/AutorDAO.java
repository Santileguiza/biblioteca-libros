package dao;

import conexion.DatabaseConnection;
import modelo.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public List<Autor> obtenerTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT id_autor, autor FROM Autores ORDER BY autor";

        Connection conn = DatabaseConnection.getConnection(); // << NO SE CIERRA

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Autor a = new Autor(
                        rs.getInt("id_autor"),
                        rs.getString("autor")
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            System.err.println("Error AutorDAO.obtenerTodos: " + e.getMessage());
        }

        return lista;
    }

    public Autor obtenerPorId(int id) {
        String sql = "SELECT id_autor, autor FROM Autores WHERE id_autor = ?";
        Autor a = null;

        Connection conn = DatabaseConnection.getConnection(); // << NO SE CIERRA

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                a = new Autor(
                        rs.getInt("id_autor"),
                        rs.getString("autor")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error AutorDAO.obtenerPorId: " + e.getMessage());
        }

        return a;
    }
}
