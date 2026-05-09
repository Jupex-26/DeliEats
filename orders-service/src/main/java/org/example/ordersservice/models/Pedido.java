package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repartidor_id")
    private Repartidor repartidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DetallePedido> detalles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id != null && id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public BigDecimal calcularTotal() {
        if (Objects.isNull(this.detalles)) return BigDecimal.ZERO;

        return this.detalles.stream()
                .filter(Objects::nonNull)
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean hasDetalles() {
        return !CollectionUtils.isEmpty(this.detalles);
    }

    public void addPedidoToDetalles() {
        this.detalles.forEach(d -> d.setPedido(this));
    }

    public boolean hasRepartidor() {
        return Objects.nonNull(this.repartidor) && Objects.nonNull(this.repartidor.getId());
    }

    public boolean hasEstado() {
        return Objects.nonNull(this.estado) && Objects.nonNull(this.estado.getId());
    }
}
