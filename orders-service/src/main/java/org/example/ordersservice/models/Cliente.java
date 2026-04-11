package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Cliente extends User {

    @Column(name = "fecha_nacimiento")
    private LocalDateTime fechaNacimiento;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Carrito> carritos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Pedido> pedidos;

}