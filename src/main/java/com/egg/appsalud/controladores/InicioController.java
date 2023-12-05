package com.egg.appsalud.controladores;

import com.egg.appsalud.Enumeracion.DiaSemana;
import com.egg.appsalud.Enumeracion.Especialidad;
import com.egg.appsalud.Enumeracion.Provincias;
import com.egg.appsalud.Exception.MiException;
import com.egg.appsalud.entidades.Consulta;
import com.egg.appsalud.entidades.Paciente;
import com.egg.appsalud.entidades.Profesional;
<<<<<<< HEAD
import com.egg.appsalud.repositorios.ProfesionalRepositorio;
import com.egg.appsalud.servicios.*;
=======
import com.egg.appsalud.servicios.PacienteServicio;
import com.egg.appsalud.servicios.ProfesionalServicio;
import com.egg.appsalud.servicios.UsuarioServicio;
>>>>>>> botonModificarAdmin
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class InicioController {

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    PacienteServicio pacienteServicio;

    @Autowired
    PacienteServicio pacienteServicio;

    @Autowired
    ProfesionalServicio profesionalServicio;

    @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    @Autowired
    TurnoServicio turnoServicio;

    @Autowired
    ConsultaServicio consultaServicio;

    @GetMapping("/registrar")
    public String registrar(){
        return "registro";
    }

    @GetMapping("/registroPaciente")
    public String registroPaciente() {
        return "paciente_registro";
    }

    @PostMapping("/registrarPaciente")
    public String registrarPaciente(@RequestParam MultipartFile archivo, @RequestParam String nombreUsuario, @RequestParam String nombre, @RequestParam String apellido, @RequestParam(required = false) Long dni, @RequestParam("fechaDeNacimiento") String fechaDeNacimientoStr, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelo) throws MiException, ParseException {
        Date fechaDeNacimiento;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fechaDeNacimiento = dateFormat.parse(fechaDeNacimientoStr);

        } catch (ParseException p) {
            modelo.put("error", "la fecha no puede venir vacía");
            return "redirect:/portal/registroUsuario";
        }

        try {

<<<<<<< HEAD
=======
            System.out.println("EJECUTANDO TRY POST REGISTRAR");
>>>>>>> botonModificarAdmin
            pacienteServicio.crearPaciente(archivo, nombreUsuario, nombre, apellido, dni, fechaDeNacimiento, email, password, password2);

            modelo.addAttribute("exito", null);
            modelo.put("exito", "el usuario fue creado con exito");
            return "redirect:/portal/login";
        } catch (MiException e) {

            modelo.put("error", e.getMessage());

            return "redirect:/portal/registroUsuario";
        }
    }

    @GetMapping("/registroProfesional")
    public String registroProfesional(ModelMap modelo) {

        Especialidad[] especialidades = Especialidad.values();
        Provincias[] provincias = Provincias.values();
        modelo.addAttribute("provincias", provincias);
        modelo.addAttribute("especialidades", especialidades);
        return "profesional_registro";
    }

    @PostMapping("/registrarProfesional")
    public String registrarProfesional(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam("fechaDeNacimiento") String fechaDeNacimientoStr,
            @RequestParam(required = false) Long dni,

            @RequestParam String email,
            @RequestParam(required = false) Long matricula,
            @RequestParam Especialidad especialidad,
            @RequestParam List<DiaSemana> diasDisponibles,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioEntrada,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioSalida,
            @RequestParam Integer precioConsulta,
            @RequestParam String password,
            @RequestParam String password2,
            MultipartFile archivo,
            @RequestParam String nombreUsuario,
            @RequestParam Provincias provincias,
            @RequestParam String localidad,
            @RequestParam String direccion,
            ModelMap modelo) {

        Date fechaDeNacimiento;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fechaDeNacimiento = dateFormat.parse(fechaDeNacimientoStr);

        } catch (ParseException p) {
            modelo.addAttribute("error", "la fecha no puede venir vacía");
            return "redirect:/registroProfesional";
        }

        try {
            profesionalServicio.crearProfesional(archivo, nombreUsuario, password, password2, nombre, apellido, email,
                    fechaDeNacimiento, dni, especialidad, provincias, localidad, direccion, matricula,
                    diasDisponibles, horarioEntrada, horarioSalida, precioConsulta);

            var registrado = profesionalRepositorio.buscarPorEmail(email);

            turnoServicio.generarTurnos(registrado);
            modelo.put("exito", "Su cuenta fue creada con exito");

        } catch (MiException e) {

            modelo.put("error", e.getMessage());
            return "redirect:/registroProfesional";
        }
        return "redirect:/portal/login";
    }

    @GetMapping("/listaProfesionales")
    public String listarProfesionales(@Param("especialidad") String especialidad,
                                      @Param("columna") String columna, ModelMap modelo, HttpSession session) {


        List<Profesional> profesionales = profesionalServicio.listarProfesional(especialidad, columna);
        modelo.addAttribute("profesional", profesionales);
        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);
        modelo.addAttribute("valorSeleccionado", especialidad);
        modelo.addAttribute("ordenSeleccionado", columna);
        return "profesional_lista";
    }

    @GetMapping("/terminos")
    public String terminos() {
        return "terminos";
    }

    @GetMapping("/privacidad")
    public String privacidad() {
        return "privacidad";
    }
}
