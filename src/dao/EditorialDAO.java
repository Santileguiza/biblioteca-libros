package dao;

import conexion.DatabaseConnection;
import modelo.Editorial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAO {

    public List<Editorial> obtenerTodos() {
        List<Editorial> lista = new ArrayList<>();
        String sql = "SELECT id_editorial, editorial FROM Editoriales ORDER BY editorial";

        Connection conn = DatabaseConnection.getConnection();  // << NO se cierra

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Editorial e = new Editorial(
                    rs.getInt("id_editorial"),
                    rs.getString("editorial")
                );
                lista.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Error EditorialDAO.obtenerTodos: " + e.getMessage());
        }

        return lista;
    }

    public Editorial obtenerPorId(int id) {
        String sql = "SELECT id_editorial, editorial FROM Editoriales WHERE id_editorial = ?";
        Editorial e = null;

        Connection conn = DatabaseConnection.getConnection(); // << NO se cierra

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                e = new Editorial(
                    rs.getInt("id_editorial"),
                    rs.getString("editorial")
                );
            }

        } catch (SQLException ex) {
            System.err.println("Error EditorialDAO.obtenerPorId: " + ex.getMessage());
        }

        return e;
    }
}

