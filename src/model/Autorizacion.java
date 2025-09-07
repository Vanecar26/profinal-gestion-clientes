package model;

public class Autorizacion {
    
    public static void requiereAdmin(Usuario actor) {
        if (actor == null || actor.getRol() != Rol.ADMINISTRADOR)
            throw new PermisoDenegadoException("Se requiere rol ADMINISTRADOR");
    }

    public static void requiereMismoUsuario(Usuario actor, Usuario objetivo) {
        if (actor == null || objetivo == null || !actor.getId().equals(objetivo.getId()))
            throw new PermisoDenegadoException("Solo puede operar sobre su propio perfil");
    }
}
