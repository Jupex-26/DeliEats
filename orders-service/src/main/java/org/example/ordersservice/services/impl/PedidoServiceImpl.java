package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.exception.custom.UnauthorizedException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.models.Estado;
import org.example.ordersservice.models.Pedido;
import org.example.ordersservice.repositories.PedidoRepository;
import org.example.ordersservice.services.*;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final EstadoService estadoService;
    private final RepartidorService repartidorService;
    private final EmpresaService empresaService;
    private final ClienteService clienteService;

    @Override
    public Pedido save(Pedido pedido) {
        pedido.setFechaCompra(LocalDateTime.now());
        return validateFieldsAndSave(pedido);
    }

    @Override
    public Page<Pedido> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    @Override
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado con ID: " + id));
    }

    @Override
    public Page<Pedido> findByClienteId(Long clienteId, Pageable pageable) {
        Cliente cliente = clienteService.findById(clienteId);

        return pedidoRepository.findAllByClienteId(cliente.getId(), pageable);
    }

    @Override
    public Page<Pedido> findByEmpresaId(Long empresaId, Pageable pageable) {
        Empresa empresa = empresaService.findById(empresaId);

        return pedidoRepository.findAllByEmpresaId(empresa.getId(), pageable);
    }

    @Override
    public Page<Pedido> findByEmpresaIdMesActual(Long empresaId, Pageable pageable) {
        Empresa empresa = empresaService.findById(empresaId);

        LocalDateTime inicioMes = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime finMes = YearMonth.now().atEndOfMonth().atTime(23, 59, 59, 999999999);

        return pedidoRepository.findAllByEmpresaIdAndFechaCompraBetween(empresa.getId(), inicioMes, finMes, pageable);
    }

    @Override
    public Page<Pedido> findByEmpresaIdAndMesAndAnio(Long empresaId, int mes, int anio, Pageable pageable) {
        Empresa empresa = empresaService.findById(empresaId);

        YearMonth yearMonth = YearMonth.of(anio, mes);
        LocalDateTime inicioMes = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime finMes = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        return pedidoRepository.findAllByEmpresaIdAndFechaCompraBetween(empresa.getId(), inicioMes, finMes, pageable);
    }

    @Override
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public Pedido update(Long id, Pedido pedido) {
        Pedido pedidoUpdated = findById(id);
        
        pedido.setId(pedidoUpdated.getId());
        pedido.setFechaCompra(pedidoUpdated.getFechaCompra());

        return validateFieldsAndSave(pedido);
    }



    @Override
    public Pedido updateEstado(Long id, Long estadoId) {
        Pedido pedido = findById(id);
        Estado nuevoEstado = estadoService.findById(estadoId);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
    
    @Override
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = findById(id);
        Estado estadoCancelado = estadoService.findByNombre("CANCELADO");
        pedido.setEstado(estadoCancelado);
        return pedidoRepository.save(pedido);
    }

    @NonNull
    private Pedido validateFieldsAndSave(Pedido pedido) {
        pedido.setEmpresa(empresaService.findById(pedido.getEmpresa().getId()));


        if (pedido.hasDetalles()){
            pedido.addPedidoToDetalles();
            pedido.setPrecio(pedido.calcularTotal());
        }

        if (pedido.hasRepartidor()) {
            if (Objects.equals(pedido.getRepartidor().getId(), pedido.getCliente().getId())) {
                throw new UnauthorizedException("No puede asignarse el mismo repartidor que cliente");
            }
            pedido.setRepartidor(repartidorService.findById(pedido.getRepartidor().getId()));
        } else {
            pedido.setRepartidor(null);
        }

        if (pedido.hasEstado()){
            pedido.setEstado(estadoService.findById(pedido.getEstado().getId()));
        }else {
            pedido.setEstado(estadoService.findByNombre("PENDIENTE"));
        }

        return pedidoRepository.save(pedido);
    }
}
