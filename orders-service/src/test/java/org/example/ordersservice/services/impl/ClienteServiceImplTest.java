package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.exception.custom.RepartidorExistsException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.repositories.ClienteRepository;
import org.example.ordersservice.repositories.UserRepository;
import org.example.ordersservice.services.RepartidorService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RolService rolService;
    @Mock
    private RepartidorService repartidorService;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private Rol rolCliente;

    @BeforeEach
    void setUp() {
        rolCliente = new Rol();
        rolCliente.setNombre("ROLE_CLIENTE");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEmail("test@test.com");
        cliente.setPassword("Password123!"); // Contraseña que cumple el formato
        cliente.setRol(rolCliente);
    }

    @Test
    void save_Success() {
        String originalPassword = cliente.getPassword(); // guardar antes

        lenient().when(userRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        lenient().when(rolService.findByNombre("ROLE_CLIENTE")).thenReturn(rolCliente);
        lenient().when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.save(cliente);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(rolCliente, result.getRol());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void save_EmailExists() {
        when(userRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        assertThrows(EmailExistsException.class, () -> clienteService.save(cliente));
        verify(userRepository).existsByEmail(cliente.getEmail());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Cliente> page = new PageImpl<>(List.of(cliente));
        when(clienteRepository.findAll(pageable)).thenReturn(page);

        Page<Cliente> result = clienteService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(clienteRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(clienteRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clienteService.findById(id));
        verify(clienteRepository).findById(id);
    }

    @Test
    void update_Success() {
        Long id = 1L;
        Cliente existingCliente = new Cliente();
        existingCliente.setId(id);
        existingCliente.setEmail("old@test.com");
        existingCliente.setPassword("oldEncodedPassword");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(existingCliente));
        lenient().when(userRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword"); // anyString()
        lenient().when(rolService.findByNombre("ROLE_CLIENTE")).thenReturn(rolCliente);
        lenient().when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.update(id, cliente);

        assertNotNull(result);
        assertEquals("newEncodedPassword", result.getPassword());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void update_EmailExists() {
        Long id = 1L;
        Cliente existingCliente = new Cliente();
        existingCliente.setId(id);
        existingCliente.setEmail("old@test.com");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(existingCliente));
        when(userRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        assertThrows(EmailExistsException.class, () -> clienteService.update(id, cliente));
        verify(clienteRepository).findById(id);
        verify(userRepository).existsByEmail(cliente.getEmail());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(clienteRepository).deleteById(id);

        clienteService.deleteById(id);

        verify(clienteRepository).deleteById(id);
    }

    @Test
    void solicitarSerRepartidor_Success() {
        Long id = 1L;
        when(repartidorService.existsByClienteId(id)).thenReturn(false);
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        doNothing().when(repartidorService).createFromCliente(cliente);

        clienteService.solicitarSerRepartidor(id);

        verify(repartidorService).existsByClienteId(id);
        verify(clienteRepository).findById(id);
        verify(repartidorService).createFromCliente(cliente);
    }

    @Test
    void solicitarSerRepartidor_AlreadyExists() {
        Long id = 1L;
        when(repartidorService.existsByClienteId(id)).thenReturn(true);

        assertThrows(RepartidorExistsException.class, () -> clienteService.solicitarSerRepartidor(id));
        verify(repartidorService).existsByClienteId(id);
        verify(clienteRepository, never()).findById(anyLong());
        verify(repartidorService, never()).createFromCliente(any(Cliente.class));
    }
}
