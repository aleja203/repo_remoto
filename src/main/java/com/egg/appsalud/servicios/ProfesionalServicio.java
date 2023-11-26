package com.egg.appsalud.servicios;

import com.egg.appsalud.Enumeracion.Especialidad;
import com.egg.appsalud.Enumeracion.Provincias;
import com.egg.appsalud.Enumeracion.Rol;
import com.egg.appsalud.Exception.MiException;
import com.egg.appsalud.entidades.Consulta;

import com.egg.appsalud.entidades.FichaMedica;
import com.egg.appsalud.entidades.Imagen;
import com.egg.appsalud.entidades.ObraSocial;
import com.egg.appsalud.entidades.Paciente;
import com.egg.appsalud.entidades.Profesional;
import com.egg.appsalud.entidades.Usuario;
import com.egg.appsalud.repositorios.ConsultaRepositorio;
import com.egg.appsalud.repositorios.ProfesionalRepositorio;
import com.egg.appsalud.repositorios.UsuarioRepositorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfesionalServicio implements UserDetailsService {

    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private ConsultaRepositorio consultaRepositorio;


    @Transactional
    public void crearProfesional(MultipartFile archivo, String nombreUsuario, String password, String password2, String nombre, String apellido,
                                 String email, Date fechaNacimiento, Long DNI, Especialidad especialidad, Provincias provincias, String localidad, String direccion,
                                 Long matricula, Set<String> horariosAtencion, int precioConsulta/*, List<ObraSocial> obraSocial*/) throws MiException {

        validar(nombreUsuario, password, password2, nombre, apellido, fechaNacimiento, DNI, email, matricula, especialidad, provincias, localidad, direccion, horariosAtencion, precioConsulta);

        //Usuario usuario = buscarUsuarioPorID(id);
        Profesional profesional = new Profesional();

        profesional.setNombreUsuario(nombreUsuario);
        profesional.setPassword(new BCryptPasswordEncoder().encode(password));
        profesional.setFechaDeAlta(new Date());

        profesional.setRol(Rol.PROFESIONAL);

        Imagen imagen = imagenServicio.guardar(archivo);

        profesional.setNombre(nombre);
        profesional.setApellido(apellido);
        profesional.setEmail(email);
        profesional.setFechaDeNacimiento(fechaNacimiento);
        profesional.setActivo(true);
        profesional.setDNI(DNI);
        profesional.setEspecialidad(especialidad);
        profesional.setProvincias(provincias);
        profesional.setLocalidad(localidad);
        profesional.setDireccion(direccion);
        profesional.setImagen(imagen);
        profesional.setMatricula(matricula);
        profesional.setHorariosAtencion(horariosAtencion);
        profesional.setPrecioConsulta(precioConsulta);
        //profesional.setObraSocial(obraSocial);


        profesionalRepositorio.save(profesional);
    }

    @Transactional
    public void modificarProfesional(String id, /*MultipartFile archivo, */ String nombreUsuario, String nombre, String apellido,
                                     Long DNI, Date fechaDeNacimiento, String email, String password, String password2,
                                     boolean activo, Especialidad especialidad, Provincias provincias, String localidad, String direccion,
                                     Long matricula, Set<String> horariosAtencion, int precioConsulta) throws MiException {

        validar(nombreUsuario, password, password2, nombre, apellido, fechaDeNacimiento, DNI, email);
        validar(nombreUsuario, password, password2, nombre, apellido, fechaDeNacimiento, DNI, email);


        validar(nombreUsuario, password, password2, nombre, apellido, fechaDeNacimiento, DNI, email, matricula, especialidad, provincias, localidad, direccion, horariosAtencion, precioConsulta);

        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            profesional.setNombre(nombre);
            profesional.setPassword(new BCryptPasswordEncoder().encode(password));
            profesional.setDNI(DNI);
            profesional.setApellido(apellido);
            profesional.setFechaDeNacimiento(fechaDeNacimiento);
            profesional.setEmail(email);
            profesional.setFechaDeAlta(new Date());
            profesional.setActivo(activo);
            profesional.setNombreUsuario(nombreUsuario);
            profesional.setMatricula(matricula);
            profesional.setEspecialidad(especialidad);

            profesional.setProvincias(provincias);
            profesional.setLocalidad(localidad);
            profesional.setDireccion(direccion);
            
            profesional.setHorariosAtencion(horariosAtencion);
            profesional.setPrecioConsulta(precioConsulta);
            
            /*String idImagen = null;
            
            if(profesional.getImagen() != null){
                idImagen = profesional.getImagen().getId();
            }
            
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);*/

            profesionalRepositorio.save(profesional);

        }

    }

    private void validar(String nombreUsuario, String password, String password2, String nombre, String apellido, Date fechaDeNacimiento, Long DNI,
                         String email, Long matricula, Especialidad especialidad, Provincias provincias, String localidad, String direccion, Set<String> horariosAtencion, int precioConsulta) throws MiException {


        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiException("El nombre de usuario no puede estar vacio o Nulo");

        }


        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacío o ser nulo");
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("El apellido no puede estar vacío o ser nulo");
        }

        if (DNI == null) {
            throw new MiException("El DNI no puede ser nulo");
        }

        if (fechaDeNacimiento == null) {
            throw new MiException("La fecha de nacimiento no puede ser nula");
        }

        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede estar vacío o ser nulo");
        }

        if (especialidad == null) {
            throw new MiException("La especialidad no puede ser nula");
        }

        if (provincias == null) {
            throw new MiException("La provincia no puede ser nula");
        }

        if (localidad == null) {
            throw new MiException("La Localidad no puede ser nula");
        }

        if (direccion == null) {
            throw new MiException("La especialidad no puede ser nula");
        }

        if (matricula == null) {
            throw new MiException("La matrícula no puede ser nula");
        }
        if (horariosAtencion.isEmpty() || horariosAtencion == null){
            throw new MiException ("Los Horarios de Atencion no pueden estar vacios o nulo");
        }
        if (precioConsulta == 0){
            throw new MiException ("El precio de Consulta no pueden estar vacios o cero");
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("Las contraseñas no pueden estar vacias y tener menos de 5 caracteres ");
        }

        if (!password.equals(password2)) {
            throw new MiException("las contraseñas deben coincidir");
        }


    }
    @Transactional
    public void recibirPuntuacion(String idConsulta, int puntuacion) {
        Optional<Consulta> consultaOptional = consultaRepositorio.findById(idConsulta);
        if (consultaOptional.isPresent()) {
            Consulta consulta = consultaOptional.get();
            Profesional profesional = consulta.getProfesional();

            profesional.recibirPuntuacion(consulta, puntuacion);

            profesionalRepositorio.save(profesional);
        }
    }

    @Transactional
    public void eliminarProfesional(String id) {
        profesionalRepositorio.deleteById(id);
    }


    public Usuario buscarUsuarioPorID(String id) {

        Optional<Usuario> oUsuario = usuarioRepositorio.findById(id);
        Usuario Usuario = null;
        if (oUsuario.isPresent()) {

            Usuario = oUsuario.get();

        }
        return Usuario;
    }

    public List<Profesional> listarProfesional(String especialidad) {

        if (especialidad != null) {
            return profesionalRepositorio.buscarProfesionalPorEspecialidad(especialidad);
        }

        return profesionalRepositorio.listarOrdenadoPorEspecialidad();
    }

    public void validar(String nombreUsuario, String password, String password2, String nombre, String apellido, Date fechaDeNacimiento, Long DNI, String email) throws MiException {

        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiException("El nombre de usuario no puede estar vacio o Nulo");

        }

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacío o ser nulo");
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("El apellido no puede estar vacío o ser nulo");
        }

        if (DNI == null) {
            throw new MiException("El DNI no puede ser nulo");
        }

        if (fechaDeNacimiento == null) {
            throw new MiException("La fecha de nacimiento no puede ser nula");
        }

        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede estar vacío o ser nulo");
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("Las contraseñas no pueden estar vacias y tener menos de 5 caracteres ");
        }

        if (!password.equals(password2)) {
            throw new MiException("las contraseñas deben coincidir");
        }

    }

    public Profesional getOne(String id) {
        return profesionalRepositorio.getOne(id);
    }

    @Override
    public UserDetails loadUserByUsername(String nombreProfesional) throws UsernameNotFoundException {

        Profesional profesional = profesionalRepositorio.buscarNombre(nombreProfesional);

        if (profesional != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + profesional.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", profesional);
            User user = new User(profesional.getNombreUsuario(), profesional.getPassword(), permisos);

            return user;
        } else {
            return null;
        }

    }

}