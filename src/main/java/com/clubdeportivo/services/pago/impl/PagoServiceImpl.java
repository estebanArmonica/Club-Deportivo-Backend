package com.clubdeportivo.services.pago.impl;

import com.clubdeportivo.dtos.pago.EstadisticasPagoDTO;
import com.clubdeportivo.models.Pago;
import com.clubdeportivo.repositories.IPagoRepository;
import com.clubdeportivo.services.pago.IPagoService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PagoServiceImpl implements IPagoService {

    private final IPagoRepository pagoRepository;

    // validaciones privadas
    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        String estadoLower = estado.toLowerCase();
        if (!estadoLower.equals("pendiente") &&
                !estadoLower.equals("pagado") &&
                !estadoLower.equals("cancelado") &&
                !estadoLower.equals("reembolsado")) {
            throw new RuntimeException("Estado no válido. Debe ser: pendiente, pagado, cancelado o reembolsado");
        }
    }

    private void validarMetodoPago(String metodoPago) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new RuntimeException("El método de pago es obligatorio");
        }
    }

    private void validarMonto(int monto) {
        if (monto <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }
    }

    // crud
    @Override
    public Pago create(Pago pago) {
        // Validar monto
        validarMonto(pago.getMonto());

        // Validar método de pago
        validarMetodoPago(pago.getMetodoPago());

        // Validar estado
        validarEstado(pago.getEstado());

        // Si no se especifica fecha de pago, usar la actual
        if (pago.getFechPago() == null) {
            pago.setFechPago(LocalDate.now());
        }

        // Validar observaciones (si es null, asignar vacío)
        if (pago.getObservaciones() == null) {
            pago.setObservaciones("");
        }

        return pagoRepository.save(pago);
    }

    @Override
    public Pago update(Long id, Pago pagoActualizado) {
        Pago pagoExistente = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        // Validar monto
        validarMonto(pagoActualizado.getMonto());

        // Validar método de pago
        validarMetodoPago(pagoActualizado.getMetodoPago());

        // Validar estado
        validarEstado(pagoActualizado.getEstado());

        // Si el pago ya está pagado, no se puede modificar (solo observaciones)
        if (pagoExistente.getEstado().equalsIgnoreCase("pagado") &&
                !pagoActualizado.getEstado().equalsIgnoreCase("pagado")) {
            throw new RuntimeException("No se puede modificar un pago ya pagado");
        }

        pagoExistente.setMonto(pagoActualizado.getMonto());
        pagoExistente.setFechPago(pagoActualizado.getFechPago());
        pagoExistente.setMetodoPago(pagoActualizado.getMetodoPago());
        pagoExistente.setEstado(pagoActualizado.getEstado());
        pagoExistente.setObservaciones(pagoActualizado.getObservaciones());

        return pagoRepository.save(pagoExistente);
    }

    @Override
    public Pago cambiarEstado(Long id, String nuevoEstado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        validarEstado(nuevoEstado);

        String nuevoEstadoLower = nuevoEstado.toLowerCase();

        // Si el pago ya está pagado, no se puede cambiar a otro estado que no sea reembolsado
        if (pago.getEstado().equalsIgnoreCase("pagado") &&
                !nuevoEstadoLower.equals("reembolsado")) {
            throw new RuntimeException("Un pago pagado solo puede cambiarse a reembolsado");
        }

        pago.setEstado(nuevoEstadoLower);
        return pagoRepository.save(pago);
    }

    @Override
    public Pago marcarComoPagado(Long id) {
        return cambiarEstado(id, "pagado");
    }

    @Override
    public Pago marcarComoCancelado(Long id) {
        return cambiarEstado(id, "cancelado");
    }

    @Override
    public Pago marcarComoReembolsado(Long id) {
        return cambiarEstado(id, "reembolsado");
    }

    @Override
    public void delete(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }

    // busquedas

    @Override
    public Pago findById(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }

    @Override
    public List<Pago> findAll() {
        return pagoRepository.findAllByOrderByFechPagoDesc();
    }

    @Override
    public List<Pago> findByEstado(String estado) {
        return pagoRepository.findByEstadoOrderByFechPagoDesc(estado);
    }

    @Override
    public List<Pago> findByMetodoPago(String metodoPago) {
        return pagoRepository.findByMetodoPagoOrderByFechPagoDesc(metodoPago);
    }

    @Override
    public List<Pago> findByFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        return pagoRepository.findByFechPago(fecha);
    }

    @Override
    public List<Pago> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return pagoRepository.findByFechPagoBetweenOrderByFechPagoDesc(fechaInicio, fechaFin);
    }

    @Override
    public List<Pago> findByMontoBetween(int montoMin, int montoMax) {
        if (montoMin > montoMax) {
            throw new RuntimeException("El monto mínimo no puede ser mayor al monto máximo");
        }
        return pagoRepository.findByMontoBetween(montoMin, montoMax);
    }

    @Override
    public List<Pago> findByEstadoAndMetodoPago(String estado, String metodoPago) {
        return pagoRepository.findByEstadoAndMetodoPago(estado, metodoPago);
    }

    @Override
    public List<Pago> findPagosDelDia() {
        return pagoRepository.findPagosDelDia();
    }

    @Override
    public List<Pago> findPagosDeLaSemana() {
        return pagoRepository.findPagosDeLaSemana();
    }

    @Override
    public List<Pago> findPagosDelMes() {
        return pagoRepository.findPagosDelMes();
    }

    // Reportes

    @Override
    public EstadisticasPagoDTO getEstadisticas() {
        return EstadisticasPagoDTO.builder()
                .total(pagoRepository.count())
                .pendientes(pagoRepository.countPagosPendientes())
                .pagados(pagoRepository.countPagosPagados())
                .cancelados(pagoRepository.countPagosCancelados())
                .reembolsados(pagoRepository.countPagosReembolsados())
                .pagosPorEstado(pagoRepository.countPagosByEstado())
                .pagosPorMetodoPago(pagoRepository.countPagosByMetodoPago())
                .pagosPorMes(pagoRepository.countPagosByMes())
                .sumPagosPorEstado(pagoRepository.sumPagosByEstado())
                .sumPagosPorMetodoPago(pagoRepository.sumPagosByMetodoPago())
                .montoMaximo(pagoRepository.findMaxMonto())
                .montoMinimo(pagoRepository.findMinMonto())
                .build();
    }

    @Override
    public List<Object[]> getResumenDiario(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return pagoRepository.getResumenDiario(fechaInicio, fechaFin);
    }

    @Override
    public Long getTotalPagosByFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return pagoRepository.sumPagosByFechasBetween(fechaInicio, fechaFin);
    }

    @Override
    public Long getTotalPagosByEstadoAndFechas(String estado, LocalDate fechaInicio, LocalDate fechaFin) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return pagoRepository.sumPagosByEstadoAndFechasBetween(estado, fechaInicio, fechaFin);
    }

    // Validaciones

    @Override
    public boolean existsById(Long id) {
        return pagoRepository.existsById(id);
    }

    @Override
    public boolean isPagado(Long id) {
        Boolean pagado = pagoRepository.isPagoPagado(id);
        if (pagado == null) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        return pagado;
    }

    @Override
    public boolean isPendiente(Long id) {
        Boolean pendiente = pagoRepository.isPagoPendiente(id);
        if (pendiente == null) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        return pendiente;
    }
}
