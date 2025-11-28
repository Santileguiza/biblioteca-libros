package dao;

import conexion.DatabaseConnection;
import modelo.Autor;
import modelo.Editorial;
import modelo.Genero;
import modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // CREATE - devuelve id generado
    public int crear(Libro libro) {

        String sql = "INSERT INTO Libros (titulo, id_autor, id_genero, id_editorial, precio, isbn, tipo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, libro.getTitulo());
            ps.setInt(2, libro.getAutor().getId());
            ps.setInt(3, libro.getGenero().getId());
            ps.setInt(4, libro.getEditorial().getId());
            ps.setDouble(5, libro.getPrecio());
            ps.setString(6, libro.getIsbn());
            ps.setString(7, libro.getTipo()); 

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    libro.setId(id);
                    return id;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error LibroDAO.crear: " + e.getMessage());
        }

        return -1;
    }

    // LEER TODOS
    public List<Libro> obtenerTodos() {

        List<Libro> lista = new ArrayList<>();

        String sql = "SELECT l.id_libro, l.titulo, l.precio, l.isbn, l.tipo, " +
                     "a.id_autor, a.autor, g.id_genero, g.genero, e.id_editorial, e.editorial " +
                     "FROM Libros l " +
                     "JOIN Autores a ON l.id_autor = a.id_autor " +
                     "JOIN Generos g ON l.id_genero = g.id_genero " +
                     "JOIN Editoriales e ON l.id_editorial = e.id_editorial " +
                     "ORDER BY l.titulo";

        Connection conn = DatabaseConnection.getConnection();

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Autor a = new Autor(rs.getInt("id_autor"), rs.getString("autor"));
                Genero g = new Genero(rs.getInt("id_genero"), rs.getString("genero"));
                Editorial ed = new Editorial(rs.getInt("id_editorial"), rs.getString("editorial"));

                Libro l = new Libro(
                        rs.getInt("id_libro"),
                        rs.getString("titulo"),
                        a,
                        g,
                        ed,
                        rs.getDouble("precio"),
                        rs.getString("isbn"),
                        rs.getString("tipo") 
                );

                lista.add(l);
            }

        } catch (SQLException e) {
            System.err.println("Error LibroDAO.obtenerTodos: " + e.getMessage());
        }

        return lista;
    }

    // LEER POR ID
    public Libro obtenerPorId(int idLibro) {

        String sql = "SELECT l.id_libro, l.titulo, l.precio, l.isbn, l.tipo, " +
                     "a.id_autor, a.autor, g.id_genero, g.genero, e.id_editorial, e.editorial " +
                     "FROM Libros l " +
                     "JOIN Autores a ON l.id_autor = a.id_autor " +
                     "JOIN Generos g ON l.id_genero = g.id_genero " +
                     "JOIN Editoriales e ON l.id_editorial = e.id_editorial " +
                     "WHERE l.id_libro = ?";

        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Autor a = new Autor(rs.getInt("id_autor"), rs.getString("autor"));
                Genero g = new Genero(rs.getInt("id_genero"), rs.getString("genero"));
                Editorial ed = new Editorial(rs.getInt("id_editorial"), rs.getString("editorial"));

                return new Libro(
                        rs.getInt("id_libro"),
                        rs.getString("titulo"),
                        a,
                        g,
                        ed,
                        rs.getDouble("precio"),
                        rs.getString("isbn"),
                        rs.getString("tipo") 
                );
            }

        } catch (SQLException e) {
            System.err.println("Error LibroDAO.obtenerPorId: " + e.getMessage());
        }

        return null;
    }

    // UPDATE
    public boolean actualizar(Libro libro) {

        String sql = "UPDATE Libros SET titulo=?, id_autor=?, id_genero=?, id_editorial=?, precio=?, isbn=?, tipo=? " +
                     "WHERE id_libro=?";

        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setInt(2, libro.getAutor().getId());
            ps.setInt(3, libro.getGenero().getId());
            ps.setInt(4, libro.getEditorial().getId());
            ps.setDouble(5, libro.getPrecio());
            ps.setString(6, libro.getIsbn());
            ps.setString(7, libro.getTipo()); // << NUEVO
            ps.setInt(8, libro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error LibroDAO.actualizar: " + e.getMessage());
        }

        return false;
    }

    // DELETE
    public boolean eliminar(int idLibro) {

        String sql = "DELETE FROM Libros WHERE id_libro = ?";

        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error LibroDAO.eliminar: " + e.getMessage());
        }

        return false;
    }
}
