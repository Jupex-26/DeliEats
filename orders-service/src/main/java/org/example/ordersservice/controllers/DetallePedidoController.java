package org.example.ordersservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoInputDto;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoOutputDto;
import org.example.ordersservice.models.DetallePedido;
import org.example.ordersservice.mappers.DetallePedidoMapper;
import org.example.ordersservice.services.DetallePedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalles-pedido")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;
    private final DetallePedidoMapper detallePedidoMapper;

    @PostMapping
    public ResponseEntity<DetallePedidoOutputDto> create(@Valid @RequestBody DetallePedidoInputDto dto) {
        DetallePedido entity = detallePedidoMapper.toEntity(dto);
        DetallePedido saved = detallePedidoService.save(entity);
        return new ResponseEntity<>(detallePedidoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<DetallePedidoOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<DetallePedidoOutputDto> dtos = detallePedidoService.findAll(pageable)
                .map(detallePedidoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoOutputDto> findById(@PathVariable Long id) {
        DetallePedido entity = detallePedidoService.findById(id);
        return ResponseEntity.ok(detallePedidoMapper.toDto(entity));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Page<DetallePedidoOutputDto>> findByPedidoId(@PathVariable Long pedidoId, @PageableDefault Pageable pageable) {
        Page<DetallePedidoOutputDto> dtos = detallePedidoService.findByPedidoId(pedidoId, pageable)
                .map(detallePedidoMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoOutputDto> update(@PathVariable Long id, @Valid @RequestBody DetallePedidoInputDto dto) {
        DetallePedido entity = detallePedidoMapper.toEntity(dto);
        DetallePedido updated = detallePedidoService.update(id, entity);
        return ResponseEntity.ok(detallePedidoMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        detallePedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
