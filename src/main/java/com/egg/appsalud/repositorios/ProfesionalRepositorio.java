package com.egg.appsalud.repositorios;

import com.egg.appsalud.entidades.Profesional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional,String>{
    @Query("SELECT p FROM Profesional p WHERE p.nombre= :nombre")
public Profesional buscarNombre(@Param("nombre") String titulo);

@Query("SELECT p FROM Profesional p ORDER BY p.nombre DESC")
public List<Profesional> buscarOrdenado();
}
