package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "apertura")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apertura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Dia dia;

    @Column(name = "hora_apertura")
    private LocalTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalTime horaCierre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Apertura apertura)) return false;
        return id != null && id.equals(apertura.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
