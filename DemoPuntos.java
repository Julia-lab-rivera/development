package back.puntos;

import back.puntos.modelo.*;
import back.puntos.servicio.ServicioPuntos;
import back.puntos.servicio.ServicioRecompensas;

/**
 * Demo del módulo de puntos.
 *
 * OJO: esta clase NO es el Main de la aplicación JavaFX (ese vive en
 * el paquete por defecto, como indica el pom.xml: <mainClass>Main</mainClass>).
 * Es solo un punto de entrada de prueba para verificar que la lógica de
 * puntos funciona, y sirve de ejemplo de cómo otro módulo (por ejemplo
 * un controlador de JavaFX) debería usar ServicioPuntos y ServicioRecompensas.
 *
 * Ejecutar con: java -cp out back.puntos.DemoPuntos
 */
public class DemoPuntos {

    public static void main(String[] args) {
        ServicioPuntos servicioPuntos = new ServicioPuntos();
        ServicioRecompensas servicioRecompensas = new ServicioRecompensas();

        // 1. Un estudiante de la Sergio
        Estudiante juan = new Estudiante("EST001", "Juan Pérez", "juan.perez@usergioarboleda.edu.co");

        // 2. Un objeto de alto valor, perdido en el campus
        Objeto billetera = new Objeto(
                "OBJ001",
                "Billetera de cuero",
                "Billetera café con documentos y una tarjeta débito adentro",
                CategoriaObjeto.ACCESORIOS,
                ValorObjeto.ALTO
        );

        System.out.println("== Estado inicial ==");
        System.out.println(juan);

        // 3. Juan ve el objeto en la lista de perdidos -> gana 1 punto
        servicioPuntos.verObjeto(juan, billetera);
        System.out.println("\nDespués de ver el objeto:");
        System.out.println(juan);

        // 4. Ver el mismo objeto otra vez NO debe volver a dar puntos
        servicioPuntos.verObjeto(juan, billetera);
        System.out.println("\nDespués de ver el mismo objeto otra vez (no debe subir):");
        System.out.println(juan);

        // 5. Juan registra un objeto que encontró -> gana 5 puntos
        Objeto llaves = new Objeto(
                "OBJ002",
                "Llavero con 3 llaves",
                "Encontrado en la cafetería",
                CategoriaObjeto.LLAVES,
                ValorObjeto.BAJO
        );
        servicioPuntos.registrarObjeto(juan, llaves);
        System.out.println("\nDespués de registrar un objeto encontrado:");
        System.out.println(juan);

        // 6. Juan devuelve la billetera (objeto perdido de gran valor)
        //    -> gana puntos base + bonificación por valor ALTO
        servicioPuntos.devolverObjetoPerdido(juan, billetera);
        System.out.println("\nDespués de devolver la billetera (valor ALTO):");
        System.out.println(juan);
        System.out.println(billetera);

        // 7. Historial completo de puntos
        System.out.println("\n== Historial de puntos de " + juan.getNombre() + " ==");
        juan.getHistorialPuntos().forEach(System.out::println);

        // 8. Canje de una recompensa con los puntos acumulados
        Recompensa vale = new Recompensa("REC001", "Vale de $10.000 en la cafetería", 50);
        boolean canjeExitoso = servicioRecompensas.canjear(juan, vale);

        System.out.println("\n== Canje de recompensa ==");
        System.out.println("¿Canje exitoso? " + canjeExitoso);
        System.out.println(juan);
    }
}
