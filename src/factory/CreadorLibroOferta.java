package factory;

import modelo.Libro;

public class CreadorLibroOferta extends CreadorLibro {

    private double descuento = 0.20; // 20%

    public CreadorLibroOferta() {}

    public CreadorLibroOferta(double descuento) {
        this.descuento = descuento;
    }

    @Override
    public Libro crear(Libro base) {
        base.setTipo("OFERTA");
        base.setPrecio(roundTwoDecimals(base.getPrecio() * (1.0 - descuento)));
        return base;
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
