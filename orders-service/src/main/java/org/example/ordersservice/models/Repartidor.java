package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "repartidor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Repartidor extends User {

    @Column(nullable = false)
    private Boolean disponible = true;

    @OneToMany(mappedBy = "repartidor")
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