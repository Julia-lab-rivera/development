package back.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import back.model.Usuario;
import back.validation.EmailValidator;

public class AuthService {

    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final Path ARCHIVO_USUARIOS = Paths.get("data", "usuarios.txt");

    static {
        cargarUsuarios();
    }

    private static void cargarUsuarios() {
        if (!Files.exists(ARCHIVO_USUARIOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_USUARIOS)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }

                String[] partes = linea.split("\\|", -1);
                if (partes.length == 3) {
                    usuarios.add(new Usuario(partes[0], partes[1], partes[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudieron cargar los usuarios guardados: " + e.getMessage());
        }
    }

    private static void guardarUsuarios() {
        try {
            Path carpeta = ARCHIVO_USUARIOS.getParent();
            if (carpeta != null && !Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }

            try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_USUARIOS)) {
                for (Usuario usuario : usuarios) {
                    escritor.write(
                            usuario.getNombre() + "|" +
                            usuario.getCorreo() + "|" +
                            usuario.getContraseña()
                    );
                    escritor.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudieron guardar los usuarios: " + e.getMessage());
        }
    }

    public static String registrarUsuario(
            String nombre,
            String correo,
            String contraseña,
            String confirmarContraseña) {

        if (nombre == null || nombre.isBlank()) {
            return "El nombre es obligatorio.";
        }

        if (!EmailValidator.esCorreoInstitucional(correo)) {
            return "Debes utilizar un correo @usa.edu.co.";
        }

        if (contraseña == null || contraseña.length() < 6) {
            return "La contraseña debe tener mínimo 6 caracteres.";
        }

        if (!contraseña.equals(confirmarContraseña)) {
            return "Las contraseñas no coinciden.";
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo)) {
                return "Este correo ya está registrado.";
            }
        }

        Usuario nuevoUsuario =
                new Usuario(nombre, correo, contraseña);

        usuarios.add(nuevoUsuario);
        guardarUsuarios();

        return "REGISTRO_EXITOSO";
    }

    public static Usuario iniciarSesion(
            String correo,
            String contraseña) {

        for (Usuario usuario : usuarios) {

            if (usuario.getCorreo().equalsIgnoreCase(correo)
                    && usuario.getContraseña().equals(contraseña)) {

                return usuario;
            }
        }

        return null;
    }
}