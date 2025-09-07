package model;

public class Accion {
    
    private final String descripcion;
    private final long timestamp; 

    public Accion(String descripcion, long timestamp) {
    this.descripcion = descripcion;
    this.timestamp = timestamp;
    }


    public String getDescripcion() {
        return descripcion;
    }
    public long getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
    return "[" + timestamp + "] " + descripcion;
}
}
