package com.clubdeportivo.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/cancha")
@Tag(name = "Canchas", description = "Endpoints for Canchas")
public class CanchaController {
}
