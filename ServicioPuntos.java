package back.puntos.servicio;

import back.puntos.modelo.Estudiante;
import back.puntos.modelo.EstadoObjeto;
import back.puntos.modelo.Objeto;

/**
 * Implementa la historia de usuario:
 * "Como estudiante de la Sergio, quiero ganar puntos al ver o registrar
 *  objetos y recibir aún más puntos cuando devuelvo un objeto perdido
 *  de gran valor, para poder reclamar recompensas en el futuro."
 *
 * Esta clase es intencionalmente independiente de cualquier capa de
 * persistencia o de interfaz gráfica: solo recibe objetos del modelo
 * y aplica la lógica de negocio sobre ellos. Esto facilita integrarla
 * con otros módulos del repositorio (por ejemplo uno de UI, otro de
 * base de datos, otro de autenticación, etc.) sin acoplarse a ellos.
 */
public class ServicioPuntos {

    // Puntos otorgados por acción. Se dejan como constantes públicas
    // para que otros módulos puedan mostrarlos en pantalla si lo desean.
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
        boolean esPrimeraVez = objeto.registrarVisualizacion(estudiante.getId());
        if (esPrimeraVez) {
            estudiante.otorgarPuntos(
                    PUNTOS_POR_VER_OBJETO,
                    "Visualización del objeto '" + objeto.getNombre() + "'"
            );
        }
    }

    /**
     * Otorga puntos por registrar un nuevo objeto (perdido o encontrado)
     * en el sistema.
     */
    public void registrarObjeto(Estudiante estudiante, Objeto objeto) {
        validarParametros(estudiante, objeto);
        objeto.setEstudianteRegistraId(estudiante.getId());
        if (objeto.getEstado() == null) {
            objeto.setEstado(EstadoObjeto.REGISTRADO);
        }
        estudiante.otorgarPuntos(
                PUNTOS_POR_REGISTRAR_OBJETO,
                "Registro del objeto '" + objeto.getNombre() + "'"
        );
    }

    /**
     * Otorga puntos por devolver un objeto perdido a su dueño.
     * Los puntos totales = puntos base de devolución + bonificación
     * según el valor del objeto (a mayor valor, mayor bonificación).
     *
     * @throws IllegalStateException si el objeto ya fue devuelto,
     *         o si no está en un estado válido para ser devuelto.
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
        objeto.setEstudianteDevuelveId(estudiante.getId());

        estudiante.otorgarPuntos(
                puntosTotales,
                "Devolución del objeto '" + objeto.getNombre() + "' (valor " + objeto.getValor() + ")"
        );
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
