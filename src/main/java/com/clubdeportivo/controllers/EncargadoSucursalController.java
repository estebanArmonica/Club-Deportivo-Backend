package com.clubdeportivo.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/encargado-sucursal")
@Tag(name = "EncargadosSucursales", description = "Endpoints for Encargados Sucursales")
public class EncargadoSucursalController {
}
