package back.validation;

public class EmailValidator {

    public static boolean esCorreoInstitucional(String correo) {

        if (correo == null || correo.isBlank()) {
            return false;
        }

        return correo.matches(
                "^[A-Za-z0-9._%+-]+@usa\\.edu\\.co$"
        );
    }
}