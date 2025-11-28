package modelo;

public class Libro {
    private int id;
    private String titulo;
    private Autor autor;
    private Genero genero;
    private Editorial editorial;
    private double precio;
    private String isbn;
    private String tipo; 

    public Libro() { }

    // Constructor para SELECT
    public Libro(int id, String titulo, Autor autor, Genero genero,
                 Editorial editorial, double precio, String isbn, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.editorial = editorial;
        this.precio = precio;
        this.isbn = isbn;
        this.tipo = tipo;
    }

    // Constructor para INSERT
    public Libro(String titulo, Autor autor, Genero genero,
                 Editorial editorial, double precio, String isbn, String tipo) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.editorial = editorial;
        this.precio = precio;
        this.isbn = isbn;
        this.tipo = tipo;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public Editorial getEditorial() { return editorial; }
    public void setEditorial(Editorial editorial) { this.editorial = editorial; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return titulo + " (" + tipo + ")";
    }
}


