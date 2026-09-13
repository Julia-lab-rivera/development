package front.register;

import back.service.AuthService;
import front.login.LoginView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView {

    public void start(Stage stage) {

        Label titulo = new Label("CREAR CUENTA");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label(
                "Regístrate con tu correo institucional"
        );

        subtitulo.getStyleClass().add("subtitulo");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre completo");

        TextField correo = new TextField();
        correo.setPromptText("usuario@usa.edu.co");

        PasswordField contraseña = new PasswordField();
        contraseña.setPromptText("Contraseña");

        PasswordField confirmarContraseña = new PasswordField();
        confirmarContraseña.setPromptText("Confirmar contraseña");

        Button registrar = new Button("CREAR CUENTA");
        registrar.getStyleClass().add("boton-principal");

        Label mensaje = new Label();
        mensaje.getStyleClass().add("mensaje");

        Hyperlink volver = new Hyperlink(
                "¿Ya tienes una cuenta? Inicia sesión"
        );

        VBox formulario = new VBox(
                15,
                titulo,
                subtitulo,
                nombre,
                correo,
                contraseña,
                confirmarContraseña,
                registrar,
                mensaje,
                volver
        );

        formulario.setAlignment(Pos.CENTER);
        formulario.setPadding(new Insets(40));
        formulario.setMaxWidth(420);
        formulario.getStyleClass().add("tarjeta");

        StackPane root = new StackPane(formulario);
        root.getStyleClass().add("fondo");

        Scene scene = new Scene(root, 900, 600);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/front/styles/styles.css")
                        .toExternalForm()
        );

        registrar.setOnAction(event -> {

            String resultado = AuthService.registrarUsuario(
                    nombre.getText().trim(),
                    correo.getText().trim(),
                    contraseña.getText(),
                    confirmarContraseña.getText()
            );

            if (resultado.equals("REGISTRO_EXITOSO")) {

                mensaje.setText(
                        "Cuenta creada correctamente."
                );

                mensaje.getStyleClass().removeAll("error");
                mensaje.getStyleClass().add("exito");

                nombre.clear();
                correo.clear();
                contraseña.clear();
                confirmarContraseña.clear();

            } else {

                mensaje.setText(resultado);

                mensaje.getStyleClass().removeAll("exito");
                mensaje.getStyleClass().add("error");
            }
        });

        volver.setOnAction(event -> {

            LoginView loginView = new LoginView();

            try {
                loginView.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        stage.setTitle("Crear cuenta - Objetos Perdidos");
        stage.setScene(scene);
        stage.show();
    }
}