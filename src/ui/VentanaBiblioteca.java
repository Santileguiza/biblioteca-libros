package ui;

import dao.AutorDAO;
import dao.EditorialDAO;
import dao.GeneroDAO;
import dao.LibroDAO;
import factory.CreadorLibro;
import factory.CreadorLibroNormal;
import factory.CreadorLibroOferta;
import factory.CreadorLibroPremium;
import modelo.Autor;
import modelo.Editorial;
import modelo.Genero;
import modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


 // Interfaz Swing completa para CRUD de Libros.
 
public class VentanaBiblioteca extends JFrame {

    private LibroDAO libroDAO = new LibroDAO();
    private AutorDAO autorDAO = new AutorDAO();
    private GeneroDAO generoDAO = new GeneroDAO();
    private EditorialDAO editorialDAO = new EditorialDAO();

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtId, txtTitulo, txtPrecio, txtIsbn;
    private JComboBox<Autor> cbAutor;
    private JComboBox<Genero> cbGenero;
    private JComboBox<Editorial> cbEditorial;
    private JComboBox<String> cbTipoCreacion; // Normal / Oferta / Premium

    public VentanaBiblioteca() {
        setTitle("Biblioteca - CRUD de Libros");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Gestión de Libros", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(
        new Object[]{"ID","Título","Autor","Género","Editorial","Precio","ISBN","Tipo"},
        0
        ) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Panel formulario 
        JPanel panelForm = new JPanel(new GridLayout(14, 1, 5,5));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtTitulo = new JTextField();
        txtPrecio = new JTextField();
        txtIsbn = new JTextField();
        cbAutor = new JComboBox<>();
        cbGenero = new JComboBox<>();
        cbEditorial = new JComboBox<>();
        cbTipoCreacion = new JComboBox<>(new String[]{"Normal","Oferta","Premium"});

