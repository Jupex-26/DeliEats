package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "apertura")
@Data
public class Apertura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // En el diagrama es un ENUM, lo mapeamos como String para mayor flexibilidad
    @Column(nullable = false)
    private String dia;

    @Column(name = "hora_apertura")
    private LocalDateTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalDateTime horaCierre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}