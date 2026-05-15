package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Apertura;
import org.example.ordersservice.repositories.AperturaRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AperturaServiceImplTest {

    @Mock
    private AperturaRepository aperturaRepository;

    @InjectMocks
    private AperturaServiceImpl aperturaService;

    @Test
    void save() {
        Apertura apertura = new Apertura();
        when(aperturaRepository.save(apertura)).thenReturn(apertura);

        Apertura result = aperturaService.save(apertura);

        assertNotNull(result);
        verify(aperturaRepository).save(apertura);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Apertura> page = new PageImpl<>(List.of(new Apertura()));
        when(aperturaRepository.findAll(pageable)).thenReturn(page);

        Page<Apertura> result = aperturaService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(aperturaRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        Apertura apertura = new Apertura();
        when(aperturaRepository.findById(id)).thenReturn(Optional.of(apertura));

        Apertura result = aperturaService.findById(id);

        assertNotNull(result);
        verify(aperturaRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(aperturaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> aperturaService.findById(id));
        verify(aperturaRepository).findById(id);
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(aperturaRepository).deleteById(id);

        aperturaService.deleteById(id);

        verify(aperturaRepository).deleteById(id);
    }

    @Test
    void update_Found() {
        Long id = 1L;
        Apertura existingApertura = new Apertura();
        Apertura newAperturaDetails = new Apertura();
        when(aperturaRepository.findById(id)).thenReturn(Optional.of(existingApertura));
        when(aperturaRepository.save(any(Apertura.class))).thenReturn(newAperturaDetails);

        Apertura result = aperturaService.update(id, newAperturaDetails);

        assertNotNull(result);
        verify(aperturaRepository).findById(id);
        verify(aperturaRepository).save(any(Apertura.class));
    }

    @Test
    void update_NotFound() {
        Long id = 1L;
        Apertura newAperturaDetails = new Apertura();
        when(aperturaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> aperturaService.update(id, newAperturaDetails));
        verify(aperturaRepository).findById(id);
        verify(aperturaRepository, never()).save(any(Apertura.class));
    }
}
