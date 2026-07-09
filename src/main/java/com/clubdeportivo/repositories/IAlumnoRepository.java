package com.clubdeportivo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clubdeportivo.models.Alumno;

@Repository
public interface IAlumnoRepository extends JpaRepository<Alumno, Long> {
    /*
    *  En esté repositorio tenemos consultas básicas para realizar el CRUD correspondiente a ALumnos
    *  Además se realizan sentencias JPQL usando @Query para logica más tecnica usando JOINS
    *  También se agrego consultas de estadisticas que nos pueden servir a futuro
    */

    // realización de consultas básicas
    Optional<Alumno> findByRut(String rut);
    Boolean existsByRut(String rut);
    List<Alumno> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
    List<Alumno> findByActivoTrue();
    List<Alumno> findByActivoFalse();
    List<Alumno> findByFechInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Query con JOIN a Apoderado
    @Query("SELECT a FROM Alumno a JOIN a.apoderados ap WHERE ap.id = :apoderadoId")
    List<Alumno> findAlumnosByApoderadoId(@Param("apoderadoId") Long apoderadoId);

    @Query("SELECT a FROM Alumno a LEFT JOIN FETCH a.apoderados WHERE a.id = :id")
    Optional<Alumno> findByIdWithApoderados(@Param("id") Long id);

    @Query("SELECT a FROM Alumno a LEFT JOIN FETCH a.apoderados")
    List<Alumno> findAllWithApoderados();

    /*
    * Query con JOIN recorrido hasta Inscripcion
    * Verifica si un alumno está activo en algún grupo
    */
    @Query("SELECT DISTINCT a FROM Alumno a " +
            "JOIN Inscripcion i ON i.alumno.id = a.id " +
            "WHERE i.grupo.id = :grupoId " +
            "AND i.estado = 'activa' " +
            "AND a.activo = true")
    List<Alumno> findAlumnosActivosByGrupoId(@Param("grupoId") Long grupoId);

    /*
    * Query el cual nos dice si algún alumno asistio a las clases del taller
    * con su respectivo grupo
    */
    @Query("SELECT DISTINCT a FROM Alumno a " +
            "JOIN Inscripcion i ON i.alumno.id = a.id " +
            "JOIN Grupo g ON g.id = i.grupo.id " +
            "JOIN Clase c ON c.grupo.id = g.id " +
            "JOIN Asistencia asis ON asis.clase.id = c.id " +
            "WHERE c.id = :claseId " +
            "AND asis.asistio = true")
    List<Alumno> findAlumnosQueAsistieronAClase(@Param("claseId") Long claseId);


    /*
     * Query el cual nos dice si algún alumno no asistio a las clases del taller
     * con su respectivo grupo
     */
    @Query("SELECT DISTINCT a FROM Alumno a " +
            "JOIN Inscripcion i ON i.alumno.id = a.id " +
            "JOIN Grupo g ON g.id = i.grupo.id " +
            "JOIN Clase c ON c.grupo.id = g.id " +
            "WHERE c.id = :claseId " +
            "AND a.id NOT IN (" +
            "    SELECT a2.id FROM Alumno a2 " +
            "    JOIN Inscripcion i2 ON i2.alumno.id = a2.id " +
            "    JOIN Grupo g2 ON g2.id = i2.grupo.id " +
            "    JOIN Clase c2 ON c2.grupo.id = g2.id " +
            "    JOIN Asistencia asis2 ON asis2.clase.id = c2.id " +
            "    WHERE c2.id = :claseId " +
            "    AND asis2.asistio = true" +
            ") AND a.activo = true")
    List<Alumno> findAlumnosQueNoAsistieronAClase(@Param("claseId") Long claseId);

    /*
    * Consulta de validación el cual nos ayuda a saber si un alumno está inscrito en x grupo
    */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Inscripcion i " +
            "WHERE i.alumno.id = :alumnoId " +
            "AND i.grupo.id = :grupoId " +
            "AND i.estado = 'activa'")
    Boolean isAlumnoInscritoEnGrupo(@Param("alumnoId") Long alumnoId,
                                    @Param("grupoId") Long grupoId);

    /*
    * Nos cuenta cuatos alumnos están activos
    */
    @Query("SELECT COUNT(a) FROM Alumno a WHERE a.activo = true")
    Long countAlumnosActivos();


    /*
    * Consulta el cual nos dicta cual o cuales alumnos han tenido más asistencia al taller
    */
    @Query("SELECT a, COUNT(asis) AS totalAsistencias " +
            "FROM Alumno a " +
            "JOIN Inscripcion i ON i.alumno.id = a.id " +
            "JOIN Grupo g ON g.id = i.grupo.id " +
            "JOIN Clase c ON c.grupo.id = g.id " +
            "JOIN Asistencia asis ON asis.clase.id = c.id " +
            "WHERE asis.asistio = true " +
            "GROUP BY a.id " +
            "ORDER BY totalAsistencias DESC")
    List<Object[]> findAlumnosConMasAsistencias();
}
