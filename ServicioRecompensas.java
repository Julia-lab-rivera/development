package back.service;

import back.model.Estudiante;
import back.model.Recompensa;


public class ServicioRecompensas {

    /**
     * Intenta canjear una recompensa con los puntos del estudiante.
     *
     * @return true si el canje fue exitoso, false si no tenía puntos suficientes.
     */
    public boolean canjear(Estudiante estudiante, Recompensa recompensa) {
        if (estudiante == null || recompensa == null) {
            throw new IllegalArgumentException("Estudiante y recompensa no pueden ser nulos");
        }

        if (estudiante.getPuntosTotales() < recompensa.getCostoPuntos()) {
            return false;
        }

        estudiante.descontarPuntos(
                recompensa.getCostoPuntos(),
                "Canje de recompensa: " + recompensa.getNombre()
        );
        ServicioEstudiantes.guardarCambios();
        return true;
    }
}
