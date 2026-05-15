package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.EmailExistsException;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.models.Dia;
import org.example.ordersservice.models.Empresa;
import org.example.ordersservice.models.Rol;
import org.example.ordersservice.repositories.EmpresaRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceImplTest {

    @Mock
    private EmpresaRepository empresaRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RolService rolService;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    private Empresa empresa;
    private Rol rolEmpresa;

    @BeforeEach
    void setUp() {
        rolEmpresa = new Rol();
        rolEmpresa.setNombre("ROLE_EMPRESA");

        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setEmail("test@empresa.com");
        empresa.setPassword("Password123!");
        empresa.setRol(rolEmpresa);
    }

    @Test
    void save_Success() {
        when(userRepository.existsByEmail(empresa.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(empresa.getPassword())).thenReturn("encodedPassword");
        when(rolService.findByNombre("ROLE_EMPRESA")).thenReturn(rolEmpresa);
        when(empresaRepository.save(empresa)).thenReturn(empresa);

        Empresa result = empresaService.save(empresa);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(rolEmpresa, result.getRol());
        verify(userRepository).existsByEmail(empresa.getEmail());
        verify(passwordEncoder).encode(empresa.getPassword());
        verify(rolService).findByNombre("ROLE_EMPRESA");
        verify(empresaRepository).save(empresa);
    }

    @Test
    void save_EmailExists() {
        when(userRepository.existsByEmail(empresa.getEmail())).thenReturn(true);

        assertThrows(EmailExistsException.class, () -> empresaService.save(empresa));
        verify(userRepository).existsByEmail(empresa.getEmail());
        verify(empresaRepository, never()).save(any(Empresa.class));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Empresa> page = new PageImpl<>(List.of(empresa));
        when(empresaRepository.findAll(pageable)).thenReturn(page);

        Page<Empresa> result = empresaService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(empresaRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresa));

        Empresa result = empresaService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(empresaRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(empresaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> empresaService.findById(id));
        verify(empresaRepository).findById(id);
    }

    @Test
    void update_Success_WithNewAperturas() {
        Long id = 1L;
        Empresa existingEmpresa = new Empresa();
        existingEmpresa.setId(id);
        existingEmpresa.setEmail("old@empresa.com");
        existingEmpresa.setPassword("oldEncodedPassword");
        existingEmpresa.setAperturas(new ArrayList<>());

        Apertura nuevaApertura = new Apertura();
        nuevaApertura.setDia(Dia.LUNES);
        nuevaApertura.setHoraApertura(LocalTime.of(9, 0));
        nuevaApertura.setHoraCierre(LocalTime.of(17, 0));
        empresa.setAperturas(List.of(nuevaApertura));

        when(empresaRepository.findById(id)).thenReturn(Optional.of(existingEmpresa));
        when(userRepository.existsByEmail(empresa.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(empresa.getPassword())).thenReturn("newEncodedPassword");
        when(rolService.findByNombre("ROLE_EMPRESA")).thenReturn(rolEmpresa);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        Empresa result = empresaService.update(id, empresa);

        assertNotNull(result);
        assertEquals(1, result.getAperturas().size());
        assertEquals(Dia.LUNES, result.getAperturas().get(0).getDia());
        verify(empresaRepository).findById(id);
        verify(empresaRepository).save(any(Empresa.class));
    }

    @Test
    void update_Success_UpdateExistingApertura() {
        Long id = 1L;
        Empresa existingEmpresa = new Empresa();
        existingEmpresa.setId(id);
        existingEmpresa.setEmail("old@empresa.com");
        existingEmpresa.setPassword("oldEncodedPassword");
        
        Apertura aperturaExistente = new Apertura();
        aperturaExistente.setDia(Dia.LUNES);
        aperturaExistente.setHoraApertura(LocalTime.of(8, 0));
        aperturaExistente.setHoraCierre(LocalTime.of(16, 0));
        existingEmpresa.setAperturas(new ArrayList<>(List.of(aperturaExistente)));

        Apertura aperturaActualizada = new Apertura();
        aperturaActualizada.setDia(Dia.LUNES);
        aperturaActualizada.setHoraApertura(LocalTime.of(9, 0));
        aperturaActualizada.setHoraCierre(LocalTime.of(17, 0));
        empresa.setAperturas(List.of(aperturaActualizada));

        when(empresaRepository.findById(id)).thenReturn(Optional.of(existingEmpresa));
        when(userRepository.existsByEmail(empresa.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(empresa.getPassword())).thenReturn("newEncodedPassword");
        when(rolService.findByNombre("ROLE_EMPRESA")).thenReturn(rolEmpresa);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        Empresa result = empresaService.update(id, empresa);

        assertNotNull(result);
        assertEquals(1, result.getAperturas().size());
        assertEquals(LocalTime.of(9, 0), result.getAperturas().get(0).getHoraApertura());
        verify(empresaRepository).findById(id);
        verify(empresaRepository).save(any(Empresa.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(empresaRepository).deleteById(id);

        empresaService.deleteById(id);

        verify(empresaRepository).deleteById(id);
    }
}
