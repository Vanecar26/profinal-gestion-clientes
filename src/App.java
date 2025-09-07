import model.PermisoDenegadoException;
import model.Usuario;// ...existing code...
import model.UsuarioRepository;
import model.UsuarioService;
import model.AuthService;
import model.AuditoriaService;
import model.Rol;
// ...existing code...

public class App {
    public static void main(String[] args) {

        UsuarioRepository repo = new UsuarioRepository();
        UsuarioService usuarios = new UsuarioService(repo);
        AuthService auth = new AuthService(repo);
        AuditoriaService auditoria = new AuditoriaService(repo);


        Usuario admin = new Usuario("U-001", "Admin Principal", "admin", "admin123", Rol.ADMINISTRADOR);
            repo.agregar(admin);
            admin.registrarAccion("Usuario inicial creado (bootstrap)");


        Usuario alicia = new Usuario("U-100", "Alicia González", "alicia", "alicia123", Rol.ESTANDAR);
            repo.agregar(alicia);
            alicia.registrarAccion("Usuario inicial creado (bootstrap)");


        // Login admin
        Usuario logged = auth.login("admin", "admin123");
        System.out.println("Login admin: " + (logged != null));


        // Prueba de bloqueo en Alicia
        auth.login("alicia", "x");
        auth.login("alicia", "y");
        auth.login("alicia", "z");
        System.out.println("Alicia bloqueada: " + alicia.isBloqueado());


        usuarios.desbloquear(admin, "U-100");
        System.out.println("Alicia bloqueada? " + alicia.isBloqueado());
        System.out.println("Login Alicia: " + (auth.login("alicia", "alicia123") != null));


        usuarios.crearUsuario(admin, "U-200", "Juan Pérez", "juan", "juan123", Rol.ESTANDAR);
        usuarios.actualizarNombre(admin, "U-200", "Juan P. Pérez");


        try { usuarios.eliminarUsuario(alicia, "U-200"); }
        catch (PermisoDenegadoException e) { System.out.println("(OK) Alicia no puede eliminar: " + e.getMessage()); }


        usuarios.actualizarNombre(alicia, "U-100", "Alicia G. Actualizada");
        usuarios.cambiarPassword(alicia, "U-100", "alicia123", "claveNueva!");
        usuarios.cambiarRol(admin, "U-200", Rol.SUPERVISOR);


        System.out.println("— Búsqueda por 'Juan' —");
        for (Usuario u : usuarios.buscarPorNombreParcial(admin, "Juan"))
        System.out.println("Encontrado: " + u.getUsername());


        System.out.println("— Auditoría Global —" + auditoria.auditoriaGlobal(admin));


        System.out.println("— Historial Alicia —" + auditoria.exportarHistorial(alicia, "U-100"));


        usuarios.eliminarUsuario(admin, "U-200");
        System.out.println("*** DEMO COMPLETADA ***");
    }
}
