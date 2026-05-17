package org.example.ordersservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.pedido.PedidoInputDto;
import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.models.Pedido;
import org.example.ordersservice.mappers.PedidoMapper;
import org.example.ordersservice.services.PedidoService;
import org.example.ordersservice.services.PdfService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;
    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<PedidoOutputDto> create(@Valid @RequestBody PedidoInputDto dto) {
        Pedido entity = pedidoMapper.toEntity(dto);
        Pedido saved = pedidoService.save(entity);
        return new ResponseEntity<>(pedidoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoOutputDto>> findAll(
            @RequestParam(required = false) String search,
            @PageableDefault Pageable pageable) {
        Page<PedidoOutputDto> dtos = pedidoService.findAll(search, pageable)
                .map(pedidoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoOutputDto> findById(@PathVariable Long id) {
        Pedido entity = pedidoService.findById(id);
        return ResponseEntity.ok(pedidoMapper.toDto(entity));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PedidoOutputDto>> findByClienteId(@PathVariable Long clienteId, @PageableDefault Pageable pageable) {
        Page<PedidoOutputDto> dtos = pedidoService.findByClienteId(clienteId, pageable)
                .map(pedidoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Page<PedidoOutputDto>> findByEmpresaId(
            @PathVariable Long empresaId, 
            @PageableDefault(sort = "fechaCompra", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PedidoOutputDto> dtos = pedidoService.findByEmpresaId(empresaId, pageable)
                .map(pedidoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/empresa/{empresaId}/mes-actual")
    public ResponseEntity<Page<PedidoOutputDto>> findByEmpresaIdMesActual(
            @PathVariable Long empresaId, 
            @PageableDefault(sort = "fechaCompra", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PedidoOutputDto> dtos = pedidoService.findByEmpresaIdMesActual(empresaId, pageable)
                .map(pedidoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/empresa/{empresaId}/mes")
    public ResponseEntity<Page<PedidoOutputDto>> findByEmpresaIdAndMes(
            @PathVariable Long empresaId, 
            @RequestParam int mes,
            @RequestParam int anio,
            @PageableDefault(sort = "fechaCompra", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PedidoOutputDto> dtos = pedidoService.findByEmpresaIdAndMesAndAnio(empresaId, mes, anio, pageable)
                .map(pedidoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoOutputDto> update(@PathVariable Long id, @Valid @RequestBody PedidoInputDto dto) {
        Pedido entity = pedidoMapper.toEntity(dto);
        Pedido updated = pedidoService.update(id, entity);
        return ResponseEntity.ok(pedidoMapper.toDto(updated));
    }

    @PatchMapping("/{id}/estado/{estadoId}")
    public ResponseEntity<PedidoOutputDto> updateEstado(@PathVariable Long id, @PathVariable Long estadoId) {
        Pedido updated = pedidoService.updateEstado(id, estadoId);
        return ResponseEntity.ok(pedidoMapper.toDto(updated));
    }
    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoOutputDto> cancelarPedido(@PathVariable Long id) {
        Pedido updated = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedidoMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/factura")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable Long id) {
        Pedido pedido = pedidoService.findById(id);
        byte[] pdfBytes = pdfService.generarFactura(pedido);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "factura_" + pedido.getId() + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