        panelForm.add(new JLabel("ID (seleccionar en la tabla):"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Título:"));
        panelForm.add(txtTitulo);
        panelForm.add(new JLabel("Autor:"));
        panelForm.add(cbAutor);
        panelForm.add(new JLabel("Género:"));
        panelForm.add(cbGenero);
        panelForm.add(new JLabel("Editorial:"));
        panelForm.add(cbEditorial);
        panelForm.add(new JLabel("Precio:"));
        panelForm.add(txtPrecio);
        panelForm.add(new JLabel("ISBN:"));
        panelForm.add(txtIsbn);
        panelForm.add(new JLabel("Tipo de creación (Factory):"));
        panelForm.add(cbTipoCreacion);

        add(panelForm, BorderLayout.EAST);

        // Panel botones
        JPanel panelBot = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar por ID");
        JButton btnMostrar = new JButton("Mostrar Todos");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBot.add(btnAgregar);
        panelBot.add(btnActualizar);
        panelBot.add(btnEliminar);
        panelBot.add(btnBuscar);
        panelBot.add(btnMostrar);
        panelBot.add(btnLimpiar);

        add(panelBot, BorderLayout.SOUTH);

        // Cargar combobox y tabla
        cargarCombos();
        cargarTabla();

        // Listeners
        btnAgregar.addActionListener(e -> agregar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscarPorId());
        btnMostrar.addActionListener(e -> cargarTabla());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cargarDesdeTabla();
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                
            }
        });

        setVisible(true);
    }

    private void cargarCombos() {
        cbAutor.removeAllItems();
        cbGenero.removeAllItems();
        cbEditorial.removeAllItems();

        List<Autor> autores = autorDAO.obtenerTodos();
        for (Autor a : autores) cbAutor.addItem(a);

        List<Genero> generos = generoDAO.obtenerTodos();
        for (Genero g : generos) cbGenero.addItem(g);

        List<Editorial> editoriales = editorialDAO.obtenerTodos();
        for (Editorial ed : editoriales) cbEditorial.addItem(ed);
    }

    private void cargarTabla() {
    modeloTabla.setRowCount(0);
    List<Libro> lista = libroDAO.obtenerTodos();
    for (Libro l : lista) {
        modeloTabla.addRow(new Object[]{
                l.getId(),
                l.getTitulo(),
                l.getAutor() != null ? l.getAutor().getNombreCompleto() : "—",
                l.getGenero() != null ? l.getGenero().getNombre() : "—",
                l.getEditorial() != null ? l.getEditorial().getNombre() : "—",
                String.format("%.2f", l.getPrecio()),
                l.getIsbn(),
                l.getTipo()   
        });
    }
    }


    private void agregar() {
    if (!validarCampos(false)) return;

    Autor autor = (Autor) cbAutor.getSelectedItem();
    Genero genero = (Genero) cbGenero.getSelectedItem();
    Editorial editorial = (Editorial) cbEditorial.getSelectedItem();

    double precio = Double.parseDouble(txtPrecio.getText());

    Libro base = new Libro(txtTitulo.getText(), autor, genero, editorial, precio, txtIsbn.getText(), (String) cbTipoCreacion.getSelectedItem());

    base.setTipo((String) cbTipoCreacion.getSelectedItem()); // << Guardamos tipo

    CreadorLibro creador = obtenerCreadorSeleccionado();
    Libro finalLibro = creador.crear(base);

    int id = libroDAO.crear(finalLibro);
    if (id > 0) {
        JOptionPane.showMessageDialog(this, "Libro agregado. ID = " + id);
        cargarTabla();
        limpiarFormulario();
    } else {
        JOptionPane.showMessageDialog(this, "Error al agregar libro.");
    }
    }



    private void actualizar() {
    if (!validarCampos(true)) return;

    int id = Integer.parseInt(txtId.getText());
    Autor autor = (Autor) cbAutor.getSelectedItem();
    Genero genero = (Genero) cbGenero.getSelectedItem();
    Editorial editorial = (Editorial) cbEditorial.getSelectedItem();
    double precio = Double.parseDouble(txtPrecio.getText());

    Libro libro = new Libro(id, txtTitulo.getText(), autor, genero, editorial, precio, txtIsbn.getText(), (String) cbTipoCreacion.getSelectedItem());

    libro.setTipo((String) cbTipoCreacion.getSelectedItem()); // << nuevo

    boolean ok = libroDAO.actualizar(libro);
    if (ok) {
        JOptionPane.showMessageDialog(this, "Libro actualizado.");
        cargarTabla();
        limpiarFormulario();
    } else {
        JOptionPane.showMessageDialog(this, "Error al actualizar.");
    }
}


    private void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro (o ingrese ID) para eliminar.");
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar libro ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = libroDAO.eliminar(id);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Libro eliminado.");
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar.");
        }
    }

    private void buscarPorId() {
        String s = JOptionPane.showInputDialog(this, "Ingrese ID de libro a buscar:");
        if (s == null || s.trim().isEmpty()) return;
        try {
            int id = Integer.parseInt(s.trim());
            Libro l = libroDAO.obtenerPorId(id);
            if (l == null) {
                JOptionPane.showMessageDialog(this, "No encontrado.");
                return;
            }
            txtId.setText(String.valueOf(l.getId()));
            txtTitulo.setText(l.getTitulo());
            txtPrecio.setText(String.valueOf(l.getPrecio()));
            txtIsbn.setText(l.getIsbn());
            // seleccionar en combos
            seleccionarComboAutor(l.getAutor());
            seleccionarComboGenero(l.getGenero());
            seleccionarComboEditorial(l.getEditorial());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtTitulo.setText("");
        txtPrecio.setText("");
        txtIsbn.setText("");
        if (cbAutor.getItemCount() > 0) cbAutor.setSelectedIndex(0);
        if (cbGenero.getItemCount() > 0) cbGenero.setSelectedIndex(0);
        if (cbEditorial.getItemCount() > 0) cbEditorial.setSelectedIndex(0);
        cbTipoCreacion.setSelectedIndex(0);
    }

    private void cargarDesdeTabla() {
    int fila = tabla.getSelectedRow();
    if (fila == -1) return;

    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
    txtTitulo.setText(modeloTabla.getValueAt(fila, 1).toString());
    String autorNombre = modeloTabla.getValueAt(fila, 2).toString();
    String generoNombre = modeloTabla.getValueAt(fila, 3).toString();
    String editorialNombre = modeloTabla.getValueAt(fila, 4).toString();
    txtPrecio.setText(modeloTabla.getValueAt(fila, 5).toString());
    txtIsbn.setText(modeloTabla.getValueAt(fila, 6).toString());

    String tipo = modeloTabla.getValueAt(fila, 7).toString(); // << nuevo
    cbTipoCreacion.setSelectedItem(tipo);

    seleccionarComboPorNombre(cbAutor, autorNombre);
    seleccionarComboPorNombre(cbGenero, generoNombre);
    seleccionarComboPorNombre(cbEditorial, editorialNombre);
    }


    private void seleccionarComboPorNombre(JComboBox combo, String nombre) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item != null && item.toString().equals(nombre)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarComboAutor(Autor a) {
        if (a == null) return;
        for (int i = 0; i < cbAutor.getItemCount(); i++) {
            Autor it = cbAutor.getItemAt(i);
            if (it != null && it.getId() == a.getId()) {
                cbAutor.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarComboGenero(Genero g) {
        if (g == null) return;
        for (int i = 0; i < cbGenero.getItemCount(); i++) {
            Genero it = cbGenero.getItemAt(i);
            if (it != null && it.getId() == g.getId()) {
                cbGenero.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarComboEditorial(Editorial ed) {
        if (ed == null) return;
        for (int i = 0; i < cbEditorial.getItemCount(); i++) {
            Editorial it = cbEditorial.getItemAt(i);
            if (it != null && it.getId() == ed.getId()) {
                cbEditorial.setSelectedIndex(i);
                return;
            }
        }
    }

    private CreadorLibro obtenerCreadorSeleccionado() {
        String tipo = (String) cbTipoCreacion.getSelectedItem();
        switch (tipo) {
            case "Oferta": return new CreadorLibroOferta(); // 20% por defecto
            case "Premium": return new CreadorLibroPremium(); // +50% por defecto
            default: return new CreadorLibroNormal();
        }
    }

    private boolean validarCampos(boolean requiereId) {
        if (requiereId && txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID requerido.");
            return false;
        }
        if (txtTitulo.getText().trim().isEmpty() ||
                txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completar título y precio.");
            return false;
        }
        try {
            Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
            return false;
        }
        if (cbAutor.getSelectedItem() == null || cbGenero.getSelectedItem() == null || cbEditorial.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione autor, género y editorial.");
            return false;
        }
        return true;
    }
}
