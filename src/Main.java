import conexion.DatabaseConnection;
import ui.VentanaBiblioteca;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        // Configuramos el Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        // Inicializamos la conexión cuando DAO la pida
        DatabaseConnection.getConnection();

        // Lanzamos la interfaz en el EDT
        javax.swing.SwingUtilities.invokeLater(() -> new VentanaBiblioteca());
    }
}
