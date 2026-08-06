package com.clubdeportivo.services.asistencia.impl;

import com.clubdeportivo.dtos.asistencia.EstadisticasAsistenciaDTO;
import com.clubdeportivo.models.Asistencia;
import com.clubdeportivo.models.Clase;
import com.clubdeportivo.models.Pago;
import com.clubdeportivo.repositories.IAsistenciaRepository;
import com.clubdeportivo.repositories.IClaseRepository;
import com.clubdeportivo.repositories.IPagoRepository;
import com.clubdeportivo.services.asistencia.IAsistenciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements IAsistenciaService {

    private IAsistenciaRepository asistenciaRepository;
    private IClaseRepository claseRepository;
    private IPagoRepository pagoRepository;

    // Validaciones privadas
    private String validarHoraLlegadaConObservacion(LocalTime horaLlegada, LocalTime horaInicioClase) {
        if (horaLlegada == null) {
            return ""; // No hay hora de llegada
        }

        if (horaInicioClase == null) {
            throw new RuntimeException("La clase no tiene hora de inicio definida");
        }

        // Definir tiempos de tolerancia
        int minutosTolerancia = 15;
        int minutosTardia = 30;
        int minutosMuyTarde = 60;

        LocalTime horaTopeTolerancia = horaInicioClase.plusMinutes(minutosTolerancia);
        LocalTime horaTopeTardia = horaInicioClase.plusMinutes(minutosTardia);
        LocalTime horaTopeMuyTarde = horaInicioClase.plusMinutes(minutosMuyTarde);

        // Caso 1: Llegó antes o a tiempo
        if (horaLlegada.equals(horaInicioClase) || horaLlegada.isBefore(horaInicioClase)) {
            return "Llegó puntual";
        }

        // Caso 2: Llegó dentro del tiempo de tolerancia (hasta 15 min)
        if (horaLlegada.isBefore(horaTopeTolerancia) || horaLlegada.equals(horaTopeTolerancia)) {
            long minutosRetraso = java.time.Duration.between(horaInicioClase, horaLlegada).toMinutes();
            return "Llegó con " + minutosRetraso + " minutos de retraso (dentro de tolerancia)";
        }

        // Caso 3: Llegó con retraso significativo (15-30 min)
        if (horaLlegada.isBefore(horaTopeTardia) || horaLlegada.equals(horaTopeTardia)) {
            long minutosRetraso = java.time.Duration.between(horaInicioClase, horaLlegada).toMinutes();
            return "Llegó con " + minutosRetraso + " minutos de retraso (retraso significativo)";
        }

        // Caso 4: Llegó muy tarde (30-60 min)
        if (horaLlegada.isBefore(horaTopeMuyTarde) || horaLlegada.equals(horaTopeMuyTarde)) {
            long minutosRetraso = java.time.Duration.between(horaInicioClase, horaLlegada).toMinutes();
            return "Llegó con " + minutosRetraso + " minutos de retraso (muy tarde)";
        }

        // Caso 5: Llegó extremadamente tarde (más de 60 min)
        long minutosRetraso = java.time.Duration.between(horaInicioClase, horaLlegada).toMinutes();
        return "Llegó con " + minutosRetraso + " minutos de retraso (extremadamente tarde - considerar ausente)";
    }

    private Clase validarClase(Long claseId) {
        if (claseId == null) {
            throw new RuntimeException("La asistencia debe tener una clase asociada");
        }

        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + claseId));

        if (!clase.getActivo()) {
            throw new RuntimeException("No se puede crear una asistencia para una clase inactiva");
        }

        return clase;
    }

    private Pago validarPago(Long pagoId) {
        if (pagoId == null) {
            throw new RuntimeException("La asistencia debe tener un pago asociado");
        }

        return pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + pagoId));
    }

    // logica de negocio crud
    @Override
    public Asistencia create(Asistencia asistencia) {
        // Validar clase
        Long claseId = asistencia.getClase() != null ? asistencia.getClase().getId() : null;
        Clase clase = validarClase(claseId);

        // Validar pago
        Long pagoId = asistencia.getPago() != null ? asistencia.getPago().getId() : null;
        Pago pago = validarPago(pagoId);

        // Validar que no exista ya una asistencia para esta clase y pago
        if (asistenciaRepository.existsByClaseIdAndPagoId(claseId, pagoId)) {
            throw new RuntimeException("Ya existe una asistencia para esta clase y pago");
        }

        // Si no se especifica fecha de registro, usar la actual
        if (asistencia.getFechRegistro() == null) {
            asistencia.setFechRegistro(LocalDate.now());
        }

        // Validar hora de llegada
        validarHoraLlegadaConObservacion(asistencia.getHoraLlegada(), clase.getHoraInicio());

        // Validar observaciones (si es null, asignar vacío)
        if (asistencia.getObservaciones() == null) {
            asistencia.setObservaciones("");
        }

        asistencia.setClase(clase);
        asistencia.setPago(pago);

        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia registrarAsistencia(Long claseId, Long pagoId, Boolean asistio) {
        Clase clase = validarClase(claseId);
        Pago pago = validarPago(pagoId);

        Asistencia asistencia = Asistencia.builder()
                .clase(clase)
                .pago(pago)
                .asistio(asistio)
                .fechRegistro(LocalDate.now())
                .observaciones("")
                .build();

        // Si asistió, registrar la hora de llegada
        if (asistio) {
            LocalTime horaActual = LocalTime.now();
            asistencia.setHoraLlegada(horaActual);

            // validamos la hora de llegada y generarmos la observacion
            String observacion = validarHoraLlegadaConObservacion(horaActual, clase.getHoraInicio());
            asistencia.setObservaciones(observacion);
        } else {
            asistencia.setObservaciones("Alumno ausente");
        }

        return create(asistencia);
    }

    @Override
    public Asistencia update(Long id, Asistencia asistenciaActualizada) {
        Asistencia asistenciaExistente = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));

        // Si se cambia la clase, validar que exista y esté activa
        Long nuevaClaseId = asistenciaActualizada.getClase() != null ?
                asistenciaActualizada.getClase().getId() : null;
        Long claseActualId = asistenciaExistente.getClase().getId();

        if (nuevaClaseId != null && !nuevaClaseId.equals(claseActualId)) {
            Clase clase = validarClase(nuevaClaseId);

            // Validar que no exista ya una asistencia para esta clase y pago
            Long pagoId = asistenciaActualizada.getPago() != null &&
                    asistenciaActualizada.getPago().getId() != null ?
                    asistenciaActualizada.getPago().getId() :
                    asistenciaExistente.getPago().getId();

            if (asistenciaRepository.existsByClaseIdAndPagoId(nuevaClaseId, pagoId)) {
                throw new RuntimeException("Ya existe una asistencia para esta clase y pago");
            }

            asistenciaExistente.setClase(clase);
        }

        // Si se cambia el pago, validar que exista
        Long nuevoPagoId = asistenciaActualizada.getPago() != null ?
                asistenciaActualizada.getPago().getId() : null;
        Long pagoActualId = asistenciaExistente.getPago().getId();

        if (nuevoPagoId != null && !nuevoPagoId.equals(pagoActualId)) {
            Pago pago = validarPago(nuevoPagoId);

            // Validar que no exista ya una asistencia para esta clase y pago
            Long claseId = asistenciaActualizada.getClase() != null &&
                    asistenciaActualizada.getClase().getId() != null ?
                    asistenciaActualizada.getClase().getId() :
                    asistenciaExistente.getClase().getId();

            if (asistenciaRepository.existsByClaseIdAndPagoId(claseId, nuevoPagoId)) {
                throw new RuntimeException("Ya existe una asistencia para esta clase y pago");
            }

            asistenciaExistente.setPago(pago);
        }

        // Validar hora de llegada
        if (asistenciaActualizada.getAsistio()) {
            Clase clase = asistenciaExistente.getClase();
            validarHoraLlegadaConObservacion(asistenciaActualizada.getHoraLlegada(), clase.getHoraInicio());
        }

        asistenciaExistente.setAsistio(asistenciaActualizada.getAsistio());
        asistenciaExistente.setHoraLlegada(asistenciaActualizada.getHoraLlegada());
        asistenciaExistente.setObservaciones(asistenciaActualizada.getObservaciones());
        asistenciaExistente.setFechRegistro(asistenciaActualizada.getFechRegistro());

        return asistenciaRepository.save(asistenciaExistente);
    }

    @Override
    public Asistencia marcarPresente(Long id) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));

        asistencia.setAsistio(true);
        if (asistencia.getHoraLlegada() == null) {
            asistencia.setHoraLlegada(LocalTime.now());
        }
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia marcarAusente(Long id) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));

        asistencia.setAsistio(false);
        asistencia.setHoraLlegada(null);
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public void deletePhysical(Long id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new RuntimeException("Asistencia no encontrada con ID: " + id);
        }
        asistenciaRepository.deleteById(id);
    }

    // Busquedas

    @Override
    public Asistencia findById(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));
    }

    @Override
    public Asistencia findByIdWithClaseAndPago(Long id) {
        return asistenciaRepository.findByIdWithClaseAndPago(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));
    }

    @Override
    public Asistencia findByIdWithAllRelations(Long id) {
        return asistenciaRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));
    }

    @Override
    public List<Asistencia> findAll() {
        return asistenciaRepository.findAllByOrderByFechRegistroDesc();
    }

    @Override
    public List<Asistencia> findAllWithClaseAndPago() {
        return asistenciaRepository.findAllWithClaseAndPago();
    }

    @Override
    public List<Asistencia> findByClase(Long claseId) {
        return asistenciaRepository.findByClaseIdOrderByFechaDesc(claseId);
    }

    @Override
    public List<Asistencia> findPresentesByClase(Long claseId) {
        return asistenciaRepository.findPresentesByClaseId(claseId);
    }

    @Override
    public List<Asistencia> findAusentesByClase(Long claseId) {
        return asistenciaRepository.findAusentesByClaseId(claseId);
    }

    @Override
    public List<Asistencia> findByGrupo(Long grupoId) {
        return asistenciaRepository.findByGrupoIdOrderByFechaDesc(grupoId);
    }

    @Override
    public List<Asistencia> findByPago(Long pagoId) {
        return asistenciaRepository.findByPagoId(pagoId);
    }

    @Override
    public List<Asistencia> findByFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        return asistenciaRepository.findByFechRegistro(fecha);
    }

    @Override
    public List<Asistencia> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return asistenciaRepository.findByFechRegistroBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Asistencia> findByAlumno(Long alumnoId) {
        return asistenciaRepository.findByAlumnoId(alumnoId);
    }

    @Override
    public List<Asistencia> findPresentesByAlumno(Long alumnoId) {
        return asistenciaRepository.findByAlumnoIdAndAsistio(alumnoId, true);
    }

    @Override
    public List<Asistencia> findAusentesByAlumno(Long alumnoId) {
        return asistenciaRepository.findByAlumnoIdAndAsistio(alumnoId, false);
    }

    @Override
    public boolean existsByClaseAndPago(Long claseId, Long pagoId) {
        return asistenciaRepository.existsByClaseIdAndPagoId(claseId, pagoId);
    }

    @Override
    public EstadisticasAsistenciaDTO getEstadisticas() {
        return EstadisticasAsistenciaDTO.builder()
                .total(asistenciaRepository.countTotalAsistencias())
                .presentes(asistenciaRepository.countAsistenciasPresentes())
                .ausentes(asistenciaRepository.countAsistenciasAusentes())
                .asistenciasPorClase(asistenciaRepository.countAsistenciasByClase())
                .asistenciasPorGrupo(asistenciaRepository.countAsistenciasByGrupo())
                .asistenciasPorMes(asistenciaRepository.countAsistenciasByMes())
                .porcentajeAsistenciaPorClase(asistenciaRepository.calcularPorcentajeAsistenciaByClase())
                .porcentajeAsistenciaPorGrupo(asistenciaRepository.calcularPorcentajeAsistenciaByGrupo())
                .build();
    }
}
