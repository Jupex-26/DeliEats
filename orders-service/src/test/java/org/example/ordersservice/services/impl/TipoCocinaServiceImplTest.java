package org.example.ordersservice.services.impl;

import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.TipoCocina;
import org.example.ordersservice.repositories.TipoCocinaRepository;
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
class TipoCocinaServiceImplTest {

    @Mock
    private TipoCocinaRepository tipoCocinaRepository;

    @InjectMocks
    private TipoCocinaServiceImpl tipoCocinaService;

    @Test
    void save() {
        TipoCocina tipoCocina = new TipoCocina();
        when(tipoCocinaRepository.save(tipoCocina)).thenReturn(tipoCocina);

        TipoCocina result = tipoCocinaService.save(tipoCocina);

        assertNotNull(result);
        verify(tipoCocinaRepository).save(tipoCocina);
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.unpaged();
        Page<TipoCocina> page = new PageImpl<>(List.of(new TipoCocina()));
        when(tipoCocinaRepository.findAll(pageable)).thenReturn(page);

        Page<TipoCocina> result = tipoCocinaService.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(tipoCocinaRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        Long id = 1L;
        TipoCocina tipoCocina = new TipoCocina();
        when(tipoCocinaRepository.findById(id)).thenReturn(Optional.of(tipoCocina));

        TipoCocina result = tipoCocinaService.findById(id);

        assertNotNull(result);
        verify(tipoCocinaRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        Long id = 1L;
        when(tipoCocinaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> tipoCocinaService.findById(id));
        verify(tipoCocinaRepository).findById(id);
    }

    @Test
    void update_Found() {
        Long id = 1L;
        TipoCocina existingTipoCocina = new TipoCocina();
        TipoCocina newTipoCocina = new TipoCocina();
        when(tipoCocinaRepository.findById(id)).thenReturn(Optional.of(existingTipoCocina));
        when(tipoCocinaRepository.save(any(TipoCocina.class))).thenReturn(newTipoCocina);

        TipoCocina result = tipoCocinaService.update(id, newTipoCocina);

        assertNotNull(result);
        verify(tipoCocinaRepository).findById(id);
        verify(tipoCocinaRepository).save(any(TipoCocina.class));
    }

    @Test
    void update_NotFound() {
        Long id = 1L;
        TipoCocina newTipoCocina = new TipoCocina();
        when(tipoCocinaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> tipoCocinaService.update(id, newTipoCocina));
        verify(tipoCocinaRepository).findById(id);
        verify(tipoCocinaRepository, never()).save(any(TipoCocina.class));
    }

    @Test
    void deleteById() {
        Long id = 1L;
        doNothing().when(tipoCocinaRepository).deleteById(id);

        tipoCocinaService.deleteById(id);

        verify(tipoCocinaRepository).deleteById(id);
    }
}
