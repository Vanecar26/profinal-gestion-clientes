package model;

public class AuthService {

    private final UsuarioRepository repo;


    public AuthService(UsuarioRepository repo) { 
        this.repo = repo; 
    }


    /** Intento de login. Éxito: retorna Usuario; Falla: retorna null. */
    public Usuario login(String username, String password) {
    Usuario u = repo.buscarPorUsername(username);
        if (u == null) 
            return null; 
        if (u.isBloqueado()) 
            return null; 
        if (u.verificarPassword(password)) {
            u.registrarInicioExitoso();
            return u;
        } else {
            u.registrarIntentoFallido();
            return null;
        }
    }
}
