package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByEmail(String email);

    // Para buscar por nombre (ej: "Pizzería Napolitana")
    List<Empresa> findByNombreContainingIgnoreCase(String nombre);

    // Para filtrar por tipo de cocina (ej: "Italiana", "Mexicana")
    List<Empresa> findByTipoCocina(String tipoCocina);

}
