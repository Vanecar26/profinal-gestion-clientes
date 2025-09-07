package model;

import java.util.Objects;

public class Usuario {
    private final String id; 
    private String nombreCompleto;
    private final String username; 
    private String password; 
    private Rol rol;                


    // Historial de acciones (composición)
    private static final int MAX_ACCIONES = 100;
    private final Accion[] historial = new Accion[MAX_ACCIONES];
    private int accionesUsadas = 0;


    // Meta: control de acceso
    private boolean bloqueado = false;
    private int intentosFallidos = 0;


    // Auditoría de creación
    private final long creadoEn;


    public Usuario(String id, String nombreCompleto, String username, String password, Rol rol) {
    this.id = Objects.requireNonNull(id);
    this.nombreCompleto = Objects.requireNonNull(nombreCompleto);
    this.username = Objects.requireNonNull(username);
    this.password = Objects.requireNonNull(password);
    this.rol = Objects.requireNonNull(rol);
    this.creadoEn = System.currentTimeMillis();
}


    public String getId() {
        return id;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Rol getRol() {
        return rol;
    }
    public long getCreadoEn() {
        return creadoEn;
    }
    public boolean isBloqueado() { 
        return bloqueado; }
    public int getIntentosFallidos() {
        return intentosFallidos;
    }

        // Mutadores controlados
    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = Objects.requireNonNull(nombreCompleto); }

    public void setRol(Rol nuevoRol) {
        this.rol = Objects.requireNonNull(nuevoRol); }

    public void setBloqueado(boolean bloqueado) {
    this.bloqueado = bloqueado;
    if (!bloqueado) this.intentosFallidos = 0;
    }


    public boolean verificarPassword(String intento) { 
        return Objects.equals(getPassword(), intento); }


    /** Cambia la contraseña si coincide la contraseña actual */
    public boolean cambiarPassword(String actual, String nueva) {
    if (verificarPassword(actual)) {
    this.password = Objects.requireNonNull(nueva);
    return true;
    }
    return false;
    }


    /** Registrar una acción en el historial del propio usuario */
    public void registrarAccion(String descripcion) {

        if (accionesUsadas >= MAX_ACCIONES) return; // simple: ignorar si se llena
        historial[accionesUsadas++] = new Accion(descripcion, System.currentTimeMillis());
    }


    public Accion[] getHistorial() {
        Accion[] copia = new Accion[accionesUsadas];
            for (int i = 0; i < accionesUsadas; i++) copia[i] = historial[i];
        return copia;
    }


    // Intentos fallidos (para bloqueo)
    public void registrarIntentoFallido() {
        intentosFallidos++;
            registrarAccion("Intento de inicio de sesión fallido (#" + intentosFallidos + ")");
            if (intentosFallidos >= 3) {
                bloqueado = true;
                    registrarAccion("Cuenta bloqueada por múltiples intentos fallidos");
            }
    }


    public void registrarInicioExitoso() {
        intentosFallidos = 0;
            registrarAccion("Inició sesión");
    }

}
