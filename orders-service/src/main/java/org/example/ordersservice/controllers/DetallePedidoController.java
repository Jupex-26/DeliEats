package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoInputDto;
import org.example.ordersservice.dtos.detallepedido.DetallePedidoOutputDto;
import org.example.ordersservice.models.DetallePedido;
import org.example.ordersservice.mappers.DetallePedidoMapper;
import org.example.ordersservice.services.DetallePedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/detalles-pedido")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;
    private final DetallePedidoMapper detallePedidoMapper;

    @PostMapping
    public ResponseEntity<DetallePedidoOutputDto> create(@RequestBody DetallePedidoInputDto dto) {
        DetallePedido entity = detallePedidoMapper.toEntity(dto);
        DetallePedido saved = detallePedidoService.save(entity);
        return new ResponseEntity<>(detallePedidoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DetallePedidoOutputDto>> findAll() {
        List<DetallePedidoOutputDto> dtos = detallePedidoService.findAll()
                .stream()
                .map(detallePedidoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoOutputDto> findById(@PathVariable Long id) {
        DetallePedido entity = detallePedidoService.findById(id);
        return ResponseEntity.ok(detallePedidoMapper.toDto(entity));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedidoOutputDto>> findByPedidoId(@PathVariable Long pedidoId) {
        List<DetallePedidoOutputDto> dtos = detallePedidoService.findByPedidoId(pedidoId)
                .stream()
                .map(detallePedidoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoOutputDto> update(@PathVariable Long id, @RequestBody DetallePedidoInputDto dto) {
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