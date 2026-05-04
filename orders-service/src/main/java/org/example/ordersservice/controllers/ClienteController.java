package org.example.ordersservice.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.cliente.ClienteInputDto;
import org.example.ordersservice.dtos.cliente.ClienteOutputDto;
import org.example.ordersservice.mappers.ClienteMapper;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.services.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @PostMapping
    public ResponseEntity<ClienteOutputDto> create(@Valid @RequestBody ClienteInputDto dto) {
        Cliente entity = clienteMapper.toEntity(dto);
        Cliente saved = clienteService.save(entity);
        return new ResponseEntity<>(clienteMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<ClienteOutputDto> dtos = clienteService.findAll(pageable)
                .map(clienteMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteOutputDto> findById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(clienteMapper.toDto(cliente));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteOutputDto> update(@PathVariable Long id, @Valid @RequestBody ClienteInputDto dto) {
        Cliente updated = clienteService.update(id, clienteMapper.toEntity(dto));
        return ResponseEntity.ok(clienteMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
