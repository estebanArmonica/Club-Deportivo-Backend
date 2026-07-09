package com.clubdeportivo.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "apoderado")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Apoderado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_apoderado")
    private Long id;

    @Column(name = "parentesco", nullable = false, length = 30)
    private String parentesco;

    @Column(name = "contacto", nullable = false, length = 20)
    private String contacto;

    // relacion con Alumnos (N:N)
    @ManyToMany(mappedBy = "apoderados", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Alumno> alumnos = new HashSet<>();
}
