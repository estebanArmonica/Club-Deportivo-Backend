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
@Table(name = "encargado_sucursal")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncargadoSucursal {

    @EmbeddedId
    private EncargadoSucursalId id;

    @Column(name = "turno", nullable = false, length = 20)
    private String turno;

    @Column(name = "fech_asignacion", nullable = false)
    private LocalDate fechAsignacion;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("id_sucursal")
    @JoinColumn(name = "id_sucursal", insertable = false, updatable = false)
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("id_encargado")
    @JoinColumn(name = "id_encargado", insertable = false, updatable = false)
    private Usuario user;

    // sub-clase
    @Data
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EncargadoSucursalId implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "id_sucursal")
        private Long idSucursal;

        @Column(name = "id_encargado")
        private Long idEncargado;

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            EncargadoSucursalId that = (EncargadoSucursalId) o;

            return Objects.equals(idSucursal, that.idSucursal) && Objects.equals(idEncargado, that.idEncargado);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idSucursal, idEncargado);
        }
    }
}
