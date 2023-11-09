package com.egg.appsalud.servicios;

import com.egg.appsalud.Exception.MiException;
import com.egg.appsalud.entidades.Profesional;
import com.egg.appsalud.entidades.Usuario;
import com.egg.appsalud.repositorios.ProfesionalRepositorio;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesionalServicio {
       
    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;
    
    public List<Profesional> listarProfesional() {

        List<Profesional> profesional = profesionalRepositorio.buscarOrdenado();

        return profesional;
    }
}
