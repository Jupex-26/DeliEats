package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return super.getId() != null && super.getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}