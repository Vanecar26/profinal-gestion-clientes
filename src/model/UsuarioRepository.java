package model;

public class UsuarioRepository {

    private static final int MAX_USERS = 50;
    private final Usuario[] datos = new Usuario[MAX_USERS];
    private int usados = 0;


    public int size() { 
        return usados; 
    }


    public Usuario[] listarTodos() {
    Usuario[] copia = new Usuario[usados];
    for (int i = 0; i < usados; i++) copia[i] = datos[i];
    return copia;
    }


    public void agregar(Usuario u) {
        if (usados >= MAX_USERS) 
            throw new CapacidadExcedidaException("Capacidad de usuarios llena");
        if (buscarPorId(u.getId()) != null)
            throw new DuplicadoException("ID ya existe: " + u.getId());
        if (buscarPorUsername(u.getUsername()) != null)
            throw new DuplicadoException("Username ya existe: " + u.getUsername());
        datos[usados++] = u;
    }


    public Usuario buscarPorId(String id) {
        for (int i = 0; i < usados; i++) 
            if (datos[i].getId().equals(id)) 
                return datos[i];
    return null;
    }


    public Usuario buscarPorUsername(String username) {
        for (int i = 0; i < usados; i++) 
            if (datos[i].getUsername().equals(username)) 
                return datos[i];
    return null;
    }


    public boolean eliminarPorId(String id) {
        for (int i = 0; i < usados; i++) {
            if (datos[i].getId().equals(id)) {
                for (int j = i; j < usados - 1; j++) datos[j] = datos[j + 1];
                    datos[--usados] = null;
                return true;
        }
        }
        return false;
    }


    // Búsquedas avanzadas
    public Usuario[] buscarPorNombreParcial(String parcial) {
    Usuario[] temp = new Usuario[usados];
    int k = 0;
    String p = parcial.toLowerCase();
        for (int i = 0; i < usados; i++) {
            if (datos[i].getNombreCompleto().toLowerCase().contains(p)) temp[k++] = datos[i];
        }
    return recortar(temp, k);
    }


    public Usuario[] buscarPorRol(Rol rol) {
    Usuario[] temp = new Usuario[usados];
    }
}
