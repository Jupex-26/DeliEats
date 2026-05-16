package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tipo_cocina")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoCocina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "tipoCocina")
    @ToString.Exclude
    private List<Empresa> empresas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoCocina)) return false;
        TipoCocina that = (TipoCocina) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
