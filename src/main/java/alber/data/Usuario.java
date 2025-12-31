package alber.data;
/*ahora mismo no necesitariamos estrictamente una clase Usuario, pero si quisieramos ampliar el programa y darle más complejidad
* de cara al futuro es más fácil refactorizar usando ya la clase en vez de un string*/
public class Usuario {
    private String nombre;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
//no voy a usar un toString porque si necesito coger el nombre uso getNombre y estamos
}
