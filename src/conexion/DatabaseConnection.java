package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


  //Singleton para la conexión a SQL Server.
  //Ajustá URL, USER y PASSWORD según el entorno.

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Biblioteca;encrypt=true;trustServerCertificate=true";       
    private static final String USER = "usuarioJava";              
    private static final String PASSWORD = "Tomi2001+"; 

    private static Connection conn = null;

    private DatabaseConnection() { }

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión a SQL Server establecida.");
            } catch (SQLException e) {
                System.err.println("Error al conectar a la base de datos: " + e.getMessage());
                
            }
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
