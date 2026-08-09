package com.clubdeportivo.dtos.apoderado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApoderadoDTO {
    private Long id;
    private String parentesco, contacto;
    private Set<Long> alumnosIds;
}
