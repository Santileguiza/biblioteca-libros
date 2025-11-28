package factory;

import modelo.Libro;

public class CreadorLibroNormal extends CreadorLibro {

    @Override
    public Libro crear(Libro base) {
        base.setTipo("NORMAL");
        // sin cambios de precio
        return base;
    }
}
