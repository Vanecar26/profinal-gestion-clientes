package model;

public class AuditoriaService {

    private final UsuarioRepository repo;


    public AuditoriaService(UsuarioRepository repo) { 
        this.repo = repo; 
    }


    public Accion[] verHistorial(Usuario actor, String idUsuario) {

        Usuario u = repo.buscarPorId(idUsuario);
            if (u == null) throw new NoEncontradoException("No existe usuario con id: " + idUsuario);
            if (actor.getRol() != Rol.ADMINISTRADOR && !actor.getId().equals(u.getId()))
                throw new PermisoDenegadoException("No puede ver el historial de otros usuarios");
            return u.getHistorial();
    }


        // ...existing code...
        public String auditoriaGlobal(Usuario actor) {
            Autorizacion.requiereAdmin(actor);
            StringBuilder sb = new StringBuilder();
            sb.append("==== AUDITORÍA GLOBAL ====\n");
        
            for (Usuario u : repo.listarTodos()) {
                sb.append("Usuario: ").append(u.getUsername()).append(" (ID ").append(u.getId()).append(")\n");
                for (Accion a : u.getHistorial()) sb.append("  ").append(a.toString()).append("\n");
                sb.append("\n");
            }
            return sb.toString();
        }
    
        public String exportarHistorial(Usuario actor, String idUsuario) {
            Accion[] h = verHistorial(actor, idUsuario);
            StringBuilder sb = new StringBuilder();
            sb.append("Historial de acciones (ID ").append(idUsuario).append(")\n");
            for (Accion a : h) sb.append(a.toString()).append("\n");
            return sb.toString();
        }
    // ...existing code...
}
