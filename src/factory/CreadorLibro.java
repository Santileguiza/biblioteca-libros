package factory;

import modelo.Libro;

public abstract class CreadorLibro {

    
      //Recibe un libro base ya creado por la UI,
      //aplica modificaciones según el tipo (precio, atributos extra)
      //y DEBE asignar base.setTipo("NORMAL"/"OFERTA"/"PREMIUM")
     
    public abstract Libro crear(Libro base);
}
