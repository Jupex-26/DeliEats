package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.repositories.RepartidorRepository;
import org.example.ordersservice.repositories.UserRepository;
import org.example.ordersservice.services.RolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepartidorServiceImplTest {

    @Mock
    private RepartidorRepository repartidorRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RolService rolService;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RepartidorServiceImpl repartidorService;

    private Repartidor repartidor;
    private Rol rolRepartidor;

    @BeforeEach
    void setUp() {
        rolRepartidor = new Rol(1L, "ROLE_REPARTIDOR");
        repartidor = new Repartidor();
        repartidor.setId(1L);
        repartidor.setEmail("repartidor@test.com");
        repartidor.setPassword("Password123!");
        repartidor.setRol(rolRepartidor);
    }

    @Test
    void save_Success() {
        when(userRepository.existsByEmail(repartidor.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(repartidor.getPassword())).thenReturn("encodedPassword");
        when(rolService.findByNombre("ROLE_REPARTIDOR")).thenReturn(rolRepartidor);
        when(repartidorRepository.save(repartidor)).thenReturn(repartidor);

        Repartidor result = repartidorService.save(repartidor);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(repartidorRepository).save(repartidor);
    }

    @Test
    void save_EmailExists() {
        when(userRepository.existsByEmail(repartidor.getEmail())).thenReturn(true);
        assertThrows(EmailExistsException.class, () -> repartidorService.save(repartidor));
        verify(repartidorRepository, never()).save(any(Repartidor.class));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Repartidor> page = new PageImpl<>(List.of(repartidor));
        when(repartidorRepository.findAll(pageable)).thenReturn(page);

        Page<Repartidor> result = repartidorService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(repartidorRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidor));
        Repartidor result = repartidorService.findById(id);
        assertNotNull(result);
        verify(repartidorRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(repartidorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> repartidorService.findById(id));
        verify(repartidorRepository).findById(id);
    }

    @Test
    void findByDisponible() {
        Pageable pageable = Pageable.unpaged();
        Page<Repartidor> page = new PageImpl<>(List.of(repartidor));
        when(repartidorRepository.findByDisponible(true, pageable)).thenReturn(page);

        Page<Repartidor> result = repartidorService.findByDisponible(true, pageable);

        assertFalse(result.isEmpty());
        verify(repartidorRepository).findByDisponible(true, pageable);
    }

    @Test
    void findByAprobado() {
        Pageable pageable = Pageable.unpaged();
        Page<Repartidor> page = new PageImpl<>(List.of(repartidor));
        when(repartidorRepository.findByAprobado(true, pageable)).thenReturn(page);

        Page<Repartidor> result = repartidorService.findByAprobado(true, pageable);

        assertFalse(result.isEmpty());
        verify(repartidorRepository).findByAprobado(true, pageable);
    }

    @Test
    void update() {
        Long id = 1L;
        Repartidor existingRepartidor = new Repartidor();
        existingRepartidor.setEmail("old@test.com");
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(existingRepartidor));
        when(userRepository.existsByEmail(repartidor.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(repartidor.getPassword())).thenReturn("newEncodedPassword");
        when(rolService.findByNombre("ROLE_REPARTIDOR")).thenReturn(rolRepartidor);
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidor);

        Repartidor result = repartidorService.update(id, repartidor);

        assertNotNull(result);
        verify(repartidorRepository).save(any(Repartidor.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(repartidorRepository).deleteById(id);
        repartidorService.deleteById(id);
        verify(repartidorRepository).deleteById(id);
    }

    @Test
    void updateDisponibilidad() {
        Long id = 1L;
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidor));
        when(rolService.findByNombre(anyString())).thenReturn(rolRepartidor);
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidor);

        repartidorService.updateDisponibilidad(id, true);

        assertTrue(repartidor.getDisponible());
        verify(repartidorRepository).save(repartidor);
    }

    @Test
    void createFromCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(jdbcTemplate.update(anyString(), anyLong(), anyBoolean(), anyBoolean())).thenReturn(1);
        
        repartidorService.createFromCliente(cliente);

        verify(jdbcTemplate).update(anyString(), eq(1L), eq(false), eq(false));
    }

    @Test
    void aprobarRepartidor() {
        Long id = 1L;
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidor));
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidor);

        repartidorService.aprobarRepartidor(id, true);

        assertTrue(repartidor.getAprobado());
        verify(repartidorRepository).save(repartidor);
    }
}
