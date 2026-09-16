package back.service;

import back.model.Estudiante;
import back.model.EstadoObjeto;
import back.model.Objeto;


public class ServicioPuntos {

    // Puntos otorgados por accion. Se dejan como constantes publicas
    // para que otros modulos puedan mostrarlos en pantalla si lo desean.
    public static final int PUNTOS_POR_VER_OBJETO = 1;
    public static final int PUNTOS_POR_REGISTRAR_OBJETO = 5;
    public static final int PUNTOS_BASE_POR_DEVOLUCION = 20;

    /**
     * Otorga puntos por ver un objeto. Solo se otorgan la primera vez
     * que un estudiante ve un objeto en particular, para evitar que
     * se generen puntos de forma indefinida recargando la misma vista.
     */
    public void verObjeto(Estudiante estudiante, Objeto objeto) {
        validarParametros(estudiante, objeto);
        boolean esPrimeraVez = objeto.registrarVisualizacion(estudiante.getCorreo());
        if (esPrimeraVez) {
            estudiante.otorgarPuntos(
                    PUNTOS_POR_VER_OBJETO,
                    "Visualizacion del objeto '" + objeto.getNombre() + "'"
            );
            ServicioEstudiantes.guardarCambios();
        }
    }

    /**
     * Otorga puntos por registrar un nuevo objeto (perdido o encontrado)
     * en el sistema.
     */
    public void registrarObjeto(Estudiante estudiante, Objeto objeto) {
        validarParametros(estudiante, objeto);
        objeto.setCorreoEstudianteRegistra(estudiante.getCorreo());
        if (objeto.getEstado() == null) {
            objeto.setEstado(EstadoObjeto.REGISTRADO);
        }
        estudiante.otorgarPuntos(
                PUNTOS_POR_REGISTRAR_OBJETO,
                "Registro del objeto '" + objeto.getNombre() + "'"
        );
        ServicioEstudiantes.guardarCambios();
    }

    /**
     * Otorga puntos por devolver un objeto perdido a su dueno.
     * Los puntos totales = puntos base de devolucion + bonificacion
     * segun el valor del objeto (a mayor valor, mayor bonificacion).
     *
     * @throws IllegalStateException si el objeto ya fue devuelto,
     *         o si no esta en un estado valido para ser devuelto.
     */
    public void devolverObjetoPerdido(Estudiante estudiante, Objeto objeto) {
        validarParametros(estudiante, objeto);

        if (objeto.getEstado() == EstadoObjeto.DEVUELTO) {
            throw new IllegalStateException("El objeto ya fue devuelto previamente");
        }
        if (objeto.getEstado() != EstadoObjeto.PERDIDO
                && objeto.getEstado() != EstadoObjeto.ENCONTRADO) {
            throw new IllegalStateException(
                    "El objeto debe estar en estado PERDIDO o ENCONTRADO para poder devolverse");
        }

        int puntosTotales = PUNTOS_BASE_POR_DEVOLUCION + objeto.getValor().getBonificacionDevolucion();

        objeto.setEstado(EstadoObjeto.DEVUELTO);
        objeto.setCorreoEstudianteDevuelve(estudiante.getCorreo());

        estudiante.otorgarPuntos(
                puntosTotales,
                "Devolucion del objeto '" + objeto.getNombre() + "' (valor " + objeto.getValor() + ")"
        );
        ServicioEstudiantes.guardarCambios();
    }

    private void validarParametros(Estudiante estudiante, Objeto objeto) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto no puede ser nulo");
        }
    }
}
