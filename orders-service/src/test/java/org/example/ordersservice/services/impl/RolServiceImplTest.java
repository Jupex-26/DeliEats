package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.repositories.RolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    @Test
    void save() {
        Rol rol = new Rol();
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol result = rolService.save(rol);

        assertNotNull(result);
        verify(rolRepository).save(rol);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Rol> page = new PageImpl<>(List.of(new Rol()));
        when(rolRepository.findAll(pageable)).thenReturn(page);

        Page<Rol> result = rolService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(rolRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        Rol rol = new Rol();
        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));

        Rol result = rolService.findById(id);

        assertNotNull(result);
        verify(rolRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(rolRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rolService.findById(id));
        verify(rolRepository).findById(id);
    }

    @Test
    void findByNombre_Found() {
        String nombre = "ROLE_USER";
        Rol rol = new Rol();
        when(rolRepository.findByNombre(nombre)).thenReturn(Optional.of(rol));

        Rol result = rolService.findByNombre(nombre);

        assertNotNull(result);
        verify(rolRepository).findByNombre(nombre);
    }

    @Test
    void findByNombre_NotFound() {
        String nombre = "NOT_FOUND";
        when(rolRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rolService.findByNombre(nombre));
        verify(rolRepository).findByNombre(nombre);
    }

    @Test
    void update_Found() {
        Long id = 1L;
        Rol existingRol = new Rol();
        Rol newRol = new Rol();
        when(rolRepository.findById(id)).thenReturn(Optional.of(existingRol));
        when(rolRepository.save(any(Rol.class))).thenReturn(newRol);

        Rol result = rolService.update(id, newRol);

        assertNotNull(result);
        verify(rolRepository).findById(id);
        verify(rolRepository).save(any(Rol.class));
    }

    @Test
    void update_NotFound() {
        Long id = 1L;
        Rol newRol = new Rol();
        when(rolRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rolService.update(id, newRol));
        verify(rolRepository).findById(id);
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(rolRepository).deleteById(id);

        rolService.deleteById(id);

        verify(rolRepository).deleteById(id);
    }
}
