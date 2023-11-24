package com.egg.appsalud.controladores;

import com.egg.appsalud.servicios.PacienteServicio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/paciente")
public class PacienteControlador {

    @Autowired
    PacienteServicio pacienteServicio;

    @GetMapping("/solicitar")
    public String solicitarCita() {
        return "citaSolicitud";
    }

    @GetMapping("/editar")
    public String editarPaciente(){
        return "paciente_edit";
    }

    @GetMapping("/citas")
    public String listarCitas() {
        return "citasPaciente";
    }

    @GetMapping("/historia")
    public String historiaClinica() {
        return "historiaClinica";
    }

    @GetMapping("/modificar")
    public String modificarPaciente() {
        return "usuarioModificar";
    }

}

