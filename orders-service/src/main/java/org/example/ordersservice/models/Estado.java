package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estado")
@Data
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
}