package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.mensaje.MensajeInputDto;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.mappers.MensajeMapper;
import org.example.ordersservice.services.MensajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;
    private final MensajeMapper mensajeMapper;

    @PostMapping
    public ResponseEntity<MensajeOutputDto> create(@RequestBody MensajeInputDto dto) {
        Mensaje entity = mensajeMapper.toEntity(dto);
        Mensaje saved = mensajeService.save(entity);
        return new ResponseEntity<>(mensajeMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MensajeOutputDto>> findAll() {
        List<MensajeOutputDto> dtos = mensajeService.findAll()
                .stream()
                .map(mensajeMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeOutputDto> findById(@PathVariable Long id) {
        Mensaje entity = mensajeService.findById(id);
        return ResponseEntity.ok(mensajeMapper.toDto(entity));
    }

    @GetMapping("/conversacion/{conversacionId}")
    public ResponseEntity<List<MensajeOutputDto>> findByConversacionId(@PathVariable Long conversacionId) {
        List<MensajeOutputDto> dtos = mensajeService.findByConversacionId(conversacionId)
                .stream()
                .map(mensajeMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        mensajeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        mensajeService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}