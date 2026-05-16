package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.TipoCocina;
import org.example.ordersservice.repositories.TipoCocinaRepository;
import org.example.ordersservice.services.TipoCocinaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoCocinaServiceImpl implements TipoCocinaService {

    private final TipoCocinaRepository tipoCocinaRepository;

    @Override
    public TipoCocina save(TipoCocina tipoCocina) {
        return tipoCocinaRepository.save(tipoCocina);
    }

    @Override
    public Page<TipoCocina> findAll(Pageable pageable) {
        return tipoCocinaRepository.findAll(pageable);
    }

    @Override
    public TipoCocina findById(Long id) {
        return tipoCocinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de cocina no encontrado con ID: " + id));
    }

    @Override
    public TipoCocina update(Long id, TipoCocina tipoCocina) {
        TipoCocina existing = findById(id);
        tipoCocina.setId(existing.getId());
        return tipoCocinaRepository.save(tipoCocina);
    }

    @Override
    public void deleteById(Long id) {
        tipoCocinaRepository.deleteById(id);
    }
}
