package org.example.ordersservice.repositories;

import org.example.ordersservice.models.Apertura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AperturaRepository extends JpaRepository<Apertura, Long> {

}
