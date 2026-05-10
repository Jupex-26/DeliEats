package org.example.ordersservice.services;

import org.example.ordersservice.models.TipoCocina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoCocinaService {

    TipoCocina save(TipoCocina tipoCocina);

    Page<TipoCocina> findAll(Pageable pageable);

    TipoCocina findById(Long id);

    TipoCocina update(Long id, TipoCocina tipoCocina);

    void deleteById(Long id);
}
