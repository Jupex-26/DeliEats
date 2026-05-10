package org.example.ordersservice.repositories;

import org.example.ordersservice.models.TipoCocina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoCocinaRepository extends JpaRepository<TipoCocina, Long> {
    Optional<TipoCocina> findByNombre(String nombre);
}
