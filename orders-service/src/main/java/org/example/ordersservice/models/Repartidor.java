package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "repartidor")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Repartidor extends User {

    @Column(nullable = false)
    private Boolean disponible = true;

    @OneToMany(mappedBy = "repartidor")
    @ToString.Exclude
    private List<Pedido> pedidos;
}