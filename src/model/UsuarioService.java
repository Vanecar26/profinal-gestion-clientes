package model;

public class UsuarioService {
    private final UsuarioRepository repo;


    public UsuarioService(UsuarioRepository repo) { this.repo = repo; }


    public void crearUsuario(Usuario actor, String id, String nombreCompleto, String username, String password, Rol rol) {
        Autorizacion.requiereAdmin(actor);
        Usuario nuevo = new Usuario(id, nombreCompleto, username, password, rol);
            repo.agregar(nuevo);
            actor.registrarAccion("Creó usuario '" + username + "' (" + id + ") con rol " + rol);
    }


    public void eliminarUsuario(Usuario actor, String id) {

        Autorizacion.requiereAdmin(actor);
        Usuario u = repo.buscarPorId(id);

            if (u == null) throw new NoEncontradoException("No existe usuario con id: " + id);
            if (!repo.eliminarPorId(id)) throw new RuntimeException("Fallo al eliminar");
                actor.registrarAccion("Eliminó usuario '" + u.getUsername() + "' (" + id + ")");
    }


    public void actualizarNombre(Usuario actor, String idObjetivo, String nuevoNombre) {

        Usuario objetivo = repo.buscarPorId(idObjetivo);

            if (objetivo == null) throw new NoEncontradoException("No existe usuario con id: " + idObjetivo);
            if (actor.getRol() == Rol.ADMINISTRADOR || actor.getId().equals(objetivo.getId())) {
            objetivo.setNombreCompleto(nuevoNombre);
            actor.registrarAccion("Actualizó nombre de usuario '" + objetivo.getUsername() + "'");
            } else {
            throw new PermisoDenegadoException("No puede actualizar a otros usuarios");
            }
    }


    public void cambiarPassword(Usuario actor, String idObjetivo, String actual, String nueva) {

        Usuario objetivo = repo.buscarPorId(idObjetivo);
            if (objetivo == null) throw new NoEncontradoException("No existe usuario con id: " + idObjetivo);
        boolean esAdmin = actor.getRol() == Rol.ADMINISTRADOR;
        boolean esPropio = actor.getId().equals(objetivo.getId());
            if (!esAdmin && !esPropio)
            throw new PermisoDenegadoException("No puede cambiar la contraseña de otros usuarios");
        boolean ok = objetivo.cambiarPassword(actual, nueva);
            if (!ok) throw new PermisoDenegadoException("Contraseña actual incorrecta");
                actor.registrarAccion("Cambió contraseña de '" + objetivo.getUsername() + "'");
    }


    public void cambiarRol(Usuario actor, String idObjetivo, Rol nuevoRol) {

        Autorizacion.requiereAdmin(actor);
        Usuario objetivo = repo.buscarPorId(idObjetivo);
            if (objetivo == null) throw new NoEncontradoException("No existe usuario con id: " + idObjetivo);
                objetivo.setRol(nuevoRol);
                actor.registrarAccion("Cambió rol de '" + objetivo.getUsername() + "' a " + nuevoRol);
    }


    public void desbloquear(Usuario actor, String idObjetivo) {

        Autorizacion.requiereAdmin(actor);
        Usuario objetivo = repo.buscarPorId(idObjetivo);
            if (objetivo == null) throw new NoEncontradoException("No existe usuario con id: " + idObjetivo);
                objetivo.setBloqueado(false);
                actor.registrarAccion("Desbloqueó la cuenta de '" + objetivo.getUsername() + "'");
    }

    // Búsqueda por nombre parcial (requiere permisos: admin puede ver todos)
    public Usuario[] buscarPorNombreParcial(Usuario actor, String parcial) {
        Autorizacion.requiereAdmin(actor);
        return repo.buscarPorNombreParcial(parcial);
    }
}
    
