package com.egg.appsalud.servicios;

import com.egg.appsalud.Exception.MiException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UtilServicio {

    public void validar(String nombreUsuario, String password, String password2, String nombre, String
            apellido, Date fechaDeNacimiento, Long DNI, String email) throws MiException {

        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new MiException("El nombre de usuario no puede estar vacío o ser nulo");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede estar vacío o ser nulo");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new MiException("El apellido no puede estar vacío o ser nulo");
        }

        if (DNI == null) {
            throw new MiException("El DNI no puede ser nulo");
        }

        if (fechaDeNacimiento == null) {
            throw new MiException("La fecha de nacimiento no puede ser nula");
        }

        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede estar vacío o ser nulo");
        }

        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiException("Las contraseñas no pueden estar vacías y deben tener más de 5 caracteres");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas deben coincidir");
        }
    }
}


