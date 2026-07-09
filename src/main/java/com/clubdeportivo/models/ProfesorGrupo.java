package com.clubdeportivo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
@Table(name = "profesor_grupo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfesorGrupo {

    @EmbeddedId
    private ProfesorGrupoId id;

    @Column(name = "fech_creacion", nullable = false)
    private LocalDate fechCreacion;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("id_profesor")
    @JoinColumn(name = "id_profesor", insertable = false, updatable = false)
    private Usuario user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("id_grupo")
    @JoinColumn(name = "id_grupo", insertable = false, updatable = false)
    private Grupo grupo;

    @Data
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfesorGrupoId implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "id_profesor")
        private Long idProfesor;

        @Column(name = "id_grupo")
        private Long idGrupo;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProfesorGrupoId that = (ProfesorGrupoId) o;
            return Objects.equals(idProfesor, that.idProfesor) && Objects.equals(idGrupo, that.idGrupo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idProfesor, idGrupo);
        }
    }
}
