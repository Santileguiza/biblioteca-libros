package modelo;

public class Autor {
    private int id;
    private String nombreCompleto;

    public Autor() { }

    public Autor(int id, String nombreCompleto) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
    }

    public Autor(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}
