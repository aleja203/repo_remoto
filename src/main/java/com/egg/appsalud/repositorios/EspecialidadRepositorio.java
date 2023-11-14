package com.egg.appsalud.repositorios;

import com.egg.appsalud.entidades.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepositorio extends JpaRepository<Especialidad, String> {

}
