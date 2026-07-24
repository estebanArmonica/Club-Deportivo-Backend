package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Apoderado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IApoderadoRepository extends JpaRepository<Apoderado, Long> {
    // busquedas básicas

    // buscamos un apoderado por contacto (telefono o email)
    Optional<Apoderado> findByContacto(String contacto);

    // buscamos un apoderado por parentesco
    List<Apoderado> findByParentesco(String parentesco);

    // buscamos por contacto que contenga un valor (esto es para busquedas parciales)
    List<Apoderado> findByContactoContaining(String contacto);

    // Verificamos si existe un apoderado por contacto
    Boolean existsByContacto(String contacto);

    // buscamos apoderados por el id del alumno
    @Query("SELECT ap FROM Apoderado ap JOIN ap.alumnos a WHERE a.id = :alumnoId")
    List<Apoderado> findApoderadosByAlumnoId(@Param("alumnoId") Long alumnoId);

    // buscamos apoderados por el rut del alumno
    @Query("SELECT ap FROM Apoderado ap JOIN ap.alumnos a WHERE a.rut = :rut")
    List<Apoderado> findApoderadosByAlumnoRut(@Param("rut") String rut);

    // buscamos apoderados con sus alumnos (asi evitamos n+1)
    @Query("SELECT ap FROM Apoderado ap LEFT JOIN FETCH ap.alumnos a WHERE a.id = :id")
    Optional<Apoderado> findByIdWithAlumnos(@Param("id") Long id);

    // buscamos todos los apoderados con sus alumnos
    @Query("SELECT ap FROM Apoderado ap LEFT JOIN FETCH ap.alumnos")
    List<Apoderado> findAllWithAlumnos();

    // buscamos apoderados que tienen alumnos en un grupo especifico
    /*@Query(value = "SELECT DISTINCT ap.* FROM apoderado ap " +
            "INNER JOIN alumno_apoderado aa ON ap.id_apoderado = aa.id_apoderado " +
            "INNER JOIN alumno a ON aa.id_alumno = a.id_alumno " +
            "INNER JOIN inscripcion i ON a.id_alumno = i.id_alumno " +
            "WHERE i.id_grupo = :grupoId AND i.estado = 'activa'",
            nativeQuery = true)
    List<Apoderado> findApoderadosByGrupoId(@Param("grupoId") Long grupoId);*/

    @Query("SELECT DISTINCT ap FROM Apoderado ap " +
            "JOIN ap.alumnos a " +
            "JOIN Inscripcion i ON i.alumno.id = a.id " +
            "WHERE i.grupo.id = :grupoId " +
            "AND i.estado = 'activa'")
    List<Apoderado> findApoderadosByGrupoId(@Param("grupoId") Long grupoId);

    // ========================================================================================================================================

    // Contamos apoderados por tipo de parentesco
    @Query("SELECT ap.parentesco, COUNT(ap) FROM Apoderado ap GROUP BY ap.parentesco")
    List<Object[]> countApoderadosByParentesco();

    // buscamos apoderados con más alumnos a cargo
    @Query("SELECT ap, COUNT(ap) AS totalAlumnos "+
           "FROM Apoderado ap " +
           "JOIN ap.alumnos a " +
           "GROUP BY ap.id " +
           "ORDER BY totalAlumnos DESC"
    )
    List<Object[]> findApoderadosConMasAlumnos();

    // ========================================================================================================================================

    // verificamos si un apderado tiene alumnos
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM Apoderado ap JOIN ap.alumnos a WHERE ap.id = :apoderadoId"
    )
    Boolean hasAlumnos(@Param("apoderadoId") Long apoderadoId);

    // buscamos apoderados sin alumnos asignados
    @Query("SELECT ap FROM Apoderado ap WHERE ap.alumnos IS EMPTY")
    List<Apoderado> findApoderadosSinAlumnos();
}
