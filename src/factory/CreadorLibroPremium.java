package factory;

import modelo.Libro;

public class CreadorLibroPremium extends CreadorLibro {

    private double recargo = 0.50; // +50%

    public CreadorLibroPremium() {}

    public CreadorLibroPremium(double recargo) {
        this.recargo = recargo;
    }

    @Override
    public Libro crear(Libro base) {
        base.setTipo("PREMIUM");
        base.setPrecio(roundTwoDecimals(base.getPrecio() * (1.0 + recargo)));
        return base;
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}


