package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.pedido.PedidoInputDto;
import org.example.ordersservice.dtos.pedido.PedidoOutputDto;
import org.example.ordersservice.models.Pedido;
import org.example.ordersservice.mappers.PedidoMapper;
import org.example.ordersservice.services.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<PedidoOutputDto> create(@RequestBody PedidoInputDto dto) {
        Pedido entity = pedidoMapper.toEntity(dto);
        Pedido saved = pedidoService.save(entity);
        return new ResponseEntity<>(pedidoMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<PedidoOutputDto> dtos = pedidoService.findAll(pageable)
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

    @PutMapping("/{id}")
    public ResponseEntity<PedidoOutputDto> update(@PathVariable Long id, @RequestBody PedidoInputDto dto) {
        Pedido entity = pedidoMapper.toEntity(dto);
        Pedido updated = pedidoService.update(id, entity);
        return ResponseEntity.ok(pedidoMapper.toDto(updated));
    }

    @PatchMapping("/{id}/estado/{estadoId}")
    public ResponseEntity<PedidoOutputDto> updateEstado(@PathVariable Long id, @PathVariable Long estadoId) {
        Pedido updated = pedidoService.updateEstado(id, estadoId);
        return ResponseEntity.ok(pedidoMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
