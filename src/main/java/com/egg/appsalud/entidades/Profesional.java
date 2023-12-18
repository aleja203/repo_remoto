package com.egg.appsalud.entidades;

import com.egg.appsalud.Enumeracion.DiaSemana;
import com.egg.appsalud.Enumeracion.Especialidad;
import com.egg.appsalud.Enumeracion.Provincias;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.*;

import lombok.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROFESIONAL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "rol",
//        discriminatorType = DiscriminatorType.STRING)

@DiscriminatorColumn(name = "rol", discriminatorType = DiscriminatorType.STRING)

@DiscriminatorValue("PROFESIONAL")
public class Profesional extends Usuario {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private Long matricula;

    @OneToMany
    private List<ObraSocial> ObraSocial;
    @OneToMany
    private List<Paciente> pacientes;


    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    
    @Enumerated(EnumType.STRING)
    private Provincias provincias;
    
    private String localidad;
    private String direccion;


    @ElementCollection
    @CollectionTable(name = "HORARIOS_ATENCION", joinColumns = @JoinColumn(name = "profesional_id"))
    @Column(name = "horario_atencion")
    private List<LocalTime> horariosAtencion;
        //LOS HORARIOS DE ATENCION SE DARIAN DE LUNES A VIERNES EN UN HORARIO FIJO CON TURNOS DE 30 MIN//

    private int PrecioConsulta;


    @OneToMany
    private List<Consulta> consultas;

    @ManyToOne
    private FichaMedica fichaMedica;

    private int valoracionProfesional = 10;

    @ElementCollection
    @CollectionTable(name = "Profesional_DiasDisponibles")
    @Enumerated(EnumType.STRING)
    protected List<DiaSemana> diasDisponibles;
    protected LocalTime horarioEntrada;
    protected LocalTime horarioSalida;
    protected String observaciones;
    
    @ElementCollection
    @CollectionTable(name = "Profesional_FechasDisponibles")
    private List<LocalDate> fechasDisponibles;

    @OneToMany(mappedBy = "profesional", fetch = FetchType.LAZY)
    private List<Turno> turnosCreados;

    @OneToMany
    private List<FichaMedica> historialConsultas;


    public void recibirPuntuacion(Consulta consulta, int puntuacion) {
        if (consultas.contains(consulta)) {
            if (puntuacion >= 1 && puntuacion <= 10) {
                consulta.setPuntuacion(puntuacion);
                actualizarValoracion();
            } else {
                throw new IllegalArgumentException("La puntuación debe estar en el rango de 1 a 10");
            }
        } else {
            throw new IllegalArgumentException("La consulta no pertenece a este profesional");
        }
    }

    private void actualizarValoracion() {
        if (consultas != null && !consultas.isEmpty()) {
            int totalPuntuacion = 0;
            int totalConsultasConPuntuacion = 0;

            for (Consulta consulta : consultas) {
                if (consulta.getPuntuacion() > 0) {
                    totalPuntuacion += consulta.getPuntuacion();
                    totalConsultasConPuntuacion++;
                }
            }

            if (totalConsultasConPuntuacion > 0) {
                valoracionProfesional = totalPuntuacion / totalConsultasConPuntuacion;
            } else {
                valoracionProfesional = 0;
            }
        }
    }

    public void programarConsulta(Consulta consulta, LocalTime horario) {
        if (horariosAtencion.contains(horario) && !horarioOcupado(horario)
                && horarioValido(consulta.getDuracionConsulta(), horario)) {
            consulta.setHoraInicio(horario);
            consultas.add(consulta);
        } else {
            throw new IllegalArgumentException("El horario no está disponible, ya está ocupado o no es válido para la duración de la consulta");
        }
    }

    private boolean horarioOcupado(LocalTime horario) {
        for (Consulta consulta : consultas) {
            if (consulta.getHoraInicio() != null && consulta.getHoraInicio().equals(horario)) {
                return true; // Horario ocupado
            }
        }
        return false; // Horario disponible
    }

    private boolean horarioValido(int duracionConsulta, LocalTime horario) {
        LocalTime horaFinConsulta = horario.plusMinutes(duracionConsulta);

        // Verificar si hay suficiente espacio para la duración de la consulta
        if (horaFinConsulta.isAfter(LocalTime.of(24, 0))) {
            return false; // La consulta no puede finalizar después de medianoche
        }

        // Verificar si el horario se superpone con otras consultas
        for (Consulta consulta : consultas) {
            if (consulta.getHoraInicio() != null) {
                LocalTime horaFinConsultaExistente = consulta.getHoraInicio().plusMinutes(consulta.getDuracionConsulta());
                if (horario.isBefore(horaFinConsultaExistente) && horaFinConsulta.isAfter(consulta.getHoraInicio())) {
                    return false; // Hay superposición de horarios
                }
            }
        }
        return true; // Horario válido
    }

}
