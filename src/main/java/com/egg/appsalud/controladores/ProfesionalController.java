package com.egg.appsalud.controladores;

import com.egg.appsalud.Enumeracion.Especialidad;
import com.egg.appsalud.Enumeracion.Provincias;
import com.egg.appsalud.Exception.MiException;
import com.egg.appsalud.entidades.Profesional;
import com.egg.appsalud.repositorios.ProfesionalRepositorio;
import com.egg.appsalud.servicios.ProfesionalServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/profesional")
public class ProfesionalController {

    @Autowired
    ProfesionalServicio profesionalServicio;

    //    MODIFICAR DATOS COMO PROFESIONAL
    @GetMapping("/editar")
    public String editarProfesional(ModelMap modelo, HttpSession session) {

        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);

        Profesional profesionalActualizado = (Profesional) session.getAttribute("profesionalActualizado");
        session.removeAttribute("profesionalActualizado");
        modelo.addAttribute("provincias", Provincias.values());
        modelo.addAttribute("profesional", profesionalActualizado);

        return "profesional_edit";
    }

        @PostMapping("/editar/{id}")
        public String editarProfesional(@PathVariable String id, MultipartFile archivo, @RequestParam String nombreUsuario, @RequestParam String nombre, @RequestParam String apellido,
                                        @RequestParam(required = false) Long DNI, @RequestParam("fechaDeNacimiento") String fechaDeNacimientoStr, @RequestParam String email, @RequestParam Especialidad especialidad, @RequestParam Provincias provincias, @RequestParam String localidad, @RequestParam String direccion,
                                        @RequestParam int precioConsulta, @RequestParam Long matricula, ModelMap modelo, HttpSession session) {

            Date fechaDeNacimiento;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fechaDeNacimiento = dateFormat.parse(fechaDeNacimientoStr);

            } catch (ParseException p) {
                modelo.put("error", "la fecha no puede venir vacía");
                return "redirect:/profesional/editar";
            }

            try {

                //profesionalServicio.modificarProfesional(id, archivo, nombreUsuario, nombre, apellido, DNI, fechaDeNacimiento, email, password, password2, true, especialidad, provincias, localidad, direccion, matricula, precioConsulta);
                profesionalServicio.editarProfesional(id, archivo, nombreUsuario, nombre, apellido, DNI, fechaDeNacimiento, email, true, especialidad, provincias, localidad, direccion, matricula, precioConsulta);
                modelo.put("exito", "Profesional modificado con exito");

                Profesional profesionalActualizado = profesionalServicio.getOne(id);
                session.setAttribute("profesionalActualizado", profesionalActualizado);

            } catch (MiException ex) {
                modelo.put("error", ex.getMessage());
                return "redirect:/profesional/editar";
            }
            return "redirect:/";
        }

        @GetMapping("/cambiarContrasena")
        public String mostrarFormularioCambioContrasena(ModelMap modelo, HttpSession session) {
            String idProfesional = (String) session.getAttribute("profesionalId");
            modelo.addAttribute("idProfesional", idProfesional);
            return "cambiar_contrasena";
        }

        @PostMapping("/cambiarContrasena")
        public String cambiarContrasena(@RequestParam String idProfesional,
                @RequestParam String password,
                @RequestParam String newPassword,
                @RequestParam String newPasswordConfirm,
                ModelMap modelo) {
            try {
                profesionalServicio.cambiarContrasena(idProfesional, password, newPassword, newPasswordConfirm);
                modelo.addAttribute("exito", "Contraseña establecida con éxito"); 
                return "index";
            } catch (MiException ex) {
                modelo.put("error", ex.getMessage());
                System.out.println("Error al cambiar la contraseña: " + ex.getMessage());
                return "redirect:/profesional/editar";
            }
        }
}