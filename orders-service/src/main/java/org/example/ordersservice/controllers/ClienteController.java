package org.example.ordersservice.controllers;


import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.cliente.ClienteInputDto;
import org.example.ordersservice.dtos.cliente.ClienteOutputDto;
import org.example.ordersservice.mappers.ClienteMapper;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @PostMapping
    public ResponseEntity<ClienteOutputDto> create(@RequestBody ClienteInputDto dto) {
        Cliente entity = clienteMapper.toEntity(dto);
        Cliente saved = clienteService.save(entity);
        return new ResponseEntity<>(clienteMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClienteOutputDto>> findAll() {
        List<ClienteOutputDto> dtos = clienteService.findAll()
                .stream()
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteOutputDto> findById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(clienteMapper.toDto(cliente));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteOutputDto> update(@PathVariable Long id, @RequestBody ClienteInputDto dto) {
        Cliente updated = clienteService.update(id, clienteMapper.toEntity(dto));
        return ResponseEntity.ok(clienteMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}