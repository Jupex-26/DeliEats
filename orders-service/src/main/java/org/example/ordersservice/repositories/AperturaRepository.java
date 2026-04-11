package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Apertura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AperturaRepository extends JpaRepository<Apertura, Long> {

    // Para obtener todos los horarios de un restaurante específico
    List<Apertura> findByEmpresaId(Long empresaId);

    // Para buscar si un restaurante abre un día concreto (ej: "LUNES")
    List<Apertura> findByEmpresaIdAndDia(Long empresaId, String diaSemana);
}