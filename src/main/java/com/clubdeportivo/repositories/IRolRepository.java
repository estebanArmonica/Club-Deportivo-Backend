package com.clubdeportivo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clubdeportivo.models.Rol;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long>{
    Optional<Rol> findByTipoRol(String tipoRol);
}
