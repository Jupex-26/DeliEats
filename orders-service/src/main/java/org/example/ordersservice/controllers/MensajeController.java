package org.example.ordersservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.mensaje.MensajeInputDto;
import org.example.ordersservice.dtos.mensaje.MensajeOutputDto;
import org.example.ordersservice.models.Mensaje;
import org.example.ordersservice.mappers.MensajeMapper;
import org.example.ordersservice.services.MensajeService;
import org.example.ordersservice.services.MensajeProducerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mensajes")
@RequiredArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;
    private final MensajeMapper mensajeMapper;
    private final MensajeProducerService mensajeProducerService;

    @PostMapping
    public ResponseEntity<MensajeOutputDto> create(@Valid @RequestBody MensajeInputDto dto) {
        Mensaje entity = mensajeMapper.toEntity(dto);
        Mensaje saved = mensajeService.save(entity);
        MensajeOutputDto outputDto = mensajeMapper.toDto(saved);

        mensajeProducerService.sendMessage(outputDto);
        
        return new ResponseEntity<>(outputDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MensajeOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<MensajeOutputDto> dtos = mensajeService.findAll(pageable)
                .map(mensajeMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeOutputDto> findById(@PathVariable Long id) {
        Mensaje entity = mensajeService.findById(id);
        return ResponseEntity.ok(mensajeMapper.toDto(entity));
    }
    
    @GetMapping("/chat")
    public ResponseEntity<Page<MensajeOutputDto>> getChat(
            @RequestParam Long usuario1Id, 
            @RequestParam Long usuario2Id, 
            @PageableDefault Pageable pageable) {
        Page<MensajeOutputDto> dtos = mensajeService.findChat(usuario1Id, usuario2Id, pageable)
                .map(mensajeMapper::toDto);
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
