package org.example.ordersservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.dtos.user.UserInputDto;
import org.example.ordersservice.dtos.user.UserOutputDto;
import org.example.ordersservice.models.User;
import org.example.ordersservice.mappers.UserMapper;
import org.example.ordersservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserOutputDto> create(@RequestBody UserInputDto dto) {
        User entity = userMapper.toEntity(dto);
        User saved = userService.save(entity);
        return new ResponseEntity<>(userMapper.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserOutputDto>> findAll(@PageableDefault Pageable pageable) {
        Page<UserOutputDto> dtos = userService.findAll(pageable)
                .map(userMapper::toDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> findById(@PathVariable Long id) {
        User entity = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(entity));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserOutputDto> findByEmail(@PathVariable String email) {
        User entity = userService.findByEmail(email);
        return ResponseEntity.ok(userMapper.toDto(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserOutputDto> update(@PathVariable Long id, @RequestBody UserInputDto dto) {
        User entity = userMapper.toEntity(dto);
        User updated = userService.update(id, entity);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        userService.updatePassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
