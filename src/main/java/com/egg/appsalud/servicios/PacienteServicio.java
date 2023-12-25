package com.egg.appsalud.servicios;

import com.egg.appsalud.Enumeracion.Rol;
import com.egg.appsalud.Exception.MiException;
import com.egg.appsalud.entidades.FichaMedica;
import com.egg.appsalud.entidades.Imagen;
import com.egg.appsalud.entidades.Paciente;
import com.egg.appsalud.entidades.Usuario;
import com.egg.appsalud.repositorios.FichaMedicaRepositorio;
import com.egg.appsalud.repositorios.PacienteRepositorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PacienteServicio {
    @Autowired
    PacienteRepositorio pacienteRepositorio;

    @Autowired
    ImagenServicio imagenServicio;

    @Autowired
    UtilServicio utilServicio;

    @Autowired
    FichaMedicaRepositorio fichaMedicaRepositorio;

    @Transactional
    public void crearPaciente(MultipartFile archivo, String nombreUsuario, String nombre, String apellido,
                              Long DNI, Date fechaDeNacimiento, String email, String password, String password2) throws MiException {

        utilServicio.validar(nombreUsuario, password, password2, nombre, apellido, fechaDeNacimiento, DNI, email);

        Paciente paciente = new Paciente();

        paciente.setNombreUsuario(nombreUsuario);
        paciente.setPassword(new BCryptPasswordEncoder().encode(password));
        paciente.setDNI(DNI);
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setFechaDeNacimiento(fechaDeNacimiento);
        paciente.setEmail(email);
        paciente.setFechaDeAlta(new Date());
        paciente.setRol(Rol.PACIENTE);
        paciente.setActivo(true);

        Imagen imagen = imagenServicio.guardar(archivo);

        paciente.setImagen(imagen);

        pacienteRepositorio.save(paciente);

    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPacientes() {

        return pacienteRepositorio.findAll();
    }

@Transactional
public void modificarPacientes(MultipartFile archivo, String id, String nombreUsuario, String nombre, String apellido,
                               Long DNI, Date fechaNacimiento, String email, String password, String password2) throws MiException {

    try {
        utilServicio.validar(nombreUsuario, password, password2, nombre, apellido, fechaNacimiento, DNI, email);

        Optional<Paciente> respuesta = pacienteRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Paciente paciente = respuesta.get();

            paciente.setNombre(nombre);
            paciente.setApellido(apellido);
            new BCryptPasswordEncoder().encode(password);
            paciente.setEmail(email);
            paciente.setFechaDeNacimiento(fechaNacimiento);
            paciente.setNombreUsuario(nombreUsuario);

            Imagen imagen = imagenServicio.guardar(archivo);

            paciente.setImagen(imagen);

            pacienteRepositorio.save(paciente);
        } else {
            throw new MiException("El paciente con ID: " + id + " no existe");
        }
    } catch (Exception ex) {
        // Manejo de la excepción general
        throw new MiException("Error al modificar el paciente: " + ex.getMessage());
    }
}


    @Transactional(readOnly = true)
    public Paciente getOne(String id) {
        return pacienteRepositorio.getOne(id);
    }


    @Transactional
    public void eliminar(String id) throws MiException {

        Paciente paciente = pacienteRepositorio.getById(id);

        pacienteRepositorio.delete(paciente);

    }

}

