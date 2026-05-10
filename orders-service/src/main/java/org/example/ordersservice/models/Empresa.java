package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "empresa")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Empresa extends User {

    @Column(length = 500)
    private String descripcion;

    @Column(name = "correo_contacto")
    private String correoContacto;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_cocina_id")
    private TipoCocina tipoCocina;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Producto> productos;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Apertura> aperturas;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
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
