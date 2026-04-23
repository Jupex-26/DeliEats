package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Entity
@Table(name = "carrito")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarrito estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCarrito> detalles;

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

    public void addDetalles(List<DetalleCarrito> detalles){
        this.detalles.addAll(detalles);
    }

    public void eliminarProducto(Long productoId) {
        this.detalles.removeIf(d -> d.getProducto().getId().equals(productoId));
    }

    public void agregarProducto(Producto producto, Integer cantidad){
        if (Objects.isNull(cantidad) || cantidad <= 0) throw new RuntimeException();

        eliminarProducto(producto.getId());

        List<DetalleCarrito> nuevos = IntStream.range(0, cantidad)
                .mapToObj(i -> DetalleCarrito.builder()
                        .carrito(this)
                        .producto(producto)
                        .build())
                .toList();

        addDetalles(nuevos);
    }

    public BigDecimal getPrecioTotal(){
        if (this.detalles == null || this.detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return this.detalles.stream()
                .map(detalle -> detalle.getProducto().getPrecio())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}